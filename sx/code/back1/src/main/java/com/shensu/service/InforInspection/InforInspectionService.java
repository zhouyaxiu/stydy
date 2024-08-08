package com.shensu.service.InforInspection;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.constant.Constant;
import com.shensu.mybatis.entity.check.FullExcelField;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.mybatis.mapper.InforInspectionMapper.InforInspectionMapper;
import com.shensu.service.check.util.RevaccinationStatUtil;
import com.shensu.util.DateUtils;
import com.shensu.util.ExcelUtil;
import com.shensu.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ztt
 * @ClassName InforInspectionService
 * @Description 查验情况汇总:查验后结果以及补种信息的查询接口
 * @createTime 2023/4/4
 */
@Service
public class InforInspectionService {

    @Resource
    private InforInspectionMapper inforInspectionMapper;

    public PageInfo<InforReplantMess> findInforReplantMess( String schId, String classYear, String lunci, String grade, String className,int pageIndex, int pageSize) throws  Exception{
        PageHelper.startPage(pageIndex,pageSize);
        List<Student> list =inforInspectionMapper.findStudentReplate(schId,classYear,lunci,className,grade);
        List<InforReplantMess>  listInfo  = new ArrayList<>();
        for(int index=0;index<list.size();index++) {
            Student stu = list.get(index);
            InforReplantMess  replantMess  = new InforReplantMess();
            replantMess.setSchId(stu.getSchid());
            replantMess.setSchName(stu.getSchName());
            replantMess.setClaName(stu.getClazzName());
            replantMess.setClaLevel(Student.getClaLevelName(stu.getGrade()));
            replantMess.setStuRowkey(stu.getRowkey());
            replantMess.setStuName(stu.getStuName());
            replantMess.setStuBirth(stu.getStuBirth());
            replantMess.setIfFull(stu.getStuIsjz());
            StringBuffer needRelantStr = new StringBuffer();
            StringBuffer needReplantDate = new StringBuffer();
            for (int i = 0; i < Constant.fullList.size(); i++) {
                FullExcelField fef = Constant.fullList.get(i);
                System.out.println("get" + fef.getFiledName() + "After");
                System.out.println("getD"+fef.getFiledName().substring(1,4));
                Method getValueAfter = Student.class.getMethod("get" + fef.getFiledName() + "After");
                Method getValueState = Student.class.getMethod("get" + fef.getFiledName());
                Method  getDateAfter = Student.class.getMethod("getD"+fef.getFiledName().substring(1,4));
                Object invoke1 = getValueAfter.invoke(stu);
                Object  stateValue= getValueState.invoke(stu);

                if (RevaccinationStatUtil.hasVaccineState.equals(invoke1) && RevaccinationStatUtil.reachTimeButNotVaccineState.equals(stateValue) ) {
                    if (StringUtils.isBlank(Constant.ymStr.get(fef.getFiledName()))) {
                        continue;
                    }
                    needRelantStr.append(Constant.ymStr.get(fef.getFiledName())).append("，");
                    String date =(String) getDateAfter.invoke(stu);
                    if(DateUtils.isDate(date)){
                        if(date!=null){
                            if (date.indexOf("1980") != -1) {
                                date = "已种替代疫苗";
                            }else if (date.indexOf("1981") != -1){
                                date = "不详";
                            }else if (date.indexOf("1982") != -1){
                                date = "父亲/母亲/监护人要求不种";
                            }else if (date.indexOf("1983") != -1){
                                date = "禁忌";
                            }else if (date.indexOf("1984") != -1){
                                date = "超期不种";
                            }else if (date.indexOf("1985") != -1){
                                date = "已种但时间未知";
                            }else if (date.indexOf("1986") != -1){
                                date = "已患";
                            }
                            needReplantDate.append(date).append("，");
                        }
                    }else{
                            needReplantDate.append(date).append("，");
                    }
                }

            }
            if (needRelantStr.length() > 0 ) {
                needRelantStr = new StringBuffer(needRelantStr.substring(0, needRelantStr.length() - 1));
            }
            if (needReplantDate.length() > 0 ) {
                needReplantDate = new StringBuffer(needReplantDate.substring(0, needReplantDate.length() - 1));
            }
            replantMess.setReplantName(needRelantStr.toString());
            replantMess.setReplateDate(needReplantDate.toString());
            if(StringUtils.isNotEmpty(replantMess.getReplantName())){
                listInfo.add(replantMess);
            }
        }
        //根据查询条件查询学生信息
        return   new PageInfo<>(listInfo);
    }


    public void outPutReplantMess(ExcelUtil excelUtil, Map<String, Object> info, List<InforReplantMess> list) {

        String schId  = StringUtils.nullToString(info.get("schId"));
        String schName  = StringUtils.nullToString(info.get("schName"));
        String classYear  = StringUtils.nullToString(info.get("classYear"));
        String lunci  = StringUtils.nullToString(info.get("lunci"));
        String className  = StringUtils.nullToString(info.get("className"));
        String grade  = StringUtils.nullToString(info.get("grade"));

        StringBuilder build  = new StringBuilder();

        if (StringUtils.isNotEmpty(classYear)) {
            build.append(" 学年：" ).append (classYear);
        }
        if (StringUtils.isNotEmpty(grade)) {
            build.append(" 年级：" ).append (Constant.GRADE_MAP.get(grade));
        }

        if (StringUtils.isNotEmpty(lunci)) {
            build.append(" 轮次：").append("0".equals(lunci)?"秋季":"春季");
        }
        if (StringUtils.isNotEmpty(className)) {
            build .append(" 班级：" ).append(className);
        }
        excelUtil.fillCellData(1, (short) 0,build.toString());
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
        Iterator<InforReplantMess> iterator = list.iterator();
        int curRow = 0;
        //构建所有的行和列
        for (int j = 0; j < list.size() ; j++) {
            curRow = firstRowNum + j ;
            HSSFRow mess = excelUtil.createRow(curRow);
            for (int i = 0; i < 8; i++) {
                excelUtil.createCell(mess, (short) i);
                excelUtil.setCellStyle(curRow, (short) i, style);
            }
        }
        int index =0;
        for(int j=0;j< list.size();j++){
            curRow = firstRowNum + j ;
            InforReplantMess bean =list.get(j);
            index=j+1;
            excelUtil.fillCellData(curRow, (short) 0, index);
            excelUtil.fillCellData(curRow, (short) 1, bean.getClaLevel());
            excelUtil.fillCellData(curRow, (short) 2, bean.getClaName());
            excelUtil.fillCellData(curRow, (short) 3, bean.getStuName());
            excelUtil.fillCellData(curRow, (short) 4, bean.getStuBirth());
            excelUtil.fillCellData(curRow, (short) 5, bean.getReplantName());
            excelUtil.fillCellData(curRow, (short) 6, bean.getReplateDate());
            String ifFull ="";
            if("0".equals(bean.getIfFull())){
                ifFull="是";
            }else if("1".equals(bean.getIfFull())){
                ifFull="否-需补种";
            }else if("-1".equals(bean.getIfFull())){
                ifFull="否-无需补种";
            }else{
                ifFull=" ";
            }
            excelUtil.fillCellData(curRow, (short) 7, ifFull);
        }
    }
}
