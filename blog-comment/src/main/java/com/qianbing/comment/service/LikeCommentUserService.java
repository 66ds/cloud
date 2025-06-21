package com.qianbing.comment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.LikeCommentUserEntity;
import com.qianbing.common.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-24 20:58:37
 */
public interface LikeCommentUserService extends IService<LikeCommentUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R selectList(Long commentId, Integer userId);

    R likeComment(Long commentId, Integer userId);
}

