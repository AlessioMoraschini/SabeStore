package com.am.design.development.sabestore.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("SabeStore")
@SecurityRequirement(name = "bearerAuth")
public class SabeStoreTestController {

    @Value("${sabe.phrase_of_the_day:null}")
    private String phrase;

    @GetMapping("rualive")
    public ResponseEntity<Response> healthCheck(String name) {


        return ResponseEntity.status(HttpStatusCode.valueOf(518)).body(
                new Response("I'm alive: hi " + name + System.lineSeparator() + phrase, LocalDateTime.now())
        );
    }

    private void testException(){
        throw new RuntimeException("Just a test Exception ;)");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Response>  errorMapper(Exception exc){
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(
                new Response("I'm alive but with issues: " + exc, LocalDateTime.now())
        );
    }

    public static class Response {
        private String msg;
        private LocalDateTime time;

        public Response(String msg, LocalDateTime time) {
            this.time = time;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public LocalDateTime getTime() {
            return time;
        }

        public void setTime(LocalDateTime time) {
            this.time = time;
        }
    }
}