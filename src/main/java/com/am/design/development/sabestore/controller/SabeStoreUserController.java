package com.am.design.development.sabestore.controller;

import com.am.design.development.sabestore.dto.UserDto;
import com.am.design.development.sabestore.facade.UserFacade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("user")
@SecurityRequirement(name = "bearerAuth")
public class SabeStoreUserController {

    @Autowired
    private UserFacade userFacade;

    @GetMapping("getUsers")
    public ResponseEntity<List<UserDto>> getUsers() {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.getUsers()
        );
    }
    @PutMapping("createUser")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<UserDto> createUser(@Valid UserDto userDto) {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.addUser(userDto)
        );
    }

    @DeleteMapping("deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<UserDto> createUser(@PathParam("id") @NotNull Long id) throws AccountNotFoundException {

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