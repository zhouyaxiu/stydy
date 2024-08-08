package com.shensu.service.InforInspection;


import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.constant.Constant;
import com.shensu.mybatis.entity.check.FullExcelField;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.inforInspection.InforInspectionBean;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.mybatis.entity.inforInspection.SchoolNumBean;
import com.shensu.mybatis.entity.systemmanage.AreaCodeAndName;
import com.shensu.mybatis.entity.systemmanage.SysArea;
import com.shensu.mybatis.mapper.InforInspectionMapper.InforInspectionMapper;
import com.shensu.mybatis.mapper.systemmanage.SysAreaMapper;
import com.shensu.redis.RedisOperates;
import com.shensu.service.check.util.RevaccinationStatUtil;
import com.shensu.service.systemmanage.SysAreaService;
import com.shensu.util.DateUtils;
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
    @Autowired
    private RedisOperates redisOperates;

    @Autowired
    private SysAreaMapper sysAreaMapper;

    //查验信息表
    public List<InforInspectionBean> inforInspectionQuery(String areaLever, String areaCode, String year, String lunci, String checkType) {

        List<InforInspectionBean> list = inforInspectionMapper.inforInspectionQuery(areaLever, areaCode, year, lunci, checkType);
        //查询每个地区有学生的学校数量有学生的学校数量
        //查询学校总人数
        List<SchoolNumBean> listSchNums = inforInspectionMapper.findSchStu(areaLever, areaCode, year, lunci, checkType);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                InforInspectionBean nn = list.get(i);
                for (int j = 0; j < listSchNums.size(); j++) {
                    SchoolNumBean nn2 = listSchNums.get(j);
                    if (nn.getAreaCode().equals(nn2.getAreaCode())) {
                        if(nn2.getStuCount()>0){
                            nn.setShcNum(nn.getShcNum()+1);
                            nn.setCheckSchNum(nn.getShcNum());
                        }

                    }
                }
            }
        }

        //查询每个学校查验的学生的数量
        //处理合计
        InforInspectionBean bean  = new InforInspectionBean();
        for(int i=0;i<list.size();i++){
            InforInspectionBean nn = list.get(i);
            bean.setShcNum(bean.getShcNum()+nn.getShcNum());//应查验学校数
            bean.setCheckSchNum(nn.getCheckSchNum()+bean.getCheckSchNum());//查验学校数
            bean.setAllStuNum(nn.getAllStuNum()+bean.getAllStuNum());//入学入托总人数
            bean.setCheckStuNum(nn.getCheckStuNum()+bean.getCheckStuNum());//实际查验人数
            bean.setHasCard(nn.getHasCard()+bean.getHasCard()); // 有证人数
            bean.setNeedCard(nn.getNeedCard()+bean.getNeedCard()); // 补证人数
            bean.setQcjzCount(nn.getQcjzCount()+bean.getQcjzCount()); //完成全程接种人数
            bean.setNeedReplant(nn.getNeedReplant()+bean.getNeedReplant());//需补种疫苗人数
            bean.setHasReplant(nn.getHasReplant()+bean.getHasReplant());//完成全程补种人数
            //卡介
            bean.setB001NeedReplant(nn.getB001NeedReplant()+bean.getB001NeedReplant());
            bean.setB001HasReplant(nn.getB001HasReplant()+bean.getB001HasReplant());
            //乙肝
            bean.setB063NeedReplant(nn.getB063NeedReplant()+bean.getB063NeedReplant());
            bean.setB064NeedReplant(nn.getB064NeedReplant()+bean.getB064NeedReplant());
            bean.setB065NeedReplant(nn.getB065NeedReplant()+bean.getB065NeedReplant());
            bean.setB063HasReplant(nn.getB063HasReplant()+bean.getB063HasReplant());
            bean.setB064HasReplant(nn.getB064HasReplant()+bean.getB064HasReplant());
            bean.setB065HasReplant(nn.getB065HasReplant()+bean.getB065HasReplant());

            //脊灰
            bean.setB009NeedReplant(nn.getB009NeedReplant()+bean.getB009NeedReplant());
            bean.setB010NeedReplant(nn.getB010NeedReplant()+bean.getB010NeedReplant());
            bean.setB011NeedReplant( nn.getB011NeedReplant()+bean.getB011NeedReplant());
            bean.setB012NeedReplant(nn.getB012NeedReplant()+bean.getB012NeedReplant());
            bean.setB009HasReplant(nn.getB009HasReplant()+bean.getB009HasReplant());
            bean.setB010HasReplant(nn.getB010HasReplant()+bean.getB010HasReplant());
            bean.setB011HasReplant(nn.getB011HasReplant()+bean.getB011HasReplant());
            bean.setB012HasReplant(nn.getB012HasReplant()+bean.getB012HasReplant());
            //百白破
            bean.setB015NeedReplant(nn.getB015NeedReplant()+bean.getB015NeedReplant());
            bean.setB016NeedReplant( nn.getB016NeedReplant()+bean.getB016NeedReplant());
            bean.setB017NeedReplant(nn.getB017NeedReplant()+bean.getB017NeedReplant());
            bean.setB018NeedReplant( nn.getB018NeedReplant()+bean.getB018NeedReplant());
            bean.setB015HasReplant(nn.getB015HasReplant()+bean.getB015HasReplant());
            bean.setB016HasReplant(nn.getB016HasReplant()+bean.getB016HasReplant());
            bean.setB017HasReplant(nn.getB017HasReplant()+bean.getB017HasReplant());
            bean.setB018HasReplant(nn.getB018HasReplant()+bean.getB018HasReplant());
            //白破
            bean.setB037NeedReplant(nn.getB037NeedReplant()+bean.getB037NeedReplant());
            bean.setB037HasReplant(nn.getB037HasReplant()+bean.getB037HasReplant());
            //麻疹
            bean.setB059NeedReplant(nn.getB059NeedReplant()+bean.getB059NeedReplant());
            bean.setB060NeedReplant(nn.getB060NeedReplant()+bean.getB060NeedReplant());
            bean.setB059HasReplant(nn.getB059HasReplant()+bean.getB059HasReplant());
            bean.setB060HasReplant(nn.getB060HasReplant()+bean.getB060HasReplant());
            //A 群流脑
            bean.setB040NeedReplant(nn.getB040NeedReplant()+bean.getB040NeedReplant());
            bean.setB041NeedReplant(nn.getB041NeedReplant()+bean.getB041NeedReplant());
            bean.setB040HasReplant(nn.getB040HasReplant()+bean.getB040HasReplant());
            bean.setB041HasReplant(nn.getB041HasReplant()+bean.getB041HasReplant());
            //A+C 群流脑
            bean.setB045NeedReplant(nn.getB045NeedReplant()+bean.getB045NeedReplant());
            bean.setB046NeedReplant(nn.getB046NeedReplant()+bean.getB046NeedReplant());
            bean.setB045HasReplant( nn.getB045HasReplant()+bean.getB045HasReplant());
            bean.setB046HasReplant(nn.getB046HasReplant()+bean.getB046HasReplant());
            //乙脑
            bean.setB032NeedReplant(nn.getB032NeedReplant()+bean.getB032NeedReplant());
            bean.setB033NeedReplant(nn.getB033NeedReplant()+bean.getB033NeedReplant());
            bean.setB032HasReplant(nn.getB032HasReplant()+bean.getB032HasReplant());
            bean.setB033HasReplant(nn.getB033HasReplant()+bean.getB033HasReplant());
            //甲肝1
            bean.setB073NeedReplant(nn.getB073NeedReplant()+bean.getB073NeedReplant());
            bean.setB073HasReplant(nn.getB073HasReplant()+bean.getB073HasReplant());

            bean.setB074NeedReplant(nn.getB074NeedReplant()+bean.getB074NeedReplant());
            bean.setB074HasReplant(nn.getB074HasReplant()+bean.getB074HasReplant());
            //水痘
            bean.setB050NeedReplant(nn.getB050NeedReplant()+bean.getB050NeedReplant());
            bean.setB132NeedReplant(nn.getB132NeedReplant()+bean.getB132NeedReplant());
            bean.setB050HasReplant(nn.getB050HasReplant()+bean.getB050HasReplant());
            bean.setB132HasReplant(nn.getB132HasReplant()+bean.getB132HasReplant());

        }
        list.add(bean);
        return list;
    }

    public void outPutInforInspect(ExcelUtil excelUtil, Map<String, Object> info, List<InforInspectionBean> list) {
        String areaLever = StringUtils.nullToString(info.get("areaLever"));
        String areaName = StringUtils.nullToString(info.get("areaName"));
        String areaCode = StringUtils.nullToString(info.get("areaCode"));
        String classYear  = StringUtils.nullToString(info.get("classYear"));
        String checkType  = StringUtils.nullToString(info.get("checkType"));
        String lunci  = StringUtils.nullToString(info.get("lunci"));
        if(!"4".equals(areaLever)){
            excelUtil.fillCellData(1, (short) 9, "（省、市、县、乡级使用，逐级汇总上报）");
        }
        if (StringUtils.isNotEmpty(areaName)) {
            excelUtil.fillCellData(2, (short) 0, "填报单位：" + areaName);
        }

        if (StringUtils.isNotEmpty(lunci)) {

            excelUtil.fillCellData(2, (short) 4, "轮次：" + ("0".equals(lunci)?" 春季（ ） 秋季（ √ ）":"春季（ √ ） 秋季（ ） "));
        }

        if (StringUtils.isNotEmpty(checkType)) {
            excelUtil.fillCellData(2, (short) 8, "入学类型：" + ("0".equals(checkType) ? "入托（ √ ） 入学（ ）":("1".equals(checkType)?"入托（ ） 入学（ √ ）":"入托（ ） 入学（ ）")));
        }

        excelUtil.fillCellData(2, (short) 13, "填报日期：" + DateUtils.getDate("yyyy-MM-dd"));

        int totalCount = list.size();// 记录总条数
        int firstRowNum =6;// 记录在excel文件中的起始行号
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
        Iterator<InforInspectionBean> iterator = list.iterator();
        int curRow = 0;
        //构建所有的行和列
        for (int j = 0; j < list.size(); j++) {
            curRow = firstRowNum + j ;
            HSSFRow mess = excelUtil.createRow(curRow);
            for (int i = 0; i < 56; i++) {
                excelUtil.createCell(mess, (short) i);
                excelUtil.setCellStyle(curRow, (short) i, style);
            }
        }
           for(int j=0;j< list.size();j++){
            curRow = firstRowNum + j ;
            InforInspectionBean bean =list.get(j);
            if(j==list.size()-1){
                excelUtil.fillCellData(curRow, (short) 0, "合  计");
            }else {
                excelUtil.fillCellData(curRow, (short) 0, bean.getAreaName());
            }
            excelUtil.fillCellData(curRow, (short) 1, bean.getCheckSchNum());
            excelUtil.fillCellData(curRow, (short) 2, bean.getCheckSchNum());
            excelUtil.fillCellData(curRow, (short) 3, bean.getAllStuNum());
            excelUtil.fillCellData(curRow, (short) 4, bean.getCheckStuNum());
            excelUtil.fillCellData(curRow, (short) 5, bean.getHasCard());
            excelUtil.fillCellData(curRow, (short) 6, bean.getNeedCard());
            excelUtil.fillCellData(curRow, (short) 7, bean.getQcjzCount());
            excelUtil.fillCellData(curRow, (short) 8, bean.getNeedReplant());
            excelUtil.fillCellData(curRow, (short) 9, bean.getHasReplant());


            excelUtil.fillCellData(curRow, (short) 10, bean.getB001NeedReplant());
            excelUtil.fillCellData(curRow, (short) 11, bean.getB001HasReplant());

            excelUtil.fillCellData(curRow, (short) 12, bean.getB063NeedReplant());
            excelUtil.fillCellData(curRow, (short) 13, bean.getB063HasReplant());
            excelUtil.fillCellData(curRow, (short) 14, bean.getB064NeedReplant());
            excelUtil.fillCellData(curRow, (short) 15, bean.getB064HasReplant());
            excelUtil.fillCellData(curRow, (short) 16, bean.getB065NeedReplant());
            excelUtil.fillCellData(curRow, (short) 17, bean.getB065HasReplant());


            excelUtil.fillCellData(curRow, (short) 18, bean.getB009NeedReplant());
            excelUtil.fillCellData(curRow, (short) 19, bean.getB009HasReplant());
            excelUtil.fillCellData(curRow, (short) 20, bean.getB010NeedReplant());
            excelUtil.fillCellData(curRow, (short) 21, bean.getB010HasReplant());
            excelUtil.fillCellData(curRow, (short) 22, bean.getB011NeedReplant());
            excelUtil.fillCellData(curRow, (short) 23, bean.getB011HasReplant());
            excelUtil.fillCellData(curRow, (short) 24, bean.getB012NeedReplant());
            excelUtil.fillCellData(curRow, (short) 25, bean.getB012HasReplant());


            excelUtil.fillCellData(curRow, (short) 26, bean.getB015NeedReplant());
            excelUtil.fillCellData(curRow, (short) 27, bean.getB015HasReplant());
            excelUtil.fillCellData(curRow, (short) 28, bean.getB016NeedReplant());
            excelUtil.fillCellData(curRow, (short) 29, bean.getB016HasReplant());
            excelUtil.fillCellData(curRow, (short) 30, bean.getB017NeedReplant());
            excelUtil.fillCellData(curRow, (short) 31, bean.getB017HasReplant());
            excelUtil.fillCellData(curRow, (short) 32, bean.getB018NeedReplant());
            excelUtil.fillCellData(curRow, (short) 33, bean.getB018HasReplant());

            excelUtil.fillCellData(curRow, (short) 34, bean.getB037NeedReplant());
            excelUtil.fillCellData(curRow, (short) 35, bean.getB037HasReplant());

            excelUtil.fillCellData(curRow, (short) 36, bean.getB059NeedReplant());
            excelUtil.fillCellData(curRow, (short) 37, bean.getB059HasReplant());
            excelUtil.fillCellData(curRow, (short) 38, bean.getB060NeedReplant());
            excelUtil.fillCellData(curRow, (short) 39, bean.getB060HasReplant());

            excelUtil.fillCellData(curRow, (short) 40, bean.getB040NeedReplant());
            excelUtil.fillCellData(curRow, (short) 41, bean.getB040HasReplant());
            excelUtil.fillCellData(curRow, (short) 42, bean.getB041NeedReplant());
            excelUtil.fillCellData(curRow, (short) 43, bean.getB041HasReplant());


            excelUtil.fillCellData(curRow, (short) 44, bean.getB045NeedReplant());
            excelUtil.fillCellData(curRow, (short) 45, bean.getB045HasReplant());
            excelUtil.fillCellData(curRow, (short) 46, bean.getB046NeedReplant());
            excelUtil.fillCellData(curRow, (short) 47, bean.getB046HasReplant());

            excelUtil.fillCellData(curRow, (short) 48, bean.getB032NeedReplant());
            excelUtil.fillCellData(curRow, (short) 49, bean.getB032HasReplant());
            excelUtil.fillCellData(curRow, (short) 50, bean.getB033NeedReplant());
            excelUtil.fillCellData(curRow, (short) 51, bean.getB033HasReplant());

            excelUtil.fillCellData(curRow, (short) 52, bean.getB073NeedReplant());
            excelUtil.fillCellData(curRow, (short) 53, bean.getB073HasReplant());
            excelUtil.fillCellData(curRow, (short) 54, bean.getB074NeedReplant());
            excelUtil.fillCellData(curRow, (short) 55, bean.getB074HasReplant());
        }

            font = excelUtil.createFont();
            font.setFontHeightInPoints((short) 9);
            font.setFontName("宋体");
            style = excelUtil.createCellStyle();
            style.setBorderTop(BorderStyle.NONE);// 上边框
            style.setBorderBottom(BorderStyle.NONE);// 下边框
            style.setBorderLeft(BorderStyle.NONE);// 左边框
            style.setBorderRight(BorderStyle.NONE);// 右边框
            style.setAlignment(HorizontalAlignment.LEFT);// 左右居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
            style.setFont(font);// 设置样式的字体
            style.setFont(font);// 设置样式的字体
            style.setWrapText(false);//自动换行

         curRow++;
          HSSFRow mess = excelUtil.createRow(curRow);
          for (int i = 0; i < 57; i++) {
            excelUtil.createCell(mess, (short) i);
            excelUtil.setCellStyle(curRow, (short) i, style);
          }
        //处理需要展示的内容
        excelUtil.fillCellData(curRow, (short) 0,"说明：");
        curRow++;
        mess = excelUtil.createRow(curRow);
        for (int i = 0; i < 57; i++) {
            excelUtil.createCell(mess, (short) i);
            excelUtil.setCellStyle(curRow, (short) i, style);
        }
        excelUtil.fillCellData(curRow, (short) 0, "1、本表为通用汇总表，各级分入托、入学儿童分类进行统计报告；");
        curRow++;
        mess = excelUtil.createRow(curRow);
        for (int i = 0; i < 57; i++) {
            excelUtil.createCell(mess, (short) i);
            excelUtil.setCellStyle(curRow, (short) i, style);
        }
        excelUtil.fillCellData(curRow, (short) 0, "2、乡镇卫生院、社区卫生服务中心根据托幼机构/学校报告的“儿童入托/入学预防接种查验情况登记表”，以托幼机构/学校为单位分别填报；");
        curRow++;
        mess = excelUtil.createRow(curRow);
        for (int i = 0; i < 57; i++) {
            excelUtil.createCell(mess, (short) i);
            excelUtil.setCellStyle(curRow, (short) i, style);
        }
        excelUtil.fillCellData(curRow, (short) 0, "3、县级以乡镇/街道为单位、市级以县（市、区）为单位、省级以市为单位，分入托、入学查验完成情况汇总报告；");
        curRow++;
        mess = excelUtil.createRow(curRow);
        for (int i = 0; i < 57; i++) {
            excelUtil.createCell(mess, (short) i);
            excelUtil.setCellStyle(curRow, (short) i, style);
        }
        excelUtil.fillCellData(curRow, (short) 0, "4、“完成全程接种人数”指在查验时，已完成该月龄/年龄应种所有疫苗接种的人数，“需补种疫苗人数”指查验时存在漏种且需补种的人数；");
        curRow++;
         mess = excelUtil.createRow(curRow);
        for (int i = 0; i < 57; i++) {
            excelUtil.createCell(mess, (short) i);
            excelUtil.setCellStyle(curRow, (short) i, style);
        }
        excelUtil.fillCellData(curRow, (short) 0,"5、疫苗漏种和补种情况，仅汇总存在漏种且需补种儿童情况，不包括因禁忌或超龄不需补种以及未到接种年龄的儿童。");

    }

    public PageInfo<InforReplantMess> findInforReplantMess(String areaCode, String  checkType, String schId, String classYear, String lunci, String className,String grade, int pageIndex, int pageSize) throws  Exception{
        PageHelper.startPage(pageIndex,pageSize);
        List<Student> list =inforInspectionMapper.findStudentReplate(areaCode,checkType,schId,classYear,lunci,className,grade);
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
        String areaCode = StringUtils.nullToString(info.get("areaCode"));
        String areaName = StringUtils.nullToString(info.get("areaName"));
        String checkType  = StringUtils.nullToString(info.get("checkType"));
        String schId  = StringUtils.nullToString(info.get("schId"));
        String schName  = StringUtils.nullToString(info.get("schName"));
        String classYear  = StringUtils.nullToString(info.get("classYear"));
        String lunci  = StringUtils.nullToString(info.get("lunci"));
        String className  = StringUtils.nullToString(info.get("className"));
        String grade  = StringUtils.nullToString(info.get("grade"));
        StringBuilder build  = new StringBuilder();
        if (StringUtils.isNotEmpty(areaName)) {
           build.append(" 选择地区：").append(areaName);
        }
        if (StringUtils.isNotEmpty(checkType)) {
            build.append(" 学校类型：" ).append ("0".equals(checkType) ? "幼托机构":("1".equals(checkType)?"小学":" "));
        }
        if (StringUtils.isNotEmpty(schName)) {
            build.append(" 学校名称：" ).append (schName);
        }
        if (StringUtils.isNotEmpty(classYear)) {
            build.append(" 学年：" ).append (classYear);
        }
        if (StringUtils.isNotEmpty(lunci)) {
          build.append(" 轮次：").append("0".equals(lunci)?"秋季":"春季");
        }
        if (StringUtils.isNotEmpty(grade)) {
            build.append(" 年级：").append(Constant.GRADE_MAP.get(grade));
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
            for (int i = 0; i < 9; i++) {
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
            excelUtil.fillCellData(curRow, (short) 1, bean.getSchName());
            excelUtil.fillCellData(curRow, (short) 2, bean.getClaLevel());
            excelUtil.fillCellData(curRow, (short) 3, bean.getClaName());
            excelUtil.fillCellData(curRow, (short) 4, bean.getStuName());
            excelUtil.fillCellData(curRow, (short) 5, bean.getStuBirth());
            excelUtil.fillCellData(curRow, (short) 6, bean.getReplantName());
            excelUtil.fillCellData(curRow, (short) 7, bean.getReplateDate());
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
            excelUtil.fillCellData(curRow, (short) 8, ifFull);
        }


    }

}
