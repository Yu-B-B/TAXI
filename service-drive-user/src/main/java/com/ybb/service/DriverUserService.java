package com.ybb.service;

import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.DriverCarConstants;
import com.ybb.dto.DriverUser;
import com.ybb.dto.DriverUserWorkStatus;
import com.ybb.dto.ResponseResult;
import com.ybb.mapper.DriverUserMapper;
import com.ybb.mapper.DriverUserWorkStatusMapper;
import com.ybb.mock.DriverUserMockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverUserService {
    @Autowired
    private DriverUserMapper driverUserMapper;
    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;

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
}
