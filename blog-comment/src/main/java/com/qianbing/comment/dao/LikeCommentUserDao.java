package com.qianbing.comment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianbing.common.entity.LikeCommentUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-24 20:58:37
 */
@Repository
@Mapper
public interface LikeCommentUserDao extends BaseMapper<LikeCommentUserEntity> {
	
}
