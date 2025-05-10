package com.ybb.apiDriver.service;

import com.ybb.apiDriver.feign.DriverUserFeignClient;
import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.DriverCarConstants;
import com.ybb.dto.DriverCarBindingRelationship;
import com.ybb.dto.DriverUserWorkStatus;
import com.ybb.dto.ResponseResult;
import com.ybb.apiDriver.feign.ServerOrderFeignClient;
import com.ybb.request.DriverGrabRequest;
import com.ybb.request.OrderRequest;
import com.ybb.response.OrderDriverResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 调用订单服务中各类接口
 */
@Service
public class OrderService {
    @Autowired
    private ServerOrderFeignClient serverOrderFeignClient;
    @Autowired
    private DriverUserFeignClient driverUserFeignClient;

    /**
     * 司机抢单
     *
     * @param driverPhone 司机电话
     * @param orderId     订单id
     * @param longitude   司机接单时经度
     * @param latitude    司机接单时纬度
     * @return
     */
    public ResponseResult grab(String driverPhone, Long orderId, String longitude, String latitude) {
        // 根据司机电话查询车辆信息
        ResponseResult<DriverCarBindingRelationship> relationShip = driverUserFeignClient.getDriverCarRelationShip(driverPhone);

        if (relationShip == null) {
            return ResponseResult.fail(CommonStateEnum.DRIVER_CAR_BIND_NOT_EXISTS.getCode(), CommonStateEnum.DRIVER_CAR_BIND_NOT_EXISTS.getMessage());
        }
        DriverCarBindingRelationship driverCarBindingRelationship = relationShip.getData();
        Long carId = driverCarBindingRelationship.getCarId();

        // 检查司机、车辆是否可用
        ResponseResult<OrderDriverResponse> availableDriver = driverUserFeignClient.getAvailableDriver(carId);
        if (availableDriver == null) {
            return ResponseResult.fail(CommonStateEnum.CAR_NOT_EXISTS.getCode(), CommonStateEnum.CAR_NOT_EXISTS.getMessage());
        }
        OrderDriverResponse orderData = availableDriver.getData();

        // 执行抢单
        DriverGrabRequest driverGrabRequest = new DriverGrabRequest();
        driverGrabRequest.setOrderId(orderId);
        driverGrabRequest.setDriverId(orderData.getDriverId());
        driverGrabRequest.setCarId(carId);
        driverGrabRequest.setDriverPhone(driverPhone);
        driverGrabRequest.setLicenseId(orderData.getLicenseId());
        driverGrabRequest.setVehicleNo(orderData.getVehicleNo());
        driverGrabRequest.setVehicleType(orderData.getVehicleType());
        driverGrabRequest.setReceiveOrderCarLatitude(latitude);
        driverGrabRequest.setReceiveOrderCarLongitude(longitude);

        ResponseResult responseResult = serverOrderFeignClient.orderGrab(driverGrabRequest);
        if (responseResult.getCode() != CommonStateEnum.SUCCESS.getCode()){
            return ResponseResult.fail(CommonStateEnum.ORDER_UPDATE_ERROR.getCode(),CommonStateEnum.ORDER_UPDATE_ERROR.getMessage());
        }

        // 修改司机的工作状态

        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(orderData.getDriverId());
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_SERVING);

        responseResult = driverUserFeignClient.changeWorkStatus(driverUserWorkStatus);
        if (responseResult.getCode() != CommonStateEnum.SUCCESS.getCode()){
            return ResponseResult.fail(CommonStateEnum.DRIVER_STATUS_UPDATE_ERROR.getCode(),CommonStateEnum.DRIVER_STATUS_UPDATE_ERROR.getMessage());
        }

        return ResponseResult.success();

    }

    /**
     * 去接乘客
     *
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest) {
        return serverOrderFeignClient.changeStatus(orderRequest);
    }


    /**
     * 到达乘客上车点
     *
     * @param orderRequest
     * @return
     */
    public ResponseResult arrivedDeparture(OrderRequest orderRequest) {
        return serverOrderFeignClient.arrivedDeparture(orderRequest);
    }


    public ResponseResult pickUpPassenger(OrderRequest orderRequest) {
        return serverOrderFeignClient.pickUpPassenger(orderRequest);
    }

    public ResponseResult passengerGetoff(OrderRequest orderRequest) {
        return serverOrderFeignClient.passengerGetOff(orderRequest);
    }
}
