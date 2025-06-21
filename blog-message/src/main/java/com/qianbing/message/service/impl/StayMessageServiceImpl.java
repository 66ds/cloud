package com.qianbing.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianbing.common.Result.R;
import com.qianbing.common.constrant.StayMessageConstrant;
import com.qianbing.common.entity.StayMessageEntity;
import com.qianbing.common.entity.UsersEntity;
import com.qianbing.common.utils.Constant;
import com.qianbing.common.utils.PageUtils;
import com.qianbing.common.utils.Query;
import com.qianbing.message.dao.StayMessageDao;
import com.qianbing.message.feign.UserFeignClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("stayMessageService")
public class StayMessageServiceImpl extends ServiceImpl<StayMessageDao, StayMessageEntity> implements StayMessageService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    @Transactional //TODO 分布式事务
    public PageUtils getStayMessageList(Map<String, Object> params) {
        String content = (String) params.get("content");
        //查询父留言id为0的所有留言
        QueryWrapper<StayMessageEntity> queryWrapper = new QueryWrapper<StayMessageEntity>();
        if(!StringUtils.isEmpty(content)){
            queryWrapper.like("message_content",content);
        }
        queryWrapper.eq("parent_stay_id",0);
        //设置排序字段
        params.put(Constant.ORDER_FIELD, "message_stay_time");
        //设置降序
        params.put(Constant.ORDER, "desc");
        IPage<StayMessageEntity> page = this.page(
                new Query<StayMessageEntity>().getPage(params),
                queryWrapper
        );
        List<StayMessageEntity> stayMessageEntities = page.getRecords();
        stayMessageEntities = stayMessageEntities.stream().map(item -> {
            //远程调用（根据用户id查询用户信息）
            UsersEntity pEntity = userFeignClient.getUserById(item.getStayUserId());
            item.setUserName(pEntity.getUserNickname());
            item.setUserImg(pEntity.getUserProfilePhoto());
            //设置子评论
            StayMessageEntity entity = this.baseMapper.selectOne(new QueryWrapper<StayMessageEntity>().eq("parent_stay_id", item.getStayId()));
            if(!StringUtils.isEmpty(entity)){
                //远程调用（查询子评论用户信息）【因为只有一条所以不递归了】
                UsersEntity cNtity = userFeignClient.getUserById(item.getStayUserId());
                entity.setUserName(cNtity.getUserNickname());
                entity.setUserImg(cNtity.getUserProfilePhoto());
            }
            item.setStayMessageEntity(entity);
            return item;
        }).collect(Collectors.toList());
        page.setRecords(stayMessageEntities);
        return new PageUtils(page);
    }

    @Override
    public R addStayMessage(StayMessageEntity stayMessage, HttpServletRequest request) {
        stayMessage.setMessageStayTime(new Date());
        //设置留言者的登录Ip
        String ipAddr = this.getClientIp(request);
        stayMessage.setStayUserIp(ipAddr);
        this.baseMapper.insert(stayMessage);
        return R.ok(StayMessageConstrant.STAY_MESSAGE_SUCCESS);
    }

    /**
     * 获取客户端ip
     * @param request 参数
     * @author Qianbing
     * @since 2025-04-22
     * @return String
     */
    public static String getClientIp(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if(checkIsIp(ip)){
            return ip;
        }

        ip = request.getHeader("X-Real-IP");
        if(checkIsIp(ip)){
            return ip;
        }

        ip = request.getRemoteAddr();
        if(ip.equals("0:0:0:0:0:0:0:1")){
            //本地 localhost访问 ipv6
            ip = "127.0.0.1";
        }
        if(checkIsIp(ip)){
            return ip;
        }

        return "";
    }

    /**
     * 检测是否为ip
     * @param ip 参数
     * @author Mr.Zhang
     * @since 2025-04-22
     * @return String
     */
    public static boolean checkIsIp(String ip){
        if(org.apache.commons.lang.StringUtils.isBlank(ip)){
            return false;
        }

        if(ip.equals("unKnown")){
            return false;
        }

        if(ip.equals("unknown")){
            return false;
        }

        return ip.split("\\.").length == 4;
    }

    @Override
    public StayMessageEntity selectStayInfo(Long stayId) {
        StayMessageEntity parent_stay_id = this.baseMapper.selectOne(new QueryWrapper<StayMessageEntity>().eq("parent_stay_id", stayId));
        return parent_stay_id;
    }

    @Transactional
    @Override
    public R deleteBatchByIds(List<Long> asList) {
        //如果有管理员留言则一起删除(没有就删除用户留言)
        this.baseMapper.deleteBatchIds(asList);
        this.baseMapper.delete(new QueryWrapper<StayMessageEntity>().in("parent_stay_id",asList));
        return R.ok(StayMessageConstrant.STAY_DELETE_SUCCESS);
    }

    @Transactional
    @Override
    public R delete(Long stayId) {
        //如果有管理员留言则一起删除(没有就删除用户留言)
        this.baseMapper.deleteById(stayId);
        this.baseMapper.delete(new QueryWrapper<StayMessageEntity>().eq("parent_stay_id",stayId));
        return R.ok(StayMessageConstrant.STAY_DELETE_SUCCESS);
    }

}