package com.bjpowernode.crm.base.constans;

//枚举，定义业务类型
public enum CrmEnum {

    LOGIN_ACCOUNT("001-001","用户名或密码错误"),
    LOGIN_EXPIRE("001-002","账户失效"),
    LOGIN_LOCKED("001-003","账户被锁定"),
    LOGIN_ALLOWEDIP("001-004","不允许登陆的IP"),
    LOGIN_LOGINPWD("001-005","原始密码不正确"),
    LOGIN_UPLOAD("001-006","上传头像失败"),
    ACTIVITY_SAVE("002-001","添加市场活动失败"),
    ACTIVITY_UPDATE("002-002","更新市场活动失败");

    private String code;
    private String message;

    CrmEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
