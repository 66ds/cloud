package com.qianbing.article.feign;

import com.qianbing.article.feign.failback.ArticlesFeignClientFailBack;
import com.qianbing.common.entity.LabelsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "blog-search", fallback = ArticlesFeignClientFailBack.class)
public interface LabelsFeignClient {

    @RequestMapping("/getLabelEntityByIds")
    List<LabelsEntity> getLabelEntityByIds(@RequestParam("labelIds") List<Long> labelIds);
}
