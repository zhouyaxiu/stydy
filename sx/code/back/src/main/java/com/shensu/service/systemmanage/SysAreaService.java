package com.shensu.service.systemmanage;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shensu.mybatis.entity.systemmanage.SysArea;
import com.shensu.mybatis.entity.systemmanage.SysStation;
import com.shensu.mybatis.mapper.systemmanage.SysAreaMapper;
import com.shensu.redis.RedisOperates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 地区控件管理
 * @Author:ztt
 * */
@Service
public class SysAreaService {

    @Autowired
    public SysAreaMapper sysAreaMapper;
    @Autowired
    private RedisOperates redisOperates;
    /**
     * 分级查看地区控件
     * */
    public JSONArray listChildWithStationByAreaCode(String areaCode) {
        //县
        SysArea sysArea = sysAreaMapper.findByAreaCode(areaCode);
        if(sysArea!=null){
            List<SysArea> sysAreas = sysAreaMapper.listChildByAreaID(sysArea.getId());
            return dealSysAreaWithStationData(sysArea, sysAreas);
        }
        //没有在地区表中查到数据则看看是不是接种单位
        sysArea = sysAreaMapper.findStationByAreaCode(areaCode);
        JSONArray rtnData = new JSONArray();
        JSONObject areaObj = new JSONObject();
        if(sysArea!=null) {
            //获取接种单位的上一级的编码和id
            SysArea supArea  = sysAreaMapper.findSupAreaByStationId(sysArea.getSupid());
            areaObj.put("areaname", sysArea.getAreaname());
            areaObj.put("areacode", sysArea.getAreacode());
            areaObj.put("newBaseCode", sysArea.getAreacode().substring(0, 10));
            areaObj.put("id", sysArea.getId());
            areaObj.put("supcode", supArea.getAreacode());
            areaObj.put("supid", supArea.getId());
            areaObj.put("leaf", true);
            rtnData.add(areaObj);
        }
        return  rtnData;
    }

    private JSONArray dealSysAreaWithStationData(SysArea supArea, List<SysArea> areaList) {
        if (areaList.isEmpty()) {
            //传入的list为空 说明没有子节点，此时需要查询sys_station看是否存在站点
            JSONArray rtnData = new JSONArray();
            List<SysStation> sysStations = sysAreaMapper.listByAreaId(supArea.getId());
            if (!sysStations.isEmpty()) {
                for (SysStation sysStation : sysStations) {
                    System.out.println(sysStation);
                    JSONObject areaObj = new JSONObject();
                    areaObj.put("areaname", sysStation.getOrganName());
                    areaObj.put("areacode", sysStation.getOrganCode());
                    areaObj.put("newBaseCode", sysStation.getNewBaseCode());
                    areaObj.put("id", sysStation.getId());
                    areaObj.put("supcode", supArea.getAreacode());
                    areaObj.put("supid", supArea.getId());
                    areaObj.put("leaf", true);
                    rtnData.add(areaObj);
                }
            }
            return rtnData;
        } else {
            return dealSysAreaData(supArea, areaList, true);
        }
    }

    /**
     * 处理数据并返回前端所需数据
     *
     * @param areaList
     * @return
     */
    private JSONArray dealSysAreaData(SysArea supArea, List<SysArea> areaList, boolean isRelateSataion) {
        JSONArray rtnData = new JSONArray();
        for (SysArea sysArea : areaList) {
            JSONObject areaObj = new JSONObject();
            areaObj.put("areaname", sysArea.getAreaname());
            areaObj.put("areacode", sysArea.getAreacode());
            areaObj.put("newBaseCode", sysArea.getAreacode().substring(0,10));
            areaObj.put("id", sysArea.getId());
            areaObj.put("supcode", supArea.getAreacode());
            areaObj.put("supid", supArea.getId());
            areaObj.put("leaf", false);

            List<SysArea> childList = sysAreaMapper.listChildByAreaID(sysArea.getId());
            if (childList.isEmpty()) {
                if (isRelateSataion) {
                    List<SysStation> sysStations = sysAreaMapper.listByAreaId(sysArea.getId());
                    if (sysStations.isEmpty()) {
                        areaObj.put("leaf", true);
                    }
                } else {
                    areaObj.put("leaf", true);
                }
            }
            rtnData.add(areaObj);
        }
        return rtnData;
    }
}
