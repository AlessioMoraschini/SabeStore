package com.am.design.development.sabestore.controller;

import com.am.design.development.security.AuthRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthController {

    @PostMapping("/login")
    public void login(@RequestBody AuthRequest authRequest) {
        // No implementation needed, managed by JWTAuthenticationFilter
    }

}
