package com.am.design.development.utilities.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptHasher {

    public static void main(String[] args) {
        for(String pass : args) {
            String hashed = BCrypt.hashpw(pass, BCrypt.gensalt());
            System.out.println(hashed);
            System.out.println(BCrypt.checkpw(pass, hashed));
            System.out.println("----------------------");
        }
    }
}
