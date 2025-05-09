package com.ybb.serviceUser.controller;

import com.ybb.constant.DriverCarConstants;
import com.ybb.dto.DriverCarBindingRelationship;
import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import com.ybb.response.DriverUserExistsResponse;
import com.ybb.response.OrderDriverResponse;
import com.ybb.serviceUser.service.DriverCarBindingRelationshipService;
import com.ybb.serviceUser.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DriverUserController {
    @Autowired
    private DriverUserService driverUserService;

    @Autowired
    private DriverCarBindingRelationshipService driverCarBindingRelationshipService;

    /**
     * 新增司机
     *
     * @param driverUser
     * @return
     */
    @PostMapping("/user/insert")
    public ResponseResult addUser(@RequestBody DriverUser driverUser) {
        return driverUserService.addDriverUser(driverUser);
    }

    @PostMapping("/user/update")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser) {
        return driverUserService.updateUser(driverUser);
    }

    /**
     * 根据手机号验证司机是否存在
     *
     * @param checkPhone
     * @return
     */
    @GetMapping("/driver-user/{checkPhone}")
    public ResponseResult<DriverUserExistsResponse> checkDriver(@PathVariable("checkPhone") String checkPhone) {
        ResponseResult<DriverUser> driverUser = driverUserService.checkUserExistsWithPhone(checkPhone);
        DriverUser driverUserDb = driverUser.getData();

        DriverUserExistsResponse response = new DriverUserExistsResponse();

        int ifExists = DriverCarConstants.DRIVER_EXISTS;
        if (driverUserDb == null){
            ifExists = DriverCarConstants.DRIVER_NOT_EXISTS;
            response.setDriverPhone(checkPhone);
            response.setIfExists(ifExists);
        }else {
            response.setDriverPhone(driverUserDb.getDriverPhone());
            response.setIfExists(ifExists);
        }

        return ResponseResult.success(response);
    }

    /**
     * 检查当前城市是否有可用司机
     * @return
     */
    @GetMapping("/checkExistsUsefulDriver")
    public ResponseResult<Boolean> checkExistsUsefulDriver(String cityCode) {
        return driverUserService.checkExistsUsefulDriver(cityCode);
    }

    /**
     * 根据车辆Id查询订单需要的司机信息
     * @param carId
     * @return
     */
    @GetMapping("/get-available-driver/{carId}")
    public ResponseResult<OrderDriverResponse> getAvailableDriver(@PathVariable("carId") Long carId){
        return driverUserService.getAvailableDriver(carId);
    }

    /**
     * 根据司机手机号查询司机和车辆绑定关系
     * @param driverPhone
     * @return
     */
    @GetMapping("/driver-car-binding-relationship")
    public ResponseResult<DriverCarBindingRelationship> getDriverCarRelationShip(@RequestParam String driverPhone){
        return driverCarBindingRelationshipService.getDriverCarRelationShipByDriverPhone(driverPhone);
    }
}
