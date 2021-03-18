package com.bjpowernode.crm.base.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 饼状图的vo
 */
@Data
public class ChartsVo {


    private List<String> titles;//标题
    //private List<Map<String,String>> dataList;//具体的数据
    private List<Charts> charts;

    //柱状图
    private List<String> counts;//柱状图的数据
}
