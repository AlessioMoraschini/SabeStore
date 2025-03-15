package com.am.design.development.config;

import com.am.design.development.dto.AppProfiles;
import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Profile("!" + AppProfiles.TEST_JUNIT)
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

    @Autowired
    Environment environment;

    @PostConstruct
    public void migrateFlyway() {
        var flywayUserConf = Flyway.configure()
                .dataSource(userDataSourceUrl, userDataSourceUsername, userDataSourcePassword)
                .locations("classpath:flyway/user");

        if (environment.matchesProfiles(AppProfiles.DEFAULT))
            flywayUserConf.locations("classpath:flyway/user", "classpath:flyway/userinserts");

        var flywayUser = flywayUserConf.load();

        Flyway flywayDefault = Flyway.configure()
                .dataSource(defaultDataSourceUrl, defaultDataSourceUsername, defaultDataSourcePassword)
                .locations("classpath:flyway/default")
                .load();

        flywayUser.migrate();
        flywayDefault.migrate();
    }
}
