package com.am.design.development.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class FlywayConfiguration {

    @Value("${spring.datasource.user.url}")
    String userDataSourceUrl;
    @Value("${spring.datasource.user.username}")
    String userDataSourceUsername;
    @Value("${spring.datasource.user.password}")
    String userDataSourcePassword;

    @Value("${spring.datasource.default.url}")
    String defaultDataSourceUrl;
    @Value("${spring.datasource.default.username}")
    String defaultDataSourceUsername;
    @Value("${spring.datasource.default.password}")
    String defaultDataSourcePassword;

    @PostConstruct
    public void migrateFlyway() {
        Flyway flywayUser = Flyway.configure()
                .dataSource(userDataSourceUrl, userDataSourceUsername, userDataSourcePassword)
                .locations("classpath:flyway/user")
                .load();

        Flyway flywayDefault = Flyway.configure()
                .dataSource(defaultDataSourceUrl, defaultDataSourceUsername, defaultDataSourcePassword)
                .locations("classpath:flyway/default")
                .load();

        flywayUser.migrate();
        flywayDefault.migrate();
    }
}
