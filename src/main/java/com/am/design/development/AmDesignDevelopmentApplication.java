package com.am.design.development;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {
		"com.am.design.development.config",
		"com.am.design.development.sabestore"
})
@EnableTransactionManagement
public class AmDesignDevelopmentApplication {

	public static void main(String[] args) {

		SpringApplication.run(AmDesignDevelopmentApplication.class, args);
	}

}
