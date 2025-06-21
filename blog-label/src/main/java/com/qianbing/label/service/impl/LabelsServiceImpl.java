package com.qianbing.label.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianbing.common.entity.LabelsEntity;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.label.dao.LabelsDao;
import com.qianbing.label.service.LabelsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class LabelsServiceImpl extends ServiceImpl<LabelsDao, LabelsEntity> implements LabelsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<LabelsEntity> page = this.page(
//                new Query<LabelsEntity>().getPage(params),
//                new QueryWrapper<LabelsEntity>()
//        );

        return new PageUtils(new ArrayList<>(),10,1,1);
    }

    @Override
    public List<LabelsEntity> selectListInfo(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper<LabelsEntity>();
        if(!StringUtils.isEmpty(userId)){
            queryWrapper.eq("user_id",userId);
        }
        List<LabelsEntity> labelsEntities = this.baseMapper.selectList(queryWrapper);
        return labelsEntities;
    }

    /**
     * 根据多个id查询标签信息
     * @param labelIds
     * @return
     */
    @Override
    public List<LabelsEntity> getLabelEntityByIds(List<Long> labelIds) {
        List<LabelsEntity> labelsEntities = this.baseMapper.selectList(
                new QueryWrapper<LabelsEntity>().in("label_id", labelIds));
        return labelsEntities;
    }

    /**
     * 根据标签ids查询标签信息
     * @param labelIds
     * @return
     */
    @Override
    public List<LabelsEntity> getLabelsByIds(List<Long> labelIds) {
        List<LabelsEntity> labelsEntities = this.baseMapper.selectList(
                new QueryWrapper<LabelsEntity>().in("label_id", labelIds));
        return labelsEntities;
    }

}