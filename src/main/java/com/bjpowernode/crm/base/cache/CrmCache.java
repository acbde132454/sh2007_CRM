package com.bjpowernode.crm.base.cache;

import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.List;

/**
 * 数据缓冲类
 *
 */
@Component
public class CrmCache {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ServletContext servletContext;

    @PostConstruct
    public void init(){
       //查询用户表所有数据
        List<User> users = userMapper.selectAll();
        servletContext.setAttribute("users",users);
    }
}
