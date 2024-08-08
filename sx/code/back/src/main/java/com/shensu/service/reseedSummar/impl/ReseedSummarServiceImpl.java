package com.shensu.service.reseedSummar.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.constant.Constant;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBean;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBeanOne;
import com.shensu.mybatis.entity.round.Round;
import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
import com.shensu.mybatis.mapper.reseedSummar.ReseedSummarMapper;
import com.shensu.mybatis.mapper.round.RoundMapper;
import com.shensu.service.reseedSummar.ReseedSummarService;
import com.shensu.util.Constants;
import com.shensu.util.ExcelUtil;
import com.shensu.util.StringUtils;
import com.shensu.util.Util;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.Year;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author zcy
 * @ClassName ReseedSummarServiceImpl
 * @Description
 * @createTime 2023/4/19
 */
@Service
public class ReseedSummarServiceImpl implements ReseedSummarService {
    @Autowired
    private ReseedSummarMapper reseedSummarMapper;
    @Autowired
    private RoundMapper roundMapper;


    @Override
    public PageInfo<StaticInforInspectionBean> queryList(Map<String,Object> info) {
        List<StaticInforInspectionBean> list = new ArrayList<>();
        // 查询条件
        String schId = info.get("schId").toString();
        String schName = info.get("schName").toString();
        String claNameYear = info.get("claNameYear").toString();
        String roundId = info.get("roundId").toString();
        String roundName = info.get("roundName").toString();
        String claLevel = info.get("claLevel").toString();
        String claId = StringUtils.nullToString(info.get("claId"));
        int pageNum = Integer.parseInt(info.get("pageNum").toString());
        int pageSize = Integer.parseInt(info.get("pageSize").toString());
        // 查询出指定班级
        Round round = roundMapper.getRoundById(roundId);

        String schIds = round.getSchIds();
        if (schIds.contains(schId)) {
            List<String> dbIds = new ArrayList<>();
            if (StringUtils.isEmpty(claId)) {
                dbIds = reseedSummarMapper.getClaIdBySchIdAndYearAndLevel(schId, claNameYear, claLevel);
            } else {
                dbIds = Arrays.asList(claId.split(","));
            }
            if (!dbIds.isEmpty()) {
                PageHelper.startPage(pageNum, pageSize);
                list = reseedSummarMapper.queryList(dbIds,roundId);
            }
        }
        if (!list.isEmpty()) {
            for (StaticInforInspectionBean bean : list) {
                bean.setRoundName(roundName);
                bean.setYear(claNameYear);
                bean.setSchName(schName);
            }
        }
        return new PageInfo<>(list);
    }

    public  void output(ExcelUtil excelUtil,List<StaticInforInspectionBean> list,Map<String,Object> info) {
        // 设置查询条件
        StringBuffer condition = new StringBuffer();
        String fullAreaName = info.get("areaCode").toString() + info.get("areaName").toString();
        String schTypeCn = StringUtils.nullToString(info.get("schTypeCn"));
        String schName = info.get("schName").toString();
        String claNameYear = info.get("claNameYear").toString();
        String year = info.get("year").toString();
        String vaccineName = info.get("vaccineName").toString();
        String roundName = info.get("roundName").toString();
        String claLevel = info.get("claLevel").toString();
        String claName = StringUtils.nullToString(info.get("claName"));
        condition.append("选择地区："+fullAreaName);
        if (StringUtils.isNotEmpty(schTypeCn)) {
            condition.append(" 学校类型："+schTypeCn);
        }
        condition.append(" 学校名称："+schName).append(" 学年："+claNameYear).append(" 年份："+year).append(" 疫苗："+vaccineName).append(" 轮次："+roundName);
        if (StringUtils.isNotEmpty(claLevel)) {
            condition.append(" 年级："+ ClassInfo.getClaLevelName(claLevel));
        }
        if (StringUtils.isNotEmpty(claName)) {
            condition.append(" 班级："+ claName);
        }
        excelUtil.fillCellData(1, (short) 0, condition);


        // 创建新行
        int firstRowNum = 5;//第一行记录在excel文件中的位置
        int firstPageCount = 1;//第一页的记录行个数
        int pageCount = 1;//每页显示的记录行个数
        int newRowCount = excelUtil.getNewRowCount(firstPageCount, list.size(), pageCount);//需要创建的行数
        int organLastRowNum = 5; //原始模板的最后一行
        int lastRowNum = organLastRowNum + newRowCount; //创建新行后的最后一行
        if (newRowCount > 0) {
            List<Integer> colnumlist = new ArrayList<>();
            for (int i = 0; i < 142; i++) {
                colnumlist.add(i);
            }

            //填表人、备注等占用的行下移到指定位置
            // excelUtil.createNewRow(organLastRowNum + 1, colnumlist, lastRowNum + 1);
            // excelUtil.fillCellData(lastRowNum + 1, (short) 0, excelUtil.getCellData(organLastRowNum + 1, (short) 0));

            //删除填表人、备注等占用的行
            //excelUtil.removeOldRow(organLastRowNum + 1);

            excelUtil.createNewRowAll(organLastRowNum, colnumlist, newRowCount);//创建新行
        }

        //格式
        HSSFSheet sheet = excelUtil.getWb().getSheetAt(0);
        HSSFCellStyle style = sheet.getRow(firstRowNum).getCell(1).getCellStyle();
        int i = 0, rownum = 0;
        for (StaticInforInspectionBean bean : list) {
            int cellNum = 0;
            rownum = firstRowNum + i;
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getRoundName(),style);     //轮次
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getYear(),style);     //学年
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getSchName(),style);     //学校名称
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getClaName(),style);     //班级
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getStuTotal(),style);     //学生人数
            if ("水痘疫苗".equals(vaccineName)) {
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getVarOneSz(),style);
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getVarTwoSz(),style);
            } else if ("流感疫苗".equals(vaccineName)) {
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getFluSz(),style);
            } else {
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getThisProvinceYz(),style);
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getOutProvinceYz(),style);
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getThisProvinceSz(),style);
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getOutProvinceSz(),style);
            }
            i++;
        }
    }


    @Override
    public List<StaticInforInspectionBeanOne> queryListOne(String areaCode, String areaLevel, String schId, String schType, String year,
                                                             String grade, String round, String isStation, String className) {
        String areaSql = null;
        String groupBySql = null;
        String selectNameSql = null;
        switch (areaLevel) {
            case "0":
                areaSql = "vs.CITYCODE is not null ";
                groupBySql = "vs.CITYCODE";
                selectNameSql = "vs.CITYNAME name,vs.CITYCODE code";
                break;
            case "1":
                areaSql = "vs.CITYCODE='" + areaCode + "'";
                groupBySql = "vs.countycode";
                selectNameSql = "vs.countyname name,vs.countycode code";
                break;
            case "2":
                areaSql = "vs.countycode='" + areaCode + "'";
                groupBySql = "vs.supcode";
                selectNameSql = "vs.supname name,vs.supcode code";
                break;
            case "3":
                areaSql = "vs.supcode='" + areaCode + "'";
                groupBySql = "vs.stationcode";
                selectNameSql = "vs.stationname name,vs.stationcode code";
                break;
            case "4":
                areaSql = "vs.stationcode='" + areaCode + "'";
                groupBySql = "vs.stationcode";
                selectNameSql = "vs.stationname name,vs.stationcode code";
                break;
        }
        if ("0".equals(isStation) ) {
            groupBySql = "sch.sch_id";
            selectNameSql  = "sch.sch_name name";
            if (StringUtils.isNotEmpty(schId)) {
                groupBySql = "stu.clazz_name,stu.grade";
                selectNameSql  = "stu.clazz_name name,stu.grade";
            }
        }

        Map<String,Integer> map = new HashMap<>();
        List<StaticInforInspectionBeanOne> list = reseedSummarMapper.queryListOne(areaSql, selectNameSql, groupBySql, schId, year, grade, round, className,schType);

        List<StaticInforInspectionBeanOne> schNumList = reseedSummarMapper.querySchNumOne(areaSql, selectNameSql, groupBySql, schId, year, grade, round, className,schType);
        schNumList.forEach(bo -> {
            map.put(bo.getCode(),bo.getSchoolCount());
        });
        if ("0".equals(isStation)) {
            map.put(areaCode,schNumList.size());
        }

        // 求部分疫苗小计情况
        for(StaticInforInspectionBeanOne bean : list) {
            bean.setGrade(Constant.GRADE_MAP.get(bean.getGrade()));
            // 卡介 b001
            // 乙肝
            bean.setYiGanNeedReplantSubtotal(bean.getB063NeedReplant()+ bean.getB064NeedReplant()+bean.getB065NeedReplant());
            bean.setYiGanHasReplantSubtotal(bean.getB063HasReplant()+bean.getB064HasReplant()+bean.getB065HasReplant());
            // 脊灰
            bean.setJiHuiNeedReplantSubtotal(bean.getB009NeedReplant()+bean.getB010NeedReplant()+bean.getB011NeedReplant()+bean.getB012NeedReplant());
            bean.setJiHuiHasReplantSubtotal(bean.getB009HasReplant()+bean.getB010HasReplant()+bean.getB011HasReplant()+bean.getB012HasReplant());
            // 百白破
            bean.setBaiBaiPoHasReplantSubtotal(bean.getB015HasReplant()+bean.getB016HasReplant()+bean.getB017HasReplant()+bean.getB018HasReplant());
            bean.setBaiBaiPoNeedReplantSubtotal(bean.getB015NeedReplant()+bean.getB016NeedReplant()+bean.getB017NeedReplant()+bean.getB018NeedReplant());
            // 白破
            bean.setBaiPoHasReplantSubtotal(bean.getB037HasReplant()+bean.getB038HasReplant()+bean.getB039HasReplant());
            bean.setBaiPoNeedReplantSubtotal(bean.getB037NeedReplant()+bean.getB038NeedReplant()+bean.getB039NeedReplant());
            // 麻疹类疫苗
            bean.setMaFengHasReplantSubtotal(bean.getB059HasReplant()+bean.getB060HasReplant());
            bean.setMaFengNeedReplantSubtotal(bean.getB059NeedReplant()+bean.getB060NeedReplant());
            // A群流脑疫苗
            bean.setAQunLiuNaoHasReplantSubtotal(bean.getB040HasReplant()+bean.getB041HasReplant());
            bean.setAQunLiuNaoNeedReplantSubtotal(bean.getB041NeedReplant()+bean.getB040NeedReplant());
            // A+C群流脑疫苗
            bean.setACQunLiuNaoHasReplantSubtotal(bean.getB045HasReplant()+bean.getB046HasReplant());
            bean.setACQunLiuNaoNeedReplantSubtotal(bean.getB045NeedReplant()+bean.getB046NeedReplant());
            // 乙脑
            bean.setYiNaoJianDuHuoHasReplantSubtotal(bean.getB032HasReplant()+bean.getB033HasReplant());
            bean.setYiNaoJianDuHuoNeedReplantSubtotal(bean.getB032NeedReplant()+bean.getB033NeedReplant());
            // 甲肝
            bean.setJiaGanMieHasReplantSubtotal(bean.getB073HasReplant()+bean.getB074HasReplant());
            bean.setJiaGanMieNeedReplantSubtotal(bean.getB073NeedReplant()+bean.getB074NeedReplant());
            // 水痘
            bean.setShuiDouHasReplantSubtotal(bean.getB050HasReplant() + bean.getB132HasReplant());
            bean.setShuiDouNeedReplantSubtotal(bean.getB050NeedReplant() + bean.getB132NeedReplant());
            // 已补种剂次总数
            int vaccineHasReplant = bean.getYiGanHasReplantSubtotal() + bean.getJiHuiHasReplantSubtotal() + bean.getBaiBaiPoHasReplantSubtotal() + bean.getBaiPoHasReplantSubtotal() + bean.getMaFengHasReplantSubtotal()
                    + bean.getAQunLiuNaoHasReplantSubtotal() + bean.getACQunLiuNaoHasReplantSubtotal() + bean.getYiNaoJianDuHuoHasReplantSubtotal() + bean.getJiaGanMieHasReplantSubtotal();
            bean.setVaccineHasReplant(vaccineHasReplant);
            // 需补种剂次总数
            int vaccineNeedReplant = bean.getYiGanNeedReplantSubtotal() + bean.getJiHuiNeedReplantSubtotal() + bean.getBaiBaiPoNeedReplantSubtotal() + bean.getBaiPoNeedReplantSubtotal() + bean.getMaFengNeedReplantSubtotal()
                    + bean.getAQunLiuNaoNeedReplantSubtotal() + bean.getACQunLiuNaoNeedReplantSubtotal() + bean.getYiNaoJianDuHuoNeedReplantSubtotal() + bean.getJiaGanMieNeedReplantSubtotal();
            bean.setVaccineNeedReplant(vaccineNeedReplant);
            // 补种率
            if (vaccineHasReplant == 0) {
                bean.setReplantPercent("0.00%");
            } else {
                NumberFormat nf = NumberFormat.getPercentInstance();
                nf.setMinimumFractionDigits(2);
                String replantPercent = nf.format((float) vaccineHasReplant / (float) vaccineNeedReplant);
                bean.setReplantPercent(replantPercent);
            }
            // 学校数
            //if (StringUtils.isEmpty(schId) && "1".equals(isStation)) {
            bean.setVirtualSchoolCount(map.get(bean.getCode()));
            bean.setSchoolCount(map.get(bean.getCode()));
            //  }
        }
        if (!list.isEmpty()) {
            // 计算合计项
            StaticInforInspectionBeanOne bean = new StaticInforInspectionBeanOne();
            bean.setName("合计");
            // if (StringUtils.isEmpty(schId)) {
            if ("0".equals(isStation)) {
                bean.setVirtualSchoolCount(map.get(areaCode));
                bean.setSchoolCount(map.get(areaCode));
            } else {
                bean.setVirtualSchoolCount(list.stream().mapToInt(StaticInforInspectionBeanOne :: getVirtualSchoolCount).sum());
                bean.setSchoolCount(list.stream().mapToInt(StaticInforInspectionBeanOne :: getSchoolCount).sum());
            }
            // }
            bean.setVirtualStudentCount(list.stream().mapToInt(StaticInforInspectionBeanOne::getVirtualStudentCount).sum());
            bean.setStudentCount(list.stream().mapToInt(StaticInforInspectionBeanOne :: getStudentCount).sum());
            bean.setHasNotCard(list.stream().mapToInt(StaticInforInspectionBeanOne :: getHasNotCard).sum());
            bean.setNeedCard(list.stream().mapToInt(StaticInforInspectionBeanOne :: getNeedCard).sum());
            bean.setNeedReplant(list.stream().mapToInt(StaticInforInspectionBeanOne :: getNeedReplant).sum());
            bean.setNeedReplantFull(list.stream().mapToInt(StaticInforInspectionBeanOne :: getNeedReplantFull).sum());

            bean.setB001HasReplant(list.stream().mapToInt(StaticInforInspectionBeanOne :: getB001HasReplant).sum());
            bean.setB001NeedReplant(list.stream().mapToInt(StaticInforInspectionBeanOne :: getB001NeedReplant).sum());
            bean.setYiGanNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getYiGanNeedReplantSubtotal).sum());
            bean.setYiGanHasReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getYiGanHasReplantSubtotal).sum());
            bean.setJiHuiHasReplantSubtotal( list.stream().mapToInt(StaticInforInspectionBeanOne :: getJiHuiHasReplantSubtotal).sum());
            bean.setJiHuiNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getJiHuiNeedReplantSubtotal).sum());
            bean.setBaiBaiPoHasReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getBaiBaiPoHasReplantSubtotal).sum());
            bean.setBaiBaiPoNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getBaiBaiPoNeedReplantSubtotal).sum());
            bean.setBaiPoHasReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getBaiPoHasReplantSubtotal).sum());
            bean.setBaiPoNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getBaiPoNeedReplantSubtotal).sum());
            bean.setMaFengHasReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getMaFengHasReplantSubtotal).sum());
            bean.setMaFengNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getMaFengNeedReplantSubtotal).sum());
            bean.setAQunLiuNaoHasReplantSubtotal( list.stream().mapToInt(StaticInforInspectionBeanOne :: getAQunLiuNaoHasReplantSubtotal).sum());
            bean.setAQunLiuNaoNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getAQunLiuNaoNeedReplantSubtotal).sum());
            bean.setACQunLiuNaoHasReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getACQunLiuNaoHasReplantSubtotal).sum());
            bean.setACQunLiuNaoNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getACQunLiuNaoNeedReplantSubtotal).sum());
            bean.setYiNaoJianDuHuoHasReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getYiNaoJianDuHuoHasReplantSubtotal).sum());
            bean.setYiNaoJianDuHuoNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getYiNaoJianDuHuoNeedReplantSubtotal).sum());
            bean.setJiaGanMieHasReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getJiaGanMieHasReplantSubtotal).sum());
            bean.setJiaGanMieNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getJiaGanMieNeedReplantSubtotal).sum());
            bean.setShuiDouHasReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getShuiDouHasReplantSubtotal).sum());
            bean.setShuiDouNeedReplantSubtotal(list.stream().mapToInt(StaticInforInspectionBeanOne :: getShuiDouNeedReplantSubtotal).sum());
            list.add(bean);
        }
        return list;
    }

    public  void outputOne(ExcelUtil excelUtil,List<StaticInforInspectionBeanOne> list,String areaName,String schType,String schName,String year,
                        String grade,String round,String isStation,String className,String userLevel) {
        StringBuffer condition = new StringBuffer();
        condition.append("选择地区：" + areaName);
        if (!StringUtils.isEmpty(schType)) {
            condition.append(" 学校类型：" + ("0".equals(schType) ? "幼托机构":"小学"));
        }
        if (!StringUtils.isEmpty(schName)) {
            condition.append(" 学校名称：" + schName);
        }
        if (!StringUtils.isEmpty(year)) {
            condition.append(" 学年：" + year);
        }
        if (!StringUtils.isEmpty(grade)) {
            condition.append(" 年级：" + Student.getClaLevelName(grade));
        }
        if (!StringUtils.isEmpty(className)) {
            condition.append(" 班级：" + className);
        }
        if (!StringUtils.isEmpty(round)) {
            condition.append(" 轮次：" + ("0".equals(round)? "秋季":"春季"));
        }
        excelUtil.fillCellData(1, (short) 0, condition);


        // 创建新行
        int firstRowNum = 5;//第一行记录在excel文件中的位置
        int firstPageCount = 1;//第一页的记录行个数
        int pageCount = 1;//每页显示的记录行个数
        int newRowCount = excelUtil.getNewRowCount(firstPageCount, list.size(), pageCount);//需要创建的行数
        int organLastRowNum = 5; //原始模板的最后一行
        int lastRowNum = organLastRowNum + newRowCount; //创建新行后的最后一行
        if (newRowCount > 0) {
            List<Integer> colnumlist = new ArrayList<>();
            for (int i = 0; i < 142; i++) {
                colnumlist.add(i);
            }

            //填表人、备注等占用的行下移到指定位置
            excelUtil.createNewRow(organLastRowNum + 1, colnumlist, lastRowNum + 1);
            excelUtil.fillCellData(lastRowNum + 1, (short) 0, excelUtil.getCellData(organLastRowNum + 1, (short) 0));

            //删除填表人、备注等占用的行
            excelUtil.removeOldRow(organLastRowNum + 1);

            excelUtil.createNewRowAll(organLastRowNum, colnumlist, newRowCount);//创建新行
        }

        //格式
        HSSFSheet sheet = excelUtil.getWb().getSheetAt(0);
        HSSFCellStyle style = sheet.getRow(firstRowNum).getCell(1).getCellStyle();
        if (StringUtils.isNotEmpty(schName)) {
            excelUtil.fillCellData(2, (short) 0, "名称");
        }
        int i = 0, rownum = 0;
        for (StaticInforInspectionBeanOne bean : list) {
            int cellNum = 0;
            rownum = firstRowNum + i;
            excelUtil.fillCellData(rownum, (short) cellNum++,StringUtils.isNotBlank(bean.getGrade())?(bean.getGrade()+ bean.getName()):bean.getName(),style);     //名称
            if ("1".equals(isStation)||!("4".equals(userLevel))) {
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getVirtualSchoolCount(),style);     //应查验学校数
                excelUtil.fillCellData(rownum, (short) cellNum++, bean.getSchoolCount(),style);     //实查验学xi
            }
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getVirtualStudentCount(),style);     //应查验人数
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getStudentCount(),style);     //实查验人数
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getHasNotCard(),style);     //无证
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getNeedCard(),style);     //已补证
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getNeedReplant(),style);     //需补种
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getNeedReplantFull(),style);     //已补种全
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getB001NeedReplant(),style);     //卡介
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getB001HasReplant(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getYiGanNeedReplantSubtotal(),style);  //乙肝
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getYiGanHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getJiHuiNeedReplantSubtotal(),style); // 脊灰
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getJiHuiHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getBaiBaiPoNeedReplantSubtotal(),style);    //百白破
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getBaiBaiPoHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getBaiPoNeedReplantSubtotal(),style);  // 白破
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getBaiPoHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getMaFengNeedReplantSubtotal(),style);   //麻疹
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getMaFengHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getAQunLiuNaoNeedReplantSubtotal(),style); // A群
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getAQunLiuNaoHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getACQunLiuNaoNeedReplantSubtotal(),style); // AC
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getACQunLiuNaoHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getYiNaoJianDuHuoNeedReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getYiNaoJianDuHuoHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getJiaGanMieNeedReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getJiaGanMieHasReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getShuiDouNeedReplantSubtotal(),style);
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getShuiDouHasReplantSubtotal(),style);

            i++;
        }
    }
}
