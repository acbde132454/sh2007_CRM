package com.bjpowernode.crm.base.bean;

import lombok.Data;

import java.util.List;

/**
 * 1、给客户端返回普通消息
 * 2、给客户返回操作是否成功
 * 3、给客户端返回需要的结果集
 * setIsOk
 */
@Data
public class ResultVo<T,V> {

    private boolean isOk;//操作是否成功
    private String mess;//返回的消息

    private T t;
    private V v;
    private List<T> data;

}
