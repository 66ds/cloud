package com.qianbing.article.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ArticleConfig {

    /**
     * RestTemplate是spring提供的线程安全的http客户端，用于发送http请求
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 开启Feign重试功能(返回一个默认的Retryer实例，设置初始重试间隔、最大重试间隔和重试次数。)
      * @return
     */
    @Bean
    Retryer retryer() {
        return new Retryer.Default(100,100,3);
    }

    /**
     * 开启远程调用（Feign）日志(返回 Feign 日志的级别为 FULL,表示记录所有请求和响应的详细信息)
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
