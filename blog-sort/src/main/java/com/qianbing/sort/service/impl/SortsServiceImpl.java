package com.qianbing.sort.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianbing.common.Result.R;
import com.qianbing.common.constrant.SortsConstrant;
import com.qianbing.common.entity.ArticlesEntity;
import com.qianbing.common.entity.LabelsEntity;
import com.qianbing.common.entity.SetArtitleSortEntity;
import com.qianbing.common.entity.SortsEntity;
import com.qianbing.common.exception.BlogException;
import com.qianbing.common.utils.Constant;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.common.utils.Query;

import com.qianbing.sort.dao.SortsDao;
import com.qianbing.sort.feign.ArticleFeignClient;
import com.qianbing.sort.feign.LabelFeignClient;
import com.qianbing.sort.service.SortsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("sortsService")
public class SortsServiceImpl extends ServiceImpl<SortsDao, SortsEntity> implements SortsService {

    @Autowired
    private SortsDao sortsDao;

    @Autowired
    private ArticleFeignClient articleFeignClient;

    @Autowired
    private LabelFeignClient labelFeignClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<SortsEntity> queryWrapper = new QueryWrapper<SortsEntity>();
        queryWrapper.eq("user_id", params.get("userId"));
        //设置排序字段
        params.put(Constant.ORDER_FIELD, "sort_time");
        //设置升序
        params.put(Constant.ORDER, "asc");
        //设置其他搜索参数
        Integer sortId = (Integer) params.get("sortId");
        if(!StringUtils.isEmpty(sortId)){
            queryWrapper.eq("sort_id",sortId);
        }
        String name = (String) params.get("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.and((obj) -> {
                obj.like("sort_name", name).or().like("sort_alias", name).or().like("sort_description", name);
            });
        }
        IPage<SortsEntity> page = this.page(
                new Query<SortsEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }


    @Override
    public List<SortsEntity> findCatagorysByUserId(Integer userId) {
        QueryWrapper<SortsEntity> queryWrapper = new QueryWrapper<SortsEntity>();
        if(userId != null){
            queryWrapper.eq("user_id", userId);
        }
        List<SortsEntity> sortsEntities = sortsDao.selectList(queryWrapper);
        List<SortsEntity> list = sortsEntities.stream().filter(sortsEntity ->
                sortsEntity.getParentSortId() == 0
        ).map(menu -> {
            menu.setChildren(getChildrenTree(menu, sortsEntities));
            return menu;
        }).sorted((men1, men2) ->
                (int) (men1.getSortTime().getTime() - men2.getSortTime().getTime())
        ).collect(Collectors.toList());
        return list;
    }

    @Override
    public R saveSort(SortsEntity sorts) {
        sorts.setSortTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper<SortsEntity>();
        queryWrapper.eq("sort_name",sorts.getSortName());
        SortsEntity sortsEntity = this.baseMapper.selectOne(queryWrapper);
        if(!StringUtils.isEmpty(sortsEntity)){
            throw new BlogException(SortsConstrant.SORT_AREADY_EXIST);
        }
        this.baseMapper.insert(sorts);
        return R.ok();
    }

    @Override
    public R updateSorts(SortsEntity sorts) {
        QueryWrapper<SortsEntity> queryWrapper = new QueryWrapper<SortsEntity>();
        queryWrapper.eq("sort_name", sorts.getSortName());
        queryWrapper.and((obj) -> {
            obj.ne("sort_id", sorts.getSortId());
        });
        SortsEntity sortEntity = this.baseMapper.selectOne(queryWrapper);
        if(!StringUtils.isEmpty(sortEntity)){
            throw new BlogException(SortsConstrant.SORT_AREADY_EXIST);
        }
        sorts.setSortTime(new Date());
        this.baseMapper.updateById(sorts);
        return R.ok();
    }

    @Override
    public R deleteSorts(Long sortId, Integer userId) {
        //根据sortId先查询
        SortsEntity sortsEntity = this.baseMapper.selectById(sortId);
        //查询该用户的所有分类
        List<SortsEntity> sortsEntities = this.baseMapper.selectList(new QueryWrapper<SortsEntity>().eq("user_id", userId));
        List<Long> longs = new ArrayList<>();
        List<SortsEntity> childrenTree = getChildrenTree(sortsEntity, sortsEntities);
        List<Long> sortIdsTree = getSortIdsTree(longs, childrenTree);
        //把自己的分类id加上去
        sortIdsTree.add(sortsEntity.getSortId());
        //判断所有的分类id中是不是有文章,如果有则不删除
        List<SetArtitleSortEntity> entities = setArtitleSortDao.selectList(new QueryWrapper<SetArtitleSortEntity>().in("sort_id", sortIdsTree));
        if(entities.size()>0){
            throw new BlogException(SortsConstrant.SORT_ARTICLE_AREADY_EXIST);
        }
        //删除所有分类id
        this.baseMapper.deleteBatchIds(sortIdsTree);
        return R.ok();
    }

    /**
     * 获取所有的分类
     * @param userId
     * @return
     */
    @Override
    public List<SortsEntity> getAllSorts(Long userId) {
        QueryWrapper<SortsEntity> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(userId)){
            queryWrapper.eq("user_id",userId);
        }
        List<SortsEntity> sortsEntities = this.baseMapper.selectList(queryWrapper);
        List<Long> sortIds = sortsEntities.stream().map(item -> {
            return item.getParentSortId();
        }).collect(Collectors.toList());
        List<SortsEntity> collect = sortsEntities.stream().filter(item -> {
            return !sortIds.contains(item.getSortId());
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 查看某个分类下的所有文章
     * @param sortId
     * @param params
     * @return
     */
    @Override
    public PageUtils getArticlesBySortId(Long sortId,Map<String,Object> params) {
        //远程调用（根据分类id查询文章ids)
        List<Long> articleIds = articleFeignClient.getArticleIdsBySortId(sortId);
        //远程调用（根据分类文章ids查询文章信息）
        IPage<ArticlesEntity> page = articleFeignClient.getArticlesBySortIds(articleIds,params);
        List<ArticlesEntity> articlesEntities = page.getRecords().stream().map(item -> {
            Long articleId = item.getArticleId();
            //远程调用（根据文章id查询标签ids）
            List<Long> labelIds = articleFeignClient.getLabelIdsByArticleId(articleId);
            //远程调用（根据标签ids查询标签信息）
            List<LabelsEntity> labelsEntities = labelFeignClient.getLabelsByIds(labelIds);
            item.setLabelsEntityList(labelsEntities);
            //远程调用（根据文章id查询分类id）
            Long id = articleFeignClient.getSortIdByArticleId(articleId);
            SortsEntity sortsEntity = this.baseMapper.selectById(id);
            item.setSortName(sortsEntity.getSortName());
            return item;
        }).collect(Collectors.toList());
        page.setRecords(articlesEntities);
        return new PageUtils(page);
    }

    /**
     * 根据分类id查询分类信息
     * @param sortId
     * @return
     */
    @Override
    public SortsEntity getSortEntityById(Long sortId) {
        SortsEntity sortsEntity = this.baseMapper.selectById(sortId);
        return sortsEntity;
    }

    //根据普通用户和管理员来实现权限的管理,当未登录时显示最火的10条博客
    private List<SortsEntity> getChildrenTree(SortsEntity sortsEntity, List<SortsEntity> sortsEntities) {
        List<SortsEntity> list = sortsEntities.stream().filter(sortsEntity1 ->
                sortsEntity1.getParentSortId() == sortsEntity.getSortId()
        ).map(menu -> {
            menu.setChildren(getChildrenTree(menu, sortsEntities));
            return menu;
        }).sorted((men1, men2) ->
                (int) (men1.getSortTime().getTime() - men2.getSortTime().getTime())
        ).collect(Collectors.toList());
        return list;
    }

    //获取所有的下级Id包括自己
    private List<Long> getSortIdsTree(List<Long> list,List<SortsEntity> sortsEntities){
        sortsEntities.stream().forEach(item->{
            list.add(item.getSortId());
            if(item.getChildren() != null){
                getSortIdsTree(list,item.getChildren());
            }
        });
        return list;
    }

}