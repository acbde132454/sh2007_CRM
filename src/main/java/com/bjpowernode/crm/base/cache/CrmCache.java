package com.bjpowernode.crm.base.cache;

import com.bjpowernode.crm.base.bean.DictionaryType;
import com.bjpowernode.crm.base.bean.DictionaryValue;
import com.bjpowernode.crm.base.mapper.DictionaryTypeMapper;
import com.bjpowernode.crm.base.mapper.DictionaryValueMapper;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.*;

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
            //查询每个数据字典种类下的所有字典值
            Example example = new Example(DictionaryValue.class);
            example.setOrderByClause("orderNo");
            example.createCriteria().andEqualTo("typeCode",dictionaryType.getCode());
            List<DictionaryValue> dictionaryValues = dictionaryValueMapper.selectByExample(example);
            data.put(dictionaryType.getCode(),dictionaryValues);
        }
        servletContext.setAttribute("data",data);

        //缓冲阶段和可能性
        Map<String,String> stage2Possibility = new TreeMap<>();
        //找到属性文件 文件的后缀名不用写
        ResourceBundle resourceBundle = ResourceBundle.getBundle("mybatis.Stage2Possibility");
        Enumeration<String> keys = resourceBundle.getKeys();
        //hasMoreElements:判断是否可以取出更多的元素
        while(keys.hasMoreElements()){
            //获取每一个元素
            String key = keys.nextElement();
            String value = resourceBundle.getString(key);
            stage2Possibility.put(key,value);
        }
        servletContext.setAttribute("stage2Possibility",stage2Possibility);
    }
}
