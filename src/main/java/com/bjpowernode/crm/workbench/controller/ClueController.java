package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.Transaction;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ClueController {

    @Autowired
    private ClueService clueService;

    @RequestMapping("/workbench/clue/list")
    @ResponseBody
    public ResultVo list(Clue clue){
        ResultVo resultVo = new ResultVo();
        List<Clue> clueList = clueService.list(clue);
        resultVo.setData(clueList);
        return resultVo;
    }

    //异步查询线索关联的市场活动

    /**
     *
     * @param id:线索的id号
     * @return resultVo包含弹出的消息和重新刷新页面的数据
     */
    @RequestMapping("/workbench/clue/queryActivityAsync")
    @ResponseBody
    public ResultVo queryActivityAsync(String id){
        ResultVo resultVo = clueService.queryActivityAsync(id);
        return resultVo;
    }

    //从市场活动列表页面跳转到详情页  转发
    @RequestMapping("/workbench/clue/clueDetail")
    public String clueDetail(String id, Model model){
        Clue clue = clueService.clueDetail(id);
        model.addAttribute("clue",clue);
        return "/workbench/clue/detail";
    }

    //模糊查询线索可以关联的市场活动
    @RequestMapping("/workbench/clue/relationActivity")
    @ResponseBody
    public List<Activity> relationActivity(String name,String id){
        List<Activity> activityList = clueService.relationActivity(name,id);
        return activityList;
    }

    //绑定选中的市场活动
    @RequestMapping("/workbench/clu/bind")
    @ResponseBody
    public ResultVo bind(String ids,String id){
        ResultVo resultVo = null;
        try{
            resultVo = clueService.bind(ids,id);
            resultVo.setOk(true);
            resultVo.setMess("关联市场活动成功");
        }catch (CrmException e){
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }

        return resultVo;
    }

    @RequestMapping("/workbench/clu/unbind")
    @ResponseBody
    public ResultVo unbind(String activityId,String id){
        ResultVo resultVo = null;
        try{
            resultVo = clueService.unbind(activityId,id);
            resultVo.setOk(true);
            resultVo.setMess("解除关联市场活动成功");
        }catch (CrmException e){
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //点击转换按钮，查询线索数据，跳转到线索转换页面
    @RequestMapping("/workbench/clue/toTransferView")
    public String toTransferView(String id,Model model){
        Clue clue =clueService.queryById(id);
        model.addAttribute("clue",clue);
        return "workbench/clue/convert";
    }

    //线索转换
    @RequestMapping("/workbench/clue/transfer")
    @ResponseBody
    public ResultVo transfer(String id, String isTran, HttpSession session, Transaction transaction){
        ResultVo resultVo = new ResultVo();
        User user = (User) session.getAttribute("user");
        try{
            clueService.transfer(id,user,isTran,transaction);
            resultVo.setOk(true);
            resultVo.setMess("线索转换成功");
        }catch (CrmException e){
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }
}
