package com.qianbing.article.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianbing.article.dao.SetArtitleLabelDao;
import com.qianbing.article.service.SetArtitleLabelService;
import com.qianbing.common.entity.SetArtitleLabelEntity;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("setArtitleLabelService")
public class SetArtitleLabelServiceImpl extends ServiceImpl<SetArtitleLabelDao, SetArtitleLabelEntity> implements SetArtitleLabelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SetArtitleLabelEntity> page = this.page(
                new Query<SetArtitleLabelEntity>().getPage(params),
                new QueryWrapper<SetArtitleLabelEntity>()
        );

        return new PageUtils(page);
    }

    //查出文章对应得所有标签id
    @Override
    public List<Long> getLabelIds(Long articleId){
        List<SetArtitleLabelEntity> article_id = this.baseMapper.selectList(new QueryWrapper<SetArtitleLabelEntity>().eq("article_id", articleId));
        if(article_id != null && article_id.size()>0){
            List<Long> collect = article_id.stream().map(item -> {
                return item.getLabelId();
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

}