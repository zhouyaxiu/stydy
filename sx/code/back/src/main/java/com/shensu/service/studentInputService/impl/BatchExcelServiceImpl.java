package com.shensu.service.studentInputService.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.check.InfoBean;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
import com.shensu.mybatis.mapper.highStu.HighStuMapper;
import com.shensu.mybatis.mapper.nurClass.NurClassMapper;
import com.shensu.mybatis.mapper.student.ExcelImportResutlMapper;
import com.shensu.mybatis.mapper.student.StudentMapper;
import com.shensu.service.check.CheckInfoService;
import com.shensu.service.check.SaasInfoService;
import com.shensu.service.studentInputService.BatchExcelService;
import com.shensu.util.ExcelUtil;
import com.shensu.util.StringUtils;
import com.shensu.util.Util;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.CellType.*;

/**
 * @author zcy
 * @ClassName BatchExcelServiceImpl
 * @Description
 * @createTime 2023/4/12
 */
@Service
public class BatchExcelServiceImpl implements BatchExcelService {
    @Autowired
    private SaasInfoService saasInfoService;

    @Autowired
    private ExcelImportResutlMapper importResutlMapper;

    @Autowired
    private HighStuMapper highStuMapper;

    @Autowired
    private NurClassMapper classMapper;

    @Override
    @DSTransactional
    public Map<String, Object> excelImport(HSSFSheet sheet, String schId, String classId,String className,String schStationCode) {
        Map<String, Object> map = new HashMap<>();
        int lastRowNum = sheet.getLastRowNum();
        List<ExcelImportResult> errorList = new ArrayList<>();
        List<HighStudent> insertList = new ArrayList<>();
        List<ExcelImportResult> checkAgainList = new ArrayList<>();
        ClassInfo classById = classMapper.getClassById(classId);
        int total = Integer.parseInt(classById.getClaTotal());
        int stuNum = classMapper.getStudentNumByClassId(classId);
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            // 判断必填项
            row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(STRING);
            String cid = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().trim();

            if (StringUtils.isEmpty(cid)) {
                // 出现空行，停止导入
                break;
            }
            ExcelImportResult result = new ExcelImportResult();
            result.setSchId(schId);
            result.setCid(cid);
            result.setClassName(className);
            result.setClaId(classId);
            result.setStationCode(schStationCode);
            // 先判断是否已插入定时匹配表
            HighStudent loseStuByCid = highStuMapper.getLoseStuByCid(cid);
            if (null != loseStuByCid) {
                result.setMsg("该学生已进入定时匹配，无需重复录入！");
                errorList.add(result);
                continue;
            }
            // 根据cid去saas中匹配数据
            List<InfoBean> fchildnos = saasInfoService.getInfoByCid(cid);
            if (fchildnos.isEmpty()) {
                result.setMsg("该学生在免疫库中查询不到！");
                errorList.add(result);
                checkAgainList.add(result);
                continue;
            }
            InfoBean infoBean = fchildnos.get(0);
            if (fchildnos.size() != 1) {
                result.setMsg("该编码匹配到多个学生，无法录入！");
                result.setName(infoBean.getName());
                errorList.add(result);
                continue;
            }
            // 查询本地库， 本地库不同学校直接新增，同一学校更新学生
            HighStudent oldStu = highStuMapper.getStuByCid(cid);
            if (null != oldStu) {
                result.setMsg("该学生已存在，无法录入！");
                result.setName(infoBean.getName());
                errorList.add(result);
                continue;
            }
            // 判断是否超出班级限定人数
            if (total < stuNum + insertList.size() +1) {
                result.setMsg("班级人数已达到上限，无法录入！");
                result.setName(infoBean.getName());
                errorList.add(result);
                continue;
            }
            HighStudent highStudent = saasInfoService.sassBeanToStudent(infoBean);
            // 填充学校班级信息
            highStudent.setSchid(schId);
            highStudent.setStuClacode(classId);
            highStudent.setStuStationcode(schStationCode);
            insertList.add(highStudent);
        }
        // 新增学生
        if (!insertList.isEmpty()) {
            // 批量插入学生
            highStuMapper.batchInsertStu(insertList);
        }
        // 批量插入失败数据
        if (!errorList.isEmpty()) {
            importResutlMapper.saveBatchList(errorList);
        }
        // 插入晚上定时匹配身份证
        if (!checkAgainList.isEmpty()) {
            importResutlMapper.saveCheckAgainList(checkAgainList);
        }
        int successTotal = insertList.size();
        map.put("data",errorList);
        map.put("message","excel总数"+(successTotal+errorList.size())+"条，导入成功"+successTotal+"条，失败"+errorList.size()+"条！");
        return map;
    }

    @Override
    public PageInfo<ExcelImportResult> queryList(String schId, String claId, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<ExcelImportResult> list = importResutlMapper.getListBySchIdAndYearAndGradeAndRound(schId,claId);
        return new PageInfo<>(list);
    }

    @Override
    public void outPut(ExcelUtil excelUtil, Map<String,Object> info, List<ExcelImportResult> list) {
        StringBuffer condition = new StringBuffer();
        String areaName = info.get("areaName").toString() + info.get("areaCode").toString();
        String schTypeCn = info.get("schTypeCn").toString();
        String year = info.get("claNameYear").toString();
        String claLevel = info.get("claLevel").toString();
        String claName = info.get("claName").toString();
        condition.append("地区：" + areaName);
        if (!StringUtils.isEmpty(schTypeCn)) {
            condition.append("学校类型：" + schTypeCn);
        }
        condition.append("学年：" + year);
        condition.append("年级：" + Student.getClaLevelName(claLevel));
        condition.append("班级：" + claName);
        excelUtil.fillCellData(1, (short) 0, condition);


        // 创建新行
        int firstRowNum = 4;//第一行记录在excel文件中的位置
        int firstPageCount = 1;//第一页的记录行个数
        int pageCount = 1;//每页显示的记录行个数
        int newRowCount = excelUtil.getNewRowCount(firstPageCount, list.size(), pageCount);//需要创建的行数
        int organLastRowNum = 4; //原始模板的最后一行
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

        int i = 0, rownum = 0;
        for (ExcelImportResult bean : list) {
            int cellNum = 0;
            rownum = firstRowNum + i;
            excelUtil.fillCellData(rownum, (short) cellNum++, cellNum,style);     //序号
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getName(),style);     //姓名
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getCid(),style);     //编码
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getClassName(),style);     //所在班级
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getMsg(),style);     //导入失败原因
            i++;
        }
    }
}
