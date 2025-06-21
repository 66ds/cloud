package com.qianbing.article.feign;

import com.qianbing.article.feign.failback.ArticlesFeignClientFailBack;
import com.qianbing.common.entity.UsersEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "blog-user", fallback = ArticlesFeignClientFailBack.class)
public interface UserFeignClient {

    @RequestMapping("/getCardInfo/{userId}")
    UsersEntity getUserEntityById(@PathVariable("userId") Long userId);
}
