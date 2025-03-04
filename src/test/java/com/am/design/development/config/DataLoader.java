package com.am.design.development.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@TestConfiguration
public class DataLoader {

    @Autowired
    @Qualifier("userDbJdbcTemplate")
    private JdbcTemplate userDbJdbcTemplate;

    @Autowired
    @Qualifier("defaultDbJdbcTemplate")
    private JdbcTemplate defaultDbJdbcTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    @Transactional(transactionManager = "defaultDbTransactionManager")
    public void loadDataDefault() throws Exception {
        String defaultSchemaSql = FileCopyUtils.copyToString(new InputStreamReader(
                resourceLoader.getResource("classpath:default-schema.sql").getInputStream(), StandardCharsets.UTF_8));
        defaultDbJdbcTemplate.execute(defaultSchemaSql);

        String defaultDataSql = FileCopyUtils.copyToString(new InputStreamReader(
                resourceLoader.getResource("classpath:default-data.sql").getInputStream(), StandardCharsets.UTF_8));
        defaultDbJdbcTemplate.execute(defaultDataSql);
    }

    @PostConstruct
    @Transactional(transactionManager = "defaultDbTransactionManager")
    public void loadDataUser() throws Exception {
        String defaultSchemaSql = FileCopyUtils.copyToString(new InputStreamReader(
                resourceLoader.getResource("classpath:user-schema.sql").getInputStream(), StandardCharsets.UTF_8));
        userDbJdbcTemplate.execute(defaultSchemaSql);

        String defaultDataSql = FileCopyUtils.copyToString(new InputStreamReader(
                resourceLoader.getResource("classpath:user-data.sql").getInputStream(), StandardCharsets.UTF_8));
        userDbJdbcTemplate.execute(defaultDataSql);
    }
}

