package com.qianbing.link.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianbing.common.entity.FriendlyLinkEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-12-16 20:43:29
 */
@Repository
@Mapper
public interface FriendlyLinkDao extends BaseMapper<FriendlyLinkEntity> {
	
}
