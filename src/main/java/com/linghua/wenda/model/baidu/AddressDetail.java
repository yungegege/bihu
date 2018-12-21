package com.linghua.wenda.model.baidu;

import lombok.Data;

@Data
public class AddressDetail {

    /**
     * 城市
     */
    private String city;

    /**
     * 百度城市代码
     */
    private String city_code;

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
    private String street_number;
}
