package com.ybb.apipassenger.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.DriverCarConstants;
import com.ybb.dto.*;
import com.ybb.mapper.CarMapper;
import com.ybb.mapper.DriverCarBindingRelationshipMapper;
import com.ybb.mapper.DriverUserMapper;
import com.ybb.mapper.DriverUserWorkStatusMapper;
import com.ybb.mock.DriverUserMockData;
import com.ybb.response.OrderDriverResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DriverUserService {
    @Autowired
    private DriverUserMapper driverUserMapper;
    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;
    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;
    @Autowired
    private CarMapper carMapper;
    /**
     * 司机做个人信息录入
     *
     * @param driverUser
     * @return
     */
    public ResponseResult addDriverUser(DriverUser driverUser) {
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtCreate(now);
        driverUser.setGmtModified(now);

        driverUserMapper.insert(driverUser);
        // 初始化 司机工作状态表
        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(driverUser.getId());
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_STOP); // 初始化司机工作状态
        driverUserWorkStatus.setGmtModified(now);
        driverUserWorkStatus.setGmtCreate(now);
        // TODO:插入一条司机信息
//        driverUserWorkStatusMapper.insert(driverUserWorkStatus);
        return ResponseResult.success("");
    }

    /**
     * 司机做个人信息修改
     *
     * @param driverUser
     * @return
     */
    public ResponseResult updateUser(DriverUser driverUser) {
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtModified(now);
        // TODO：司机修改个人信息放行
//        driverUserMapper.updateById(driverUser);
        return ResponseResult.success("已成功修改");
    }

    /**
     * 根据司机手机号判断用户是否存在
     *
     * @param driverPhone
     * @return
     */
    public ResponseResult<DriverUser> checkUserExistsWithPhone(String driverPhone) {
        Map<String, Object> map = new HashMap<>();
        map.put("driver_phone", driverPhone);
        map.put("state", DriverCarConstants.DRIVER_STATE_VALID);
        // TODO: 根据手机号与状态查询可用的司机信息
//        List<DriverUser> driverUsers = driverUserMapper.selectByMap(map);
//        if (driverUsers.isEmpty()) {
//            return ResponseResult.fail(CommonStateEnum.DRIVER_NOT_EXITST.getCode(), CommonStateEnum.DRIVER_NOT_EXITST.getMessage());
//        }
//        DriverUser driverUser = driverUsers.get(0);
        DriverUserMockData driverUserMockData = new DriverUserMockData();
        DriverUser driverUser = driverUserMockData.getDriverUser(driverPhone);
        return ResponseResult.success(driverUser);
    }

    /**
     * 检查当前城市是否有可用司机
     *
     * @return
     */
    public ResponseResult checkExistsUsefulDriver(String cityCode) {
        int count = driverUserMapper.selectDriverUserCountByCityCode(cityCode);
        if (count > 0) {
            return ResponseResult.success(true);
        } else {
            return ResponseResult.fail(false);
        }
    }

    /**
     * 根据车辆id获取司机信息
     * @param carId
     * @return
     */
    public ResponseResult<OrderDriverResponse> getAvailableDriver(Long carId) {
        LambdaQueryWrapper<DriverCarBindingRelationship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverCarBindingRelationship::getCarId,carId);
        wrapper.eq(DriverCarBindingRelationship::getBindState,DriverCarConstants.DRIVER_CAR_BIND);

        DriverCarBindingRelationship driverCarBindingRelationship = driverCarBindingRelationshipMapper.selectOne(wrapper);
        Long driverId = driverCarBindingRelationship.getDriverId(); // 司机ID
        // 司机工作状态的查询
        LambdaQueryWrapper<DriverUserWorkStatus> statusWrapper = new LambdaQueryWrapper<>();
        statusWrapper.eq(DriverUserWorkStatus::getDriverId,driverId);
        statusWrapper.eq(DriverUserWorkStatus::getWorkStatus,DriverCarConstants.DRIVER_WORK_STATUS_START);

        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatusMapper.selectOne(statusWrapper);
        if (null == driverUserWorkStatus){
            return ResponseResult.fail(CommonStateEnum.AVAILABLE_DRIVER_EMPTY.getCode(),CommonStateEnum.AVAILABLE_DRIVER_EMPTY.getMessage());

        }else {
            // 查询司机信息
            LambdaQueryWrapper<DriverUser> driverWrapper = new LambdaQueryWrapper<>();
            driverWrapper.eq(DriverUser::getId,driverId);
            DriverUser driverUser = driverUserMapper.selectOne(driverWrapper);
            // 查询车辆信息
            LambdaQueryWrapper<Car> carQueryWrapper = new LambdaQueryWrapper<>();
            carQueryWrapper.eq(Car::getId,carId);
            Car car = carMapper.selectOne(carQueryWrapper);

            OrderDriverResponse orderDriverResponse = new OrderDriverResponse();
            orderDriverResponse.setCarId(carId);
            orderDriverResponse.setDriverId(driverId);
            orderDriverResponse.setDriverPhone(driverUser.getDriverPhone());

            orderDriverResponse.setLicenseId(driverUser.getLicenseId());
            orderDriverResponse.setVehicleNo(car.getVehicleNo());
            orderDriverResponse.setVehicleType(car.getVehicleType());

            return ResponseResult.success(orderDriverResponse);
        }
    }
}
