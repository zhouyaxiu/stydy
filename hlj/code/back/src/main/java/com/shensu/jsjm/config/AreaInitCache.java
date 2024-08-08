package com.shensu.jsjm.config;

import com.shensu.jsjm.constant.ScheduledTypeEnum;
import com.shensu.jsjm.mapper.AreaMapper;
import com.shensu.jsjm.model.AreaBean;
import com.shensu.jsjm.model.SimpleAreaBean;
import com.shensu.jsjm.redis.RedisAreaService;
import com.shensu.jsjm.redis.RedisVaccinationUnitService;
import com.shensu.jsjm.task.MultithreadScheduleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
public class AreaInitCache {

    public static Map<String, AreaBean> areaMapCode = new ConcurrentSkipListMap<>();

    private static final Logger logger = LoggerFactory.getLogger(AreaInitCache.class);

    private static final String areaCodeKey = "code";
    private static final String areaLevelKey = "level";

    @Resource
    private RedisAreaService redisAreaService;

    @Resource
    private AreaMapper areaMapper;

    @Resource
    private RedisVaccinationUnitService redisVaccinationUnitService;

    @Resource
    private MultithreadScheduleTask multithreadScheduleTask;

    @PostConstruct
    public void init1() {
        logger.info("准备加载所有地区到内存中");
        multithreadScheduleTask.getSupAndCityCodes();
        logger.info("地区加载缓存成功！");

        logger.info("====初始化更新当日接种====");
        multithreadScheduleTask.scheduledUpdate(ScheduledTypeEnum.SameDayVaccination.name());
        logger.info("====初始化当日接种结束====");

        logger.info("====初始化更新疫苗接种率====");
        multithreadScheduleTask.scheduledUpdate(ScheduledTypeEnum.VaccinationRate.name());
        logger.info("====初始化疫苗接种率结束====");

        logger.info("====初始化更新扫码接种率====");
        multithreadScheduleTask.scheduledUpdate(ScheduledTypeEnum.ScanVaccinationRate.name());
        logger.info("====初始化扫码接种率结束====");

        logger.info("====初始化更新NIP疫苗接种率====");
        multithreadScheduleTask.scheduledUpdate(ScheduledTypeEnum.NIPVaccination.name());
        logger.info("====初始化NIP疫苗接种率结束====");

        logger.info("====开始更新当年非免疫规划疫苗接种统计====");
        multithreadScheduleTask.scheduledUpdate(ScheduledTypeEnum.VaccinationCount.name());
        logger.info("【更新当年非免疫规划疫苗接种统计结束】");

        logger.info("====初始化更新实时库存====");
        multithreadScheduleTask.scheduledUpdate(ScheduledTypeEnum.Inventory.name());
        logger.info("====初始化实时库存结束====");

        logger.info("开始更新智慧决策实时库存--近效期库存");
        multithreadScheduleTask.scheduledUpdate(ScheduledTypeEnum.WisdomInventory.name());
        logger.info("更新智慧决策实时库存结束--近效期库存");
    }

    /**
     * 获取省市code和层级
     */
    public List<Map<String, Object>> getSupAndCityCodesAndLevel() {
        List<Map<String, Object>> codeAndLevels = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        SimpleAreaBean area = redisAreaService.getArea(null);
        map.put(areaCodeKey, area.getCode());
        map.put(areaLevelKey, area.getLevel());
        codeAndLevels.add(map);
        List<SimpleAreaBean> children = area.getChildren();
        for (SimpleAreaBean simpleAreaBean : children) {
            map = new HashMap<>();
            map.put(areaCodeKey, simpleAreaBean.getCode());
            map.put(areaLevelKey, simpleAreaBean.getLevel());
            codeAndLevels.add(map);
        }
        return codeAndLevels;
    }

    public void initAreas(AreaBean parentArea, Map<String, AreaBean> areaMapCode, Map<String, AreaBean> areaMapId) {
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

    public void initAreas(SimpleAreaBean parentArea) {
        redisVaccinationUnitService.updateClassUnitAll(parentArea.getCode());
        List<SimpleAreaBean> areasCity = areaMapper.getSimpleChildrenAreas(parentArea.getCode());
        List<SimpleAreaBean> areasCounty;
        for (SimpleAreaBean city : areasCity) {
            areasCounty = areaMapper.getSimpleChildrenAreas(city.getCode());
            redisVaccinationUnitService.updateClassUnitAll(city.getCode());
            city.setChildren(areasCounty);
            city.setLevel(1);
            for (SimpleAreaBean county : areasCounty) {
                redisVaccinationUnitService.updateClassUnitAll(county.getCode());
                county.setLevel(2);
            }
        }
        parentArea.setChildren(areasCity);
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
    }
}
