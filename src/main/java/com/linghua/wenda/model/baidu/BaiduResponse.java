package com.linghua.wenda.model.baidu;

import lombok.Data;

@Data
public class BaiduResponse {

    /**
     * 详细地址信息
     */
    private String address;

    /**
     * 结构信息
     */
    private Content content;

    /**
     * 结果状态返回码
     */
    private String status;
}
