package com.qianbing.link.controller;

import com.qianbing.common.Result.R;
import com.qianbing.common.constrant.FriendLinkConstrant;
import com.qianbing.common.entity.FriendlyLinkEntity;
import com.qianbing.link.service.FriendlyLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-12-16 20:43:29
 */
@RestController
@RequestMapping
public class FriendlyLinkController {

    @Autowired
    private FriendlyLinkService friendlyLinkService;

    
    /**
     * 查询已通过审核的友链信息
     * @return
     */
    @RequestMapping("/getApprovedFriendLinks")
    public R getApprovedFriendLinks(){
        List<FriendlyLinkEntity> friendlyLinkEntities = friendlyLinkService.getApprovedFriendLinks();
        return R.ok().setData(friendlyLinkEntities);
    }

    /**
     * 添加友链信息
     * @param friendlyLink
     * @return
     */
    @RequestMapping("/addFriendLink")
    public R addFriendLink(@RequestBody FriendlyLinkEntity friendlyLink){
        friendlyLink.setLinkCreateTime(new Date());
        friendlyLink.setLinkAllow(0L);
		friendlyLinkService.save(friendlyLink);
        return R.ok(FriendLinkConstrant.LINK_ADD_SUCCESS);
    }


}
