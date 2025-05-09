package com.ybb.serviceUser.service;

import com.ybb.dto.Car;
import com.ybb.dto.ResponseResult;
import com.ybb.serviceUser.feign.ServiceMapClient;
import com.ybb.serviceUser.mapper.CarMapper;
import com.ybb.response.TerminalResponse;
import com.ybb.response.TrackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // 增加：车辆与终端关联，【车牌号】，增加车辆与终端关联，carId
        ResponseResult<TerminalResponse> terminal = serviceMapClient.addTerminal(car.getVehicleNo(), car.getId() + "");
        String tid = terminal.getData().getTid();
        car.setTid(tid);

        // 增加：初始化轨迹
        ResponseResult<TrackResponse> trackResponse = serviceMapClient.addTrack(tid);
        String trid = trackResponse.getData().getTrid();
        String trname = trackResponse.getData().getTrname();

        car.setTrname(trname);
        car.setTrid(trid);

        carMapper.updateById(car);

        return ResponseResult.success("");
    }

    /**
     * 通过车牌查询车俩信息
     * @param carId
     * @return
     */
    public ResponseResult<Car> getCarById(Long carId) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",carId);

        List<Car> cars = carMapper.selectByMap(map);

        return ResponseResult.success(cars.get(0));
    }
}
