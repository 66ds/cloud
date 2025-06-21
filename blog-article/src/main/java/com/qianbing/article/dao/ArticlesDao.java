package com.qianbing.article.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianbing.common.entity.ArticlesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-10 15:44:24
 */
@Mapper
@Repository
public interface ArticlesDao extends BaseMapper<ArticlesEntity> {

    int incrementCommentCount(@Param("articleId") Long articleId);
}
