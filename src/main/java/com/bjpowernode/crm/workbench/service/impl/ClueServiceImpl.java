package com.bjpowernode.crm.workbench.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.constans.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public List<Clue> list(Clue clue) {
        Example example = new Example(Clue.class);
        Example.Criteria criteria = example.createCriteria();
        //名称模糊查询
        if(StrUtil.isNotEmpty(clue.getFullname())){
            criteria.andLike("fullname","%" + clue.getFullname() + "%");
        }
        //公司模糊查询
        if(StrUtil.isNotEmpty(clue.getCompany())){
            criteria.andLike("company","%" + clue.getCompany() + "%");
        }
        //公司座机模糊查询
        if(StrUtil.isNotEmpty(clue.getPhone())){
            criteria.andLike("phone","%" + clue.getPhone() + "%");
        }
        //线索来源精准查询
        if(StrUtil.isNotEmpty(clue.getSource())){
            criteria.andEqualTo("source",clue.getSource());
        }
        //所有者模糊查询
        if(StrUtil.isNotEmpty(clue.getOwner())){
            //根据用户输入所有者查询满足条件的用户
            Example e = new Example(User.class);
            e.createCriteria().andLike("name","%" + clue.getOwner() + "%");
            List<User> users = userMapper.selectByExample(e);

            //把用户主键存储在计集合中
            List<String> ids = new ArrayList<>();
            for (User user : users) {
                ids.add(user.getId());
            }
            criteria.andIn("owner",ids);
        }
        //手机模糊查询
        if(StrUtil.isNotEmpty(clue.getMphone())){
            criteria.andLike("mphone","%" + clue.getMphone() + "%");
        }

        //线索状态精准查询
        if(StrUtil.isNotEmpty(clue.getState())){
            criteria.andEqualTo("state",clue.getState());
        }
        List<Clue> clueList = clueMapper.selectByExample(example);
        return clueList;
    }

    @Override
    public Clue clueDetail(String id) {
        //根据主键查询线索
        Clue clue = clueMapper.selectByPrimaryKey(id);
        //查询线索备注信息
        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setClueId(id);
        List<ClueRemark> clueRemarks = clueRemarkMapper.select(clueRemark);
        clue.setClueRemarks(clueRemarks);

        //查询所有者
        User user = userMapper.selectByPrimaryKey(clue.getOwner());
        clue.setOwner(user.getName());

        //查询线索关联的市场活动 得关联查询中间表
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(id);
        List<ClueActivityRelation> clueActivityRelations =
                clueActivityRelationMapper.select(clueActivityRelation);
        List<Activity> activities = new ArrayList<>();
        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            //根据中间表查询出来的市场活动外键关联查询市场活动
            Activity activity = activityMapper.selectByPrimaryKey(activityRelation.getActivityId());
            activities.add(activity);
        }
        clue.setActivities(activities);
        return clue;
    }

    @Override
    public List<Activity> relationActivity(String name,String id) {


        //查询当前线索已经关联的市场活动
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(id);
        List<ClueActivityRelation> clueActivityRelations =
                clueActivityRelationMapper.select(clueActivityRelation);
        List<String> ids = new ArrayList<>();
        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            //根据中间表查询出来的市场活动外键关联查询市场活动
            ids.add(activityRelation.getActivityId());
        }
        Example example = new Example(Activity.class);
        Example.Criteria criteria = example.createCriteria();
        //没有输入名称，查询所有，排除已经关联的市场活动
        if(StrUtil.isNotEmpty(name)){
            criteria.andLike("name","%" + name + "%")
                    .andNotIn("id",ids);

        }else{
            criteria.andNotIn("id",ids);
        }
        //排除当前线索已经关联的市场活动

        List<Activity> activityList = activityMapper.selectByExample(example);
        return activityList;
    }

    @Override
    public ResultVo bind(String ids,String id) {
        ResultVo resultVo = new ResultVo();
        String activityIds[] = ids.split(",");
        for (String activityId : activityIds) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setClueId(id);
            clueActivityRelation.setActivityId(activityId);
            int count = clueActivityRelationMapper.insertSelective(clueActivityRelation);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_ACTIVITY_BIND);
            }

        }
        //插入成功，再次查询关联的市场活动
        Clue clue = clueDetail(id);
        resultVo.setT(clue);
        return resultVo;
    }

    @Override
    public ResultVo unbind(String activityId, String id) {
        //删除中间表即可
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setActivityId(activityId);
        clueActivityRelation.setClueId(id);
        int count = clueActivityRelationMapper.delete(clueActivityRelation);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_ACTIVITY_BIND);
        }
        //解除关联成功，再次查询关联的市场活动
        ResultVo resultVo = new ResultVo();
        Clue clue = clueDetail(id);
        resultVo.setT(clue);
        return resultVo;
    }

    @Override
    public ResultVo queryActivityAsync(String id) {
        ResultVo resultVo = new ResultVo();
        Clue clue = clueDetail(id);
        resultVo.setT(clue);
        return resultVo;
    }

    @Override
    public Clue queryById(String id) {
        return clueMapper.selectByPrimaryKey(id);
    }

    @Override
    public void transfer(String id,User user,String isTran,Transaction transaction) {
        int count = 0;
        //先把线索表中关于客户的信息抽取出来，保存在客户表中
        Clue clue = clueMapper.selectByPrimaryKey(id);

        Customer customer = new Customer();
        customer.setName(clue.getCompany());
        List<Customer> customers = customerMapper.select(customer);
        //系统中无该客户
        if(customers.size() == 0){
            //先查询线索中的客户在系统中存不存在，不存在才创建新客户
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(clue.getCompany());
            customer.setOwner(clue.getOwner());
            customer.setWebsite(clue.getWebsite());
            customer.setCreateBy(user.getName());
            customer.setPhone(clue.getPhone());
            count = customerMapper.insertSelective(customer);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_TRANSFER);
            }
        }

        //把线索中联系人信息抽取出来保存在联系人表中
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getName());
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        //联系人所在公司
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        count = contactsMapper.insertSelective(contacts);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_TRANSFER);
        }
        //把线索中的备注信息保存到联系人备注中
        ContactsRemark contactsRemark = new ContactsRemark();
        contactsRemark.setId(UUIDUtil.getUUID());
        contactsRemark.setContactsId(contacts.getId());
        contactsRemark.setCreateBy(contacts.getCreateBy());
        contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
        contactsRemarkMapper.insertSelective(contactsRemark);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_TRANSFER);
        }
        //把线索中的备注信息保存到客户备注中
        CustomerRemark customerRemark = new CustomerRemark();
        customerRemark.setId(UUIDUtil.getUUID());
        customerRemark.setCreateBy(customer.getCreateBy());
        customerRemark.setCreateTime(DateTimeUtil.getSysTime());
        customerRemark.setCustomerId(customer.getId());
        count = customerRemarkMapper.insertSelective(customerRemark);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_TRANSFER);
        }

        //将"线索和市场活动的关系"转换到"联系人和市场活动的关系中"
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(id);
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.select(clueActivityRelation);
        for (ClueActivityRelation clueActivityRelation1 : clueActivityRelations) {
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(clueActivityRelation1.getActivityId());
            contactsActivityRelation.setContactsId(contacts.getId());
            count = contactsActivityRelationMapper.insert(contactsActivityRelation);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_TRANSFER);
            }
        }

        //如果转换过程中发生了交易，创建一条新的交易，交易信息不全，后面可以通过修改交易来完善交易信息
        if(StrUtil.isNotEmpty(isTran)){
            //发生交易
            //创建交易对象
            transaction.setId(UUIDUtil.getUUID());
            transaction.setContactsId(contacts.getId());
            transaction.setCreateBy(user.getName());
            transaction.setCreateTime(DateTimeUtil.getSysTime());
            transaction.setCustomerId(customer.getId());
            transaction.setOwner(clue.getOwner());
            count = transactionMapper.insertSelective(transaction);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_TRANSFER);
            }

            //创建交易历史
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setId(UUIDUtil.getUUID());
            transactionHistory.setCreateBy(user.getName());
            transactionHistory.setCreateTime(DateTimeUtil.getSysTime());
            transactionHistory.setExpectedDate(transaction.getExpectedDate());
            transactionHistory.setMoney(transaction.getMoney());
            transactionHistory.setStage(transaction.getStage());
            transactionHistory.setTranId(transaction.getId());
            count = transactionHistoryMapper.insertSelective(transactionHistory);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_TRANSFER);
            }

            //创建交易备注
            TransactionRemark transactionRemark = new TransactionRemark();
            transactionRemark.setId(UUIDUtil.getUUID());
            transactionRemark.setCreateBy(user.getName());
            transactionRemark.setCreateTime(DateTimeUtil.getSysTime());
            transactionRemark.setEditFlag("0");
            transactionRemark.setTranId(transaction.getId());
            count = transactionRemarkMapper.insertSelective(transactionRemark);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_TRANSFER);
            }
        }

        //删除线索的备注信息
        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setClueId(clue.getId());
        count = clueRemarkMapper.delete(clueRemark);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_TRANSFER);
        }

        //删除线索和市场活动的关联关系，因为在第5步已经将"线索和市场活动的关系
        // "转换到"联系人和市场活动的关系中"
        ClueActivityRelation clueActivityRelation1 = new ClueActivityRelation();
        clueActivityRelation1.setClueId(clue.getId());
        count = clueActivityRelationMapper.delete(clueActivityRelation1);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_TRANSFER);
        }

        //删除线索
        count = clueMapper.deleteByPrimaryKey(id);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_TRANSFER);
        }
    }


}
