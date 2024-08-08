package com.shensu.jsjm.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shensu.jsjm.config.AreaInitCache;
import com.shensu.jsjm.constant.RedisKeyEnum;
import com.shensu.jsjm.constant.ScheduledTypeEnum;
import com.shensu.jsjm.model.AreaBean;
import com.shensu.jsjm.model.LabelValueBean;
import com.shensu.jsjm.model.SimpleAreaBean;
import com.shensu.jsjm.redis.RedisAreaService;
import com.shensu.jsjm.redis.RedisVaccinationService;
import com.shensu.jsjm.redis.RedisVaccinationUnitService;
import com.shensu.jsjm.service.CommonService;
import com.shensu.jsjm.service.RedisService;
import com.shensu.jsjm.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 多线程定时任务
 *
 * @author 17977 <br>
 * @Component 注解用于对那些比较中立的类进行注释<br>
 * @Repository 注解用于持久层 <br>
 * @Service 注解用于业务层<br>
 * @Controller 注解用于控制层 <br>
 * @EnableScheduling 1.开启定时任务 <br>
 * @EnableAsync 2.开启多线程 <br>
 */
@Component
@EnableScheduling
@EnableAsync
public class MultithreadScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(MultithreadScheduleTask.class);

    @Resource
    private CommonService commonService;

    @Resource
    private RedisVaccinationUnitService redisVaccinationUnitService;

    @Resource
    private RedisVaccinationService redisVaccinationService;

    @Resource
    private RedisService redisService;

    @Resource
    private AreaInitCache areaInitCache;

    private static final String areaCodeKey = "code";
    private static final String areaLevelKey = "level";


    /**
     * Cron表达式参数分别表示： [秒] [分] [时] [日] [月] [周]<br>
     * 秒（0~59） 例如0/5表示每5秒 <br>
     * 分（0~59）<br>
     * 时（0~23） <br>
     * 日（0~31）的某天，需计算 <br>
     * 月（0~11） <br>
     * 周几（ 可填1-7 或SUN/MON/TUE/WED/THU/FRI/SAT）
     *
     * @Scheduled：除了支持灵活的参数表达式cron之外，还支持简单的延时操作，例如 fixedDelay ，fixedRate 填写相应的毫秒数即可。
     * @throws InterruptedException
     */

    /**
     * Cron常用表达式示例： <br>
     * 0/2 * * * * ? 表示每2秒 执行任务 <br>
     * 0 0/2 * * * ? 表示每2分钟 执行任务<br>
     * 0 0 2 1 * ? 表示在每月的1日的凌晨2点调整任务 <br>
     * 0 15 10 ? * MON-FRI 表示周一到周五每天上午10:15执行作业 <br>
     * 0 0 10,14,16 * * ? 每天上午10点，下午2点，4点 <br>
     * 0 0/30 9-17 * * ? 朝九晚五工作时间内每半小时 <br>
     * 0 0 12 ? * WED 表示每个星期三中午12点 <br>
     * 0 0 12 * * ? 每天中午12点触发 <br>
     * 0 15 10 ? * * 每天上午10:15触发 <br>
     * 0 15 10 * * ? 每天上午10:15触发<br>
     * 0 15 10 * * ? 每天上午10:15触发<br>
     * 0 * 14 * * ? 在每天下午2点到下午2:59期间的每1分钟触发<br>
     * 0 0/5 14 * * ? 在每天下午2点到下午 2:55期间的每5分钟触发<br>
     * 0 0/5 14,18 * * ? 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发 <br>
     * 0 0-5 14 * * ? 在每天下午2点到下午2:05期间的每1分钟触发 <br>
     * 0 10,44 14 ? 3 WED 每年三月的星期三的下午2:10和2:44触发 <br>
     * 0 15 10 ? * MON-WED,SAT 周一至周三和周六的上午10:15触发 <br>
     * 0 15 10 15 * ? 每月15日上午10:15触发 <br>
     * 0 15 10 L * ? 每月最后一日的上午10:15触发 <br>
     * 0 15 10 ? * 6L 每月的最后一个星期五上午10:15触发 <br>
     * 0 15 10 ? * 6#3 每月的第三个星期五上午10:15触发 <br>
     */

    @Async
    public void scheduledUpdate(String type) {
        String key = RedisKeyEnum.visual_area_supCodeAndCityCode.getKey();
        String str = (String) redisService.get(key);
        List<Map<String, Object>> codes =  JSONObject.parseObject(str,List.class);
        ScheduledTypeEnum typeEnum = ScheduledTypeEnum.valueOf(type);
        for (Map<String, Object> map : codes) {
            String areaCode = (String) map.get(areaCodeKey);
            int level;
            switch (typeEnum) {
                case ClassUnit:
                    redisVaccinationUnitService.updateClassUnitAll(areaCode);
                    break;
                case SameDayVaccination:
                    level = (int) map.get(areaLevelKey);
                    logger.info("SameDayVaccination,areaCode:{},level:{}", areaCode, level);
                    redisVaccinationService.setSameDayVaccination(areaCode, level);
                    break;
                case ScanVaccinationRate:
                    level = (int) map.get(areaLevelKey);
                    redisVaccinationService.setScanVaccinationRate(areaCode, level);
                    break;
                case VaccinationRate:
                    level = (int) map.get(areaLevelKey);
                    List<LabelValueBean> ageMonthList = CommonUtil.getAgeMonthList();
                    for (LabelValueBean valueBean : ageMonthList) {
                        redisVaccinationService.setVaccinationRate(areaCode, level, Integer.parseInt(valueBean.getValue()));
                    }
                    break;
                case Inventory:
                    redisVaccinationService.setInventory(areaCode);
                    break;
                case WisdomInventory:
                    redisVaccinationService.setWisdomInventory(areaCode);
                    break;
                case NIPVaccination:
                    level = (int) map.get(areaLevelKey);
                    logger.info("NIPVaccination,areaCode:{},level:{}", areaCode, level);
                    redisVaccinationService.setNIPVaccination(areaCode, level);
                    break;
                case VaccinationCount:
                    level = (int) map.get(areaLevelKey);
                    logger.info("VaccinationCount,areaCode:{},level:{}", areaCode, level);
                    redisVaccinationService.setVaccinationCounts(areaCode, level);
                    break;
            }
        }
    }

    public void getSupAndCityCodes() {
        logger.info("准备加载所有地区到内存中");
        long t = System.currentTimeMillis();
        Map<String, AreaBean> areaMapId = new ConcurrentSkipListMap<>();
        SimpleAreaBean areaBean = new SimpleAreaBean();
        AreaBean areaBean2 = new AreaBean();
        areaBean.setCode("230000000000");
        areaBean.setName("黑龙江");
        areaBean.setLevel(0);
        areaBean2.setId("1");
        areaBean2.setCode("230000000000");
        areaBean2.setName("黑龙江 2300000000");
        areaBean2.setSimpleName("黑龙江");
        areaBean2.setLevel(0);
        AreaInitCache.areaMapCode.put(areaBean2.getCode(), areaBean2);
        areaMapId.put(areaBean2.getId(), areaBean2);
        areaInitCache.initAreas(areaBean);
        areaInitCache.initAreas(areaBean2, AreaInitCache.areaMapCode, areaMapId);
        redisService.set(RedisKeyEnum.visual_areaMapCode.getKey(), JSON.toJSONString(areaBean));
        long t2 = System.currentTimeMillis();
        logger.info("加载所有地区到内存中完成.耗时：{}秒", (t2 - t) / 1000);
        logger.info("地区加载缓存成功！");

        if (!redisService.exists(RedisKeyEnum.visual_area_supCodeAndCityCode.getKey())) {
            logger.info("准备加载省市code和层级到内存中");
            List<Map<String, Object>> supAndCityCodesAndLevelMap = areaInitCache.getSupAndCityCodesAndLevel();
            redisService.set(RedisKeyEnum.visual_area_supCodeAndCityCode.getKey(), JSON.toJSONString(supAndCityCodesAndLevelMap));
            logger.info("加载省市code和层级缓存成功！");
        }
    }

    /**
     * 更新接种单位分类
     */
    @Scheduled(cron = "${job.updateClassUnit}")
    public void updateClassUnit() {
        logger.info("====开始更新接种单位分类====");
        scheduledUpdate(ScheduledTypeEnum.ClassUnit.name());
        logger.info("【更新接种单位分类结束】");
    }


    /**
     * 更新当日接种
     */
    @Scheduled(cron = "${job.updateSameDayVaccination}")
    public void updateSameDayVaccination() {
        logger.info("====开始更新当日接种====");
        scheduledUpdate(ScheduledTypeEnum.SameDayVaccination.name());
        logger.info("【更新当日接种结束】");
    }

    /**
     * 更新疫苗接种率
     */
    @Scheduled(cron = "${job.updateVaccinationRate}")
    public void updateVaccinationRate() {
        logger.info("====开始更新疫苗接种率====");
        scheduledUpdate(ScheduledTypeEnum.VaccinationRate.name());
        logger.info("【更新疫苗接种率结束】");
    }

    /**
     * 更新扫码接种率
     */
    @Scheduled(cron = "${job.updateScanVaccinationRate}")
    public void updateScanVaccinationRate() {
        logger.info("====开始更新扫码接种率====");
        scheduledUpdate(ScheduledTypeEnum.ScanVaccinationRate.name());
        logger.info("【更新扫码接种率结束】");
    }

    /**
     * 更新实时库存
     */
    @Scheduled(cron = "${job.updateInventory}")
    public void updateInventory() {
        logger.info("开始更新实时库存");
        scheduledUpdate(ScheduledTypeEnum.Inventory.name());
        logger.info("更新实时库存结束");
    }

    /**
     * 更新智慧决策实时库存
     */
    @Scheduled(cron = "${job.updateWisdomInventory}")
    public void updateWisdomInventory() {
        logger.info("开始更新智慧决策实时库存");
        scheduledUpdate(ScheduledTypeEnum.WisdomInventory.name());
        logger.info("更新智慧决策实时库存结束");
    }


    /**
     * 更新NIP全程接种率
     */
    @Scheduled(cron = "${job.updateNIPVaccination}")
    public void updateNIPVaccination() {
        logger.info("开始更新NIP全程接种率");
        scheduledUpdate(ScheduledTypeEnum.NIPVaccination.name());
        logger.info("开始更新NIP全程接种率");
    }


    /**
     * 更新当年非免疫规划疫苗接种统计
     */
    @Scheduled(cron = "${job.updateVaccinationCount}")
    public void updateVaccinationCount() {
        logger.info("====开始更新当年非免疫规划疫苗接种统计====");
        scheduledUpdate(ScheduledTypeEnum.VaccinationCount.name());
        logger.info("【更新当年非免疫规划疫苗接种统计结束】");
    }

    /**
     * 更新新生儿年份统计
     */
    @PostConstruct
    @Scheduled(cron = "${job.updateNeonateYear}")
    public void updateNeonateYear() {
        logger.info("====开始更新新生儿年份统计====");
        commonService.neonateYearCount();
        logger.info("【更新新生儿年份统计结束】");
    }

    /**
     * 更新新生儿年份统计
     */
    @PostConstruct
    @Scheduled(cron = "${job.updateNeonateMonth}")
    public void updateNeonateMonth() {
        logger.info("====开始更新新生儿月份统计====");
        commonService.neonateMonthCount();
        logger.info("【更新新生儿月份统计结束】");
    }

    /**
     * 更新建档接种数量统计
     */
    @PostConstruct
    @Scheduled(cron = "${job.updateWisdomTodayData}")
    public void updateWisdomTodayData() {
        logger.info("====开始更新建档接种数量统计====");
        redisVaccinationService.setWisdomTodayData();
        logger.info("【更新建档接种数量统计结束】");
    }

}