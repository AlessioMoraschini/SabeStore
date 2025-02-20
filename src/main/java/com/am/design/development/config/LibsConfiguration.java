package com.am.design.development.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibsConfiguration {

    @Value("${libs.path}")
    private String libsPath;

    @PostConstruct
    public void init(){
        System.setProperty("java.library.path", libsPath);
    }
}
