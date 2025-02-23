package com.am.design.development;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {
		"com.am.design.development.config",
		"com.am.design.development.sabestore"
})
@EnableTransactionManagement
@EnableJpaRepositories("com.am.design.development.sabestore.data.*")
@EntityScan("com.am.design.development.sabestore.data.*")
public class AmDesignDevelopmentApplication {

	public static void main(String[] args) {

		SpringApplication.run(AmDesignDevelopmentApplication.class, args);
	}

}
