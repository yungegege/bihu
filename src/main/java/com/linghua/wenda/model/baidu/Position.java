package com.linghua.wenda.model.baidu;

import lombok.Data;

import java.util.Date;

@Data
public class Position {

    /**
     * 主键
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 调用时间
     */
    private Date executeTime;

    /**
     * 访问url
     */
    private String url;

    /**
     * 详细地址
     */
    private String allAddress;

    /**
     * 简要地址
     */
    private String simpleAddress;

    /**
     * 城市
     */
    private String city;
    /**
     * 百度城市代码
     */
    private String cityCode;
    /**
     * 区县
     */
    private String district;
    /**
     * 省份
     */
    private String province;
    /**
     * 街道
     */
    private String street;
    /**
     * 门牌号
     */
    private String streetNumber;
    /**
     * 当前城市中心点经度
     */
    private String x;
    /**
     * 当前城市中心点纬度
     */
    private String y;
}
