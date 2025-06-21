package com.qianbing.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianbing.common.entity.UsersEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-09 09:12:57
 */
@Mapper
@Repository
public interface UsersDao extends BaseMapper<UsersEntity> {
	
}
