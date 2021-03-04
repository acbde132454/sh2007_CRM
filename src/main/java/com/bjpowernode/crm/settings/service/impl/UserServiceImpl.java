package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.base.constans.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.MD5Util;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(User user) throws CrmException{
        //先获取用户登录的ip地址
        String allowIp = user.getAllowIps();
        String loginPwd = MD5Util.getMD5(user.getLoginPwd());
        user.setLoginPwd(loginPwd);
        //把允许的ip置为null，目的是为了当前这个属性不参与查询，或者使用Example查询
        user.setAllowIps(null);
        user = userMapper.selectOne(user);
        //校验用户名和密码是否正确
        if(user == null){
            throw new CrmException(CrmEnum.LOGIN_ACCOUNT);
        }else{
            //账号是否失效
            String expireTime = user.getExpireTime();
            if(expireTime.compareTo(DateTimeUtil.getSysTime()) < 0){
                //账号失效了
                throw new CrmException(CrmEnum.LOGIN_EXPIRE);
            }
            //账号是否被锁定
            if("0".equals(user.getLockState())){
                //账号被锁定
                throw new CrmException(CrmEnum.LOGIN_LOCKED);
            }
            //登陆IP地址
            String allowIps = user.getAllowIps();
            if(!allowIps.contains(allowIp)){
                //不包含登陆的ip
                throw new CrmException(CrmEnum.LOGIN_ALLOWEDIP);
            }
        }
        return user;

    }

    @Override
    public void verifyOldPwd(User user) throws CrmException {
        String loginPwd = MD5Util.getMD5(user.getLoginPwd());
        user = userMapper.selectByPrimaryKey(user);
        if(!user.getLoginPwd().equals(loginPwd)){
            throw new CrmException(CrmEnum.LOGIN_LOGINPWD);
        }
    }

    @Override
    public void updatePwd(User user) {
        user.setLoginPwd(MD5Util.getMD5(user.getLoginPwd()));
        int count = userMapper.updateByPrimaryKey(user);
        if(count == 0){
            throw new CrmException(CrmEnum.LOGIN_UPLOAD);
        }
    }
}
