package com.qianbing.article.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianbing.article.dao.ArticlesDao;
import com.qianbing.article.dao.LikeArticleUserDao;
import com.qianbing.article.service.LikeArticleUserService;
import com.qianbing.article.vo.WhoDigMeVo;
import com.qianbing.common.Result.R;
import com.qianbing.common.constrant.LikeArticleUserConstrant;
import com.qianbing.common.entity.ArticlesEntity;
import com.qianbing.common.entity.LikeArticleUserEntity;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.common.utils.Query;
import com.qianbing.common.vo.CommonVo;
import com.qianbing.common.vo.DigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service("likeArticleUserService")
public class LikeArticleUserServiceImpl extends ServiceImpl<LikeArticleUserDao, LikeArticleUserEntity> implements LikeArticleUserService {

    @Autowired
    private ArticlesDao articlesDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LikeArticleUserEntity> page = this.page(
                new Query<LikeArticleUserEntity>().getPage(params),
                new QueryWrapper<LikeArticleUserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R selectList(Long articleId, Integer userId) {
        LikeArticleUserEntity likeArticleUserEntity = this.baseMapper.selectOne(new QueryWrapper<LikeArticleUserEntity>().eq("article_id", articleId).eq("user_id", userId));
        if(StringUtils.isEmpty(likeArticleUserEntity)){
            return R.ok();
        }
        return R.ok().setData(likeArticleUserEntity);
    }

    @Transactional
    @Override
    public R likeArticle(Long articleId, Integer userId) {
        //如果已经点赞则取消,并且将文章的点赞量-1
        ArticlesEntity articlesEntity = articlesDao.selectById(articleId);
        LikeArticleUserEntity likeArticleUserEntity = this.baseMapper.selectOne(new QueryWrapper<LikeArticleUserEntity>().eq("article_id", articleId).eq("user_id", userId));
        if(!StringUtils.isEmpty(likeArticleUserEntity)){
            this.baseMapper.deleteById(likeArticleUserEntity);
            articlesEntity.setArticleLikeCount(articlesEntity.getArticleLikeCount()-1);
            articlesDao.updateById(articlesEntity);
            return R.ok();
        }
        //没有点赞则添加,并且修改文章的赞数量
        LikeArticleUserEntity entity = new LikeArticleUserEntity();
        entity.setArticleId(articleId);
        entity.setLikeDate(new Date());
        entity.setUserId(userId.longValue());
        //默认未读
        entity.setIsRead(0L);
        this.baseMapper.insert(entity);
        //修改文章的点赞量+1
        articlesEntity.setArticleLikeCount(articlesEntity.getArticleLikeCount()+1);
        articlesDao.updateById(articlesEntity);
        return R.ok().setData(entity);
    }

    @Override
    public R getWhoDigMeInfo(long userId) {
        List<WhoDigMeVo> whoDigMeInfo = this.baseMapper.getWhoDigMeInfo(userId);
        List<DigVo> collect = whoDigMeInfo.stream().map(item -> {
            List<CommonVo.User> list = new ArrayList<>();
            DigVo digVo = new DigVo();
            String likeIds = item.getLikeIds();
            String userIds = item.getUserIds();
            String userNames = item.getUserNames();
            List<Long> ts = Arrays.asList(likeIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<Long> longs = Arrays.asList(userIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<String> strings = Arrays.asList(userNames.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
            for (int i = 0; i < longs.size(); i++) {
                CommonVo.User user = new CommonVo.User();
                user.setUserId(longs.get(i));
                user.setUserName(strings.get(i));
                list.add(user);
            }
            digVo.setIds(ts);
            digVo.setCreateTime(item.getLikeDate());
            digVo.setUsers(list);
            digVo.setArticleId(item.getArticleId());
            digVo.setContent(item.getContent());
            digVo.setAlias(item.getAlias());
            return digVo;
        }).collect(Collectors.toList());
        return R.ok().setData(collect);
    }

    @Override
    public R deleteWhoDigMeInfo(List<Map<String, Object>> likeIds) {
        likeIds.stream().forEach(item->{
            Set<String> strings = item.keySet();
            for (String key : strings) {
                ArrayList<Integer> datas  = (ArrayList) item.get(key);
                Integer[] ints = datas.toArray(new Integer[datas.size()]);
                //如果是1则删除文章的赞，如果是2则删除评论的赞
                this.baseMapper.deleteWhoDigMeInfo(key,ints);
            }
        });
        return  R.ok(LikeArticleUserConstrant.DELETE_SUCCESS);
    }

}