package com.bjpowernode.crm.settings.controller;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.FileUploadUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


   //用户登录功能
    @RequestMapping("/settings/user/login")
    public String login(User user, Model model, HttpServletRequest request, HttpSession session){
        try {
            //获取用户登陆IP
            String remoteAddr = request.getRemoteAddr();
            user.setAllowIps(remoteAddr);
            user = userService.login(user);
            //登录成功，把信息信息保存在session中
            session.setAttribute("user",user);
        } catch (CrmException e) {
            //e.printStackTrace();
            //当前该方法就是向request域放值的
            model.addAttribute("message",e.getMessage());
            return "../login";
        }
        return "/workbench/index";
    }

    //用户登录功能
    @RequestMapping("/settings/user/asyncLogin")
    @ResponseBody
    public ResultVo asyncLogin(User user,HttpServletRequest request){
        ResultVo resultVo = new ResultVo();
        try {
            //获取用户登陆IP
            String remoteAddr = request.getRemoteAddr();
            user.setAllowIps(remoteAddr);
            userService.login(user);
            resultVo.setOk(true);
            resultVo.setMess("登陆成功");
        } catch (CrmException e) {
           resultVo.setOk(false);
           resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //登出功能
    @RequestMapping("/settings/user/loginOut")
    public String loginOut(HttpSession session){
        //将用户从session中移除
        session.removeAttribute("user");
        //跳转到登陆页面
        return "../login";
    }
    //异步校验用户输入的原始密码是否正确
    @RequestMapping("/settings/user/verifyOldPwd")
    @ResponseBody
    public ResultVo verifyOldPwd(User user,HttpServletRequest request){
        ResultVo resultVo = new ResultVo();
        try {
            userService.verifyOldPwd(user);
            resultVo.setOk(true);
        } catch (CrmException e) {
            resultVo.setOk(false);
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //异步上传文件
    @RequestMapping("/settings/user/fileUpload")
    @ResponseBody
    public ResultVo fileUpload(MultipartFile img,HttpServletRequest request){
        ResultVo resultVo = new ResultVo();
        try {
            String uploadPath = FileUploadUtil.fileUpload(img,request);
            resultVo.setOk(true);
            resultVo.setMess("上传头像成功");
            request.getSession().setAttribute("uploadPath",uploadPath);
            //返回上传文件路径
            resultVo.setT(uploadPath);
        } catch (CrmException e) {
          e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultVo;
    }

    //更新密码
    @RequestMapping("/settings/user/updatePwd")
    @ResponseBody
    public ResultVo updatePwd(User user,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try {
            String loginPwd = user.getLoginPwd();
            //去除登陆用户的信息
            user = (User) session.getAttribute("user");
            //去除上传图片的路径
            String uploadPath = (String) session.getAttribute("uploadPath");
            user.setImg(uploadPath);
            user.setLoginPwd(loginPwd);
            userService.updatePwd(user);
            resultVo.setOk(true);
            resultVo.setMess("更新密码成功");
        } catch (CrmException e) {
            resultVo.setOk(false);
            resultVo.setMess("更新密码失败");
            e.printStackTrace();
        }
       return resultVo;
    }
}
