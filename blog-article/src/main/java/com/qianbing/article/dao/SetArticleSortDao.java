package com.qianbing.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianbing.common.entity.SetArtitleSortEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-12 16:13:08
 */
@Repository
@Mapper
public interface SetArticleSortDao extends BaseMapper<SetArtitleSortEntity> {

}