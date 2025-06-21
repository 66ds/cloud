package com.qianbing.sort.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qianbing.common.entity.ArticlesEntity;
import com.qianbing.common.entity.SetArtitleSortEntity;
import com.qianbing.sort.feign.failback.ArticlesFeignClientFailBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "blog-article", fallback = ArticlesFeignClientFailBack.class)
public interface ArticleFeignClient {

    /**
     * 根据分类id查询文章ids
     * @param sortId
     * @return
     */
    @RequestMapping("/getArticleIdsBySortId/{sortId}")
    public List<Long> getArticleIdsBySortId(@PathVariable("sortId") Long sortId);

    /**
     * 根据分类文章ids查询文章信息
     * @param articleIds
     * @param params
     * @return
     */
    @RequestMapping("/getArticlesBySortIds")
    IPage<ArticlesEntity> getArticlesBySortIds(@RequestBody List<Long> articleIds,
                                                @RequestBody Map<String,Object> params);

    /**
     * 根据文章id查询标签ids
     * @param articleId
     * @return
     */
    @RequestMapping("/getLabelIdsByArticleId/{articleId}")
    public List<Long> getLabelIdsByArticleId(@PathVariable("articleId") Long articleId);

    /**
     * 根据文章id查询分类id
     * @param articleId
     * @return
     */
    @RequestMapping("/getSortIdByArticleId/{articleId}")
    public Long getSortIdByArticleId(@PathVariable("articleId") Long articleId);
}
