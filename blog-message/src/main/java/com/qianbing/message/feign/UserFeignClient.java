package com.qianbing.message.feign;

import com.qianbing.common.entity.UsersEntity;
import com.qianbing.message.feign.failback.UserFeignClientFailBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "blog-message", fallback = UserFeignClientFailBack.class)
public interface UserFeignClient {

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @RequestMapping("/getUserById/{userId}")
    public UsersEntity getUserById(@PathVariable("userId") Long userId);
}
