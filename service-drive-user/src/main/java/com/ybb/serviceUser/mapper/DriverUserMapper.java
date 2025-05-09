package com.ybb.serviceUser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybb.dto.DriverUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DriverUserMapper extends BaseMapper<DriverUser> {
    public int selectDriverUserCountByCityCode(@Param("cityCode") String cityCode);
}
