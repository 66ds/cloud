package com.qianbing.article.feign;

import com.qianbing.article.feign.failback.ArticlesFeignClientFailBack;
import com.qianbing.common.entity.LabelsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "blog-label", fallback = ArticlesFeignClientFailBack.class)
public interface LabelsFeignClient {

    @RequestMapping("/getLabelEntityByIds")
    List<LabelsEntity> getLabelEntityByIds(@RequestParam("labelIds") List<Long> labelIds);

    /**
     * 插入多个标签信息
     * @param labelsEntities
     * @return
     */
    @RequestMapping("/saveLabelEntities")
    void saveLabelEntities(@RequestBody List<LabelsEntity> labelsEntities);
}
