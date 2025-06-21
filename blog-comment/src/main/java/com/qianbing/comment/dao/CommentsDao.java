package com.qianbing.comment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianbing.comment.vo.NoReadCommentVo;
import com.qianbing.common.entity.CommentsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-23 10:12:03
 */
@Repository
@Mapper
public interface CommentsDao extends BaseMapper<CommentsEntity> {

    /**
     * 获取未读评论信息
     * @param userId
     * @return
     */
    public List<NoReadCommentVo> getNoReadCommentInfo(@Param("userId") Long userId);

    /**
     * 清空信息(单个和多个)
     * @param commentIds
     */
    void deleteNoReadCommentInfo(@Param("list") List<Long> commentIds);
}
