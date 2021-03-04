package com.bjpowernode.crm.settings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Table;

@Controller
public class ResutfulController {

    @RequestMapping("/test01/{name}/{age}")
    public void test01(@PathVariable("name") String name, @PathVariable("age") int age){
        System.out.println(name + ":" + age);
    }
}
