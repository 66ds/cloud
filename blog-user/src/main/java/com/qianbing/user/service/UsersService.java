package com.qianbing.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qianbing.common.Result.R;
import com.qianbing.common.entity.UsersEntity;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.user.entity.RegisterEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-09 09:12:57
 */
public interface UsersService extends IService<UsersEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用户注册
     * @param registerEntity
     * @param httpServletRequest
     * @return
     */
    Integer userRegister(RegisterEntity registerEntity, HttpServletRequest httpServletRequest);

    R login(LoginVo vo);

    R getUserInfoById(Integer userId);

    R selectCardInfo(Long userId);

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    UsersEntity getUserById(Long userId);
}

