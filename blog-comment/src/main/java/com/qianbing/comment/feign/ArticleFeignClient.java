package com.qianbing.comment.feign;

import com.qianbing.comment.feign.failback.ArticlesFeignClientFailBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "blog-article", fallback = ArticlesFeignClientFailBack.class)
public interface ArticleFeignClient {

    @RequestMapping("/updateArticleCommentCount")
    public void updateArticleCommentCount(Long articleId);
}
