package com.qianbing.sort.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qianbing.common.entity.ArticlesEntity;
import com.qianbing.common.entity.LabelsEntity;
import com.qianbing.sort.feign.failback.ArticlesFeignClientFailBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "blog-label", fallback = ArticlesFeignClientFailBack.class)
public interface LabelFeignClient {

    /**
     * 根据标签ids查询标签信息
     * @param labelIds
     * @return
     */
    @RequestMapping("/getLabelsByIds")
    public List<LabelsEntity> getLabelsByIds(@RequestBody List<Long> labelIds);

}
