package com.ybb.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PassengerUser {
    private Long id;
    private LocalDateTime gmtCreate; // 创建时间
    private LocalDateTime gmtModified; // 更新时间
    private String passengerPhone; // 乘客手机号码
    private String passengerName; // 乘客姓名
    private byte passengerGender; // 乘客性别
    private byte state; // 乘客状态 0有效，1失效
    private String profilePhoto; // 头像信息

}
