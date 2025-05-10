package com.ybb.serviceorder.baseMethod;

import com.ybb.constant.OrderConstants;
import com.ybb.dto.OrderInfo;
import com.ybb.request.DriverGrabRequest;

import java.time.LocalDateTime;

public class DtoCopy {
    public static OrderInfo grabOrder(OrderInfo orderInfo, DriverGrabRequest request){
        // 填充订单信息
        Long driverId = request.getDriverId();
        Long carId = request.getCarId();
        String licenseId = request.getLicenseId();
        String vehicleNo = request.getVehicleNo();
        String receiveOrderCarLatitude = request.getReceiveOrderCarLatitude();
        String receiveOrderCarLongitude = request.getReceiveOrderCarLongitude();
        String vehicleType = request.getVehicleType();
        String driverPhone = request.getDriverPhone();

        orderInfo.setDriverId(driverId);
        orderInfo.setDriverPhone(driverPhone);
        orderInfo.setCarId(carId);

        orderInfo.setReceiveOrderCarLongitude(receiveOrderCarLongitude);
        orderInfo.setReceiveOrderCarLatitude(receiveOrderCarLatitude);
        orderInfo.setReceiveOrderTime(LocalDateTime.now());

        orderInfo.setLicenseId(licenseId);
        orderInfo.setVehicleNo(vehicleNo);

        orderInfo.setVehicleType(vehicleType);

        orderInfo.setOrderStatus(OrderConstants.DRIVER_RECEIVE_ORDER);
        return orderInfo;
    }
}
