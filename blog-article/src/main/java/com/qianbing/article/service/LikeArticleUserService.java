package com.qianbing.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.LikeArticleUserEntity;
import com.qianbing.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-20 09:51:18
 */
public interface LikeArticleUserService extends IService<LikeArticleUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R selectList(Long articleId, Integer userId);

    R likeArticle(Long articleId, Integer userId);

    R getWhoDigMeInfo(long longValue);

    R deleteWhoDigMeInfo(List<Map<String, Object>> likeIds);
}

