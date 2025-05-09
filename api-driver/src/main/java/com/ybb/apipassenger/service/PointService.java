package com.ybb.apipassenger.service;

import com.ybb.apipassenger.feign.DriverUserFeignClient;
import com.ybb.apipassenger.feign.ServiceMapClient;
import com.ybb.dto.Car;
import com.ybb.dto.ResponseResult;
import com.ybb.request.ApiDriverPointRequest;
import com.ybb.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    private ServiceMapClient serviceMapClient;

    @Autowired
    private DriverUserFeignClient driverUserFeignClient;

    /**
     * 根据当前的位置信息上传到终端中
     * @param apiDriverPointRequest
     * @return
     */
    public ResponseResult upload(ApiDriverPointRequest apiDriverPointRequest){
        // 获取carId
        Long carId = apiDriverPointRequest.getCarId();

        // 通过carId 获取 tid、trid，调用 service-driver-user的接口
        ResponseResult<Car> carById = driverUserFeignClient.getCarById(carId);
        Car car = carById.getData();
        String tid = car.getTid();
        String trid = car.getTrid();

        // 调用地图去上传
        PointRequest pointRequest = new PointRequest();
        pointRequest.setTid(tid);
        pointRequest.setTrid(trid);
        pointRequest.setPoints(apiDriverPointRequest.getPoints());

        return serviceMapClient.upload(pointRequest);

    }
}
