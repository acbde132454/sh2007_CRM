package com.bjpowernode.crm.workbench.bean;

import com.bjpowernode.crm.settings.bean.User;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tbl_activity_remark")
@NameStyle(Style.normal)
public class ActivityRemark {

    @Id
    private String id;
    private String noteContent;
    private String createTime;
    private String createBy;
    private String editTime;
    private String editBy;
    private String editFlag;
    private String activityId;
    private String uid;

    private User user;
}
