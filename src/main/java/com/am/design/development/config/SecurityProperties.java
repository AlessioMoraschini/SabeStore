package com.am.design.development.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
    private String[] unauthenticatedEndpoints;
    @Value("${security.enabled:true}")
    private boolean enabled;
}
