package com.ybb.serviceMap.remote;

import com.ybb.constant.MapConstant;
import com.ybb.response.DirectionResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 高德地图远程调用获取距离和时常
 */
@Service
@Slf4j
public class MapDirectionClient {
    @Value("${amap.app.key}")
    private String appKey;

    @Autowired
    private RestTemplate restTemplate;

    public DirectionResponse directionData(String depLatitude, String depLongitude, String destLongitude, String destLatitude) {
        // https://restapi.amap.com/v3/direction/driving?origin=116.481028,39.989643&destination=116.465302,40.004717&extensions=all&output=xml&key=f0c42d8524d2eea5e41a445b53f9f38e
        StringBuilder sb = new StringBuilder();
        sb.append(MapConstant.MAP_DIRECTION);
        sb.append("?");
        sb.append("origin=" + depLongitude);
        sb.append("," + depLatitude);
        sb.append("&destination=" + destLongitude);
        sb.append("," + destLatitude);
        sb.append("&extensions=base");
        sb.append("&output=json");
        sb.append("&key=").append(appKey);

        log.info("高德地图当前坐标位置" + sb.toString());
        String url = sb.toString();

        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        log.info("高德地图返回参数：" + entity.getBody());
        return resolveDirectionData(entity.getBody());

    }

    public DirectionResponse resolveDirectionData(String jsonData) {
        DirectionResponse result = null;
        // 将字符串转为JSON对象
        try {
            JSONObject object = JSONObject.fromObject(jsonData);
            if (object.has(MapConstant.STATUS)) {
                // 0,解析失败，1解析成功
                if (object.getInt(MapConstant.STATUS) == 1) {
                    if(object.has(MapConstant.ROUTE)) {
                        JSONObject routeObject = object.getJSONObject(MapConstant.ROUTE);
                        JSONArray pathList = routeObject.getJSONArray(MapConstant.PATHS);

                        JSONObject pathObject = pathList.getJSONObject(0);

                        result = new DirectionResponse();
                        if(pathObject.has(MapConstant.DURATION)) {
                            int duration = pathObject.getInt(MapConstant.DURATION);
                            result.setDuration(duration);
                        }
                        if(pathObject.has(MapConstant.DISTANCE)) {
                           int distance = pathObject.getInt(MapConstant.DISTANCE);
                           result.setDistance(distance);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return result;
    }
}
