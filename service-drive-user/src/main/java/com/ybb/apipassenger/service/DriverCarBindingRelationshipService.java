package com.ybb.apipassenger.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.DriverCarConstants;
import com.ybb.dto.DriverCarBindingRelationship;
import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import com.ybb.mapper.DriverCarBindingRelationshipMapper;
import com.ybb.mapper.DriverUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverCarBindingRelationshipService {
    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;
    @Autowired
    private DriverUserMapper driverUserMapper;

    public ResponseResult bind(DriverCarBindingRelationship driverCarBindingRelationship){
        // 判断，如果参数中的车辆和司机，已经做过绑定，则不允许再次绑定
        LambdaQueryWrapper<DriverCarBindingRelationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DriverCarBindingRelationship::getDriverId,driverCarBindingRelationship.getDriverId());
        queryWrapper.eq(DriverCarBindingRelationship::getCarId,driverCarBindingRelationship.getCarId());
        queryWrapper.eq(DriverCarBindingRelationship::getBindState, DriverCarConstants.DRIVER_CAR_BIND);

        Integer integer = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if ((integer.intValue() > 0)){
            // 司机与车辆已存在绑定关系
            return ResponseResult.fail(CommonStateEnum.DRIVER_CAR_BIND_EXISTS.getCode(),CommonStateEnum.DRIVER_CAR_BIND_EXISTS.getMessage());
        }

        // 司机被绑定了
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DriverCarBindingRelationship::getDriverId,driverCarBindingRelationship.getDriverId());
        queryWrapper.eq(DriverCarBindingRelationship::getBindState,DriverCarConstants.DRIVER_CAR_BIND);
        integer = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if ((integer.intValue() > 0)){
            // 司机已被绑定
            return ResponseResult.fail(CommonStateEnum.DRIVER_BIND_EXISTS.getCode(),CommonStateEnum.DRIVER_BIND_EXISTS.getMessage());
        }

        // 车辆被绑定了
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DriverCarBindingRelationship::getCarId,driverCarBindingRelationship.getCarId());
        queryWrapper.eq(DriverCarBindingRelationship::getBindState,DriverCarConstants.DRIVER_CAR_BIND);
        integer = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if ((integer.intValue() > 0)){
            // 车辆已被绑定
            return ResponseResult.fail(CommonStateEnum.CAR_BIND_EXISTS.getCode(),CommonStateEnum.CAR_BIND_EXISTS.getMessage());
        }

        LocalDateTime now = LocalDateTime.now();
        driverCarBindingRelationship.setBindingTime(now);

        driverCarBindingRelationship.setBindState(DriverCarConstants.DRIVER_CAR_BIND);

        driverCarBindingRelationshipMapper.insert(driverCarBindingRelationship);
        return ResponseResult.success("");

    }

    public ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship){
        LocalDateTime now = LocalDateTime.now();

        Map<String,Object> map = new HashMap<>();
        map.put("driver_id",driverCarBindingRelationship.getDriverId()); // 司机id
        map.put("car_id",driverCarBindingRelationship.getCarId()); // 车辆id
        map.put("bind_state", DriverCarConstants.DRIVER_CAR_BIND); // 存在绑定关系的内容

        List<DriverCarBindingRelationship> driverCarBindingRelationships = driverCarBindingRelationshipMapper.selectByMap(map);
        if (driverCarBindingRelationships.isEmpty()){
            return ResponseResult.fail(CommonStateEnum.DRIVER_CAR_BIND_NOT_EXISTS.getCode(),CommonStateEnum.DRIVER_CAR_BIND_NOT_EXISTS.getMessage());
        }

        DriverCarBindingRelationship relationship = driverCarBindingRelationships.get(0);
        relationship.setBindState(DriverCarConstants.DRIVER_CAR_UNBIND);
        relationship.setUnBindingTime(now);

        driverCarBindingRelationshipMapper.updateById(relationship);
        return ResponseResult.success("");

    }

    public ResponseResult<DriverCarBindingRelationship> getDriverCarRelationShipByDriverPhone(@RequestParam String driverPhone){
        QueryWrapper<DriverUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_phone",driverPhone);

        DriverUser driverUser = driverUserMapper.selectOne(queryWrapper);
        Long driverId = driverUser.getId();

        QueryWrapper<DriverCarBindingRelationship> driverCarBindingRelationshipQueryWrapper = new QueryWrapper<>();
        driverCarBindingRelationshipQueryWrapper.eq("driver_id",driverId);
        driverCarBindingRelationshipQueryWrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);

        DriverCarBindingRelationship driverCarBindingRelationship = driverCarBindingRelationshipMapper.selectOne(driverCarBindingRelationshipQueryWrapper);
        return ResponseResult.success(driverCarBindingRelationship);

    }
}
