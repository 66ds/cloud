package com.qianbing.message.feign.failback;

import com.qianbing.common.entity.UsersEntity;
import com.qianbing.message.feign.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * FeignClient兜底回调，FailBack；
 * 可以返回缓存，假数据，默认数据等
 */
@Slf4j
@Component
public class UserFeignClientFailBack implements UserFeignClient {

    @Override
    public UsersEntity getUserById(Long userId) {
        return null;
    }
}
