package com.qianbing.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianbing.common.constrant.UserContrant;
import com.qianbing.common.entity.ArticlesEntity;
import com.qianbing.common.entity.UsersEntity;

import com.qianbing.common.utils.PageUtils;

import com.qianbing.user.dao.UsersDao;
import com.qianbing.user.entity.RegisterEntity;
import com.qianbing.user.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {

//    @Autowired
//    private UsersDao usersDao;

    @Autowired
    private ArticlesDao articlesDao;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UsersEntity> page = this.page(
                new Query<UsersEntity>().getPage(params),
                new QueryWrapper<UsersEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 用户注册
     * @param registerEntity
     * @param httpServletRequest
     * @return
     */
    @Override
    public Integer userRegister(RegisterEntity registerEntity, HttpServletRequest httpServletRequest) {
        checkPhone(registerEntity.getUserTelephoneNumber());
        String ip = YouaUtil.getClientIp(httpServletRequest);
        UsersEntity usersEntity = buildDefaultUser(registerEntity, ip);
        int insert = this.baseMapper.insert(usersEntity);
        //TODO 常量
        rabbitTemplate.convertAndSend("email.exchange", "email.welcome", usersEntity);
        return insert;
    }

    private UsersEntity buildDefaultUser(RegisterEntity registerEntity, String ip) {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setUserName(registerEntity.getUserTelephoneNumber());
        usersEntity.setUserIp(ip);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        usersEntity.setUserPassword(bCryptPasswordEncoder.encode(registerEntity.getUserPassword()));
        usersEntity.setUserTelephoneNumber(registerEntity.getUserTelephoneNumber());
        usersEntity.setUserNickname("66ds");
        usersEntity.setUserLock(0);
        usersEntity.setUserFreeze(0);
        usersEntity.setUserProfilePhoto("http://guliqianbing.oss-cn-beijing.aliyuncs.com/2020/11/27/daa494cd-d334-400b-8d2f-782310566bf6MrQian.jpg");
        return usersEntity;
    }

    @Override
    public R login(LoginVo vo) {
        //判断验证码
//        Boolean check = validateCodeService.check(vo.getKey(), vo.getCode());
//        if(!check){
//            throw new BlogException(CodeConstrant.CODE_ERROR);
//        }
        //判断账号和密码是否正确
        UsersEntity usersEntity = this.baseMapper.selectOne(new QueryWrapper<UsersEntity>().eq("user_telephone_number", vo.getUserTelephoneNumber()));
        if(usersEntity == null){
            //登录失败
            throw new BlogException(UserContrant.LOGIN_ERROR);
        }else{
            String pass = usersEntity.getUserPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(vo.getUserPassword(), pass);
            if(matches){
                String token = JwtUtils.geneJsonWebToken(usersEntity);
                //修改用户的状态变为在线
                usersEntity.setUserIsOnline(UserContrant.ONLINE_SUCCESS);
                this.baseMapper.updateById(usersEntity);
                return R.ok(UserContrant.LOGIN_SUCCRSS).setData(token);
            }else {
                throw new BlogException(UserContrant.LOGIN_ERROR);
            }
        }
    }

    @Override
    public R getUserInfoById(Integer userId) {
        UsersEntity usersEntity = this.baseMapper.selectById(userId);
        return R.ok().setData(usersEntity);
    }

    @Override
    public R selectCardInfo(Long userId) {
        //根据用户id查找所有的文章数
        UsersEntity usersEntity = this.baseMapper.selectById(userId);
        CardVo cardVo = new CardVo();
        cardVo.setUserId(usersEntity.getUserId());
        cardVo.setUserImg(usersEntity.getUserProfilePhoto());
        cardVo.setUserName(usersEntity.getUserNickname());
        List<ArticlesEntity> articlesEntities = articlesDao.selectList(new QueryWrapper<ArticlesEntity>().eq("user_id", userId));
        cardVo.setAllArticlesLikeNumber(articlesEntities.stream().mapToLong(item -> item.getArticleLikeCount()).sum());
        cardVo.setAllArticlesCommentsNumber(articlesEntities.stream().mapToLong(item -> item.getArticleCommentCount()).sum());
        cardVo.setAllArticleViewsNumber(articlesEntities.stream().mapToLong(item -> item.getArticleViews()).sum());
        cardVo.setAllArticlesNumber((long) articlesEntities.size());
        return R.ok().setData(cardVo);
    }

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @Override
    public UsersEntity getUserById(Long userId) {
        UsersEntity usersEntity = this.baseMapper.selectById(userId);
        return usersEntity;
    }

    private void checkPhone(String phone) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<UsersEntity>().eq("user_telephone_number", phone));
        if(count>0){
            throw new BlogException(UserContrant.PHONE_EXIST);
        }
    }
}