package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<String> queryCustomerName(String customerName) {
        Example example = new Example(Customer.class);
        example.createCriteria().andLike("name","%" + customerName + "%");
        List<Customer> customers = customerMapper.selectByExample(example);

        List<String> names = new ArrayList<>();
        for (Customer customer : customers) {
            names.add(customer.getName());
        }
        return names;
    }

    @Override
    public Transaction queryById(String id) {
        Transaction transaction = transactionMapper.selectByPrimaryKey(id);
        //查询所有者信息
        User user = userMapper.selectByPrimaryKey(transaction.getOwner());
        transaction.setOwner(user.getName());
        //查询公司信息
        Customer customer = customerMapper.selectByPrimaryKey(transaction.getCustomerId());
        transaction.setCustomerId(customer.getName());

        //查询联系人信息
        Contacts contacts = contactsMapper.selectByPrimaryKey(transaction.getContactsId());
        transaction.setContactsId(contacts.getFullname());

        //查询市场活动源
        Activity activity = activityMapper.selectByPrimaryKey(transaction.getActivityId());
        transaction.setActivityId(activity.getName());
        //查询交易备注信息
        TransactionRemark transactionRemark = new TransactionRemark();
        transactionRemark.setTranId(id);
        List<TransactionRemark> transactionRemarks = transactionRemarkMapper.select(transactionRemark);
        transaction.setTransactionRemarks(transactionRemarks);
        //交易历史信息
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTranId(id);
        List<TransactionHistory> transactionHistories = transactionHistoryMapper.select(transactionHistory);
        transaction.setTransactionHistories(transactionHistories);
        return transaction;
    }

    @Override
    public  List<Map<String,String>> queryStage(String id,Map<String,String> stage2Possibility,Integer index,User user) {
        Transaction transaction = transactionMapper.selectByPrimaryKey(id);
        int pointer = 0;
        String currentStage = null;
        String currentPossibility = null;

        //为了方便使用索引遍历阶段和可能性，把map转换成list集合
        List<Map.Entry<String, String>> list = new ArrayList(stage2Possibility.entrySet());
        //查询交易可能性为0的第一个阶段的索引位置
        for (int i = 0; i < list.size(); i++) {
            String possibility = list.get(i).getValue();
            if(possibility.equals("0")){
                pointer = i;
                break;
            }
        }
        //判断是第一次查询阶段图标还是点击阶段图标修改图标状态
        if(index == null){
            //查询当前的交易对象
            //第一次查询图标
            //获取当前交易的所处的阶段
            currentStage = transaction.getStage();

            //获取当前交易的可能性 为了判断失败情况的临界点
            currentPossibility = transaction.getPossibility();

            //交易中(包含成功)
            //获取当前交易所处阶段索引位置
            for (int i = 0; i < list.size(); i++) {
                String stage = list.get(i).getKey();
                if(stage.equals(currentStage)){
                    index = i;
                    break;
                }
            }
        }else{
            currentStage = list.get(index).getKey();
            currentPossibility = list.get(index).getValue();
            //点击图标，更新数据库信息
            transaction.setStage(currentStage);
            transaction.setPossibility(currentPossibility);
            transaction.setId(id);
            transactionMapper.updateByPrimaryKeySelective(transaction);
            //添加交易历史记录
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setId(UUIDUtil.getUUID());
            transactionHistory.setTranId(id);
            transactionHistory.setStage(currentStage);
            transactionHistory.setMoney(transaction.getMoney());
            transactionHistory.setExpectedDate(transaction.getExpectedDate());
            transactionHistory.setCreateTime(DateTimeUtil.getSysTime());
            transactionHistory.setCreateBy(user.getName());
            transactionHistory.setPossibility(transaction.getPossibility());
            transactionHistoryMapper.insertSelective(transactionHistory);
        }

        //使用list存储交易图标
        List<Map<String,String>> stages = new ArrayList<>();

        //交易失败，只能判断前7个是黑圈，后2个黑x
        if (currentPossibility.equals("0")) {
            for (int i = 0; i < list.size(); i++) {
               Map<String,String> stagesMap = new HashMap<>();
               String stage = list.get(i).getKey();
               String possibility = list.get(i).getValue();
                stagesMap.put("text",stage);
                stagesMap.put("possibility",possibility);
                //当前阶段所处的索引
                stagesMap.put("index",i+"");

                //交易失败，所有阶段第一次处于失败情况
               if("0".equals(possibility)){
                 if(currentStage.equals(stage)){
                     //当前阶段的图标为红x
                     System.out.println("红x");
                     stagesMap.put("type","红x");
                 }else{
                     System.out.println("黑x");
                     stagesMap.put("type","黑x");
                 }
               }else{
                   System.out.println("黑圈");
                   stagesMap.put("type","黑圈");
               }
                stages.add(stagesMap);
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                Map<String,String> stagesMap = new HashMap<>();
                String stage = list.get(i).getKey();
                String possibility = list.get(i).getValue();
                stagesMap.put("text",stage);
                stagesMap.put("possibility",possibility);
                //当前阶段所处的索引
                stagesMap.put("index",i+"");
                if(i < index){
                    System.out.println("绿圈");
                    stagesMap.put("type","绿圈");
                }else if(i == index){
                    System.out.println("锚点");
                    stagesMap.put("type","锚点");
                }else if(i > index && i < pointer){
                    System.out.println("黑圈");
                    stagesMap.put("type","黑圈");
                }else{
                    System.out.println("黑x");
                    stagesMap.put("type","黑x");
                }
                stages.add(stagesMap);
            }
        }
        return stages;
    }

    @Override
    public List<TransactionHistory> queryHistories(String id) {
        //交易历史信息
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTranId(id);
        List<TransactionHistory> transactionHistories = transactionHistoryMapper.select(transactionHistory);
        return transactionHistories;
    }
}
