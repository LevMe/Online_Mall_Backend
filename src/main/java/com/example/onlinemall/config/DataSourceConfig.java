package com.example.onlinemall.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 统一数据源配置类
 * 使用 DataSourceProperties 进行更健壮的多数据源配置
 */
@Configuration
public class DataSourceConfig {

    // --- MySQL Configuration ---

    /**
     * 创建 MySQL 的数据源属性配置对象。
     * Spring Boot 会自动从 application.yml 中读取 "spring.datasource.mysql" 前缀的配置并填充这个对象。
     * @return 包含MySQL连接信息的属性对象
     */
    @Primary
    @Bean("mysqlDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建主数据源 (MySQL) 的 DataSource Bean。
     * @Primary 注解是关键，它告诉 Spring Boot 和 Mybatis-Plus 这是默认的数据源。
     * @return 配置好的 MySQL DataSource 实例
     */
    @Primary
    @Bean("mysqlDataSource")
    public DataSource mysqlDataSource() {
        // 从属性对象构建并初始化 DataSource
        return mysqlDataSourceProperties().initializeDataSourceBuilder().build();
    }


    // --- ClickHouse Configuration ---

    /**
     * 创建 ClickHouse 的数据源属性配置对象。
     * Spring Boot 会自动从 application.yml 中读取 "spring.datasource.clickhouse" 前缀的配置。
     * @return 包含ClickHouse连接信息的属性对象
     */
    @Bean("clickhouseDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.clickhouse")
    public DataSourceProperties clickhouseDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建 ClickHouse 的 DataSource Bean。
     * 这个数据源将通过 @Qualifier("clickhouseDataSource") 按名称注入到需要它的地方。
     * @return 配置好的 ClickHouse DataSource 实例
     */
    @Bean("clickhouseDataSource")
    public DataSource clickhouseDataSource() {
        // 从属性对象构建并初始化 DataSource
        return clickhouseDataSourceProperties().initializeDataSourceBuilder().build();
    }
}