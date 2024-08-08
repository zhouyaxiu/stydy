package com.shensu.service.nurClass.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.mybatis.mapper.nurClass.NurClassMapper;
import com.shensu.service.nurClass.ClassService;
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
 * @ClassName ClassServiceImpl
 * @Description
 * @createTime 2023/9/5
 */
@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private NurClassMapper nurClassMapper;


    @Override
    public PageInfo<ClassInfo> classList(Map<String, Object> info) {
        // 查询条件
        String schId = info.get("schId").toString();
        String claNameYear = StringUtils.nullToString(info.get("claNameYear")); // 学年
        String claLevel = StringUtils.nullToString(info.get("claLevel"));   // 年纪
        String claShortName = StringUtils.nullToString(info.get("claShortName")); // 班级名称
        int pageNum = Integer.parseInt(info.get("pageNum").toString());
        int pageSize = Integer.parseInt(info.get("pageSize").toString());
        PageHelper.startPage(pageNum, pageSize);
        List<ClassInfo> classList = nurClassMapper.getClassList(schId,claNameYear, claLevel, claShortName);
        return new PageInfo<>(classList);
    }

    @Override
    public String insertClass(ClassInfo classInfo) {
        String claNameYear = classInfo.getClaNameYear();
        String claLevel = classInfo.getClaLevel();
        String claShortName = classInfo.getClaShortName();
        classInfo.setClaName(claNameYear+ClassInfo.getClaLevelName(claLevel)+claShortName);
        nurClassMapper.insertClass(classInfo);
        return "新增成功！";
    }

    @Override
    public ClassInfo getClass(String classId) {
        ClassInfo classById = nurClassMapper.getClassById(classId);
        return classById;
    }

    @Override
    public String updateClass(String classId, String claTotal, String claLinkman, String claPhone) {
        ClassInfo classById = nurClassMapper.getClassById(classId);
        if (null == classById) {
            return "该班级已被删除！";
        }
        if (!claTotal.equals(classById.getClaTotal())) {
            int studentNumByClassCode = nurClassMapper.getStudentNumByClassId(classId);
            if (studentNumByClassCode > Integer.valueOf(claTotal)) return "该班级实际人数大于要修改的班级总人数，无法修改！";
        }
        nurClassMapper.updateClass(classId,claTotal,claLinkman,claPhone);
        return "修改成功！";
    }

    @Override
    public String deleteClass(String classIds) {
        List<String> Ids = Arrays.asList(classIds.split(","));
        int nums = nurClassMapper.getStudentNumByClassIds(Ids);
        if(nums >0) {
            if (Ids.size() > 1) {
                return "部分班级中包含学生，不能进行删除操作";
            } else {
                return "该班级下包含学生，不能进行删除操作";
            }
        } else {
            nurClassMapper.deleteStudentByClaCode(Ids);
            nurClassMapper.deleteClassByIds(Ids);
        }
        return "删除成功!";
    }

    @Override
    public void outPut(ExcelUtil excelUtil, Map<String, Object> info, List<ClassInfo> list) {
        //设置查询条件
        String condition = "";

        String claNameYear = StringUtils.nullToString(info.get("claNameYear")); // 学年
        String claLevel = StringUtils.nullToString(info.get("claLevel"));   // 年纪
        String claShortName = StringUtils.nullToString(info.get("claShortName")); // 班级名称

        if (!StringUtils.isEmpty(claNameYear) || !StringUtils.isEmpty(claLevel) || !StringUtils.isEmpty(claShortName)) {
            condition = "班级名称：";
            if (!StringUtils.isEmpty(claNameYear)) condition += claNameYear + "年份" + " ";
            if (!StringUtils.isEmpty(claLevel)) condition += ClassInfo.getClaLevelName(claLevel) + " ";
            if (!StringUtils.isEmpty(claShortName)) condition += claShortName;
        }
        excelUtil.fillCellData(2, (short) 0, condition);



        // 创建新行
        int firstRowNum = 4;//第一行记录在excel文件中的位置
        int firstPageCount = 1;//第一页的记录行个数
        int pageCount = 1;//每页显示的记录行个数
        int newRowCount = excelUtil.getNewRowCount(firstPageCount, list.size(), pageCount);//需要创建的行数
        int organLastRowNum = 4; //原始模板的最后一行
        if (newRowCount > 0) {
            List<Integer> colnumlist = new ArrayList<>();
            for (int i = 0; i < 142; i++) {
                colnumlist.add(i);
            }
            excelUtil.createNewRowAll(organLastRowNum, colnumlist, newRowCount);//创建新行
        }

        //格式
        HSSFSheet sheet = excelUtil.getWb().getSheetAt(0);
        HSSFCellStyle style = sheet.getRow(firstRowNum).getCell(1).getCellStyle();

        int i = 0, rownum = 0;
        for (ClassInfo bean : list) {
            rownum = firstRowNum + i;
            int cellNum = 0;
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getClaName(),style);     //班级名称（全称）
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getClaTotal(),style);     //总人数
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getClaLinkman(),style);     //联系人
            excelUtil.fillCellData(rownum, (short) cellNum++, bean.getClaPhone(),style);     //电话
            i++;
        }
    }
}
