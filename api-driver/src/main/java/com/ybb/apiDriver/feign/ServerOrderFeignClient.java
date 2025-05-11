package com.ybb.apiDriver.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;
import com.ybb.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-order")
public interface ServerOrderFeignClient {

    /**
     * 抢单业务
     * @param request
     * @return
     */
    @PostMapping("/order/grab")
    ResponseResult orderGrab(@RequestBody DriverGrabRequest request);


    /**
     * 去接乘客
     *
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    ResponseResult changeStatus(@RequestBody OrderRequest orderRequest);

    /**
     * 到达乘客上车点
     *
     * @param orderRequest
     * @return
     */
    @PostMapping("/arrived-departure")
    ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest);

    /**
     * 接到乘客
     *
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest);

    @PostMapping("/passenger-getoff")
    ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest);


    void pushPayInfo(OrderRequest orderRequest);
}
