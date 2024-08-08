package com.shensu.utils;

import com.alibaba.fastjson.JSONObject;
import com.shensu.mybatis.entity.systemmanage.SysStation;
import com.shensu.util.Sm4Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FindStationMess {

    @Value("${downPath}")
    private String path;
    public  SysStation findStationMess(String areaCode){
        try {
                JSONObject obj = new JSONObject();
                obj.put("appKey", "appKey003");
                obj.put("secret", "cb0ca1baca9efff2b4a6f8a6337182de");
                obj.put("areaCode", areaCode);
                JSONObject result = HttpUtils.sendGet(path + "/area/findSassStation?params=" + Sm4Util.encryptParams(obj.toString()));
                SysStation station = JSONObject.parseObject(Sm4Util.decryptEcb(result.getString("data")), SysStation.class);
                return  station;
            }catch (Exception e){
                e.printStackTrace();
            }
            return  null;
        }
    }
