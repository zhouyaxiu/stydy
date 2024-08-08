package com.shensu.jsjm.redis.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shensu.jsjm.constant.RedisKeyEnum;
import com.shensu.jsjm.constant.VaccinationCountEnum;
import com.shensu.jsjm.constant.VaccinationEnum;
import com.shensu.jsjm.mapper.CommonMapper;
import com.shensu.jsjm.mapper.VaccinationMapper;
import com.shensu.jsjm.model.*;
import com.shensu.jsjm.redis.RedisAreaService;
import com.shensu.jsjm.redis.RedisVaccinationService;
import com.shensu.jsjm.service.RedisService;
import com.shensu.jsjm.util.CommonUtil;
import com.shensu.jsjm.util.HttpUtil;
import com.shensu.jsjm.util.PropertyUtil;
import com.shensu.jsjm.util.ResponseBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

import static com.shensu.jsjm.constant.Constants.PROVINCE_CODE;

@Service
public class RedisVaccinationServiceImpl implements RedisVaccinationService {

    private static final Logger log = LoggerFactory.getLogger(RedisVaccinationServiceImpl.class);

    @Resource
    private RedisService redisService;

    @Resource
    private VaccinationMapper vaccinationMapper;

    @Resource
    private RedisAreaService redisAreaService;

    @Resource
    private PropertyUtil propertyUtil;

    @Resource
    private CommonMapper commonMapper;

    @Override
    public void setSameDayVaccination(String areaCode, int level) {
        List<AreaBean> areaBeans;
        List<SameDayVacc> sameDayVaccs = new ArrayList<>();
        SameDayVacc sameDayVacc;
        Map<String, AreaBean> allArea = redisAreaService.getAllArea();
        areaBeans = CommonUtil.getChildAreaList(allArea, areaCode);//下级
        String preJzTableName = "epi_jzjl";
        if (areaBeans.size() > 0) {
            if (0 == level) {
                //省级去市级
                for (AreaBean areaBean : areaBeans) {
                    if (StringUtils.isNotBlank(areaBean.getCode())) {
                        int freeNum = vaccinationMapper.getSameDayFreeVacc(areaBean.getCode());
                        int chargeNum = vaccinationMapper.getSameDayCharge(areaBean.getCode());
                        sameDayVacc = new SameDayVacc();
                        sameDayVacc.setAreaName(areaBean.getSimpleName());
                        sameDayVacc.setFreeVaccNum(freeNum);
                        sameDayVacc.setChargeVaccNum(chargeNum);
                        sameDayVaccs.add(sameDayVacc);
                    }
                }
            } else if (1 == level) {
                //市级
                List<JZJL> freeJzjls = vaccinationMapper.getSameDayFreeJZJL(preJzTableName);
                List<JZJL> chargeJzjls = vaccinationMapper.getSameDayChargeJZJL(preJzTableName);
                String countryCode;
                for (AreaBean areaBean : areaBeans) {
                    sameDayVacc = new SameDayVacc();
                    sameDayVacc.setAreaName(areaBean.getSimpleName());
                    sameDayVacc.setCountryCode(StringUtils.isNotBlank(areaBean.getCode()) ? areaBean.getCode().substring(0, 6) : null);
                    sameDayVaccs.add(sameDayVacc);
                }
                for (SameDayVacc sameDayVacc1 : sameDayVaccs) {
                    if (StringUtils.isNotBlank(sameDayVacc1.getCountryCode())) {
                        countryCode = sameDayVacc1.getCountryCode();
                        if (freeJzjls.size() > 0) {
                            for (JZJL freeJzjl : freeJzjls) {
                                if (StringUtils.isNotBlank(freeJzjl.getStationCode()) && countryCode.equals(freeJzjl.getStationCode().substring(0, 6))) {
                                    sameDayVacc1.setFreeVaccNum(sameDayVacc1.getFreeVaccNum() + 1);
                                }
                            }
                        }
                        if (chargeJzjls.size() > 0) {
                            for (JZJL chargeJzjl : chargeJzjls) {
                                if (StringUtils.isNotBlank(chargeJzjl.getStationCode()) && countryCode.equals(chargeJzjl.getStationCode().substring(0, 6))) {
                                    sameDayVacc1.setChargeVaccNum(sameDayVacc1.getChargeVaccNum() + 1);
                                }
                            }
                        }
                    }
                }
            }
            redisService.addMap(RedisKeyEnum.visual_same_day_vaccination.getKey(), areaCode, JSONObject.toJSONString(sameDayVaccs));
        }

    }

    @Override
    public List<SameDayVacc> getSameDayVaccination(String areaCode, int level) {
//        if (!redisService.hasMapKey(RedisKeyEnum.visual_same_day_vaccination.getKey(), areaCode)) {
//            setSameDayVaccination(areaCode, level);
//        }
        String str = (String) redisService.getMapField(RedisKeyEnum.visual_same_day_vaccination.getKey(), areaCode);
        return JSONObject.parseObject(str, List.class);
    }

    @Override
    public void setVaccinationRate(String areaCode, int level, int ageMonth) {
        List<VaccinationRate> vaccinationRates = new ArrayList<>();

        EpiStatAge epiStatAge = null;
        QualifiedBean bean = new QualifiedBean();
        if (0 == level) {
            //省级
            //EPI_STAT_HGBHG_AGE_RPT_CITY
            epiStatAge = vaccinationMapper.getVaccRateByCity(ageMonth);
        } else if (1 == level) {
            //市级
            //EPI_STAT_HGBHG_AGE_RPT_UNIT
            epiStatAge = vaccinationMapper.getVaccRateByUnit(ageMonth, areaCode);
        }
        if (epiStatAge != null) {
            if (ageMonth == 2) {
                bean.setSz_012(-1);
                bean.setSz_061(-1);
                bean.setSz_033(-1);
                bean.setSz_045(-1);
                bean.setSz_046(-1);
                bean.setSz_045_all(-1);
                bean.setSz_037(-1);
            } else if (ageMonth == 3) {
                bean.setSz_012(-1);
                bean.setSz_061(-1);
                bean.setSz_045(-1);
                bean.setSz_046(-1);
                bean.setSz_045_all(-1);
                bean.setSz_037(-1);
            } else if (ageMonth == 4) {
                bean.setSz_012(-1);
                bean.setSz_061(-1);
                bean.setSz_046(-1);
                bean.setSz_037(-1);
            } else if (ageMonth == 5 || ageMonth == 6) {
                bean.setSz_061(-1);
                bean.setSz_046(-1);
                bean.setSz_037(-1);
            }
            bean.setChildCount(epiStatAge.getChildCount());
            bean.setSz_001(epiStatAge.getSzB001());
            bean.setSz_009(epiStatAge.getSzOPV1());
            bean.setSz_010(epiStatAge.getSzOPV2());
            bean.setSz_011(epiStatAge.getSzOPV3());
            bean.setSz_012(epiStatAge.getSzOPV4());
            bean.setSz_015(epiStatAge.getSzDPT1());
            bean.setSz_016(epiStatAge.getSzDPT2());
            bean.setSz_017(epiStatAge.getSzDPT3());
            bean.setSz_018(epiStatAge.getSzDPT4());
            bean.setSz_059(epiStatAge.getSzMV1());
            bean.setSz_060(epiStatAge.getSzMV2());
            bean.setSz_061(epiStatAge.getSzMV3());
            bean.setSz_063(epiStatAge.getSzB063());
            bean.setSz_064(epiStatAge.getSzB064());
            bean.setSz_065(epiStatAge.getSzB065());
            bean.setSz_032(epiStatAge.getSzB032());
            bean.setSz_033(epiStatAge.getSzB033());
            bean.setSz_040(epiStatAge.getSzB040());
            bean.setSz_041(epiStatAge.getSzB041());
            bean.setSz_045(epiStatAge.getSzB045());
            bean.setSz_046(epiStatAge.getSzB046());
            bean.setSz_069(epiStatAge.getSzB069());
            bean.setSz_037(epiStatAge.getSzB037());
            bean.setSz_063_all(epiStatAge.getSzAllHEPB());
            bean.setSz_040_all(epiStatAge.getSzAllMENA());
            if (ageMonth == 2) {
                bean.setSz_012(-1);
                bean.setSz_009_all(epiStatAge.getSzAllOPV());
                bean.setSz_015_all(epiStatAge.getSzAllDPT4());
                bean.setSz_061(-1);
                bean.setSz_059_all(epiStatAge.getSzAllMV2());
                bean.setSz_033(-1);
                bean.setSz_032_all(epiStatAge.getSzB032());
                bean.setSz_045(-1);
                bean.setSz_046(-1);
                bean.setSz_045_all(-1);
                bean.setSz_037(-1);
                bean.setSz_five(epiStatAge.getSzAllFive1());
                bean.setSz_eight(epiStatAge.getSzAllEight1());
            } else if (ageMonth == 3) {
                bean.setSz_012(-1);
                bean.setSz_009_all(epiStatAge.getSzAllOPV());
                bean.setSz_015_all(epiStatAge.getSzAllDPT4());
                bean.setSz_061(-1);
                bean.setSz_059_all(epiStatAge.getSzAllMV2());
                bean.setSz_032_all(epiStatAge.getSzAllJEV2());
                bean.setSz_045(-1);
                bean.setSz_046(-1);
                bean.setSz_045_all(-1);
                bean.setSz_037(-1);
                bean.setSz_five(epiStatAge.getSzAllFive1());
                bean.setSz_eight(epiStatAge.getSzAllEight2());
            } else if (ageMonth == 4) {
                bean.setSz_012(-1);
                bean.setSz_009_all(epiStatAge.getSzAllOPV());
                bean.setSz_015_all(epiStatAge.getSzAllDPT4());
                bean.setSz_061(-1);
                bean.setSz_059_all(epiStatAge.getSzAllMV2());
                bean.setSz_032_all(epiStatAge.getSzAllJEV2());
                bean.setSz_046(-1);
                bean.setSz_045_all(epiStatAge.getSzB045());
                bean.setSz_037(-1);
                bean.setSz_five(epiStatAge.getSzAllFive1());
                bean.setSz_eight(epiStatAge.getSzAllEight2());
            } else if (ageMonth == 5 || ageMonth == 6) {
                bean.setSz_009_all(epiStatAge.getSzAllOPV4());
                bean.setSz_015_all(epiStatAge.getSzAllDPT4());
                bean.setSz_061(-1);
                bean.setSz_059_all(epiStatAge.getSzAllMV2());
                bean.setSz_032_all(epiStatAge.getSzAllJEV2());
                bean.setSz_046(-1);
                bean.setSz_045_all(epiStatAge.getSzB045());
                bean.setSz_037(-1);
                bean.setSz_five(epiStatAge.getSzAllFive2());
                bean.setSz_eight(epiStatAge.getSzAllEight3());
            } else if (ageMonth == 7 || ageMonth == 8) {
                bean.setSz_009_all(epiStatAge.getSzAllOPV4());
                bean.setSz_015_all(epiStatAge.getSzAllDPT4());
                bean.setSz_059_all(epiStatAge.getSzAllMV3());
                bean.setSz_032_all(epiStatAge.getSzAllJEV2());
                bean.setSz_045_all(epiStatAge.getSzAllMENAC2());
                bean.setSz_five(epiStatAge.getSzAllFive3());
                bean.setSz_eight(epiStatAge.getSzAllEight4());
            }
            //计算百分比
            bean.calcRate();
        }
        VaccinationRate vaccinationRate;
        for (VaccinationEnum vaccinationEnum : VaccinationEnum.values()) {
            vaccinationRate = new VaccinationRate();
            vaccinationRate.setVaccinationName(vaccinationEnum.getName());
            Method method;
            String rate;
            try {
                method = bean.getClass().getMethod(vaccinationEnum.getRate());
                rate = (String) method.invoke(bean);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                rate = "0.00";
            }
            vaccinationRate.setRate(rate);
            vaccinationRates.add(vaccinationRate);
        }
        //接种率由高到低排序
        vaccinationRates.sort((v1, v2) -> {
            BigDecimal rate1 = new BigDecimal(v1.getRate());
            BigDecimal rate2 = new BigDecimal(v2.getRate());
            return rate2.compareTo(rate1);
        });
        redisService.addMap(RedisKeyEnum.visual_vaccination_rate.getKey(), areaCode + "_" + ageMonth, JSONObject.toJSONString(vaccinationRates));
    }

    @Override
    public List<VaccinationRate> getVaccinationRate(String areaCode, int age) {
        String key = RedisKeyEnum.visual_vaccination_rate.getKey();

        String str = (String) redisService.getMapField(key, areaCode + "_" + age);
        return JSONObject.parseObject(str, LinkedList.class);
    }

    @Override
    public void setScanVaccinationRate(String areaCode, int level) {
        List<ScanVaccinationRate> scanVaccinationRates = new ArrayList<>();
        Map<String, AreaBean> allArea = redisAreaService.getAllArea();
        List<AreaBean> list = CommonUtil.getChildAreaList(allArea, areaCode);
        ScanVaccinationRate scanVaccinationRate = null;
        int year = DateUtil.thisYear();
        String startDate = year + "-01";
        String endDate = year + "-12";
        NumberFormat df = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        df.setMinimumFractionDigits(2);
        df.setRoundingMode(RoundingMode.HALF_UP);
        for (AreaBean area : list) {
            scanVaccinationRate = vaccinationMapper.getScanVaccinationRates(startDate, endDate, area.getCode(),level);

            if (scanVaccinationRate != null) {
                if (scanVaccinationRate.getSzCount() == 0) {
                    scanVaccinationRate.setRate("0.00%");
                } else {
                    scanVaccinationRate.setRate(df.format((double) scanVaccinationRate.getScanCount() / scanVaccinationRate.getSzCount()));
                }
                scanVaccinationRates.add(scanVaccinationRate);
                scanVaccinationRate.setRate(scanVaccinationRate.getRate().substring(0, scanVaccinationRate.getRate().lastIndexOf("%")));
            }
        }
        //接种率由高到低排序
        scanVaccinationRates.sort((s1, s2) -> {
            BigDecimal rate1 = new BigDecimal(s1.getRate());
            BigDecimal rate2 = new BigDecimal(s2.getRate());
            return rate2.compareTo(rate1);
        });
        redisService.addMap(RedisKeyEnum.visual_scan_vaccination_rate.getKey(), areaCode, JSONObject.toJSONString(scanVaccinationRates));
    }

    @Override
    public List<ScanVaccinationRate> getScanVaccinationRate(String areaCode, int level) {
//        if (!redisService.hasMapKey(RedisKeyEnum.visual_scan_vaccination_rate.getKey(), areaCode)) {
//            setScanVaccinationRate(areaCode, level);
//        }
        String str = (String) redisService.getMapField(RedisKeyEnum.visual_scan_vaccination_rate.getKey(), areaCode);
        return JSONObject.parseObject(str, LinkedList.class);
    }

    @Override
    public List<Inventory> getInventory(String areaCode, String vaccineId) {
        String key = RedisKeyEnum.visual_inventory.getKey();
        String mapKey = areaCode + "_" + vaccineId;
        String redisStr = (String) redisService.getMapField(key, mapKey);
        return JSONObject.parseObject(redisStr, LinkedList.class);
    }

    /**
     * 实时库存
     */
    @Override
    public void setInventory(String areaCode) {
        String key = RedisKeyEnum.visual_inventory.getKey();
        String mapKey;
        ResponseBean responseBean = null;
        String shortAreaCode = null;
        try {
            shortAreaCode = areaCode.substring(0, 10);
            if (!redisService.exists(RedisKeyEnum.auth_token_user_post.getKey()) || redisService.get(RedisKeyEnum.auth_token_user_post.getKey()) == null) {
                return;
            }
            String token = redisService.get(RedisKeyEnum.auth_token_user_post.getKey()).toString();

            Map<String, Object> map = new HashMap<>();
            map.put("GBCODE", shortAreaCode);
            String json = JSON.toJSONString(map);
            responseBean = HttpUtil.postHttpWithAuth(json, propertyUtil.getInventoryUrl(), "Bearer " + token);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("HttpInventory connect timeout;areaCode:【{}】", areaCode);
            e.printStackTrace();
        }
        if (responseBean != null && 200 == responseBean.getCode()) {
            List<Inventory> inventoryList = JSONObject.parseArray(responseBean.getData().toString(), Inventory.class);
            List<Inventory> newInventoryList = new ArrayList<>();
            List<Inventory> vaccineTypeInventoryList;
            int parentLevel = CommonUtil.getZoneLevelInt(areaCode);
            if (inventoryList != null && inventoryList.size() > 0) {
                for (Inventory inventory : inventoryList) {
                    int level = CommonUtil.getZoneLevelInt(inventory.getGBCODE() + "00");
                    if ((parentLevel + 1) == level) {
                        //只需要查询下一级地区
                        boolean hasInventory = false;
                        for (Inventory inventory1 : newInventoryList) {
                            //统计过的地区的疫苗大类不需要重复插入，在已统计的地区的疫苗大类中修改
                            if (inventory1.getGBCODE().equals(inventory.getGBCODE())
                                    && inventory1.getGOODSSORTCODE().equals(inventory.getGOODSSORTCODE())) {
                                if (inventory.getEXCEEDTYPE().contains("1")) {
                                    if (inventory.getEXCEEDTYPE().contains("3")) {
                                        //效期为1-3个月
                                        inventory1.setLessThreeMonthNum(inventory.getSTORENUM());
                                    } else {
                                        //效期小于1个月
                                        inventory1.setLessOneMonthNum(inventory.getSTORENUM());
                                    }
                                    hasInventory = true;
                                } else if (inventory.getEXCEEDTYPE().contains("3")) {
                                    //效期大于3个月
                                    inventory1.setMoreThreeMonthNum(inventory.getSTORENUM());
                                    hasInventory = true;
                                }
                            }
                        }
                        //该地区没有被统计过
                        if (!hasInventory) {
                            if (inventory.getEXCEEDTYPE().contains("1")) {
                                if (inventory.getEXCEEDTYPE().contains("3")) {
                                    //效期为1-3个月
                                    inventory.setLessThreeMonthNum(inventory.getSTORENUM());
                                } else {
                                    //效期小于1个月
                                    inventory.setLessOneMonthNum(inventory.getSTORENUM());
                                }
                                newInventoryList.add(inventory);
                            } else if (inventory.getEXCEEDTYPE().contains("3")) {
                                //效期大于3个月
                                inventory.setMoreThreeMonthNum(inventory.getSTORENUM());
                                newInventoryList.add(inventory);
                            }
                        }
                    }
                }
                //按照疫苗种类区分放入缓存中
                List<VaccineType> vaccineTypeList = commonMapper.getVaccineType();
                for (VaccineType vaccineType : vaccineTypeList) {
                    vaccineTypeInventoryList = new ArrayList<>();
                    for (Inventory inventory : newInventoryList) {
                        if (vaccineType.getCode().equals(inventory.getGOODSSORTCODE())) {
                            vaccineTypeInventoryList.add(inventory);
                        }
                    }
                    mapKey = areaCode + "_" + vaccineType.getValue();
                    redisService.addMap(key, mapKey, JSONObject.toJSONString(vaccineTypeInventoryList));
                }
            }
        }
    }

    @Override
    public void setNIPVaccination(String areaCode, int level) {
        if (0 == level || 1 == level) {
            List<NIPVacc> NIPVaccs = vaccinationMapper.getNIPVaccs(areaCode,level);
            redisService.addMap(RedisKeyEnum.visual_nip_vaccination.getKey(), areaCode, JSONObject.toJSONString(NIPVaccs));
        }


    }

    @Override
    public List<NIPVacc> getNIPVaccination(String areaCode, int level) {
        String str = (String) redisService.getMapField(RedisKeyEnum.visual_nip_vaccination.getKey(), areaCode);
        return JSONObject.parseObject(str, List.class);
    }

    @Override
    public void setVaccinationCounts(String areaCode, int level) {
        if (0 == level || 1 == level) {
            GJJZJCRPT gjjzjcrpt = vaccinationMapper.getVaccinationCounts(areaCode,level);
            List<VaccinationCount> vaccinationCounts = new ArrayList<>();
            for(VaccinationCountEnum vaccinationCountEnum:VaccinationCountEnum.values()){
                VaccinationCount vaccinationCount = new VaccinationCount();
                vaccinationCount.setVaccinationName(vaccinationCountEnum.getName());
                if(gjjzjcrpt != null) {
                    Method method;
                    long count;
                    try {
                        method = gjjzjcrpt.getClass().getMethod(vaccinationCountEnum.getValue());
                        count = (long) method.invoke(gjjzjcrpt);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        count = 0l;
                        e.printStackTrace();
                    }
                    vaccinationCount.setCount(count);
                }else {
                    vaccinationCount.setCount(0l);
                }
                vaccinationCounts.add(vaccinationCount);
            }
            redisService.addMap(RedisKeyEnum.visual_vaccination_count.getKey(), areaCode, JSONObject.toJSONString(vaccinationCounts));
        }

    }

    @Override
    public List<VaccinationCount> getVaccinationCounts(String areaCode, int level) {
        String str = (String) redisService.getMapField(RedisKeyEnum.visual_vaccination_count.getKey(), areaCode);
        return JSONObject.parseObject(str, List.class);
    }


    @Override
    @Async
    @Transactional(rollbackFor = {Exception.class})
    public void setWisdomTodayData() {


        List<WisdomTodayData> todayShotInfo = vaccinationMapper.getTodayShotInfo(LocalDate.now().toString());

        List<WisdomTodayData> todayChildBook = vaccinationMapper.getTodayChildBook(LocalDate.now().toString());

        List<WisdomTodayData> totalChildBook = vaccinationMapper.getTotalChildBook();

        Map<String, WisdomTodayData> mergedDataMap = new HashMap<>();

        for (WisdomTodayData data : todayShotInfo) {
            mergedDataMap.putIfAbsent(data.getAreaCode(), new WisdomTodayData());
            mergedDataMap.get(data.getAreaCode()).merge(data);
        }

        for (WisdomTodayData data : todayChildBook) {
            mergedDataMap.putIfAbsent(data.getAreaCode(), new WisdomTodayData());
            mergedDataMap.get(data.getAreaCode()).merge(data);
        }

        for (WisdomTodayData data : totalChildBook) {
            mergedDataMap.putIfAbsent(data.getAreaCode(), new WisdomTodayData());
            mergedDataMap.get(data.getAreaCode()).merge(data);
        }

        List<WisdomTodayData> mergedList = new ArrayList<>(mergedDataMap.values());
        vaccinationMapper.deleteVaccinationInfo();
        vaccinationMapper.insertVaccinationInfo(mergedList);

    }

    @Override
    public WisdomTodayData getWisdomTodayData(String areaCode) {
        WisdomTodayData vaccinationInfo = vaccinationMapper.getVaccinationInfo(PROVINCE_CODE.equals(areaCode) ? null : areaCode);
        return vaccinationInfo;
    }

    @Override
    public List<ScanVaccinationRate> getWisdomScanVaccinationRate(String areaCode, String YearMonth ,int level) {
//        String str = (String) redisService.getMapField(RedisKeyEnum.visual_wisdom_scan_vaccination_rate.getKey(), areaCode+"-"+month);
//        return JSONObject.parseObject(str, LinkedList.class);
        Map<String, AreaBean> allArea = redisAreaService.getAllArea();
        List<AreaBean> list = CommonUtil.getChildAreaList(allArea, areaCode);
        ScanVaccinationRate scanVaccinationRate = null;
        NumberFormat df = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        df.setMinimumFractionDigits(2);
        df.setRoundingMode(RoundingMode.HALF_UP);
        List<ScanVaccinationRate> scanVaccinationRates = new ArrayList<>();

        for (AreaBean area : list) {
            scanVaccinationRate = vaccinationMapper.getScanVaccinationRates(YearMonth+"-01", YearMonth+"-31", area.getCode(),level);

            if (scanVaccinationRate != null) {
                scanVaccinationRate.setAreaName(area.getSimpleName());
                if (scanVaccinationRate.getSzCount() == 0) {
                    scanVaccinationRate.setRate("0.00%");
                } else {
                    scanVaccinationRate.setRate(df.format((double) scanVaccinationRate.getScanCount() / scanVaccinationRate.getSzCount()));
                }
                scanVaccinationRates.add(scanVaccinationRate);
                scanVaccinationRate.setRate(scanVaccinationRate.getRate().substring(0, scanVaccinationRate.getRate().lastIndexOf("%")));
            }
        }
        //接种率由高到低排序
        scanVaccinationRates.sort((s1, s2) -> {
            BigDecimal rate1 = new BigDecimal(s1.getRate());
            BigDecimal rate2 = new BigDecimal(s2.getRate());
            return rate2.compareTo(rate1);
        });
        return scanVaccinationRates;
    }


    @Override
    public List<WisdomInventory> getNIPWisdomInventory(String areaCode) {
        String key = RedisKeyEnum.visual_wisdomInventory.getKey();
        String redisStr = (String) redisService.getMapField(key, areaCode+"-01");
        return JSONObject.parseObject(redisStr, LinkedList.class);
    }

    @Override
    public List<WisdomInventory> getWisdomInventory(String areaCode) {
        String key = RedisKeyEnum.visual_wisdomInventory.getKey();
        String redisStr = (String) redisService.getMapField(key, areaCode+"-02");
        return JSONObject.parseObject(redisStr, LinkedList.class);
    }

    @Override
    public void setWisdomInventory(String areaCode) {
        ResponseBean responseBean = null;
        String shortAreaCode = null;
        try {
            shortAreaCode = areaCode.substring(0, 10);
            if (!redisService.exists(RedisKeyEnum.auth_token_user_post.getKey()) || redisService.get(RedisKeyEnum.auth_token_user_post.getKey()) == null) {
                return;
            }
            String token = redisService.get(RedisKeyEnum.auth_token_user_post.getKey()).toString();

            Map<String, Object> map = new HashMap<>();
            map.put("GBCODE", shortAreaCode);
            String json = JSON.toJSONString(map);

            responseBean = HttpUtil.postHttpWithAuth(json, propertyUtil.getWisDomInventoryUrl(), "Bearer " + token);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("HttpInventory connect timeout;areaCode:【{}】", areaCode);
            e.printStackTrace();
        }
        List<ResponseInventory> inventoryList = new ArrayList<>();
        if (responseBean != null && 200 == responseBean.getCode()) {
            inventoryList = JSONObject.parseArray(responseBean.getData().toString(), ResponseInventory.class);
        }
        List<VaccineType> vaccineTypeList = commonMapper.getVaccineType();
        List<WisdomInventory> wisdomInventoryList = new ArrayList<>();
        WisdomInventory wisdomInventory;
        if (inventoryList.size() > 0) {
            for (VaccineType vaccineType : vaccineTypeList) {
                wisdomInventory = new WisdomInventory();
                wisdomInventory.setGOODSSORTCODE(vaccineType.getCode());
                wisdomInventory.setGOODSSORTNAME(vaccineType.getLabel().replaceAll("疫苗", ""));
                for (ResponseInventory inventory : inventoryList) {
                    if (vaccineType.getCode().equals(inventory.getGOODSSORTCODE()) && "01".equals(inventory.getGOODSTYPE())) {
                        if (inventory.getGBCODELEVEL().equals("01")) {
                            wisdomInventory.setProvinceSTORENUM(inventory.getSTORENUM());
                            wisdomInventory.setProvinceLessOneMonthNum(inventory.getOVERDUENum());
                            wisdomInventory.setProvinceLessThreeMonthNum(inventory.getONEMONTHNum());
                            wisdomInventory.setProvinceMoreThreeMonthNum(inventory.getTHREEMONTHNum());
                        }
                        if (inventory.getGBCODELEVEL().equals("02")) {
                            wisdomInventory.setCitySTORENUM(inventory.getSTORENUM());
                            wisdomInventory.setCityLessOneMonthNum(inventory.getOVERDUENum());
                            wisdomInventory.setCityLessThreeMonthNum(inventory.getONEMONTHNum());
                            wisdomInventory.setCityMoreThreeMonthNum(inventory.getTHREEMONTHNum());
                        }
                        if (inventory.getGBCODELEVEL().equals("03")) {
                            wisdomInventory.setCouSTORENUM(inventory.getSTORENUM());
                            wisdomInventory.setCouLessOneMonthNum(inventory.getOVERDUENum());
                            wisdomInventory.setCouLessThreeMonthNum(inventory.getONEMONTHNum());
                            wisdomInventory.setCouMoreThreeMonthNum(inventory.getTHREEMONTHNum());
                        }
                        if (inventory.getGBCODELEVEL().equals("04")) {
                            wisdomInventory.setUnitSTORENUM(inventory.getSTORENUM());
                            wisdomInventory.setUnitLessOneMonthNum(inventory.getOVERDUENum());
                            wisdomInventory.setUnitLessThreeMonthNum(inventory.getONEMONTHNum());
                            wisdomInventory.setUnitMoreThreeMonthNum(inventory.getTHREEMONTHNum());
                        }
                    }
                }
                wisdomInventoryList.add(wisdomInventory);
                redisService.addMap(RedisKeyEnum.visual_wisdomInventory.getKey(), areaCode + "-01", JSONObject.toJSONString(wisdomInventoryList));
            }
            wisdomInventoryList = new ArrayList<>();
            for (VaccineType vaccineType : vaccineTypeList) {
                wisdomInventory = new WisdomInventory();
                wisdomInventory.setGOODSSORTCODE(vaccineType.getCode());
                wisdomInventory.setGOODSSORTNAME(vaccineType.getLabel().replaceAll("疫苗", ""));
                for (ResponseInventory inventory : inventoryList) {
                    if (vaccineType.getCode().equals(inventory.getGOODSSORTCODE()) && "02".equals(inventory.getGOODSTYPE())) {
                        if (inventory.getGBCODELEVEL().equals("01")) {
                            wisdomInventory.setProvinceSTORENUM(inventory.getSTORENUM());
                            wisdomInventory.setProvinceLessOneMonthNum(inventory.getOVERDUENum());
                            wisdomInventory.setProvinceLessThreeMonthNum(inventory.getONEMONTHNum());
                            wisdomInventory.setProvinceMoreThreeMonthNum(inventory.getTHREEMONTHNum());
                        }
                        if (inventory.getGBCODELEVEL().equals("02")) {
                            wisdomInventory.setCitySTORENUM(inventory.getSTORENUM());
                            wisdomInventory.setCityLessOneMonthNum(inventory.getOVERDUENum());
                            wisdomInventory.setCityLessThreeMonthNum(inventory.getONEMONTHNum());
                            wisdomInventory.setCityMoreThreeMonthNum(inventory.getTHREEMONTHNum());
                        }
                        if (inventory.getGBCODELEVEL().equals("03")) {
                            wisdomInventory.setCouSTORENUM(inventory.getSTORENUM());
                            wisdomInventory.setCouLessOneMonthNum(inventory.getOVERDUENum());
                            wisdomInventory.setCouLessThreeMonthNum(inventory.getONEMONTHNum());
                            wisdomInventory.setCouMoreThreeMonthNum(inventory.getTHREEMONTHNum());
                        }
                        if (inventory.getGBCODELEVEL().equals("04")) {
                            wisdomInventory.setUnitSTORENUM(inventory.getSTORENUM());
                            wisdomInventory.setUnitLessOneMonthNum(inventory.getOVERDUENum());
                            wisdomInventory.setUnitLessThreeMonthNum(inventory.getONEMONTHNum());
                            wisdomInventory.setUnitMoreThreeMonthNum(inventory.getTHREEMONTHNum());
                        }
                    }
                }
                wisdomInventoryList.add(wisdomInventory);
                redisService.addMap(RedisKeyEnum.visual_wisdomInventory.getKey(), areaCode + "-02", JSONObject.toJSONString(wisdomInventoryList));
            }
        }
    }
}
