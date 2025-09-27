package com.example.onlinemall.config;

import com.clickhouse.jdbc.ClickHouseDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class ClickHouseConfig {

    @Value("${spring.clickhouse.url}")
    private String clickhouseUrl;

    @Value("${spring.clickhouse.username}")
    private String clickhouseUsername;

    @Value("${spring.clickhouse.password}")
    private String clickhousePassword;

    @Bean
    public ClickHouseDataSource clickHouseDataSource() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", clickhouseUsername);
        props.setProperty("password", clickhousePassword);
        return new ClickHouseDataSource(clickhouseUrl, props);
    }
}