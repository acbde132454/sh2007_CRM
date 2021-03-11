package com.bjpowernode.crm.base.cache;

import com.bjpowernode.crm.base.bean.DictionaryType;
import com.bjpowernode.crm.base.bean.DictionaryValue;
import com.bjpowernode.crm.base.mapper.DictionaryTypeMapper;
import com.bjpowernode.crm.base.mapper.DictionaryValueMapper;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据缓冲类
 *
 */
@Component
public class CrmCache {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private DictionaryTypeMapper dictionaryTypeMapper;

    @Autowired
    private DictionaryValueMapper dictionaryValueMapper;

    @PostConstruct
    public void init(){
       //查询用户表所有数据
        List<User> users = userMapper.selectAll();
        servletContext.setAttribute("users",users);

        //使用Map<String,List<DictionValue>>数据结构进行存储
        //查询所有数据字典类型
        List<DictionaryType> dictionaryTypes = dictionaryTypeMapper.selectAll();

        //存储字典数据类型及其类型对应的值
        Map<String,List<DictionaryValue>> data = new HashMap<>();
        for (DictionaryType dictionaryType : dictionaryTypes) {
            DictionaryValue dictionaryValue = new DictionaryValue();
            dictionaryValue.setTypeCode(dictionaryType.getCode());
            //查询每个数据字典种类下的所有字典值
            List<DictionaryValue> dictionaryValues = dictionaryValueMapper.select(dictionaryValue);
            data.put(dictionaryType.getCode(),dictionaryValues);
        }
        servletContext.setAttribute("data",data);
    }
}
