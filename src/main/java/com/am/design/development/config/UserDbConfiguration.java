package com.am.design.development.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "userDbEntityManagerFactory",
        transactionManagerRef = "userDbTransactionManager",
        basePackages = "com.am.design.development.data.userdb.repository"
)
@EnableTransactionManagement
public class UserDbConfiguration {

    @Autowired
    JpaProperties jpaProperties;

    @Bean
    @ConfigurationProperties("spring.datasource.user")
    DataSource userDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "userDbEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean secondDbEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("userDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.am.design.development.data.userdb.entity")
                .persistenceUnit("userDb")
                .properties(jpaProperties.getProperties())
                .build();
    }

    @Bean(name = "userDbTransactionManager")
    PlatformTransactionManager userDbTransactionManager(
            @Qualifier("userDbEntityManagerFactory") EntityManagerFactory userDbEntityManagerFactory) {
        return new JpaTransactionManager(userDbEntityManagerFactory);
    }

    @Bean(name = "userDbJdbcTemplate")
    public JdbcTemplate userDbJdbcTemplate(@Qualifier("userDataSource") DataSource userDataSource) {
        return new JdbcTemplate(userDataSource);
    }

}