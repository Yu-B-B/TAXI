package com.ybb.apiBoss.service;

import com.ybb.dto.ResponseResult;
import com.ybb.dto.PassengerUser;
import com.ybb.serviceMap.mapper.PassengerUserDao;
import com.ybb.serviceMap.mapper.PassengerUserDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PassengerUserService {
    @Autowired
    PassengerUserDao baseDao;


    public ResponseResult loginOrRegister(String phone) {
        // 根据手机号获取用户信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("passenger_phone", phone);
//        List<PassengerUser> entities = baseDao.selectByMap(map);
        List<PassengerUser> entities = new ArrayList<>();
        entities.add(new PassengerUserDataModel().getData(phone));

        // 判断用户是否存在
        if(entities.size() == 0) {
            PassengerUser user = new PassengerUser();
            user.setPassengerName("嘿Bro");
            user.setPassengerGender((byte)0);
            user.setPassengerPhone(phone);
            user.setState((byte)0);
            user.setGmtCreate(LocalDateTime.now());
            user.setGmtModified(LocalDateTime.now());
//            baseDao.insert(user);
            System.out.println("插入成功");
        }

        // 不存在插入数据，
        return ResponseResult.success();
    }

    public ResponseResult getUserInfoByPhone(String phone) {
        Map<String,String> map = new HashMap<>();
        map.put("passenger_phone", phone);

//        List<PassengerUser> entities = baseDao.selectByMap(map);
        List<PassengerUser> entities = new ArrayList<>();
        entities.add(new PassengerUserDataModel().getData(phone));

        return ResponseResult.success(entities.get(0));
    }
}
