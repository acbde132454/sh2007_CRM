package com.bjpowernode.crm.workbench.controller;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.crm.base.bean.ChartsVo;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Transaction;
import com.bjpowernode.crm.workbench.bean.TransactionHistory;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/workbench/transaction/queryCustomerName")
    @ResponseBody
    public List<String> queryCustomerName(String customerName){
        List<String> names = transactionService.queryCustomerName(customerName);
        return names;
    }

    //根据选中的阶段查询对应的可能性
    @RequestMapping("/workbench/transaction/queryPossibility")
    @ResponseBody
    public String queryPossibility(String stage, HttpSession session){
        Map<String,String> stage2Possibility =
                (Map<String, String>) session.getServletContext().getAttribute("stage2Possibility");
        return stage2Possibility.get(stage);
    }

    //跳转到交易详情页面
    @RequestMapping("/workbench/transaction/queryById")
    public String queryById(String id, Model model){
        Transaction transaction = transactionService.queryById(id);
        model.addAttribute("transaction",transaction);
        return "/workbench/transaction/detail";
    }

    //查询当前交易的交易图标，还能实现点击图标修改图标信息
    @RequestMapping("/workbench/transaction/queryStage")
    @ResponseBody
    public List<Map<String,String>> queryStage(String id,HttpSession session,Integer index){
        Map<String,String> stage2Possibility =
                (Map<String, String>) session.getServletContext().getAttribute("stage2Possibility");
        User user = (User) session.getAttribute("user");
        List<Map<String,String>> stages = transactionService.queryStage(id,stage2Possibility,index,user);
        return stages;
    }

    //查询当前交易的交易历史
    @RequestMapping("/workbench/transaction/queryHistories")
    @ResponseBody
    public List<TransactionHistory> queryHistories(String id){
        List<TransactionHistory> transactionHistories = transactionService.queryHistories(id);
        return transactionHistories;
    }

    @RequestMapping("/workbench/transaction/chart")
    @ResponseBody
    public ChartsVo chart(){
        ChartsVo pieChartsVo = transactionService.chart();
       // String json = JSONObject.toJSONString(pieChartsVo);
        return pieChartsVo;
    }
}
