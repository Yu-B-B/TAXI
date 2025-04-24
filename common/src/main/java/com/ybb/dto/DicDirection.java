package com.ybb.dto;

import lombok.Data;

/**
 * 地区字典表
 */
@Data
public class DicDirection {
    private String addressCode;
    private String addressName;
    private Integer level;
    private String parentAddressCode;
}
