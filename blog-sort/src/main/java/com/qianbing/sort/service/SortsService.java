package com.qianbing.sort.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.SortsEntity;
import com.qianbing.common.utils.PageUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2020-11-12 16:12:50
 */
public interface SortsService extends IService<SortsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SortsEntity> findCatagorysByUserId(Integer userId);

    R saveSort(SortsEntity sorts);

    R updateSorts(SortsEntity sorts);

    R deleteSorts(Long sortId,Integer userId);

    /**
     * 查询所有分类
     * @param userId
     * @return
     */
    List<SortsEntity> getAllSorts(Long userId);

    /**
     * 查看某个分类下的所有文章
     * @param sortId
     * @param map
     * @return
     */
    PageUtils getArticlesBySortId(Long sortId,Map<String,Object> map);

    /**
     * 根据分类id查询分类信息
     * @param sortId
     * @return
     */
    SortsEntity getSortEntityById(@PathVariable("sortId") Long sortId);
}

