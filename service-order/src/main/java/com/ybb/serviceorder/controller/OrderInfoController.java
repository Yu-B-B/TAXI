package com.ybb.serviceorder.controller;

import com.ybb.dto.OrderInfo;
import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;
import com.ybb.request.OrderRequest;
import com.ybb.serviceorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderInfoController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     *
     * @param orderRequest
     * @return
     */
    @PostMapping("/createOrder")
    public ResponseResult createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        //
//        String deviceCode = request.getHeader(HeaderParamConstants.DEVICE_CODE);
//        orderRequest.setDeviceCode(deviceCode);

        return orderService.createOrder(orderRequest);
    }

    /**
     * 创建预约单
     *
     * @param orderRequest
     * @return
     */
    @PostMapping("/bookingOrder")
    public ResponseResult bookingOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        return orderService.bookingOrder(orderRequest);
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public ResponseResult<OrderInfo> detail(Long orderId){
        return orderService.detail(orderId);
    }

    /**
     * 抢单业务
     * @param request
     * @return
     */
    @PostMapping("/order/grab")
    public ResponseResult orderGrab(@RequestBody DriverGrabRequest request) {
        return orderService.orderGrab(request);
    }


    /**
     * 循环获取附近终端
     * @param orderInfo
     * @return
     */
    @PostMapping("/dispatchOrder")
    public ResponseResult<Boolean> dispatchOrder(@RequestBody OrderInfo orderInfo) {
        return ResponseResult.success(orderService.dispatchRealTimeOrder(orderInfo));
    }

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult changeStatus(@RequestBody OrderRequest orderRequest){
        return orderService.toPickUpPassenger(orderRequest);
    }

    /**
     * 到达乘客上车点
     * @param orderRequest
     * @return
     */
    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest){
        return orderService.arrivedDeparture(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){
        return orderService.pickUpPassenger(orderRequest);
    }

    /**
     * 乘客到达目的地，行程终止
     * @param orderRequest
     * @return
     */
    @PostMapping("/passenger-getoff")
    public ResponseResult passengerGetoff(@RequestBody OrderRequest orderRequest){
        return orderService.passengerGetoff(orderRequest);
    }
}
