package com.qianbing.comment.feign.failback;

import com.qianbing.comment.feign.ArticleFeignClient;
import com.qianbing.common.entity.UsersEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * FeignClient兜底回调，FailBack；
 * 可以返回缓存，假数据，默认数据等
 */
@Slf4j
@Component
public class ArticlesFeignClientFailBack implements ArticleFeignClient {

    @Override
    public void updateArticleCommentCount(Long articleId) {

    }
}
