package com.linghua.wenda.model.baidu;

import lombok.Data;

@Data
public class Content {

    /**
     * 简要地址信息
     */
    private String address;

    /**
     * 结构化地址信息
     */
    private AddressDetail address_detail;

    /**
     * 当前城市中心点
     */
    private Point point;
}
