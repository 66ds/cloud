package com.qianbing.comment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.CommentsEntity;
import com.qianbing.common.entity.UsersEntity;
import com.qianbing.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-23 10:12:03
 */
public interface CommentsService extends IService<CommentsEntity> {

    /**
     * 获取评论列表
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     *
     * @param articleId
     * @param params
     * @return
     */
    PageUtils selectListInfo(Long articleId,Map<String, Object> params);

    /**
     * 点击评论
     * @param comments
     * @return
     */
    void addComments(CommentsEntity comments);

    /**
     *
     * @param parentCommentId
     * @return
     */
    UsersEntity selectUserInfo(Long parentCommentId);

    /**
     * 获取未读评论信息
     * @param userId
     * @return
     */
    R getUnreadComments(long userId);

    /**
     * 获取未读评论信息
     * @param commentIds
     * @return
     */
    void deleteNoreadComments(List<Long> commentIds);

}

