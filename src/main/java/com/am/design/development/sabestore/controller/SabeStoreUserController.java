package com.am.design.development.sabestore.controller;

import com.am.design.development.sabestore.dto.UserDto;
import com.am.design.development.sabestore.facade.UserFacade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("user")
@SecurityRequirement(name = "bearerAuth") // For swagger
public class SabeStoreUserController {

    @Autowired
    private UserFacade userFacade;

    @GetMapping("getUsers")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<List<UserDto>> getUsers() {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.getUsers()
        );
    }

    @GetMapping("getCurrentUserDetails")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDto> getCurrentUserDetails() {

        String loggedUserMail = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.getCurrentUserDetails(loggedUserMail)
        );
    }
    @PutMapping("createUserWithGrants")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<UserDto> createUserWithGrants(@Valid UserDto userDto) throws MessagingException {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.addUser(userDto, true)
        );
    }
    @PutMapping("createUser")
    public ResponseEntity<UserDto> createUser(@Valid UserDto userDto) throws MessagingException {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.addUser(userDto, false)
        );
    }

    @GetMapping("verifyUser")
    public ResponseEntity<UserDto> verifyUser(
            @RequestParam(name = "userRandomIdentifier") String userRandomIdentifier,
            @RequestParam(name = "userId") Long userId
    ) {

        userFacade.verifyUser(userId, userRandomIdentifier);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }

    @GetMapping("resendVerificationMail")
    public ResponseEntity<UserDto> resendVerificationMail(@RequestParam(name = "mail") String mail)
            throws MessagingException {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.resendVerificationMail(mail)
        );
    }

    @PostMapping("updateUser")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<UserDto> updateUser(@Valid UserDto userDto) {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.updateUser(userDto, true)
        );
    }

    @PostMapping("updateLoggedUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDto> updateLoggedUser(@Valid UserDto userDto) {

        verifyLoggedUser(userDto.getMail());

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.updateUser(userDto, false)
        );
    }

    private void verifyLoggedUser(String mail){
        // Let's check the user mail to see if it matches the one passed in the DTO
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!StringUtils.equals(authentication.getName(), mail)){
            throw new RuntimeException("Logged user mail does not match the one passed in the DTO, you can only update your own based on your profile role of type USER.");
        }
    }

    @DeleteMapping("deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<UserDto> deleteUser(@PathParam("id") @NotNull Long id) throws AccountNotFoundException {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.removeById(id)
        );
    }

    @ExceptionHandler
    public ResponseEntity<String>  errorMapper(Exception exc){

        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(exc.getClass().getCanonicalName() + ": " + exc.getMessage());
    }
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<String>  errorNotFoundMapper(Exception exc){

        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(exc.getClass().getCanonicalName() + ": " + exc.getMessage());
    }

}