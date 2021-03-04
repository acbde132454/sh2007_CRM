package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.settings.bean.User;

public interface UserService {

    User login(User user) throws CrmException;

    void verifyOldPwd(User user) throws CrmException;

    void updatePwd(User user);
}
