package com.ybb.mapper;

import com.ybb.dto.PassengerUser;

import java.time.LocalDateTime;

public class PassengerUserDataModel {
    public PassengerUser getData(String phone){
        PassengerUser user = new PassengerUser();
        user.setId(123123155L);
        user.setGmtCreate(LocalDateTime.now());
        user.setGmtModified(LocalDateTime.now());
        user.setPassengerPhone(phone);
        user.setPassengerName("张三");
        return user;
    }
}
