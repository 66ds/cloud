package com.qianbing.link.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.FriendlyLinkEntity;
import com.qianbing.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-12-16 20:43:29
 */
public interface FriendlyLinkService extends IService<FriendlyLinkEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询已通过审核的友链信息
     * @return
     */
    List<FriendlyLinkEntity> getApprovedFriendLinks();

    R updateLinkInfo(FriendlyLinkEntity friendlyLink);
}

