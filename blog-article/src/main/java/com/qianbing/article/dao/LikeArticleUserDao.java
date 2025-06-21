package com.qianbing.article.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianbing.article.vo.WhoDigMeVo;
import com.qianbing.common.entity.LikeArticleUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-20 09:51:18
 */
@Mapper
public interface LikeArticleUserDao extends BaseMapper<LikeArticleUserEntity> {

    /**
     * 获取谁点赞我的信息
     * @param userId
     * @return
     */
    List<WhoDigMeVo> getWhoDigMeInfo(@Param("userId") Long userId);

    void deleteWhoDigMeInfo(@Param("alias") String alias, @Param("array") Integer[] datas);
}
