package com.example.onlinemall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.onlinemall.mapper") // 添加这行，指定Mapper接口所在的包
public class OnlineMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineMallApplication.class, args);
    }

}