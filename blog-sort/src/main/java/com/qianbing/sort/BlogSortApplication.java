package com.qianbing.sort;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qianbing.sort.dao")
public class BlogSortApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSortApplication.class, args);
    }

}
