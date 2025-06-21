package com.qianbing.comment.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianbing.comment.dao.CommentsDao;
import com.qianbing.comment.dao.LikeCommentUserDao;
import com.qianbing.comment.service.LikeCommentUserService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.CommentsEntity;
import com.qianbing.common.entity.LikeCommentUserEntity;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("likeCommentUserService")
public class LikeCommentUserServiceImpl extends ServiceImpl<LikeCommentUserDao, LikeCommentUserEntity> implements LikeCommentUserService {
    @Autowired
    private CommentsDao commentsDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LikeCommentUserEntity> page = this.page(
                new Query<LikeCommentUserEntity>().getPage(params),
                new QueryWrapper<LikeCommentUserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R selectList(Long articleId, Integer userId) {
        //获取文章对应得所有评论
        List<CommentsEntity> commentsEntities = commentsDao.selectList(new QueryWrapper<CommentsEntity>().eq("article_id", articleId).eq("user_id", userId));
        //获取用户所赞的评论
        List<LikeCommentUserEntity> likeCommentUserEntities = this.baseMapper.selectList(new QueryWrapper<LikeCommentUserEntity>().eq("user_id", userId));
        List<Long> collect = commentsEntities.stream().map(item -> {
            return item.getCommentId();
        }).collect(Collectors.toList());
        List<Long> collect1 = likeCommentUserEntities.stream().map(item -> {
            return item.getCommentId();
        }).collect(Collectors.toList());
        //取出交集
        collect.retainAll(collect1);
        return R.ok().setData(collect);
    }

    @Transactional
    @Override
    public R likeComment(Long commentId, Integer userId) {
        //如果已经点赞则取消,并且将文章的评论数减1
        CommentsEntity commentsEntity = commentsDao.selectById(commentId);
        LikeCommentUserEntity likeCommentUserEntity = this.baseMapper.selectOne(new QueryWrapper<LikeCommentUserEntity>().eq("comment_id", commentId).eq("user_id", userId));
        if(!StringUtils.isEmpty(likeCommentUserEntity)){
            this.baseMapper.deleteById(likeCommentUserEntity);
            commentsEntity.setCommentLikeCount(commentsEntity.getCommentLikeCount()-1);
            commentsDao.updateById(commentsEntity);
            return R.ok();
        }
        //没有点赞则添加,并且修改文章的赞数量
        LikeCommentUserEntity entity = new LikeCommentUserEntity();
        entity.setCommentId(commentId);
        entity.setLikeDate(new Date());
        entity.setUserId(userId.longValue());
        //默认未读
        entity.setIsRead(0L);
        this.baseMapper.insert(entity);
        //修改文章的评论数量+1
        commentsEntity.setCommentLikeCount(commentsEntity.getCommentLikeCount()+1);
        commentsDao.updateById(commentsEntity);
        return R.ok().setData(entity);
    }


}