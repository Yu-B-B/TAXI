package com.ybb.serviceUser.mock;

import com.ybb.dto.DriverUser;

public class DriverUserMockData {
    public DriverUser getDriverUser(String checkPhone) {
        DriverUser driverUser = new DriverUser();
        driverUser.setDriverPhone(checkPhone);
        return driverUser;
    }
}
