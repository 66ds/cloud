package com.qianbing.label;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.qianbing.label.dao")
public class BlogLabelApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogLabelApplication.class, args);
    }

}
