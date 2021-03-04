package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;

import java.util.List;

public interface ActivityService {

    List<Activity> list(Activity activity,int page,int pageSize);

    ResultVo saveOrUpdateActivity(Activity activity, User user);

    Activity queryById(String id);
}
