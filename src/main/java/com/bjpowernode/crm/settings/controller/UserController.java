package com.bjpowernode.crm.settings.controller;

import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/test01")
    public void test01(){
        System.out.println("test01");
    }

    @RequestMapping("/test02")
    public void test02(){
        User user = new User();
        user.setId("abc");
        userMapper.insert(user);
        System.out.println("123");
    }
}
