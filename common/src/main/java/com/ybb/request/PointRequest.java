package com.ybb.request;

import lombok.Data;

/**
 * 轨迹上传需要的参数
 */
@Data
public class PointRequest {

    private String tid; // 终端id

    private String trid; // 设备id

    private PointDTO[] points; // 位置信息

}
