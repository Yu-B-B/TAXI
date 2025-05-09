package com.ybb.serviceMap.service;

import com.ybb.constant.MapConstant;
import com.ybb.dto.DicDistrict;
import com.ybb.dto.ResponseResult;
import com.ybb.serviceMap.mapper.DicDirectionMapper;
import com.ybb.serviceMap.remote.MapDicDirectionClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DicDirectionService {
    @Autowired
    private MapDicDirectionClient mapDicDirectionClient;
    @Autowired
    private DicDirectionMapper dicDirectionMapper;

    /**
     * 获取高德地图中的地区列表信息
     *
     * @return
     */
    public ResponseResult getResolveDirectionDic() {
        // 1、获取数据集，修改为从数据库中查询链接
        String data = mapDicDirectionClient.getDicDirectionData();
        // 2、解析数据
        JSONObject jsonData = JSONObject.fromObject(data);
        int status = jsonData.getInt(MapConstant.STATUS);
        if (status != 1) {
            return ResponseResult.fail("地图信息错误"); // 需要使用同意返回枚举中的内容
        }
        //
        JSONArray countryJsonArray = jsonData.getJSONArray(MapConstant.DISTRICTS);
        for (int country = 0; country < countryJsonArray.size(); country++) {
            JSONObject countryJsonObject = countryJsonArray.getJSONObject(country);
            String countryAddressCode = countryJsonObject.getString(MapConstant.ADCODE);
            String countryAddressName = countryJsonObject.getString(MapConstant.NAME);
            String countryParentAddressCode = "0";
            String countryLevel = countryJsonObject.getString(MapConstant.LEVEL);

            insertDicDistrict(countryAddressCode, countryAddressName, countryLevel, countryParentAddressCode);

            JSONArray proviceJsonArray = countryJsonObject.getJSONArray(MapConstant.DISTRICTS);
            for (int p = 0; p < proviceJsonArray.size(); p++) {
                JSONObject proviceJsonObject = proviceJsonArray.getJSONObject(p);
                String proviceAddressCode = proviceJsonObject.getString(MapConstant.ADCODE);
                String proviceAddressName = proviceJsonObject.getString(MapConstant.NAME);
                String proviceParentAddressCode = countryAddressCode;
                String proviceLevel = proviceJsonObject.getString(MapConstant.LEVEL);

                insertDicDistrict(proviceAddressCode, proviceAddressName, proviceLevel, proviceParentAddressCode);

                JSONArray cityArray = proviceJsonObject.getJSONArray(MapConstant.DISTRICTS);
                for (int city = 0; city < cityArray.size(); city++) {
                    JSONObject cityJsonObject = cityArray.getJSONObject(city);
                    String cityAddressCode = cityJsonObject.getString(MapConstant.ADCODE);
                    String cityAddressName = cityJsonObject.getString(MapConstant.NAME);
                    String cityParentAddressCode = proviceAddressCode;
                    String cityLevel = cityJsonObject.getString(MapConstant.LEVEL);

                    insertDicDistrict(cityAddressCode, cityAddressName, cityLevel, cityParentAddressCode);

                    JSONArray districtArray = cityJsonObject.getJSONArray(MapConstant.DISTRICTS);
                    for (int d = 0; d < districtArray.size(); d++) {
                        JSONObject districtJsonObject = districtArray.getJSONObject(d);
                        String districtAddressCode = districtJsonObject.getString(MapConstant.ADCODE);
                        String districtAddressName = districtJsonObject.getString(MapConstant.NAME);
                        String districtParentAddressCode = cityAddressCode;
                        String districtLevel = districtJsonObject.getString(MapConstant.LEVEL);

                        // 如果是街道，后面不继续
                        if (districtLevel.equals(MapConstant.LEVEL_5)) {
                            continue;
                        }

                        insertDicDistrict(districtAddressCode, districtAddressName, districtLevel, districtParentAddressCode);

                    }
                }
            }

        }


        return null;
    }

    /**
     * 地区数据字典入库
     *
     * @param addressCode       地区编码
     * @param addressName       地区名称
     * @param level             地区等级（省、市、县）
     * @param parentAddressCode 上级编码
     */
    public void insertDicDistrict(String addressCode, String addressName, String level, String parentAddressCode) {
        // 数据库对象
        DicDistrict district = new DicDistrict();
        district.setAddressCode(addressCode);
        district.setAddressName(addressName);
        int levelInt = generateLevel(level);
        district.setLevel(levelInt);

        district.setParentAddressCode(parentAddressCode);

        dicDirectionMapper.insert(district);
    }

    public int generateLevel(String level) {
        int levelInt = 0;
        if (level.trim().equals(MapConstant.LEVEL_1)) {
            levelInt = 0;
        } else if (level.trim().equals(MapConstant.LEVEL_2)) {
            levelInt = 1;
        } else if (level.trim().equals(MapConstant.LEVEL_3)) {
            levelInt = 2;
        } else if (level.trim().equals(MapConstant.LEVEL_4)) {
            levelInt = 3;
        }
        return levelInt;
    }
}
