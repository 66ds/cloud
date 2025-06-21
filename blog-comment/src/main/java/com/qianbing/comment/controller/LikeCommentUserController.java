package com.qianbing.comment.controller;

import com.qianbing.common.utils.R;
import com.qianbing.service.service.LikeCommentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-24 20:58:37
 */
@RestController
@RequestMapping("/api/front/likecommentuser")
public class LikeCommentUserController {
    @Autowired
    private LikeCommentUserService likeCommentUserService;


    /**
     * 获取用户对某篇文章的所有评论
     * @param articleId
     * @param request
     * @return
     */
    @RequestMapping("/getCommentList/{articleId}")
    public R selectList(@PathVariable("articleId") Long articleId, HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        return likeCommentUserService.selectList(articleId,userId);
    }

    /**
     * 用户点赞评论
     * @param commentId
     * @param request
     * @return
     */
    @RequestMapping("/digComment/{commentId}")
    public R likeArticle(@PathVariable("commentId") Long commentId, HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        return likeCommentUserService.likeComment(commentId,userId);
    }

}
