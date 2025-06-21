package com.qianbing.message.controller;

import com.qianbing.common.Result.R;
import com.qianbing.common.entity.StayMessageEntity;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.message.service.StayMessageService;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-12-13 20:46:28
 */
@RestController
@RequestMapping
public class StayMessageController {

    @Autowired
    private StayMessageService stayMessageService;

    /**
     * 获取留言列表
     * @param params
     * @return
     */
    @RequestMapping("/getStayMessageList")
    public R getStayMessageList(@RequestParam Map<String, Object> params){
        PageUtils page = stayMessageService.getStayMessageList(params);
        return R.ok().setData(page);
    }


    /**
     * 添加留言
     * @param stayMessage
     * @param request
     * @return
     */
    @RequestMapping("/addStayMessage")
    public R addStayMessage(@RequestBody StayMessageEntity stayMessage, HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String sys = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String chrome = userAgent.getBrowser().getName();
        Integer userId = (Integer) request.getAttribute("id");
        stayMessage.setStayUserId(userId.longValue());
        stayMessage.setStaySys(sys);
        stayMessage.setStayChrome(chrome);
		return stayMessageService.addStayMessage(stayMessage,request);
    }


}
