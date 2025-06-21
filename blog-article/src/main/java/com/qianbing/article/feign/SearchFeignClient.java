package com.qianbing.article.feign;

import com.qianbing.article.feign.failback.ArticlesFeignClientFailBack;
import com.qianbing.common.entity.LabelsEntity;
import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.common.vo.SearchResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "blog-search", fallback = ArticlesFeignClientFailBack.class)
public interface SearchFeignClient {

    @RequestMapping("/getArticlesList")
    SearchResultVo getArticlesList(@RequestBody SearchParamVo searchParamVo);

}
