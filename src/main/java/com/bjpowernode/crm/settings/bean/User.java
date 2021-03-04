package com.bjpowernode.crm.settings.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tbl_user")
@NameStyle(Style.normal)
public class User {

    @Id
    private String id;//主键 UUID处理32位长度永远不重复的字符串序列
    private String loginAct;//登陆账号
    private String name;//昵称
    private String loginPwd;//登陆密码
    private String email;//邮箱
    private String expireTime;//失效时间 char 19 yyyy-MM-dd hh:mm:ss
    private String lockState;//是否被锁定 0时表示锁定，为1时表示启用
    private String deptno;//所在部门
    private String allowIps;//允许登陆的ip
    private String createTime;//创建时间
    private String createBy;//创建者
    private String editTime;//编辑时间
    private String editBy;//编辑者
    private String img;//用户头像路径

}
