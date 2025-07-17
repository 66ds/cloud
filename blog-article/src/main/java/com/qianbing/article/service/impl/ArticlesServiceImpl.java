package com.qianbing.article.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianbing.article.dao.SetArticleSortDao;
import com.qianbing.article.dao.SetArtitleLabelDao;
import com.qianbing.article.feign.LabelsFeignClient;
import com.qianbing.article.feign.SearchFeignClient;
import com.qianbing.article.feign.SortFeignClient;
import com.qianbing.article.feign.UserFeignClient;
import com.qianbing.article.service.SetArtitleLabelService;
import com.qianbing.article.vo.ArticlesVo;
import com.qianbing.common.Result.R;
import com.qianbing.common.constrant.CommentsConstrant;
import com.qianbing.common.entity.*;
import com.qianbing.article.dao.ArticlesDao;
import com.qianbing.article.service.ArticlesService;
import com.qianbing.common.exception.BizCodeExcetionEnum;
import com.qianbing.common.exception.BlogException;
import com.qianbing.common.utils.Constant;
import com.qianbing.common.utils.Query;
import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.common.vo.SearchResultVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service("articlesService")
@Log4j2
public class ArticlesServiceImpl extends ServiceImpl<ArticlesDao, ArticlesEntity> implements ArticlesService {

    static int a = 2;

    @Autowired
    private SetArticleSortDao setArticleSortDao;

    @Autowired
    private SetArtitleLabelService setArtitleLabelService;

    @Autowired
    private SetArtitleLabelDao setArtitleLabelDao;

    @Autowired
    private LabelsFeignClient labelsFeignClient;

    @Autowired
    private SearchFeignClient searchFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private SortFeignClient sortFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 博客前台(elasticsearch-查所有文章内容信息)
     * @param searchParamVo
     * @return
     */
    @Override
    public SearchResultVo getAllArticlesDetail(SearchParamVo searchParamVo) {
        //远程调用search服务返回数据
        SearchResultVo searchResultVo = searchFeignClient.getArticlesList(searchParamVo);
        return searchResultVo;
    }


    /**
     * 查询单篇文章信息
     * @param articleId
     * @return
     */
    @Override
    @Transactional //TODO 使用分布式事务
    public ArticlesEntity getArticleById(Long articleId) {
        //根据id查出当前文章
        ArticlesEntity articlesEntity = this.baseMapper.selectById(articleId);
        if(articlesEntity == null){
            return null;
        }
        //TODO 更新文章阅读量+1（可以使用异步处理）
        articlesEntity.setArticleViews(articlesEntity.getArticleViews()+1);
        //更新阅读量
        this.baseMapper.updateById(articlesEntity);
        //查询文章对应的标签id
        List<Long> labelIds = this.getLabelIdsByArticleId(articleId);
        //远程调用标签信息
        // 发送远程请求
        List<LabelsEntity> labelsEntities = labelsFeignClient.getLabelEntityByIds(labelIds);
        articlesEntity.setLabelsEntityList(labelsEntities);
        //远程调用用户信息
        UsersEntity usersEntity = userFeignClient.getUserEntityById(articlesEntity.getUserId());
        if (usersEntity != null){
            articlesEntity.setUserNickname(usersEntity.getUserNickname());
        }
        //远程调用分类信息(分类是一个数组)
        SetArtitleSortEntity setArtitleSortEntity = setArticleSortDao.selectOne(
                new QueryWrapper<SetArtitleSortEntity>().eq("article_id", articleId));
        if (setArtitleSortEntity != null){
            Long sortId = setArtitleSortEntity.getSortId();
            //远程调用分类信息
            SortsEntity sortEntity = sortFeignClient.getSortEntityById(sortId);
            if (sortEntity != null) {
                articlesEntity.setSortName(sortEntity.getSortName());
            }
        }
        return articlesEntity;
    }

    /**
     * 根据文章id查询标签ids
     * @param articleId
     * @return
     */
    @Override
    public List<Long> getLabelIdsByArticleId(Long articleId) {
        List<SetArtitleLabelEntity> setArtitleLabelEntities = setArtitleLabelDao.selectList(
                new QueryWrapper<SetArtitleLabelEntity>().eq("article_id", articleId));
        return Optional.ofNullable(setArtitleLabelEntities).orElse(Collections.emptyList())
                .stream()
                .map(item -> {
                    return item.getLabelId();
                }).collect(Collectors.toList());
    }

    /**
     * 根据文章id查询分类id
     * @param articleId
     * @return
     */
    @Override
    public Long getSortIdByArticleId(Long articleId) {
        SetArtitleSortEntity setArtitleSortEntity = setArticleSortDao.selectOne(
                new QueryWrapper<SetArtitleSortEntity>().eq("article_id", articleId));
        return setArtitleSortEntity.getSortId();
    }
    /**
     * 添加文章信息
     * @param vo
     * @return
     */
    @Transactional
    @Override
    public R addArticleInfo(ArticlesVo vo) {
        // 1. 检查文章标题是否已存在
        if (isArticleTitleExists(vo.getArticleTitle())) {
            return R.error(BizCodeExcetionEnum.ARTICLE_ALEARDY_EXIST.getCode(),
                    BizCodeExcetionEnum.ARTICLE_ALEARDY_EXIST.getMsg());
        }
        // 2. 初始化文章信息
        ArticlesEntity article = convertVoToEntity(vo);
        this.baseMapper.insert(article);
        // 3. 保存标签及关系
        List<LabelsEntity> labels = createAndSaveLabels(vo.getLabelNames(), article.getUserId());
        saveArticleLabelRelations(article.getArticleId(), labels);
        // 4. 保存文章-分类关系
        saveArticleSortRelation(article.getArticleId(), vo.getSortIds());
        // 5. 通知延迟发布（MQ）
        //TODO 如果设置定时发表，通知MQ接收消息
        rabbitTemplate.convertAndSend("article.delay.direct","article.delay.key", article.getArticleId(),
                message -> {
                    message.getMessageProperties().setDelayLong(100L);
                    return message;
                });
        return R.ok();
    }

    private boolean isArticleTitleExists(String title) {
        QueryWrapper<ArticlesEntity> wrapper = new QueryWrapper<ArticlesEntity>()
                .eq("article_title", title);
        return this.baseMapper.selectOne(wrapper) != null;
    }

    //TODO 设置常量
    private ArticlesEntity convertVoToEntity(ArticlesVo vo) {
        ArticlesEntity entity = new ArticlesEntity();
        BeanUtils.copyProperties(vo, entity);
        entity.setArticleDate(new Date());
        entity.setArticleLikeCount(0L);
        entity.setArticleViews(0L);
        entity.setArticleCommentCount(0L);
        entity.setIsDelete(0);
        return entity;
    }

    private List<LabelsEntity> createAndSaveLabels(List<String> labelNames, Long userId) {
        List<LabelsEntity> labels = labelNames.stream().map(name -> {
            LabelsEntity label = new LabelsEntity();
            label.setLabelName(name);
            label.setUserId(userId);
            return label;
        }).collect(Collectors.toList());

        labelsFeignClient.saveLabelEntities(labels);
        return labels;
    }

    private void saveArticleLabelRelations(Long articleId, List<LabelsEntity> labels) {
        List<SetArtitleLabelEntity> relations = labels.stream().map(label -> {
            SetArtitleLabelEntity rel = new SetArtitleLabelEntity();
            rel.setArticleId(articleId);
            rel.setLabelId(label.getLabelId());
            return rel;
        }).collect(Collectors.toList());

        setArtitleLabelService.saveBatch(relations);
    }

    private void saveArticleSortRelation(Long articleId, List<Long> sortIds) {
        if (sortIds == null || sortIds.isEmpty()) return;
        Long lastSortId = sortIds.get(sortIds.size() - 1);
        SetArtitleSortEntity relation = new SetArtitleSortEntity();
        relation.setArticleId(articleId);
        relation.setSortId(lastSortId);
        setArticleSortDao.insert(relation);
    }


    /**
     * 查询用户最热门的10篇文章
     * @param userId
     * @return
     */
    @Override
    public List<ArticlesEntity> getTop10HotArticles(Long userId) {
        QueryWrapper<ArticlesEntity> queryWrapper = new QueryWrapper<>();
        if (userId != null && userId > 0) { // 更严谨的判断方式
            queryWrapper.eq("user_id", userId);
        }
        Page<ArticlesEntity> page = new Page<>(1, 10);
        queryWrapper.orderByDesc("article_like_count");
        List<ArticlesEntity> articlesEntityList = this.page(page, queryWrapper).getRecords();
        return articlesEntityList;
    }

    /**
     * 查询用户最新的10篇文章
     * @param userId
     * @return
     */
    @Override
    public List<ArticlesEntity> getTop10NewestArticles(Long userId) {
        QueryWrapper<ArticlesEntity> queryWrapper = new QueryWrapper<ArticlesEntity>();
        if(userId != null && userId > 0){
            queryWrapper.eq("user_id",userId);
        }
        Page<ArticlesEntity> page = new Page<>(1, 10);
        queryWrapper.orderByDesc("article_date");
        List<ArticlesEntity> articlesEntityList = this.page(page, queryWrapper).getRecords();
        return articlesEntityList;
    }

    /**
     * 更新文章的评论数
     * @param articleId
     */
    @Override
    public void updateArticleCommentCount(Long articleId) {
        // 更新文章评论数（使用乐观锁）
        int updated = this.baseMapper.incrementCommentCount(articleId);
        if (updated <= 0) {
            throw new BlogException(CommentsConstrant.ARTICLE_NOT_EXIST_OR_UPDATE_FAILED);
        }
    }

    /**
     * 根据分类id查询文章ids
     * @param sortId
     * @return
     */
    @Override
    public List<Long> getArticleIdsBySortId(Long sortId) {
        QueryWrapper<SetArtitleSortEntity> wrapper = new QueryWrapper<SetArtitleSortEntity>();
        wrapper.eq("sort_id", sortId);
        List<SetArtitleSortEntity> relations = setArticleSortDao.selectList(wrapper);
        //没有文章
        if (CollectionUtils.isEmpty(relations)) {
            return null;
        }
        // 获取所有文章ID
        List<Long> articleIds = relations.stream()
                .map(SetArtitleSortEntity::getArticleId)
                .distinct()
                .collect(Collectors.toList());
        return articleIds;
    }

    /**
     * 根据分类文章ids查询文章信息
     * @param articleIds
     * @param params
     * @return
     */
    @Override
    public IPage<ArticlesEntity> getArticlesBySortIds(List<Long> articleIds,
                                                           Map<String, Object> params) {
        QueryWrapper<ArticlesEntity> queryWrapper = new QueryWrapper<ArticlesEntity>();
        queryWrapper.in("article_id",articleIds);
        //设置排序字段
        params.put(Constant.ORDER_FIELD, "article_date");
        //设置升序
        params.put(Constant.ORDER, "desc");
        //设置是全部还是私有
        Object userId = params.get("userId");
        if(userId != null){
            queryWrapper.eq("user_id",userId);
        }
        IPage<ArticlesEntity> page = this.page(
                new Query<ArticlesEntity>().getPage(params),
                queryWrapper
        );
        return page;
    }


}