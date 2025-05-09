package com.ybb.serviceMap.remote;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybb.dto.DicUrl;
import com.ybb.serviceMap.mapper.DicRequestUrlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 地区字典获取
 * */
@Service
@Slf4j
public class MapDicDirectionClient {
    @Value("${amap.app.key}")
    private String appKey;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DicRequestUrlMapper dicUrlMapper;

    public String getDicDirectionData() {

        // https://restapi.amap.com/v3/config/district?keywords=%E4%B8%AD%E5%9B%BD&subdistrict=4&key=f0c42d8524d2eea5e41a445b53f9f38e
        /*StringBuilder sb = new StringBuilder();
        sb.append(MapConstant.MAP_DIC_DIRECTION);
        sb.append("?");
        sb.append("keywords=");
        sb.append("中国");
        sb.append("&subdistrict=4");
        sb.append("&key=");
        sb.append(appKey);*/

        Integer id = 1;
        DicUrl requestUrl = dicUrlMapper.selectOne(new LambdaQueryWrapper<DicUrl>()
                .eq(DicUrl::getId, id));
        String url = requestUrl.getUrl();

        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
//        log.info(entity.getBody());
        return entity.getBody();
    }
}
