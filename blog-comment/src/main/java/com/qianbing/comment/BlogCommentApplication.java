package com.qianbing.comment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qianbing.comment.dao")
public class BlogCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogCommentApplication.class, args);
    }

}
