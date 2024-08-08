package com.shensu.jsjm.redis.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shensu.jsjm.config.AreaInitCache;
import com.shensu.jsjm.constant.HttpResultCodeEnum;
import com.shensu.jsjm.constant.RedisKeyEnum;
import com.shensu.jsjm.mapper.AreaMapper;
import com.shensu.jsjm.model.*;
import com.shensu.jsjm.redis.RedisAreaService;
import com.shensu.jsjm.service.RedisService;
import com.shensu.jsjm.util.CommonUtil;
import com.shensu.jsjm.util.HttpUtil;
import com.shensu.jsjm.util.PropertyUtil;
import com.shensu.jsjm.util.SecureUtil;
import com.shensu.jsjm.vo.AreaInputVo;
import com.shensu.jsjm.vo.TotalOutputVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Service
public class RedisAreaServiceImpl implements RedisAreaService {

    private static final Logger log = LoggerFactory.getLogger(RedisAreaServiceImpl.class);

    @Resource
    private RedisService redisService;

    @Resource
    private AreaMapper areaMapper;

    @Resource
    private PropertyUtil propertyUtil;


    @Override
    public SimpleAreaBean getArea(AreaInputVo inputVo) {
        String key = RedisKeyEnum.visual_areaMapCode.getKey();

        String str = (String) redisService.get(key);
        SimpleAreaBean simpleAreaBean =  JSONObject.parseObject(str, SimpleAreaBean.class);

        if (inputVo == null || StringUtils.isBlank(inputVo.getAreaCode())){
            return simpleAreaBean;
        }else {
            if (inputVo.getAreaCode().equals(simpleAreaBean.getCode())) {
                return simpleAreaBean;
            } else {
                List<SimpleAreaBean> children = simpleAreaBean.getChildren();
                for (SimpleAreaBean simpleAreaBean1 : children) {
                    if (inputVo.getAreaCode().equals(simpleAreaBean1.getCode())) {
                        return simpleAreaBean1;
                    }
                }
            }
        }
        return simpleAreaBean;
    }

    @Override
    public Map<String, AreaBean> getAllArea() {

        return AreaInitCache.areaMapCode;
    }

    private OpdServeCount getOpdServeCount(String areaCode) {
        Oauth oauth = new Oauth();
        oauth.setCode(propertyUtil.getOauthAccount());
        oauth.setPwd(propertyUtil.getOauthPwd());
        oauth.setSmKey(propertyUtil.getOauthSmKey());
        LocalDateTime now = LocalDateTime.now();
        oauth.setAskTime(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        String oauthJsonStr = JSONObject.toJSONString(oauth);
        OpdServeCount opdServeCount = new OpdServeCount();
        String encryptRSAStr = null;
        try {
            encryptRSAStr = SecureUtil.encryptRSAHuTool(oauthJsonStr, propertyUtil.getRsaKey());
        } catch (Exception e) {
            log.error("encryptRSA getRsaKey error;areaCode:【{}】",areaCode);
            e.printStackTrace();
        }
        if (encryptRSAStr != null) {
            String resultStr = null;
            try {
                resultStr = HttpUtil.postHttpXml(encryptRSAStr, propertyUtil.getAuthUrl(), null);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("HttpOpdServeCount getAuthUrl connect timeout;areaCode:【{}】",areaCode);
//                e.printStackTrace();
            }
            HttpResult result = null;
            if (resultStr != null) {
                result = JSONObject.parseObject(resultStr, HttpResult.class);
                log.info("getOpdServeCount 登录返回：{}", JSON.toJSONString(result));
            }

            if (result != null && HttpResultCodeEnum.HTTP_SUCCESS.getKey().equals(result.getCode())) {
                String accessToken = result.getAccess_token();
                String encryptSMStr = SecureUtil.encryptSMHuTool(areaCode, propertyUtil.getOauthSmKey());
                try {
                    resultStr = null;
                    resultStr = HttpUtil.postHttpXml(encryptSMStr, propertyUtil.getOpdServeCountUrl(), accessToken);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    log.error("HttpOpdServeCount getOpdServeCountUrl connect timeout;areaCode:【{}】",areaCode);
//                    e.printStackTrace();
                }
                if (resultStr != null) {
                    result = JSONObject.parseObject(resultStr, HttpResult.class);
                    if (result != null && HttpResultCodeEnum.HTTP_SUCCESS.getKey().equals(result.getCode())) {
                        String data = result.getData();
                        String decryptSMStr = SecureUtil.decryptSMHuTool(data, propertyUtil.getOauthSmKey());
                        opdServeCount = JSONObject.parseObject(decryptSMStr, OpdServeCount.class);
                        log.info("getOpdServeCount 查询返回：{}", JSON.toJSONString(opdServeCount));
                    }
                }
            }
        }
        return opdServeCount;
    }

    private void initAreas(AreaBean parentArea, Map<String, AreaBean> areaMapCode, Map<String, AreaBean> areaMapId) {
        if (parentArea.getLevel() == 3) {
            List<AreaBean> stations = areaMapper.getChildrenStations(parentArea.getCode());
            List<AreaBean> cys = areaMapper.getChildrenCys(parentArea.getCode());
            for (AreaBean area : stations) {
                area.setLevel(4);
                area.setParentArea(parentArea);
                area.setStationOrCy(AreaBean.type_station);
                areaMapCode.put(area.getCode(), area);
                areaMapId.put(area.getId(), area);
                List<AreaBean> villages = areaMapper.getChildrenVillages(area.getId());
                for (AreaBean village : villages) {
                    village.setLevel(5);
                    village.setParentArea(area);
                    areaMapCode.put(village.getCode(), village);
                    areaMapId.put(village.getId(), area);
                }
                area.setChildren(villages);
            }
            for (AreaBean area : cys) {
                area.setLevel(4);
                area.setParentArea(parentArea);
                area.setStationOrCy(AreaBean.type_cy);
                areaMapCode.put(area.getCode(), area);
                areaMapId.put(area.getId(), area);
            }
            parentArea.setStations(stations);
            parentArea.setCys(cys);
        } else {
            List<AreaBean> areas = areaMapper.getChildrenAreas(parentArea.getCode());
            parentArea.setChildren(areas);
            if (areas.isEmpty()) {
                return;
            }
            for (AreaBean area : areas) {
                area.setLevel(parentArea.getLevel() + 1);
                area.setParentArea(parentArea);
                areaMapCode.put(area.getCode(), area);
                areaMapId.put(area.getId(), area);
                initAreas(area, areaMapCode, areaMapId);
            }
        }
    }

    private void initAreas(SimpleAreaBean parentArea) {
        List<SimpleAreaBean> areasCity = areaMapper.getSimpleChildrenAreas(parentArea.getCode());
        List<SimpleAreaBean> areasCounty;
        for (SimpleAreaBean city : areasCity) {
            areasCounty = areaMapper.getSimpleChildrenAreas(city.getCode());
            city.setChildren(areasCounty);
            city.setLevel(1);
            for (SimpleAreaBean county : areasCounty) {
                county.setLevel(2);
            }
        }
        parentArea.setChildren(areasCity);
    }
}
