package com.am.design.development.sabestore.controller;

import com.am.design.development.sabestore.dto.UserDto;
import com.am.design.development.sabestore.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("user")
@SecurityRequirement(name = "bearerAuth") // For swagger
public class SabeStoreUserController {

    @Autowired
    private UserFacade userFacade;

    @Operation(
            summary = "Returns the whole list of users",
            description = "Returns the whole list of logged in users. Requires role ROLE_SUPERUSER to be executed.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("getUsers")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.getUsers(PageRequest.of(page, size))
        );
    }

    @Operation(
            summary = "Returns details of the logged in user",
            description = "Returns the details of the user currently logged in. Requires role ROLE_USER to be executed.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("getCurrentUserDetails")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDto> getCurrentUserDetails() {

        String loggedUserMail = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.getCurrentUserDetails(loggedUserMail)
        );
    }

    @Operation(
            summary = "Creates a new user with potential SUPERUSER grants",
            description = "Creates a new user to be verified and sends an email with verification link. Requires role ROLE_SUPERUSER to be executed.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("createUserWithGrants")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<UserDto> createUserWithGrants(@Valid UserDto userDto) throws MessagingException {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.addUser(userDto, true)
        );
    }

    @Operation(
            summary = "Creates a new user",
            description = "Creates a new user to be verified and sends an email with verification link. No authentication required",
            security = @SecurityRequirement(name = "none")
    )
    @PostMapping("createUser")
    public ResponseEntity<UserDto> createUser(@Valid UserDto userDto) throws MessagingException {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.addUser(userDto, false)
        );
    }

    @Operation(
            summary = "Verify a user by ID and random identifier",
            description = "Does not require authentication, but needs a valid registered random identifier and id in order to verify the user",
            security = @SecurityRequirement(name = "none")
    )
    @GetMapping("verifyUser")
    public ResponseEntity<UserDto> verifyUser(
            @RequestParam(name = "userRandomIdentifier") String userRandomIdentifier,
            @RequestParam(name = "userId") Long userId
    ) {

        userFacade.verifyUser(userId, userRandomIdentifier);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }

    @Operation(
            summary = "Send again the verification mail to the specified mail address",
            description = "Does not require authentication, but needs a valid registered email in order to send the verification mail",
            security = @SecurityRequirement(name = "none")
    )
    @GetMapping("resendVerificationMail")
    public ResponseEntity<UserDto> resendVerificationMail(@RequestParam(name = "mail") String mail)
            throws MessagingException {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.resendVerificationMail(mail)
        );
    }

    @Operation(
            summary = "Update any of the existing users",
            description = "Requires role ROLE_SUPERUSER to be executed.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("updateUser")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<UserDto> updateUser(@Valid UserDto userDto) {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.updateUser(userDto, true)
        );
    }

    @Operation(
            summary = "Update logged user",
            description = "Requires role ROLE_USER to be executed. Cannot update the role unless the user has ROLE_SUPERUSER",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("updateLoggedUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDto> updateLoggedUser(@Valid UserDto userDto) {

        verifyLoggedUser(userDto.getMail());

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.updateUser(userDto, false)
        );
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Requires role ROLE_SUPERUSER to be executed",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<UserDto> deleteUser(@PathParam("id") @NotNull Long id) throws AccountNotFoundException {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.removeById(id)
        );
    }

    private void verifyLoggedUser(String mail){
        // Let's check the user mail to see if it matches the one passed in the DTO
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!StringUtils.equals(authentication.getName(), mail)){
            throw new RuntimeException("Logged user mail does not match the one passed in the DTO, you can only update your own based on your profile role of type USER.");
        }
    }

    @ExceptionHandler
    public ResponseEntity<String>  errorMapper(Exception exc){

        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(exc.getClass().getCanonicalName());
    }
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<String>  errorNotFoundMapper(Exception exc){

        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(exc.getClass().getCanonicalName() + ": " + exc.getMessage());
    }
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<String>  validationErrorMapper(ValidationException exc){

        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(exc.getClass().getCanonicalName() + ": " + exc.getMessage());
    }

}