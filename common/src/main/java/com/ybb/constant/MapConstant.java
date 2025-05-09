package com.ybb.constant;

public class MapConstant {
    // 基本请求前缀
    public static final String MAP_DIRECTION = "https://restapi.amap.com/v3/direction/driving";

    // 地区字典
    public static final String MAP_DIC_DIRECTION = "https://restapi.amap.com/v3/config/district";

    /**
     * 新增服务
     */
    public static final String SERVICE_ADD_URL = "https://tsapi.amap.com/v1/track/service/add";

    // 服务查询
    public static final String SERVICE_GET_URL = "https://tsapi.amap.com/v1/track/service/list";

    // 服务删除
    public static final String SERVICE_DELETE_URL = "https://tsapi.amap.com/v1/track/service/delete";

    /**
     * 创建终端
     */
    public static final String TERMINAL_ADD = "https://tsapi.amap.com/v1/track/terminal/add";

    // 终端列表
    public static final String TERMINAL_LIST = "https://tsapi.amap.com/v1/track/terminal/list";
    // 终端删除
    public static final String TERMINAL_DELETE = "https://tsapi.amap.com/v1/track/terminal/delete";

    /**
     * 创建轨迹
     */
    public static final String TRACK_ADD = "https://tsapi.amap.com/v1/track/trace/add";

    /**
     * 轨迹点上传
     */
    public static final String POINT_UPLOAD = "https://tsapi.amap.com/v1/track/point/upload";

    /**
     * 终端搜索
     */
    public static final String TERMINAL_AROUNDSEARCH = "https://tsapi.amap.com/v1/track/terminal/aroundsearch";

    /**
     * 查询轨迹结果（包括：路程和时长）
     */
    public static final String TERMINAL_TRSEARCH = "https://tsapi.amap.com/v1/track/terminal/trsearch";

    // 解析返回内容中的字段
    public static final String STATUS = "status";
    public static final String DISTANCE = "distance";
    public static final String DURATION = "duration";
    public static final String ROUTE = "route";
    public static final String PATHS = "paths";

    // 地图字典相关
    public static final String DISTRICTS = "districts";
    public static final String ADCODE = "adcode";
    public static final String NAME = "name";
    public static final String LEVEL = "level";
    public static final String LEVEL_1 = "country";
    public static final String LEVEL_2 = "province";
    public static final String LEVEL_3 = "city";
    public static final String LEVEL_4 = "district";
    public static final String LEVEL_5 = "street";
}
