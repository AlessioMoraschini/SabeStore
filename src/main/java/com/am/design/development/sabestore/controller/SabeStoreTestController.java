package com.am.design.development.sabestore.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("SabeStore")
public class SabeStoreTestController {

    @Value("${sabe.phrase_of_the_day:null}")
    private String phrase;

    @GetMapping("rualive")
    public ResponseEntity<Response> healthCheck(String name) {

        String random = String.valueOf( new Random().nextInt());

        return ResponseEntity.status(HttpStatusCode.valueOf(518)).body(
                new Response("I'm alive: hi " + name + "\r\n" + random + phrase, LocalDateTime.now())
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