package com.am.design.development;

import com.am.design.development.config.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {
		"com.am.design.development.config",
		"com.am.design.development.utilities",
		"com.am.design.development.sabestore"
})
@EnableTransactionManagement
@EnableConfigurationProperties(SecurityProperties.class)
public class AmDesignDevelopmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmDesignDevelopmentApplication.class, args);
	}

}
