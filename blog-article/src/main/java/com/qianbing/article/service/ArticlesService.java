package com.qianbing.article.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.entity.ArticlesEntity;
import com.qianbing.common.entity.SetArtitleSortEntity;
import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.common.vo.SearchResultVo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-10 15:44:24
 */
public interface ArticlesService extends IService<ArticlesEntity> {


    /**
     * 博客前台(elasticsearch-查所有文章内容信息)
     * @param searchParamVo
     * @return
     */
    SearchResultVo getAllArticlesDetail(SearchParamVo searchParamVo);

    /**
     * 获取文章详情
     * @param articleId
     * @return
     */
    ArticlesEntity getArticleById(Long articleId);


    /**
     * 获取用户最热门的10篇文章
     * @param userId
     * @return
     */
    List<ArticlesEntity> getTop10HotArticles(Long userId);

    /**
     * 获取用户最新的10篇文章
     * @param userId
     * @return
     */
    List<ArticlesEntity> getTop10NewestArticles(Long userId);

    /**
     * 更新文章的评论数
     * @param articleId
     */
    void updateArticleCommentCount(Long articleId);


    /**
     * 根据分类id查询文章ids
     * @param sortId
     * @return
     */
    List<Long> getArticleIdsBySortId(Long sortId);

    /**
     * 根据分类文章ids查询文章信息
     * @param articleIds
     * @param params
     * @return
     */
    IPage<ArticlesEntity> getArticlesBySortIds(List<Long> articleIds,
                                                Map<String, Object> params);

    /**
     * 根据文章id查询标签ids
     * @param articleId
     * @return
     */
    List<Long> getLabelIdsByArticleId(@PathVariable("articleId") Long articleId);

    /**
     * 根据文章id查询分类id
     * @param articleId
     * @return
     */
    Long getSortIdByArticleId(Long articleId);
}

