package com.qianbing.sort.controller;

import com.qianbing.common.Result.R;
import com.qianbing.common.entity.SortsEntity;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.sort.service.SortsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-12 16:12:50
 */
@RestController
@RequestMapping
public class SortsController {

    @Autowired
    private SortsService sortsService;

    /**
     * 查询所有分类
     * @param userId
     * @return
     */
    @RequestMapping("/getAllSorts")
    public R getAllSorts(Long userId){
        List<SortsEntity> list = sortsService.getAllSorts(userId);
        return R.ok().setData(list);
    }

    /**
     * 查看某个分类下的所有文章
     * @param sortId
     * @param map
     * @return
     */
    @RequestMapping("/getArticlesBySortId/{sortId}")
    public R getArticlesBySortId(@PathVariable("sortId") Long sortId,@RequestBody Map<String,Object> map){
        PageUtils pageUtils = sortsService.getArticlesBySortId(sortId,map);
        return R.ok().setData(pageUtils);
    }

    /**
     * 根据分类id查询分类信息
     * @param sortId
     * @return
     */
    @RequestMapping("/getSortEntityById/{sortId}")
    public SortsEntity getSortEntityById(@PathVariable("sortId") Long sortId){
        SortsEntity sortsEntity = sortsService.getSortEntityById(sortId);
        return sortsEntity;
    }
}
