package com.qianbing.article.feign;

import com.qianbing.article.feign.failback.ArticlesFeignClientFailBack;
import com.qianbing.common.entity.SortsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "blog-sort", fallback = ArticlesFeignClientFailBack.class)
public interface SortFeignClient {

    /**
     * 根据分类id查询分类信息
     * @param sortId
     * @return
     */
    @RequestMapping("/getSortEntityById/{sortId}")
    public SortsEntity getSortEntityById(@PathVariable("sortId") Long sortId);
}
