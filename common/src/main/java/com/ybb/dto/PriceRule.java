package com.ybb.dto;

import lombok.Data;

@Data
public class PriceRule {

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 起步价
     */
    private Double startFare;

    /**
     * 起步里程
     */
    private Integer startMile;

    /**
     * 单位距离价格（千米）
     */
    private Double unitPricePerMile;

    /**
     * 单位时长价格（分钟）
     */
    private Double unitPricePerMinute;

    /**
     * 版本，默认1，修改往上增。
     */
    private Integer fareVersion;


    /**
     * 运价类型编码
     */
    private String fareType;

}
