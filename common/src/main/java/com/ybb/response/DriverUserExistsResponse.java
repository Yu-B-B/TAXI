package com.ybb.response;

import lombok.Data;

/**
 * 司机是否存在返回结果封装
 */
@Data
public class DriverUserExistsResponse {

    private String driverPhone;

    private int ifExists;
}