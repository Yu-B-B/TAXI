package com.ybb.service;

import com.ybb.dto.Car;
import com.ybb.dto.ResponseResult;
import com.ybb.feign.ServiceMapClient;
import com.ybb.mapper.CarMapper;
import com.ybb.response.TerminalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CarService {
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private ServiceMapClient serviceMapClient;

    public ResponseResult addCar(Car car) {
        LocalDateTime now = LocalDateTime.now();
        car.setGmtModified(now);
        car.setGmtCreate(now);
        carMapper.insert(car);
        // 增加：车辆与终端关联，【车牌号】与
        ResponseResult<TerminalResponse> terminal = serviceMapClient.addTerminal(car.getVehicleNo(), car.getId() + "");
        String tid = terminal.getData().getTid();

        car.setTid(tid);


        return ResponseResult.success("");
    }
}
