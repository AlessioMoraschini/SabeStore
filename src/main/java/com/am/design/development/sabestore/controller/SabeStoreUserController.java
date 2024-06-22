package com.am.design.development.sabestore.controller;

import com.am.design.development.sabestore.dto.UserDto;
import com.am.design.development.sabestore.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class SabeStoreUserController {

    @Autowired
    private UserFacade userFacade;

    @GetMapping("getUsers")
    public ResponseEntity<List<UserDto>> getUsers() {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.getUsers()
        );
    }

    @ExceptionHandler
    public ResponseEntity<String>  errorMapper(Exception exc){
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(exc.getMessage());
    }

}