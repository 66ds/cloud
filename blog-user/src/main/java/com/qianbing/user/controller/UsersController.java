package com.qianbing.user.controller;

import com.qianbing.common.Result.R;
import com.qianbing.common.constrant.SmsContrant;
import com.qianbing.common.entity.UsersEntity;
import com.qianbing.common.exception.BizCodeExcetionEnum;
import com.qianbing.common.exception.BlogException;
import com.qianbing.user.entity.RegisterEntity;
import com.qianbing.user.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 
 *
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2025-11-09 09:12:57
 */
@RestController
@RequestMapping
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 修改个人用户信息
     * @param users
     * @return
     */
    @RequestMapping("/updateUserInfo")
    public R update(@RequestBody UsersEntity users){
        usersService.updateById(users);
        return R.ok().setData(users);
    }

    /**
     * 获取某用户的名片信息
     * @param userId
     * @return
     */
    @RequestMapping("/getCardInfo/{userId}")
    public R selectCardInfo(@PathVariable("userId") Long userId){
        return usersService.selectCardInfo(userId);
    }

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @RequestMapping("/getUserById/{userId}")
    public UsersEntity getUserById(@PathVariable("userId") Long userId){
        return usersService.getUserById(userId);
    }

    /**
     * 用户注册
     * @param registerEntity
     * @param result
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public R userRegister(@RequestBody @Validated RegisterEntity registerEntity, BindingResult result, HttpServletRequest httpServletRequest){
        //注册之前验证
        String code = registerEntity.getCode();
        if(StringUtils.isEmpty(code)){
            return R.error(BizCodeExcetionEnum.CODE_NOT_NULL.getCode(),BizCodeExcetionEnum.CODE_NOT_NULL.getMsg());
        }
        String redisCode = stringRedisTemplate.opsForValue().get(SmsContrant.SMS_CODE + registerEntity.getUserTelephoneNumber());//sms:code:18852734080
        //进行注册
        if(!StringUtils.isEmpty(redisCode)){
            String[] verficaty = redisCode.split("_");
            if(code.equals(verficaty[0])){
                //删除验证码,令牌
                stringRedisTemplate.delete(SmsContrant.SMS_CODE+registerEntity.getUserTelephoneNumber());
                //进行注册,判断是否注册成功
                Integer insertNumber = usersService.userRegister(registerEntity, httpServletRequest);
                return insertNumber > 0 ? R.error(BizCodeExcetionEnum.USER_NAME_EXIST_EXCEPTION.getCode(),BizCodeExcetionEnum.USER_NAME_EXIST_EXCEPTION.getMsg()):R.ok();
            }
        }
        return R.error(BizCodeExcetionEnum.CODE_ERROR.getCode(),BizCodeExcetionEnum.CODE_ERROR.getMsg());
    }
}
