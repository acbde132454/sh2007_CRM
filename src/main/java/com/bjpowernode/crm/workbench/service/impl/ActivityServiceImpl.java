package com.bjpowernode.crm.workbench.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.constans.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Activity> list(Activity activity,int page,int pageSize) {

        Example example = new Example(Activity.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StrUtil.isEmpty(activity.getName())){
            //名称不为空
            criteria.andLike("name","%" + activity.getName() + "%");
        }

        if(!StrUtil.isEmpty(activity.getOwner())){
            //所有者
            Example e = new Example(User.class);
            e.createCriteria().andLike("name","%" + activity.getOwner() + "%");
            List<User> users = userMapper.selectByExample(e);
            ArrayList<String> owners = new ArrayList<>();
            for (User user : users) {
                owners.add(user.getId());
            }
            criteria.andIn("owner",owners);
        }
        if(!StrUtil.isEmpty(activity.getStartDate())){
            //>=开始日期
            criteria.andGreaterThanOrEqualTo("startDate",activity.getStartDate());
        }
        if(!StrUtil.isEmpty(activity.getEndDate())){
            //<=结束日期
            criteria.andLessThanOrEqualTo("endDate",activity.getStartDate());
        }
        //等同于limit a,b page:每页的页码 pageSize:每页的数据个数
        PageHelper.startPage(page,pageSize);
        List<Activity> activityList = activityMapper.selectByExample(example);
        for (Activity activity1 : activityList) {
            User user = userMapper.selectByPrimaryKey(activity1.getOwner());
            activity1.setOwner(user.getName());
        }
        return activityList;
    }

    @Override
    public ResultVo saveOrUpdateActivity(Activity activity, User user) {
        ResultVo resultVo = new ResultVo();
        if(activity.getId() == "" || activity.getId() == null){
            //添加
            activity.setCreateBy(user.getName());
            activity.setCreateTime(DateTimeUtil.getSysTime());
            activity.setId(UUIDUtil.getUUID());
            //空值不插入null值 null和""
            int count = activityMapper.insertSelective(activity);
            if(count == 0){
                throw new CrmException(CrmEnum.LOGIN_ACCOUNT);
            }else{
                resultVo.setMess("添加市场活动成功");
            }
        }else{
            //修改
            activity.setEditTime(DateTimeUtil.getSysTime());
            activity.setEditBy(user.getName());
            //根据主键更新非null字段
            int count = activityMapper.updateByPrimaryKeySelective(activity);
            if(count == 0){
                throw new CrmException(CrmEnum.ACTIVITY_UPDATE);
            }else{
                resultVo.setMess("修改市场活动成功");
            }
        }

        return resultVo;

    }

    @Override
    public Activity queryById(String id) {
        return activityMapper.selectByPrimaryKey(id);
    }
}
