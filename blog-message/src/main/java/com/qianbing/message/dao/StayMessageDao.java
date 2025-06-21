package com.qianbing.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianbing.common.entity.StayMessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-12-13 20:46:28
 */
@Repository
@Mapper
public interface StayMessageDao extends BaseMapper<StayMessageEntity> {
	
}
