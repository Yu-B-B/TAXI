package com.ybb.serviceUser.controller;

import com.ybb.dto.DriverUserWorkStatus;
import com.ybb.dto.ResponseResult;
import com.ybb.serviceUser.service.DriverUserWorkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver-work-status")
public class DriverUserWorkStatusController {

    @Autowired
    DriverUserWorkStatusService driverUserWorkStatusService;

    /**
     * 司机出车
     * @param driverUserWorkStatus
     * @return
     */
    @PostMapping("/change-status")
    public ResponseResult changeWorkStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus){

        return driverUserWorkStatusService.changeWorkStatus(driverUserWorkStatus.getDriverId(), driverUserWorkStatus.getWorkStatus());
    }

    @GetMapping("/get-status")
    public ResponseResult<DriverUserWorkStatus> getWorkStatus(Long driverId){
        return driverUserWorkStatusService.getWorkStatus(driverId);
    }
}