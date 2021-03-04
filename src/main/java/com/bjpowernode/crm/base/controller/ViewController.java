package com.bjpowernode.crm.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

//专门负责视图跳转Controller
@Controller
public class ViewController {


    //workbench/main/index
    //workbench/customer/index
    @RequestMapping("/toView/{firstView}/{secondView}/{thirdView}")
    public String toView(
            @PathVariable("firstView") String firstView,
            @PathVariable("secondView") String secondView,
            @PathVariable("thirdView") String thirdView){

       // return "workbench/main/index";
        ///workbench/main/index
        return firstView + File.separator + secondView + File.separatorChar + thirdView;
    }
}
