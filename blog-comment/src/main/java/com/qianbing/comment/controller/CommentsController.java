package com.qianbing.comment.controller;

import com.qianbing.comment.service.CommentsService;
import com.qianbing.common.Result.R;
import com.qianbing.common.constrant.CommentsConstrant;
import com.qianbing.common.entity.CommentsEntity;
import com.qianbing.common.utils.PageUtils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-23 10:12:03
 */
@RestController
@RequestMapping
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    /**
     * 获取评论列表
     * @param params
     * @return
     */
    @RequestMapping("/getCommentList")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = commentsService.queryPage(params);
        return R.ok().setData(page);
    }


    /**
     * 点击评论
     * @param comments
     * @param request
     * @return
     */
    @RequestMapping("/addComment")
    public R addComment(@RequestBody CommentsEntity comments, HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String sys = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String chrome = userAgent.getBrowser().getName();
        Integer userId = (Integer) request.getAttribute("id");
        comments.setUserId(userId.longValue());
        comments.setCommentChrome(chrome);
        comments.setCommentSys(sys);
		commentsService.addComments(comments);
        return R.ok();
    }


    /**
     * 获取未读评论信息
     * @param request
     * @return
     */
    @RequestMapping("/getUnreadComments")
    public R getUnreadComments(HttpServletRequest request){
        Integer id = (Integer) request.getAttribute("id");
        return commentsService.getUnreadComments(id.longValue());
    }

    /**
     * 获取未读评论信息
     * @param commentIds
     * @return
     */
    @RequestMapping("/deleteNoreadComments")
    public R deleteNoreadComments(@RequestBody List<Long> commentIds){
        commentsService.deleteNoreadComments(commentIds);
        return R.ok(CommentsConstrant.DELETE_SUCCESS);
    }



}
