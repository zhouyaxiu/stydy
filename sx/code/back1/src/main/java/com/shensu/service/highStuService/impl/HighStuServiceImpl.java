package com.shensu.service.highStuService.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.constant.Constant;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.highStudent.HighStuInfoAndCheck;
import com.shensu.mybatis.entity.highStudent.HighStudCheck;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.highStudent.NoticeContent;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.mybatis.entity.round.Round;
import com.shensu.mybatis.mapper.highStu.HighStuMapper;
import com.shensu.mybatis.mapper.reseedSummar.ReseedSummarMapper;
import com.shensu.mybatis.mapper.round.RoundMapper;
import com.shensu.service.highStuService.HighStuService;
import com.shensu.util.ExcelUtil;
import com.shensu.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class HighStuServiceImpl  implements HighStuService {
    @Resource
    private HighStuMapper  highStuMapper;
    @Resource
    private RoundMapper roundMapper;

    @Autowired
    private ReseedSummarMapper reseedSummarMapper;
    /**
     * 查询班级里面的学生根据学校id班级 和出生日期
     * */
    @Override
    public List<HighStudent> findCheckStuds(String schId, String claId, String startBirth, String endBirth) {
        String[] clsIds  = claId.split(",");
        List<HighStudent> list=  highStuMapper.findCheckStuds(schId,clsIds,startBirth,endBirth);
        return list;
    }

    /**
     * 批量插入查询结果
     * */
    @Override
    public int insertStuCheckResult(List<HighStudCheck> list) {
        if(list!=null && list.size()>0) {
            int num = highStuMapper.insertCheckResult(list);
            return num;
        }
        return 0;
    }
    /**
     * 删除查验信息
     * */
    @Override
    public int deleteCheckResultById(String roundId) {
        int num =highStuMapper.deleteStuCheckResult(roundId);
        return num;
    }

    @Override
    public PageInfo<HighStuInfoAndCheck> findCheckResult(String cid,String schId, String nowYear, String vaccName, String roundName, String claYear, String claLevel, String claId, String stuName, int pageNum, int pageSize) {
        //先查班级的id
        List<String> dbIds = null;
        if (StringUtils.isEmpty(claId)) {
            dbIds = reseedSummarMapper.getClaIdBySchIdAndYearAndLevel(schId, claYear, claLevel);
        } else {
            dbIds = Arrays.asList(claId.split(","));
        }
        //将符合要求的年级的id筛选出来
        Round  round =  roundMapper.findRound(nowYear,vaccName,roundName);
        List<String>  listAfter  = null;
        if(StringUtils.isNotEmpty(round.getClaIds())){
            listAfter=  new ArrayList<>();
            for(int i=0;i<dbIds.size();i++){
                if(round.getClaIds().contains(dbIds.get(i))){
                    listAfter.add(dbIds.get(i));
                }
            }
        }
        if(listAfter!=null){
            if(listAfter.size()<=0){
                listAfter.add("-1");
            }
        }
        PageHelper.startPage(pageNum,pageSize);
        String[] schIds=schId.split(",");
        List<HighStuInfoAndCheck> list  = highStuMapper.findStuCheckResult(cid,schId,nowYear,vaccName,roundName,listAfter,stuName);

        list.forEach(stu->{
            stu.setClaLevel(Student.getClaLevelName(stu.getClaLevel()));
            String cidResult   =stu.getCid();
            if(StringUtils.isNotBlank(cidResult)&&cidResult.length()>4){
                stu.setCid(cidResult.substring(0,4)+"*******"+cidResult.substring(cidResult.length()-4));
            }
            stu.setStuType(Student.changeStuType(stu.getStuType()));
            if(vaccName.equals("麻腮风疫苗")){
                if("0".equals(stu.getFirstDose())) {
                    stu.setFirstDose("✔");
                    stu.setSecondDose("❌");
                }else{
                    stu.setSecondDose("✔");
                    stu.setFirstDose("❌");
                }
            }else{
                if("0".equals(stu.getFirstDose())) {
                    stu.setFirstDose("✔");
                }else if("1".equals(stu.getFirstDose())){
                    stu.setFirstDose("❌");
                }else{
                    stu.setFirstDose(" ");
                }
                if("0".equals(stu.getSecondDose())){
                    stu.setSecondDose("✔");
                }else if("1".equals(stu.getSecondDose())){
                    stu.setSecondDose("❌");
                }else {
                    stu.setSecondDose(" ");
                }
            }
        });


        return new PageInfo(list);
    }

    @Override
    public void outPutMaSaiFengCheckResult(ExcelUtil excelUtil, StringBuilder builder, List<HighStuInfoAndCheck> list) {
        excelUtil.fillCellData(1, (short) 0,builder.toString());
        int totalCount = list.size();// 记录总条数
        int firstRowNum = 4;// 记录在excel文件中的起始行号
        HSSFFont font = excelUtil.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName("宋体");
        HSSFCellStyle style = excelUtil.createCellStyle();
        style = excelUtil.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);// 上边框
        style.setBorderBottom(BorderStyle.THIN);// 下边框
        style.setBorderLeft(BorderStyle.THIN);// 左边框
        style.setBorderRight(BorderStyle.THIN);// 右边框
        style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        style.setFont(font);// 设置样式的字体
        style.setFont(font);// 设置样式的字体
        style.setWrapText(true);//自动换行
        Iterator<HighStuInfoAndCheck> iterator = list.iterator();
        int curRow = 0;
        //构建所有的行和列
        for (int j = 0; j < list.size() ; j++) {
            curRow = firstRowNum + j ;
            HSSFRow mess = excelUtil.createRow(curRow);
            for (int i = 0; i < 12; i++) {
                excelUtil.createCell(mess, (short) i);
                excelUtil.setCellStyle(curRow, (short) i, style);
            }
        }
        int index =0;
        for(int j=0;j< list.size();j++){
            curRow = firstRowNum + j ;
            HighStuInfoAndCheck bean =list.get(j);
            excelUtil.fillCellData(curRow, (short) 0, "第"+bean.getRoundName()+"轮");
            excelUtil.fillCellData(curRow, (short) 1, bean.getClaNameyear());
            excelUtil.fillCellData(curRow, (short) 2, bean.getSchName());
            excelUtil.fillCellData(curRow, (short) 3, bean.getClaLevel());
            excelUtil.fillCellData(curRow, (short) 4, bean.getClaName());
            excelUtil.fillCellData(curRow, (short) 5, bean.getStuName());
            excelUtil.fillCellData(curRow, (short) 6, bean.getCid());
            excelUtil.fillCellData(curRow, (short) 7, bean.getFResideAddr());
            excelUtil.fillCellData(curRow, (short) 8, bean.getStuType());
            excelUtil.fillCellData(curRow, (short) 9, bean.getFirstDose());
            excelUtil.fillCellData(curRow, (short) 10, bean.getSecondDose());
            excelUtil.fillCellData(curRow, (short) 11, bean.getMark());
        }
    }

    @Override
    public void outPutCheckResult(ExcelUtil excelUtil, StringBuilder builder, List<HighStuInfoAndCheck> list) {
        excelUtil.fillCellData(1, (short) 0,builder.toString());
        int totalCount = list.size();// 记录总条数
        int firstRowNum = 4;// 记录在excel文件中的起始行号
        HSSFFont font = excelUtil.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName("宋体");
        HSSFCellStyle style = excelUtil.createCellStyle();
        style = excelUtil.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);// 上边框
        style.setBorderBottom(BorderStyle.THIN);// 下边框
        style.setBorderLeft(BorderStyle.THIN);// 左边框
        style.setBorderRight(BorderStyle.THIN);// 右边框
        style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        style.setFont(font);// 设置样式的字体
        style.setFont(font);// 设置样式的字体
        style.setWrapText(true);//自动换行
        Iterator<HighStuInfoAndCheck> iterator = list.iterator();
        int curRow = 0;
        //构建所有的行和列
        for (int j = 0; j < list.size() ; j++) {
            curRow = firstRowNum + j ;
            HSSFRow mess = excelUtil.createRow(curRow);
            for (int i = 0; i < 10; i++) {
                excelUtil.createCell(mess, (short) i);
                excelUtil.setCellStyle(curRow, (short) i, style);
            }
        }
        int index =0;
        for(int j=0;j< list.size();j++){
            curRow = firstRowNum + j ;
            HighStuInfoAndCheck bean =list.get(j);
            excelUtil.fillCellData(curRow, (short) 0, "第"+bean.getRoundName()+"轮");
            excelUtil.fillCellData(curRow, (short) 1, bean.getClaNameyear());
            excelUtil.fillCellData(curRow, (short) 2, bean.getSchName());
            excelUtil.fillCellData(curRow, (short) 3, bean.getClaLevel());
            excelUtil.fillCellData(curRow, (short) 4, bean.getClaName());
            excelUtil.fillCellData(curRow, (short) 5, bean.getStuName());
            excelUtil.fillCellData(curRow, (short) 6, bean.getCid());
            excelUtil.fillCellData(curRow, (short) 7, bean.getFResideAddr());
            excelUtil.fillCellData(curRow, (short) 8, bean.getFirstDose());
            excelUtil.fillCellData(curRow, (short) 9, bean.getSecondDose());

        }
    }
    @Override
    public void outPutLiuGanCheckResult(ExcelUtil excelUtil, StringBuilder builder, List<HighStuInfoAndCheck> list) {
        excelUtil.fillCellData(1, (short) 0,builder.toString());
        int totalCount = list.size();// 记录总条数
        int firstRowNum = 4;// 记录在excel文件中的起始行号
        HSSFFont font = excelUtil.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName("宋体");
        HSSFCellStyle style = excelUtil.createCellStyle();
        style = excelUtil.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);// 上边框
        style.setBorderBottom(BorderStyle.THIN);// 下边框
        style.setBorderLeft(BorderStyle.THIN);// 左边框
        style.setBorderRight(BorderStyle.THIN);// 右边框
        style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        style.setFont(font);// 设置样式的字体
        style.setFont(font);// 设置样式的字体
        style.setWrapText(true);//自动换行
        Iterator<HighStuInfoAndCheck> iterator = list.iterator();
        int curRow = 0;
        //构建所有的行和列
        for (int j = 0; j < list.size() ; j++) {
            curRow = firstRowNum + j ;
            HSSFRow mess = excelUtil.createRow(curRow);
            for (int i = 0; i < 9; i++) {
                excelUtil.createCell(mess, (short) i);
                excelUtil.setCellStyle(curRow, (short) i, style);
            }
        }
        int index =0;
        for(int j=0;j< list.size();j++){
            curRow = firstRowNum + j ;
            HighStuInfoAndCheck bean =list.get(j);
            excelUtil.fillCellData(curRow, (short) 0,"第"+bean.getRoundName()+"轮");
            excelUtil.fillCellData(curRow, (short) 1, bean.getClaNameyear());
            excelUtil.fillCellData(curRow, (short) 2, bean.getSchName());
            excelUtil.fillCellData(curRow, (short) 3, bean.getClaLevel());
            excelUtil.fillCellData(curRow, (short) 4, bean.getClaName());
            excelUtil.fillCellData(curRow, (short) 5, bean.getStuName());
            excelUtil.fillCellData(curRow, (short) 6, bean.getCid());
            excelUtil.fillCellData(curRow, (short) 7, bean.getFResideAddr());
            excelUtil.fillCellData(curRow, (short) 8, bean.getFirstDose());
        }
    }
    @Override
    public int deleteCheckResult(String roundId, String stuNos) {
        String[] stuNo = null;
        if(StringUtils.isNotBlank(stuNos)){
            stuNo=stuNos.split(",");
        }
        int num = highStuMapper.deleteByRoundIdAndStuNos(roundId,stuNo);
        return num;
    }

    @Override
    public List<NoticeContent> queryBuZhongMess(String roundId, String stuNos) {
        String[] stuno=null;
        if(StringUtils.isNotBlank(stuNos)){
            stuno=stuNos.split(",");
        }
        List<NoticeContent> list = highStuMapper.queryBuZhongMess(roundId,stuno);
        return list;
    }


}
