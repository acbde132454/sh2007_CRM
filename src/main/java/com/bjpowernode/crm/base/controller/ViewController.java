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
    @RequestMapping({"/toView/{firstView}/{secondView}/{thirdView}","/toView/{firstView}/{secondView}/{thirdView}/{fourthView}"})
    public String toView(
            @PathVariable(value = "firstView",required = false) String firstView,
            @PathVariable(value = "secondView",required = false) String secondView,
            @PathVariable(value = "thirdView",required = false) String thirdView,
            @PathVariable(value = "fourthView",required = false) String fourthView){

          if(fourthView != null){
              return firstView + File.separator + secondView + File.separatorChar + thirdView
                      +File.separator + fourthView;
          }

       // return "workbench/main/index";
        ///workbench/main/index
        return firstView + File.separator + secondView + File.separatorChar + thirdView;
    }
}
