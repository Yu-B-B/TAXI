package com.ybb.dto;

import lombok.Data;

@Data
public class DicDirection {
    private String addressCode;
    private String addressName;
    private Integer level;
    private String parentAddressCode;
}
