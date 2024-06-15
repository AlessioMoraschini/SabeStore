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
    public ResponseEntity<List<UserDto>> healthCheck() {

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                userFacade.getUsers()
        );
    }

    private void testException(){
        throw new RuntimeException("Just a test Exception ;)");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String>  errorMapper(Exception exc){
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(exc.getMessage());
    }

}