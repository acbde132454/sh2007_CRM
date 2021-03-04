package com.bjpowernode.crm.test;

import com.bjpowernode.crm.base.constans.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.MD5Util;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tk.mybatis.mapper.entity.Example;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestCrm {

    //测试添加操作
    @Test
    public void test01(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId("def");
        userMapper.insert(user);

        /*BasicDataSource dataSource = (BasicDataSource) beanFactory.getBean("dataSource");
        System.out.println(dataSource);*/
    }

    //测试修改
    @Test
    public void test02(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId("def");
        user.setName("王五");
        userMapper.updateByPrimaryKey(user);
    }

    //测试删除
    @Test
    public void test03(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.deleteByPrimaryKey("def");
    }

    //测试根据条件查询：等值查询
    @Test
    public void test04(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //把所有查询条件封装到对象中
        User user = new User();
        user.setName("张三");
        user.setExpireTime("2050-02-10");
        List<User> users = userMapper.select(user);
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    //测试根据example查询
    @Test
    public void test05(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Example example = new Example(User.class);
        //离线查询对象
        Example.Criteria criteria = example.createCriteria();
        //参数一:给实体类哪个属性赋值，参数二:实际的参数
       criteria.andEqualTo("name", "张三").andEqualTo("expireTime","2050-02-10");
        List<User> users = userMapper.selectByExample(example);

        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void test06(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Example example = new Example(User.class);
        //离线查询对象
        Example.Criteria criteria = example.createCriteria();
        //参数一:给实体类哪个属性赋值，参数二:实际的参数
        criteria.andLike("name", "张");
        List<User> users = userMapper.selectByExample(example);

        for (User user : users) {
            System.out.println(user);
        }
    }

    //根据主键查询
    @Test
    public void test07(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey("c82990f8101a4e51b0e5036e933625ab");

        System.out.println(user);
    }

    //UUID生成主键
    @Test
    public void test08(){
        String value = UUID.randomUUID().toString();
        System.out.println(value);
    }


    //测试自定义异常
    @Test
    public void test09(){
        int a = 0;
       try{
           if(a == 0){
               throw new CrmException(CrmEnum.LOGIN_ACCOUNT);
           }
       }catch (CrmException e){
           System.out.println(e.getMessage());
       }
    }

    //字符串大小比较
    @Test
    public void test10(){
        String expireTime = "2008-02-10 10:10:10";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String now = sdf.format(new Date());
        System.out.println(expireTime.compareTo(now));
    }

    //测试加密
    @Test
    public void test11(){
        System.out.println(MD5Util.getMD5("admin"));
    }
}
