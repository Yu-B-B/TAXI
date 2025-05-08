package com.ybb.response;

import lombok.Data;

@Data
public class TerminalListResponse {
    private String name; // 终端名称
    private Long tid; // 终端唯一ID
    private Long locatetime; // 最后一次上传时间戳
    private Long createtime; // 终端创建时间，时间戳
    private String desc; // 描述

}
