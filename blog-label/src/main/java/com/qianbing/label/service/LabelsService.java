package com.qianbing.label.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.LabelsEntity;
import com.qianbing.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-12 16:12:50
 */
public interface LabelsService extends IService<LabelsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<LabelsEntity> selectListInfo(Long userId);

    /**
     * 根据多个id查询标签信息
     */
    List<LabelsEntity> getLabelEntityByIds(List<Long> labelIds);

    /**
     * 根据标签ids查询标签信息
     * @param labelIds
     * @return
     */
    List<LabelsEntity> getLabelsByIds(List<Long> labelIds);

    /**
     * 插入多个标签信息
     * @param labelsEntities
     * @return
     */
    void saveLabelEntities(List<LabelsEntity> labelsEntities);
}

