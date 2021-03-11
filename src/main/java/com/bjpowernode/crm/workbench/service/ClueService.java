package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.Clue;

import java.util.List;

public interface ClueService {
    List<Clue> list(Clue clue);

    Clue clueDetail(String id);

    List<Activity> relationActivity(String name,String id);

    ResultVo bind(String ids, String id);

    ResultVo unbind(String activityId, String id);

    ResultVo queryActivityAsync(String id);

    Clue queryById(String id);

    void transfer(String id, User user);
}
