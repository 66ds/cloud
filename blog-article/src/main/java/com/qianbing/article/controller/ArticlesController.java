package com.qianbing.article.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qianbing.article.service.LikeArticleUserService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.ArticlesEntity;
import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.article.service.ArticlesService;
import com.qianbing.common.vo.SearchResultVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-10 15:44:24
 */
@RestController
@RequestMapping
public class ArticlesController {

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private LikeArticleUserService likeArticleUserService;

    /**
     * 查询所有文章详情
     * @param searchParamVo
     * @return
     */
    @RequestMapping("/getAllArticlesDetail")
    public R getAllArticlesDetail(@RequestBody SearchParamVo searchParamVo) {
        SearchResultVo searchResultVo = articlesService.getAllArticlesDetail(searchParamVo);
        return R.ok().setData(searchResultVo);
    }

    /**
     * 获取文章详情
     * @param articleId
     * @return
     */
    @RequestMapping("/getArticleById/{articleId}")
    public R getArticleById(@PathVariable("articleId") Long articleId) {
        ArticlesEntity articles = articlesService.getArticleById(articleId);
        return R.ok().setData(articles);
    }

    /**
     * 获取用户最热门的10篇文章
     * @return
     */
    @RequestMapping("/getTop10HotArticles")
    public R getTop10HotArticles(Long userId){
        List<ArticlesEntity> articlesEntityList = articlesService.getTop10HotArticles(userId);
        return R.ok().setData(articlesEntityList);
    }

    /**
     * 获取用户最新的10篇文章
     * @param userId
     * @return
     */
    @RequestMapping("/getTop10NewestArticles")
    public R selectNewList(Long userId){
        List<ArticlesEntity> articlesEntityList = articlesService.getTop10NewestArticles(userId);
        return R.ok().setData(articlesEntityList);
    }

    /**
     * 更新文章评论数
     * @param articleId
     * @return
     */
    @RequestMapping("/updateArticleCommentCount")
    public void updateArticleCommentCount(Long articleId){
        articlesService.updateArticleCommentCount(articleId);
    }

    /**
     * 根据分类id查询文章ids
     * @param sortId
     * @return
     */
    @RequestMapping("/getArticleIdsBySortId/{sortId}")
    public List<Long> getArticleIdsBySortId(@PathVariable("sortId") Long sortId){
        return articlesService.getArticleIdsBySortId(sortId);
    }

    /**
     * 根据分类文章ids查询文章信息
     * @param articleIds
     * @param params
     * @return
     */
    @RequestMapping("/getArticlesBySortIds")
    public IPage<ArticlesEntity> getArticlesBySortIds(@RequestBody List<Long> articleIds,
                                                      @RequestBody Map<String,Object> params){
        return articlesService.getArticlesBySortIds(articleIds,params);
    }

    /**
     * 根据文章id查询标签ids
     * @param articleId
     * @return
     */
    @RequestMapping("/getLabelIdsByArticleId/{articleId}")
    public List<Long> getLabelIdsByArticleId(@PathVariable("articleId") Long articleId){
        return articlesService.getLabelIdsByArticleId(articleId);
    }

    /**
     * 根据文章id查询分类id
     * @param articleId
     * @return
     */
    @RequestMapping("/getSortIdByArticleId/{articleId}")
    public Long getSortIdByArticleId(@PathVariable("articleId") Long articleId){
        return articlesService.getSortIdByArticleId(articleId);
    }

    /**
     * 判断用户是否点赞了文章
     * @param articleId
     * @param request
     * @return
     */
    @RequestMapping("/getWhoDigArticle/{articleId}")
    public R selectList(@PathVariable("articleId") Long articleId, HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        return likeArticleUserService.selectList(articleId,userId);
    }

    /**
     * 用户点赞文章
     * @param articleId
     * @param request
     * @return
     */
    @RequestMapping("/digArticle/{articleId}")
    public R likeArticle(@PathVariable("articleId") Long articleId, HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        return likeArticleUserService.likeArticle(articleId,userId);
    }

    /**
     * 获取谁点赞我的信息
     * @param request
     * @return
     */
    @RequestMapping("/getWhoDigInfo")
    public R getWhoDigMeInfo(HttpServletRequest request){
        Integer id = (Integer) request.getAttribute("id");
        return likeArticleUserService.getWhoDigMeInfo(id.longValue());
    }


    /**
     * 清空信息(单个和多个)
     * @param likeIds
     * @return
     */
    @RequestMapping("/deleteWhoDigInfo")
    public R deleteWhoDigMeInfo(@RequestBody List<Map<String,Object>> likeIds){
        return likeArticleUserService.deleteWhoDigMeInfo(likeIds);
    }

}
