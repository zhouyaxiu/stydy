package com.shensu.service.reseedSummar.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBean;
import com.shensu.mybatis.entity.round.Round;
import com.shensu.mybatis.mapper.reseedSummar.ReseedSummarMapper;
import com.shensu.mybatis.mapper.round.RoundMapper;
import com.shensu.service.reseedSummar.ReseedSummarService;
import com.shensu.util.ExcelUtil;
import com.shensu.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        String claNameYear = info.get("claNameYear").toString();
        String year = info.get("year").toString();
        String vaccineName = info.get("vaccineName").toString();
        String roundName = info.get("roundName").toString();
        String claLevel = info.get("claLevel").toString();
        String claName = StringUtils.nullToString(info.get("claName"));
        condition.append(" 学年："+claNameYear).append(" 年份："+year).append(" 疫苗："+vaccineName).append(" 轮次："+roundName);
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
}
