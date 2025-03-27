package com.am.design.development.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
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
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "defaultDbEntityManagerFactory",
        transactionManagerRef = "defaultDbTransactionManager",
        basePackages = "com.am.design.development.data.defaultdb.repository"
)
@EnableTransactionManagement
@RequiredArgsConstructor
public class DefaultDbConfiguration {

    private final JpaProperties jpaProperties;

    @Bean
    @ConfigurationProperties("spring.datasource.default")
    DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public EntityManagerFactoryBuilder defaultEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "defaultDbEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean defaultDbEntityManagerFactory(
            EntityManagerFactoryBuilder defaultEntityManagerFactoryBuilder,
            @Qualifier("defaultDataSource") DataSource dataSource) {
        return defaultEntityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.am.design.development.data.defaultdb.entity")
                .persistenceUnit("default")
                .properties(jpaProperties.getProperties())
                .build();
    }

    @Bean(name = "defaultDbTransactionManager")
    PlatformTransactionManager defaultDbTransactionManager(
            @Qualifier("defaultDbEntityManagerFactory") EntityManagerFactory defaultDbEntityManagerFactory) {
        return new JpaTransactionManager(defaultDbEntityManagerFactory);
    }

    @Bean(name = "defaultDbJdbcTemplate")
    public JdbcTemplate defaultDbJdbcTemplate(@Qualifier("defaultDataSource") DataSource defaultDataSource) {
        return new JdbcTemplate(defaultDataSource);
    }
}
