package com.ybb.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class DriverUser {


    private Long id;
    private String address;
    private String driverName;
    private String driverPhone;
    private Integer driverGender;
    private LocalDate driverBirthday;
    private String driverNation;
    private String driverContactAddress;
    private String licenseId;

    private LocalDate getDriverLicenseDate;
    private LocalDate driverLicenseOn;
    private LocalDate driverLicenseOff;
    private Integer taxiDriver;
    private String certificateNo;
    private String networkCarIssueOrganization;
    private LocalDate networkCarIssueDate;
    private LocalDate getNetworkCarProofDate;
    private LocalDate networkCarProofOn;
    private LocalDate networkCarProofOff;
    private LocalDate registerDate;
    private Integer commercialType;
    private String contractCompany; // 所属公司
    private LocalDate contractOn; // 合同期开始时间
    private LocalDate contractOff; // 合同结束时间
    private Integer state; // 状态


    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
