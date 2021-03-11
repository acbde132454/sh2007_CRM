package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    //删除市场活动
    @RequestMapping("/workbench/activity/deleteActivity")
    @ResponseBody
    public ResultVo deleteActivity(String id){
        ResultVo resultVo = new ResultVo();
        try{
            activityService.deleteActivity(id);
            resultVo.setOk(true);
            resultVo.setMess("删除市场活动成功");
        }catch (CrmException e){
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //查询市场活动详情
    @RequestMapping("/workbench/activity/queryDetail")
    public String queryDetail(String id, Model model){
        Activity activity = activityService.queryDetail(id);
        model.addAttribute("activity",activity);
        return "workbench/activity/detail";
    }

    //保存市场活动备注
    @RequestMapping("/workbench/activity/saveRemark")
    @ResponseBody
    public ResultVo saveRemark(ActivityRemark activityRemark,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            User user = (User) session.getAttribute("user");
            activityRemark.setCreateBy(user.getName());
            activityRemark.setUser(user);
            activityRemark = activityService.saveRemark(activityRemark);
            resultVo.setOk(true);
            resultVo.setMess("保存市场活动备注成功");
            resultVo.setT(activityRemark);
        }catch (CrmException e){
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //异步更新市场活动备注
    @RequestMapping("/workbench/activity/updateRemark")
    @ResponseBody
    public ResultVo updateRemark(ActivityRemark activityRemark,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            User user = (User) session.getAttribute("user");
            activityRemark.setEditBy(user.getName());
            activityRemark = activityService.updateRemark(activityRemark);
            resultVo.setOk(true);
            resultVo.setMess("更新市场活动备注成功");
            //resultVo.setT(activityRemark);
        }catch (CrmException e){
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //删除备注
    @RequestMapping("/workbench/activity/delRemark")
    @ResponseBody
    public ResultVo delRemark(String id){
        ResultVo resultVo = new ResultVo();
        try{
            activityService.delRemark(id);
            resultVo.setOk(true);
            resultVo.setMess("删除市场活动备注成功");
        }catch (CrmException e){
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }
}
