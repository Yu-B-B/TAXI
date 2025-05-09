package com.ybb.request;

import lombok.Data;

/**
 * 司机抢单实体
 */
@Data
public class DriverGrabRequest {

    private Long orderId;  // 订单号
    private String receiveOrderCarLongitude; // 抢单时的经度
    private String receiveOrderCarLatitude; // 抢单时纬度
    private Long carId; // 车辆id
    private Long driverId; // 司机id
    private String licenseId; // 车架号
    private String vehicleNo; // 车辆编号
    private String vehicleType; // 车辆类型
    private String driverPhone; // 司机手机
}
