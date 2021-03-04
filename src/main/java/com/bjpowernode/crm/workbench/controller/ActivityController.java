package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ActivityController {

    @Autowired
    private ActivityService activityService;


    //分页功能
    @RequestMapping("/workbench/activity/list")
    @ResponseBody
    public ResultVo list(@RequestParam(defaultValue = "1") int page,int pageSize,
        Activity activity){
        ResultVo resultVo = new ResultVo();
        List<Activity> activityList = activityService.list(activity,page,pageSize);
        PageInfo pageInfo = new PageInfo(activityList);
        resultVo.setT(pageInfo);
        resultVo.setV(activity);
        return resultVo;
    }

    //异步添加/修改市场活动
    @RequestMapping("/workbench/activity/saveOrUpdateActivity")
    @ResponseBody
    public ResultVo saveOrUpdateActivity(Activity activity, HttpSession session){
        User user = (User) session.getAttribute("user");
        ResultVo resultVo = null;
        try{
            resultVo  = activityService.saveOrUpdateActivity(activity,user);
            resultVo.setOk(true);
        }catch (CrmException e){
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //根据主键查询
    @RequestMapping("/workbench/activity/queryById")
    @ResponseBody
    public Activity queryById(String id){
        Activity activity = activityService.queryById(id);
        return activity;
    }
}
