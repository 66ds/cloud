package com.qianbing.label.controller;

import com.qianbing.common.Result.R;
import com.qianbing.common.entity.LabelsEntity;
import com.qianbing.label.service.LabelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-10 15:44:24
 */
@RestController
@RequestMapping
public class LabelsController {

    @Autowired
    private LabelsService labelsService;

    /**
     * 所有标签查询
     * @param userId
     * @return
     */
//    @Cacheable(value = {"labels"}, key = "#root.methodName",sync = true)//代表当前的结果需要缓存,如果缓存中有,方法都不调用,没有就调用方法
    @RequestMapping("/getLabelsList")
    public R list(Long userId){
        List<LabelsEntity> list = labelsService.selectListInfo(userId);
        return R.ok().setData(list);
    }

    /**
     * 根据多个id查询标签信息
     * @param labelIds
     * @return
     */
    @RequestMapping("/getLabelEntityByIds")
    public List<LabelsEntity> getLabelEntityByIds(@RequestParam("labelIds") List<Long> labelIds){
        return labelsService.getLabelEntityByIds(labelIds);
    }

    /**
     * 根据标签ids查询标签信息
     * @param labelIds
     * @return
     */
    @RequestMapping("/getLabelsByIds")
    public List<LabelsEntity> getLabelsByIds(@RequestBody List<Long> labelIds){
        return labelsService.getLabelsByIds(labelIds);
    }

}
