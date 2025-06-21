package com.qianbing.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.StayMessageEntity;
import com.qianbing.common.utils.PageUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-12-13 20:46:28
 */
public interface StayMessageService extends IService<StayMessageEntity> {

    /**
     * 获取留言列表
     * @param params
     * @return
     */
    PageUtils getStayMessageList(Map<String, Object> params);

    /**
     * 添加留言
     * @param stayMessage
     * @param request
     * @return
     */
    R addStayMessage(StayMessageEntity stayMessage, HttpServletRequest request);

    StayMessageEntity selectStayInfo(Long stayId);

    R deleteBatchByIds(List<Long> asList);

    R delete(Long stayId);
}

