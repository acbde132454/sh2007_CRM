package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Transaction;
import com.bjpowernode.crm.workbench.bean.TransactionHistory;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    List<String> queryCustomerName(String customerName);

    Transaction queryById(String id);

    List<Map<String,String>> queryStage(String id, Map<String,String> stage2Possibility, Integer index, User user);

    List<TransactionHistory> queryHistories(String id);
}
