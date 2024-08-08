package com.shensu.service.scanCodeService;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shensu.constant.Constant;
import com.shensu.mybatis.entity.check.*;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.mybatis.entity.systemmanage.SysStation;
import com.shensu.mybatis.entity.systemmanage.VSysStation;
import com.shensu.mybatis.mapper.check.SaasInfoMapper;
import com.shensu.mybatis.mapper.highStu.HighStuMapper;
import com.shensu.mybatis.mapper.nurClass.NurClassMapper;
import com.shensu.mybatis.mapper.student.StudentInputMapper;
import com.shensu.mybatis.mapper.systemmanage.SysAreaMapper;
import com.shensu.service.check.SaasInfoService;
import com.shensu.service.check.impl.CheckInfoServiceImpl;
import com.shensu.service.check.util.RevaccinationStatUtil;
import com.shensu.util.*;
import com.shensu.utils.FindStationMess;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScanCodeService {

    @Resource
    private SaasInfoMapper saasInfoMapper;

    @Autowired
    private SaasInfoService saasInfoService;

    @Resource
    private CheckInfoServiceImpl checkInfoService;
    @Resource
    private StudentInputMapper studentInputMapper;
    @Resource
    private SysAreaMapper sysAreaMapper;
    @Resource
    private FindStationMess findStationMess;
    @Resource
    private NurClassMapper nurClassMapper;
    @Resource
    private HighStuMapper highStuMapper;


    /**
     * 通用扫码查询
     * @param info
     * @return
     */
    public JSONObject queryByConditions(JSONObject info) {
        JSONObject object = new JSONObject();
        String schid = info.getString("schid"); //学校id
        Integer type = info.getInteger("type"); //1:儿童编码, 2:儿童条码, 3:身份证件号码, 4:儿童姓名、性别、出生日期
        String childno = info.getString("childno"); //儿童编码
        String imuno = info.getString("imuno"); //儿童条码
        String cid = info.getString("cid"); //身份证件号码
        String name = info.getString("name"); //儿童姓名
        String gender = info.getString("gender"); //性别
        String birth = info.getString("birth"); //出生日期
        //根据身份证取查验库查询到用户的信息
        List<InfoBean> infoBeanList = null;
        if (type == 1) { //儿童编码
            infoBeanList = saasInfoMapper.queryInfoByFchildno(childno);
            BactInfo bactInfo = saasInfoService.getBactInfoByFchildno(childno);
            System.out.println(bactInfo);
        }
        if (type == 2) { //儿童条码 (多条)
            infoBeanList = saasInfoMapper.queryInfoByImuno(imuno);
        }
        if (type == 3) { //身份证件号码
            infoBeanList = saasInfoMapper.queryInfoByCid(cid);
        }
        if (type == 4) { //儿童姓名、性别、出生日期  (多条)
            infoBeanList = saasInfoMapper.queryInfoByThreeCondition(name, gender, birth);
        }

        //将查验结果进行处理
        if (CollectionUtils.isEmpty(infoBeanList)) {
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入" + Constant.SCAN_CODE_TYPE_MAP.get(type) + "！");
        } else if (infoBeanList.size() == 1) {
            InfoBean infoBean = infoBeanList.get(0);
            //测试身份证: 330102010120150186
            //流感2：Flu
            //水痘2：Var
            //麻疹1：MMR
            //List<JzjlBean> jzjlList = saasInfoMapper.queryJzjlByFchildnoOrdered(infoBean.getFchildno());
            //infoBean = saasInfoService.initBactInfo(infoBean, jzjlList);
            StudentBrief studentBrief = sassBeanToStudentBrief(infoBean);
            if(StringUtils.isNotEmpty(studentBrief.getStuBelongStationCode())){
                SysStation station = findStationMess.findStationMess(studentBrief.getStuBelongStationCode());
                if(station!=null) {
                    studentBrief.setStuBelongStationName(station.getOrganName());
                }
            }
            /*VSysStation station = sysAreaMapper.findStationBySchId(schid);
            if (station != null) {
                studentBrief.setStuBelongStationCode(station.getStationcode());
                studentBrief.setStuBelongStationName(station.getStationname());
            }*/
            //Student checkstu = checkInfoService.excuteCheck(stu, true);
            //changeStuToShow(checkstu);
            object.put("code", Constants.ResultSuccessCode);
            object.put("data", studentBrief);
        } else {
            object.put("data", infoBeanList);
            object.put("code", Constants.ResultSuccessCode);
        }
        return object;
    }


    /**
     * 保存扫码查询结果
     * @param info info
     * @return JSONObject
     */
    public JSONObject saveByFchildno(JSONObject info) {
        JSONObject object = new JSONObject();
        String childNo = info.getString("childno");
        String schId = info.getString("schid");
        String yearTime = info.getString("year"); //学年
        String grade = info.getString("grade"); //年级
        String className = info.getString("className"); //班级
        String classId = info.getString("classId"); //班级id
        String stationCode = info.getString("stationCode");


        //查询当前班级目前有多少人
        int num  = highStuMapper.findStuNumByClaId(classId);
        //查询班级可以容纳多少人
        ClassInfo  classInfo = nurClassMapper.getClassById(classId);
        if(classInfo!=null){
            if(StringUtils.isNotEmpty(classInfo.getClaTotal())){
                int claTotal  = Integer.parseInt(classInfo.getClaTotal());
                if(claTotal<=num){
                    object.put("code", Constants.ResultFailCode);
                    object.put("data","该班级人数已经满，请重新选择班级！");
                    return object;
                }
            }
        }

        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list = saasInfoMapper.queryInfoByFchildno(childNo);
        if (CollectionUtils.isEmpty(list)) {
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入儿童编码！");
        } else if (list.size() == 1) {
            //将这个学生进行查验后结果返回
            InfoBean infoBean = list.get(0);
            //查验前的学生
            StudentInfo stuInfo = this.sassBeanToStuInfo(infoBean);
            //看看同校下面有没有这个学生，如果有的话
            StudentInfo oldStudent = studentInputMapper.findStuInfoByNo(stuInfo.getStuNo(), schId);
            //同一学校不同学年轮次班级的也提示下转班
            if (oldStudent != null) {
                if (!grade.equals(oldStudent.getGrade())) {
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于" + oldStudent.getSchName() + "学校 " + oldStudent.getAcademicYear() + "学年" + Student.getClaLevelName(oldStudent.getGrade()) + oldStudent.getClassName() + ",是否要转年级？");
                } else if (!yearTime.equals(oldStudent.getAcademicYear())) {
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于" + oldStudent.getSchName() + "学校 " + oldStudent.getAcademicYear() + "学年" + Student.getClaLevelName(oldStudent.getGrade()) + oldStudent.getClassName() + ",是否要转学年？");
                } else if (!className.equals(oldStudent.getClassName())) {
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于" + oldStudent.getSchName() + "学校 " + oldStudent.getAcademicYear() + "学年" + Student.getClaLevelName(oldStudent.getGrade()) + oldStudent.getClassName() + ",是否要转班级？");
                } else {
                    object.put("code",Constants.STUEXIST);
                    object.put("data","该用户已经存在!");
                }
            } else {
                //看看其他学校在不在
                List<StudentInfo> listStusBefore = studentInputMapper.findStuInfoListByNo(infoBean.getFchildno());
                //如果其他学校也不在，那就直接插入
                if (CollectionUtils.isEmpty(listStusBefore)) {
                    //直接插入提示保存成功
                    //查询该学生是否在其他学校中存储，如果在其他学校中，则继续添加这个学生，但是需要给用户提示信息
                    List<JzjlBean> jzjlList = saasInfoMapper.queryJzjlByFchildnoOrdered(stuInfo.getStuNo());
                    stuInfo = initBactInfo(stuInfo, jzjlList);
                    VSysStation station = sysAreaMapper.findStationByStationCode(stationCode);
                    if (station != null) {
                        //stuInfo.setStuStationcode(station.getStationcode());
                        stuInfo.setStuStreetcode(station.getSupcode());
                        stuInfo.setStuCountycode(station.getCountycode());
                        stuInfo.setStuCitycode(station.getCitycode());

                    }
                    stuInfo.setSchid(schId);
                    stuInfo.setClassId(classId);
                    studentInputMapper.insertStuInfo(stuInfo);
                    object.put("code",Constants.ResultSuccessCode);
                    object.put("data","保存成功");
                } else {
                    //提示该用户已经在其他学校中存在了，需不需要继续操作
                    for (StudentInfo student : listStusBefore) {
                        student.setGrade(Student.getClaLevelName(student.getGrade()));
                        VSysStation station = sysAreaMapper.findStationByStationCode(student.getStuStationcode());
                        if (station != null) {
                            student.setStuStationcode(station.getStationcode());
                            student.setStuStreetcode(station.getSupcode());
                            student.setStuCountycode(station.getCountycode());
                            student.setStuCitycode(station.getCitycode());
                            student.setStuBelongStationCode(station.getStationcode());
                            student.setStuStationName(station.getStationname());

                        }
                    }
                    object.put("code", Constants.STUINOTHERCODE);
                    object.put("data", listStusBefore);
                }

            }
        }
        return object;
    }


    public JSONObject saveAndChange(JSONObject info) {
        JSONObject object = new JSONObject();
        String childNo = info.getString("childno");
        String schId = info.getString("schid");
        String classId = info.getString("classId"); //班级id
        String stationCode = info.getString("stationCode");
        String stateCode = info.getString("stateCode");
        String oldSchId = info.getString("oldSchId");

        //将这个学生进行查验后结果返回
        InfoBean infoBean = saasInfoMapper.queryInfoByChildno(childNo);
        //查验前的学生
        StudentInfo stuInfo = sassBeanToStuInfo(infoBean);
        //设置学生信息
        stuInfo.setSchid(schId);
        // 补充班级信息
        stuInfo.setClassId(classId);
        VSysStation station = sysAreaMapper.findStationByStationCode(stationCode);
        if (station != null) {
            //stuInfo.setStuStationcode(station.getStationcode());
            stuInfo.setStuStreetcode(station.getSupcode());
            stuInfo.setStuCountycode(station.getCountycode());
            stuInfo.setStuCitycode(station.getCitycode());
        }
        /*SysStation belongStation = findStationMess.findStationMess();
        if (belongStation != null) {
            stuInfo.setStuBelongStationName(belongStation.getOrganName());
        }*/
        //初始化接种记录
        List<JzjlBean> jzjlList = saasInfoMapper.queryJzjlByFchildnoOrdered(stuInfo.getStuNo());
        stuInfo = initBactInfo(stuInfo, jzjlList);

        StudentInfo oldStudent = studentInputMapper.findStuInfoByNo(stuInfo.getStuNo(), oldSchId);
        //查看在状态码是更新还是插入 204 说明这个学生在之前的判断中在其他学校中存在，所以直接插入当前的学校
        if("204".equals(stateCode)) {
            stuInfo.setRowkey(oldStudent.getRowkey());
            studentInputMapper.updateStuInfo(stuInfo);
        }else if("202".equals(stateCode)){
            stuInfo.setRowkey(oldStudent.getRowkey());
            studentInputMapper.updateStuInfo(stuInfo);
        }
        else {
            object.put("code",Constants.ResultFailCode);
            object.put("data","操作失败");
            return object;
        }
        object.put("code",Constants.ResultSuccessCode);
        object.put("data","保存成功！");
        return object;
    }


    public StudentInfo initBactInfo(StudentInfo stuInfo, List<JzjlBean> jzjlList) {
        int fluCount = 0;
        int varCount = 0;
        int mmrCount = 0;
        for (JzjlBean jzjlBean : jzjlList) {
            //流感
            if ("Flu".equals(jzjlBean.getFBactType()) && fluCount < 1) {
                if (fluCount == 0) {
                    stuInfo.setFluFir(jzjlBean.getJzrq());
                    stuInfo.setFluFirId(jzjlBean.getFbactid());
                    //stuInfo.setFluFirName(jzjlBean.getFBactName());
                }
                fluCount++;
            }
            //水痘
            if ("Var".equals(jzjlBean.getFBactType()) && varCount < 2) {
                //第一针
                if (varCount == 0) {
                    stuInfo.setVarFir(jzjlBean.getJzrq());
                    stuInfo.setVarFirId(jzjlBean.getFbactid());
                    //stuInfo.setVarFirName(jzjlBean.getFBactName());
                } else {
                    //如果接种了两针，那么由于查询的时候是时间从大到小排序的所以需要吧之前的针拿过来放到第二针，现在的针放到第一针
                    stuInfo.setVarSec(stuInfo.getVarFir());
                    stuInfo.setVarSecId(stuInfo.getVarFirId());
                    stuInfo.setVarFir(jzjlBean.getJzrq());
                    stuInfo.setVarFirId(jzjlBean.getFbactid());
                    //stuInfo.setVarSecName(jzjlBean.getFBactName());
                }
                varCount++;
            }
            //麻疹
            if ("MMR".equals(jzjlBean.getFBactType()) && mmrCount < 1) {
                stuInfo.setMeasles(jzjlBean.getJzrq());
                stuInfo.setMeaslesId(jzjlBean.getFbactid());
                //stuInfo.setMeaslesName(jzjlBean.getFBactName());
                mmrCount++;
            }
        }
        return stuInfo;
    }



    public JSONObject queryPlStu(Map<String,Object> info)  throws  Exception{
        JSONObject object  = new JSONObject();
        String  imonu =StringUtils.nullToString(info.get("imonu"));
        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list  = saasInfoMapper.queryInfoByImuno(imonu);
        if(list ==null || list.size()==0){
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入儿童条码！");
        }else if(list.size()==1){
            //将这个学生进行查验后结果返回
            InfoBean  infoBean  = list.get(0);
            //查验前的学生
            Student  stu  = this.sassBeanToStudent(infoBean);
            Student  checkstu = checkInfoService.excuteCheck(stu,true);
            changeStuToShow(checkstu);
            SysStation belongStation =  findStationMess.findStationMess(checkstu.getStuBelongStationcode());
            if(belongStation!=null) {
                checkstu.setStuBelongStationcode(belongStation.getOrganName());
            }
            object.put("code",Constants.ResultSuccessCode);
            object.put("data",checkstu);
        }else if(list.size()>1){
            List<Student> listStus  = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                InfoBean bean  = list.get(i);
                Student stu  = new Student();
                stu.setStuNo(bean.getFchildno());
                stu.setStuImuno(bean.getImuno());
                stu.setStuName(bean.getName());
                stu.setStuBirth(DateUtil.formatShort(bean.getBirth()));
                stu.setStuParentname(StringUtils.isNotBlank(bean.getMothername())?bean.getMothername():bean.getFathername());
                stu.setStuSex(bean.getSex());
                stu.setStuType(Student.changeStuType(bean.getRegtype()));
                listStus.add(stu);
                object.put("code",Constants.MultesultSuccessCode);
                object.put("data",listStus);
            }
        }
        return object;
    }
    public JSONObject savePlStu(Map<String,Object> info) {
        JSONObject object  = new JSONObject();
        String  imonu =StringUtils.nullToString(info.get("imonu"));
        String schId  = StringUtils.nullToString(info.get("schId"));
        String yearTime  =StringUtils.nullToString(info.get("stuclayear"));
        String grade  =StringUtils.nullToString(info.get("stuLevel"));
        String  lunci= StringUtils.nullToString(info.get("lunci"));
        String  className =StringUtils.nullToString(info.get("claName"));
        String  stationCode =StringUtils.nullToString(info.get("stationCode"));
        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list  = saasInfoMapper.queryInfoByImuno(imonu);
        if(list ==null || list.size()==0){
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入儿童条码！");
        }else if(list.size()==1){
            //将这个学生进行查验后结果返回
            InfoBean  infoBean  = list.get(0);
            //查验前的学生
            Student  stu  = this.sassBeanToStudent(infoBean);
            //看看同校下面有没有这个学生，如果有的话
            Student oldStudent = studentInputMapper.findStuByNo(stu.getStuNo(),schId);
            //同一学校不同学年轮次班级的也提示下转班
            if(oldStudent!=null){
                if(!grade.equals(oldStudent.getGrade())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转年级？");
                }else if(!yearTime.equals(oldStudent.getAcademicYear())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转学年？");
                }else if(!lunci.equals(oldStudent.getRounds())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转轮次？");
                }else if(!className.equals(oldStudent.getClazzName())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转班级？");
                }else{
                    object.put("code",Constants.STUEXIST);
                    object.put("data","该用户已经存在!");
                }
            }else{
                //看看其他学校在不在
                List<Student> listStusBefore = studentInputMapper.findStusByNo(stu.getStuNo());
                //如果其他学校也不在，那就直接插入
                if(listStusBefore==null || listStusBefore.size()==0){
                    //直接插入提示保存成功
                    //查询该学生是否在其他学校中存储，如果在其他学校中，则继续添加这个学生，但是需要给用户提示信息
                    Student  checkstu = checkInfoService.excuteCheck(stu,true);

                    checkstu.setSchid(schId);
                    checkstu.setStuStationcode(stationCode);
                    checkstu.setStuSourceid("0");
                    // 补充班级信息
                    checkstu.setClazzName(className);
                    checkstu.setAcademicYear(yearTime);
                    checkstu.setGrade(grade);
                    checkstu.setRounds(lunci);
                    studentInputMapper.insertStudent(checkstu);


                    object.put("code",Constants.ResultSuccessCode);
                    object.put("data","保存成功");
                }else{
                    //提示该用户已经在其他学校中存在了，需不需要继续操作
                    for(Student student : listStusBefore){
                        student.setGrade(Student.getClaLevelName(student.getGrade()));
                        SysStation stustation =  findStationMess.findStationMess(student.getStuStationcode());
                        if(stustation!=null) {
                            student.setStuStationname(stustation.getOrganName());
                        }
                    }
                    object.put("code",Constants.STUINOTHERCODE);
                    object.put("data",listStusBefore);
                }
            }
        }else if(list.size()>1){
            List<Student> listStus  = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                InfoBean bean  = list.get(i);
                Student stu  = new Student();
                stu.setStuNo(bean.getFchildno());
                stu.setStuImuno(bean.getImuno());
                stu.setStuName(bean.getName());
                stu.setStuBirth(bean.getBirth());
                stu.setStuParentname(StringUtils.isNotBlank(bean.getMothername())?bean.getMothername():bean.getFathername());
                stu.setStuSex(bean.getSex());
                stu.setStuType(Student.changeStuType(bean.getRegtype()));
                listStus.add(stu);
                object.put("code",Constants.MultesultSuccessCode);
                object.put("data",listStus);
            }
        }
        return object;
    }

    public JSONObject queryByStuNo(@NotNull Map<String,Object> info)  throws  Exception{
        JSONObject object  = new JSONObject();
        String  childNo =StringUtils.nullToString(info.get("childNo"));
        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list  = saasInfoMapper.queryInfoByFchildno(childNo);
        //将查验结果进行处理
        if(list ==null || list.size()==0){
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入儿童编码！");
        }else if(list.size()==1){
            //将这个学生进行查验后结果返回
            InfoBean  infoBean  = list.get(0);
            //查验前的学生
            Student  stu  = this.sassBeanToStudent(infoBean);
            Student  checkstu = checkInfoService.excuteCheck(stu,true);
            SysStation belongStation =  findStationMess.findStationMess(checkstu.getStuBelongStationcode());
            if(belongStation!=null) {
                checkstu.setStuBelongStationcode(belongStation.getOrganName());
            }else{
                checkstu.setStuBelongStationcode("");
            }
            changeStuToShow(checkstu);
            object.put("code",Constants.ResultSuccessCode);
            object.put("data",checkstu);
        }
        return object;
    }

    public JSONObject saveByStuNo(Map<String,Object> info) {
        JSONObject object  = new JSONObject();
        String  childNo = StringUtils.nullToString(info.get("childNo"));
        String schId  = StringUtils.nullToString(info.get("schId"));
        String yearTime  =StringUtils.nullToString(info.get("stuclayear"));
        String grade  =StringUtils.nullToString(info.get("stuLevel"));
        String  lunci= StringUtils.nullToString(info.get("lunci"));
        String  className = StringUtils.nullToString(info.get("claName"));
        String  stationCode = StringUtils.nullToString(info.get("stationCode"));
        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list  = saasInfoMapper.queryInfoByFchildno(childNo);
        if(list ==null || list.size()==0){
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入儿童编码！");
        }else if(list.size()==1){
            //将这个学生进行查验后结果返回
            InfoBean  infoBean  = list.get(0);
            //查验前的学生
            Student  stu  = this.sassBeanToStudent(infoBean);
            //看看同校下面有没有这个学生，如果有的话
            Student oldStudent = studentInputMapper.findStuByNo(stu.getStuNo(),schId);
            //同一学校不同学年轮次班级的也提示下转班
            if(oldStudent!=null){
                if(!grade.equals(oldStudent.getGrade())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转年级？");
                }else if(!yearTime.equals(oldStudent.getAcademicYear())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转学年？");
                }else if(!lunci.equals(oldStudent.getRounds())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转轮次？");
                }else if(!className.equals(oldStudent.getClazzName())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转班级？");
                }else{
                    object.put("code",Constants.STUEXIST);
                    object.put("data","该用户已经存在!");
                }
            }else{
                //看看其他学校在不在
                List<Student> listStusBefore = studentInputMapper.findStusByNo(stu.getStuNo());
                //如果其他学校也不在，那就直接插入
                if(listStusBefore==null || listStusBefore.size()==0){
                    //直接插入提示保存成功
                    //查询该学生是否在其他学校中存储，如果在其他学校中，则继续添加这个学生，但是需要给用户提示信息
                    Student checkstu = checkInfoService.excuteCheck(stu,true);

                    checkstu.setSchid(schId);
                    checkstu.setStuStationcode(stationCode);
                    checkstu.setStuSourceid("0");
                    // 补充班级信息
                    checkstu.setClazzName(className);
                    checkstu.setAcademicYear(yearTime);
                    checkstu.setGrade(grade);
                    checkstu.setRounds(lunci);
                    studentInputMapper.insertStudent(checkstu);
                    object.put("code",Constants.ResultSuccessCode);
                    object.put("data","保存成功");
                }else{
                    //提示该用户已经在其他学校中存在了，需不需要继续操作
                    for(Student student : listStusBefore){
                        student.setGrade(Student.getClaLevelName(student.getGrade()));
                        SysStation stustation = findStationMess.findStationMess(student.getStuStationcode());
                        if(stustation!=null) {
                            student.setStuStationname(stustation.getOrganName());
                        }
                    }
                    object.put("code",Constants.STUINOTHERCODE);
                    object.put("data",listStusBefore);
                }

            }
        }
        return object;
    }


    public JSONObject queryStuByThreeCondition(Map<String,Object> info) throws  Exception{
        JSONObject object  = new JSONObject();
        String  stuName = StringUtils.nullToString(info.get("stuName"));
        String  stuSex = StringUtils.nullToString(info.get("stuSex"));
        String  stuBirth =StringUtils.nullToString(info.get("stuBirth"));
        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list  = saasInfoMapper.queryInfoByThreeCondition(stuName,stuSex,stuBirth);
        if(list ==null || list.size()==0){
            object.put("code", Constants.ResultFailCode);
            object.put("data","当前个案匹配不成功，是否去手动添加模块添加该受种者？");
        }else if(list.size()==1){
            //将这个学生进行查验后结果返回
            InfoBean  infoBean  = list.get(0);
            //查验前的学生
            Student  stu  = this.sassBeanToStudent(infoBean);
            Student  checkstu = checkInfoService.excuteCheck(stu,true);
            SysStation belongStation =  findStationMess.findStationMess(checkstu.getStuBelongStationcode());
            if(belongStation!=null) {
                checkstu.setStuBelongStationcode(belongStation.getOrganName());
            }
            changeStuToShow(checkstu);
            object.put("code",Constants.ResultSuccessCode);
            object.put("data",checkstu);
        }else if(list.size()>1){
            List<Student> listStus  = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                InfoBean bean  = list.get(i);
                Student stu  = new Student();
                stu.setStuNo(bean.getFchildno());
                stu.setStuImuno(bean.getImuno());
                stu.setStuName(bean.getName());
                stu.setStuBirth(DateUtil.formatShort(bean.getBirth()));
                stu.setStuParentname(StringUtils.isNotBlank(bean.getMothername())?bean.getMothername():bean.getFathername());
                stu.setStuSex(bean.getSex());
                stu.setStuType(Student.changeStuType(bean.getRegtype()));
                listStus.add(stu);
                object.put("code",Constants.MultesultSuccessCode);
                object.put("data",listStus);
            }
        }
        return object;
    }
    public JSONObject saveStuByThreeCondition(Map<String,Object> info) {
        JSONObject object  = new JSONObject();
        String  stuName =StringUtils.nullToString(info.get("stuName"));
        String  stuSex =StringUtils.nullToString(info.get("stuSex"));
        String  stuBirth =StringUtils.nullToString(info.get("stuBirth"));
        //根据条形码取查验库查询到用户的信息
        String schId  = StringUtils.nullToString(info.get("schId"));
        String yearTime  =StringUtils.nullToString(info.get("stuclayear"));
        String grade  =StringUtils.nullToString(info.get("stuLevel"));
        String  lunci= StringUtils.nullToString(info.get("lunci"));
        String  className =StringUtils.nullToString(info.get("claName"));
        String  stationCode =StringUtils.nullToString(info.get("stationCode"));
        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list  = saasInfoMapper.queryInfoByThreeCondition(stuName,stuSex,stuBirth);
        if(list ==null || list.size()==0){
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入受种者信息！");
        }else if(list.size()==1){
            //将这个学生进行查验后结果返回
            InfoBean  infoBean  = list.get(0);
            //查验前的学生
            Student  stu  = this.sassBeanToStudent(infoBean);
            //看看同校下面有没有这个学生，如果有的话
            Student oldStudent = studentInputMapper.findStuByNo(stu.getStuNo(),schId);
            //同一学校不同学年轮次班级的也提示下转班
            if(oldStudent!=null){
                if(!grade.equals(oldStudent.getGrade())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转年级？");
                }else if(!yearTime.equals(oldStudent.getAcademicYear())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转学年？");
                }else if(!lunci.equals(oldStudent.getRounds())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转轮次？");
                }else if(!className.equals(oldStudent.getClazzName())){
                    object.put("code",Constants.STUINSCHOOL);
                    object.put("data","该学生已经存在于"+oldStudent.getSchName()+"学校 "+oldStudent.getAcademicYear()+"学年"+Student.getClaLevelName(oldStudent.getGrade())+("0".equals(oldStudent.getRounds())?"秋季":"春季")+"轮次"+oldStudent.getClazzName()+",是否要转班级？");
                }else{
                    object.put("code",Constants.STUEXIST);
                    object.put("data","该用户已经存在!");
                }
            }else{
                //看看其他学校在不在
                List<Student> listStusBefore = studentInputMapper.findStusByNo(stu.getStuNo());
                //如果其他学校也不在，那就直接插入
                if(listStusBefore==null || listStusBefore.size()==0){
                    //直接插入提示保存成功
                    //查询该学生是否在其他学校中存储，如果在其他学校中，则继续添加这个学生，但是需要给用户提示信息
                    Student  checkstu = checkInfoService.excuteCheck(stu,true);

                    checkstu.setSchid(schId);
                    checkstu.setStuStationcode(stationCode);
                    checkstu.setStuSourceid("0");
                    // 补充班级信息
                    checkstu.setClazzName(className);
                    checkstu.setAcademicYear(yearTime);
                    checkstu.setGrade(grade);
                    checkstu.setRounds(lunci);
                    studentInputMapper.insertStudent(checkstu);


                    object.put("code",Constants.ResultSuccessCode);
                    object.put("data","保存成功");
                }else{
                    //提示该用户已经在其他学校中存在了，需不需要继续操作
                    for(Student student : listStusBefore){
                        student.setGrade(Student.getClaLevelName(student.getGrade()));
                        SysStation stustation =  findStationMess.findStationMess(student.getStuStationcode());
                        if(stustation!=null) {
                            student.setStuStationname(stustation.getOrganName());
                        }
                    }
                    object.put("code",Constants.STUINOTHERCODE);
                    object.put("data",listStusBefore);
                }
            }
        }else if(list.size()>1){
            List<Student> listStus  = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                InfoBean bean  = list.get(i);
                Student stu  = new Student();
                stu.setStuNo(bean.getFchildno());
                stu.setStuImuno(bean.getImuno());
                stu.setStuName(bean.getName());
                stu.setStuBirth(bean.getBirth());
                stu.setStuParentname(StringUtils.isNotBlank(bean.getMothername())?bean.getMothername():bean.getFathername());
                stu.setStuSex(bean.getSex());
                stu.setStuType(Student.changeStuType(bean.getRegtype()));
                listStus.add(stu);
                object.put("code",Constants.MultesultSuccessCode);
                object.put("data",listStus);
            }
        }
        return object;
    }


    public JSONObject OKSave(Map<String,Object> info){
        JSONObject  object  = new JSONObject();
        String  childNo =StringUtils.nullToString(info.get("childNo"));
        String schId  = StringUtils.nullToString(info.get("schId"));
        String yearTime  =StringUtils.nullToString(info.get("stuclayear"));
        String grade  =StringUtils.nullToString(info.get("stuLevel"));
        String  lunci= StringUtils.nullToString(info.get("lunci"));
        String  className = StringUtils.nullToString(info.get("claName"));
        String  stationCode = StringUtils.nullToString(info.get("stationCode"));
        String  stateCode = StringUtils.nullToString(info.get("stateCode"));
        List<InfoBean> list  =  saasInfoMapper.queryInfoByFchildno(childNo);
        //将这个学生进行查验后结果返回
        InfoBean  infoBean  = list.get(0);
        //查验前的学生
        Student  stu  = this.sassBeanToStudent(infoBean);
        Student  checkstu = checkInfoService.excuteCheck(stu,true);
        //设置学生信息
        checkstu.setSchid(schId);
        checkstu.setStuStationcode(stationCode);
        checkstu.setStuSourceid("0");
        // 补充班级信息
        checkstu.setClazzName(className);
        checkstu.setAcademicYear(yearTime);
        checkstu.setGrade(grade);
        checkstu.setRounds(lunci);
        Student oldStudent = studentInputMapper.findStuByNo(stu.getStuNo(),schId);
        //查看在状态码是更新还是插入 204 说明这个学生在之前的判断中在其他学校中存在，所以直接插入当前的学校
        if("204".equals(stateCode)){
            studentInputMapper.insertStudent(checkstu);
        }else if("202".equals(stateCode)){
            checkstu.setRowkey(oldStudent.getRowkey());
            studentInputMapper.updateCheckInfo(checkstu);
        }else{
            object.put("code",Constants.ResultFailCode);
            object.put("data","操作失败");
        }
        object.put("code",Constants.ResultSuccessCode);
        object.put("data","保存成功！");


        return object ;
    }

    /**
     * 将查验库中的个案信息转为学生表信息
     */
    public Student sassBeanToStudent(InfoBean infoBean) {
        if (BeanUtil.isEmpty(infoBean)){
            return null;
        }
        Student studentById = new Student();
        if (!BeanUtil.isEmpty(infoBean)){

        }
        if (StringUtils.isNotEmpty(studentById.getStuNo())) {
            studentById.setStuIsjzz("0");
        } else {
            studentById.setStuIsjzz("1");
        }
        if (StringUtils.isNotEmpty(studentById.getStuMothername())) {
            studentById.setStuParentname(studentById.getStuMothername().trim());
        } else if (StringUtils.isNotEmpty(studentById.getStuFathername())) {
            studentById.setStuParentname(studentById.getStuFathername().trim());
        } else if (StringUtils.isNotEmpty(studentById.getStuOtherLinkMan())){
            studentById.setStuParentname(studentById.getStuOtherLinkMan().trim());
        }else{
            studentById.setStuParentname("");
        }
        return studentById;
    }

    /**
     * 将查验库中的个案信息转为学生表信息
     */
    public StudentBrief sassBeanToStudentBrief(InfoBean infoBean) {
        if (BeanUtil.isEmpty(infoBean)){
            return null;
        }
        StudentBrief studentById = new StudentBrief();
        if (!BeanUtil.isEmpty(infoBean)){
            DTOConvertUtil.infoBeanToStudentBrief(infoBean, studentById);
        }
        if (StringUtils.isNotEmpty(studentById.getStuNo())) {
            studentById.setStuIsjzz("0");
        } else {
            studentById.setStuIsjzz("1");
        }
        if (StringUtils.isNotEmpty(studentById.getStuMothername())) {
            studentById.setStuParentname(studentById.getStuMothername().trim());
        } else if (StringUtils.isNotEmpty(studentById.getStuFathername())) {
            studentById.setStuParentname(studentById.getStuFathername().trim());
        } else if (StringUtils.isNotEmpty(studentById.getStuOtherLinkMan())){
            studentById.setStuParentname(studentById.getStuOtherLinkMan().trim());
        }else{
            studentById.setStuParentname("");
        }
        return studentById;
    }

    /**
     * 将查验库中的个案信息转为学生表信息
     */
    public StudentInfo sassBeanToStuInfo(InfoBean infoBean) {
        if (BeanUtil.isEmpty(infoBean)){
            return null;
        }
        StudentInfo studentById = new StudentInfo();
        if (!BeanUtil.isEmpty(infoBean)){
            DTOConvertUtil.infoBeanToStudentInfo(infoBean, studentById);
        }
        if (StringUtils.isNotEmpty(studentById.getStuNo())) {
            studentById.setStuIsjzz("0");
        } else {
            studentById.setStuIsjzz("1");
        }
        if (StringUtils.isNotEmpty(studentById.getStuMothername())) {
            studentById.setStuParentname(studentById.getStuMothername().trim());
        } else if (StringUtils.isNotEmpty(studentById.getStuFathername())) {
            studentById.setStuParentname(studentById.getStuFathername().trim());
        } else if (StringUtils.isNotEmpty(studentById.getStuOtherLinkMan())){
            studentById.setStuParentname(studentById.getStuOtherLinkMan().trim());
        }else{
            studentById.setStuParentname("");
        }
        return studentById;
    }

    //补种信息查询
    public JSONObject queryReplateMess(Map<String, Object> info)  throws Exception{
        JSONObject object  = new JSONObject();
        String  childNo = StringUtils.nullToString(info.get("childNo"));
        String  stationName = StringUtils.nullToString(info.get("stationName"));
        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list  = saasInfoMapper.queryInfoByFchildno(childNo);
        //将这个学生进行查验后结果返回
        InfoBean  infoBean  = list.get(0);
        //查验前的学生
        Student  stu  = this.sassBeanToStudent(infoBean);
        //查验这个学生
        Student  checkstu = checkInfoService.excuteCheck(stu,true);
        //返回学生信息以及需要补种的疫苗信息
        Map<String,String> map =new HashMap<>();
        for (int i = 0; i < Constant.fullList.size(); i++) {
            FullExcelField fef = Constant.fullList.get(i);
            Method getValueAfter = Student.class.getMethod("get" + fef.getFiledName() + "After");
            Object invoke = getValueAfter.invoke(stu);
            if (null==invoke) {
                getValueAfter = Student.class.getMethod("get" + fef.getFiledName());
                invoke = getValueAfter.invoke(stu);
            }
            if (RevaccinationStatUtil.reachTimeButNotVaccineState.equals(invoke)) {
                if (StringUtils.isBlank(Constant.ymStr.get(fef.getFiledName()))) {
                    continue;
                }
                String ym =  Constant.ymStr.get(fef.getFiledName());
                //将疫苗名称和剂次名称分来放在map集合中
                if(StringUtils.isNumeric(ym.substring(ym.length()-1))){
                    map.put(ym.substring(0,ym.length()-1),"第"+ym.substring(ym.length()-1)+"剂次");
                }else{
                    map.put(ym,"第1剂次");
                }
            }
        }
        //获取归属接种单位信息
        SysStation belongStation =  findStationMess.findStationMess(checkstu.getStuBelongStationcode());

        InforReplantMess  replantMess  = new InforReplantMess();
        replantMess.setStuName(checkstu.getStuName());
        replantMess.setStuBirth(checkstu.getStuBirth());
        replantMess.setParentName(checkstu.getStuParentname());
        if(belongStation!=null) {
            replantMess.setBelongPalceStation(belongStation.getOrganName());
            replantMess.setBelongPlaceAddress(belongStation.getOrganAddress());
            replantMess.setBelongPhone(belongStation.getLinkPhone());
        }
        replantMess.setBelongSchoolStation(stationName);
        replantMess.setReplantMap(map);
        object.put("code",Constants.ResultSuccessCode);
        object.put("data",replantMess);
        return object;
    }

    //打印补证通知单(前)
    public JSONObject queryReplantCard(Map<String, Object> info) {
        JSONObject object  = new JSONObject();
        String  childNo =StringUtils.nullToString(info.get("childNo"));
        String  stationName = StringUtils.nullToString(info.get("stationName"));
        //根据条形码取查验库查询到用户的信息
        List<InfoBean> list  = saasInfoMapper.queryInfoByFchildno(childNo);
        //将这个学生进行查验后结果返回
        InfoBean  infoBean  = list.get(0);
        //查验前的学生
        Student  stu  = this.sassBeanToStudent(infoBean);
        //查验这个学生
        Student  checkstu = checkInfoService.excuteCheck(stu,true);
        SysStation belongStation =  findStationMess.findStationMess(checkstu.getStuBelongStationcode());

        //返回学生信息以及需要补种的疫苗信息
        InforReplantMess  replantMess  = new InforReplantMess();
        replantMess.setStuName(checkstu.getStuName());
        replantMess.setStuBirth(checkstu.getStuBirth());
        replantMess.setParentName(checkstu.getStuParentname());
        if(belongStation!=null) {
            replantMess.setBelongPalceStation(belongStation.getOrganName());
            replantMess.setBelongPlaceAddress(belongStation.getOrganAddress());
            replantMess.setBelongPhone(belongStation.getLinkPhone());
        }
        replantMess.setBelongSchoolStation(stationName);
        object.put("code",Constants.ResultSuccessCode);
        object.put("data",replantMess);
        return object;
    }

    public static void changeStuToShow(Student checkstu) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //将查验过后的学生进行处理
        String[] vaccinArray = {"D063","D064","D065","D001","D009","D010","D011","D012","D015","D016","D017","D018","D037","D059","D060","D040","D041","D045","D046","D032","D033","D073","D074","D050","D132"};
        for (String vaccin: vaccinArray) {
            String methodNameGet = "get" + vaccin;
            String methodNameSet = "set" + vaccin;
            Method methodGet = checkstu.getClass().getMethod(methodNameGet);
            Method methodSet = checkstu.getClass().getMethod(methodNameSet,String.class);
            String getValue = (String)methodGet.invoke(checkstu);
            //判断getValue是不是时间
            if(DateUtils.isDate(getValue)){
                //如果是日期的话判断下是不是特殊日期
                /**
                 * 特殊日期状态代表不需要再接种了
                 *
                 * 代码类型	中文解释	对应特殊日期年份
                 * 1    正常接种
                 * 2    禁忌	        1983
                 * 3    不详	        1981
                 * 4    已种	        1985
                 * 5    家长拒种	    1982
                 * 6    已种同效苗    1980
                 * 7    超期不种	    1984
                 * 8    已患	        1986
                 * 针对已种未种的判断：
                 * 1，4，6 算已种，3，5算未种需提示补种，2，7,8算未种但是不需提示补种。
                 */
                String year  = getValue.substring(0,4);
                //禁忌已患超级不中显示null
                if ("1983".equals(year) || "1986".equals(year) || "1984".equals(year)) {
                    methodSet.invoke(checkstu,"O");
                } else if ("1982".equals(year)||"1981".equals(year)) {
                    methodSet.invoke(checkstu,"X");
                } else {
                    methodSet.invoke(checkstu,"✔");
                }
            } else {
                if ("未到接种时间".equals(getValue)) {
                    methodSet.invoke(checkstu," ");
                } else if ("到期未种".equals(getValue)) {
                    methodSet.invoke(checkstu,"X");
                }
            }
        }
        //转换户籍类型
        checkstu.setStuType(Student.changeStuType(checkstu.getStuType()));
    }

    private void getStudentVal(String key, String value, Student stu) {
        if (key.equals("stu_no")) {
            stu.setStuNo(value);
        } else if (key.equals("UNITNAME")) {
            stu.setStuStationname(value);
        } else if (key.equals("FStatus")) {
            stu.setStuStatus(value);
        } else if (key.equals("stu_streetcode")) {
            stu.setStuStreetcode(value);
        } else if (key.equals("stu_imuno")) {
            stu.setStuImuno(value);
        } else if (key.equals("NAME")) {
            stu.setStuName(value);
        } else if (key.equals("BIRTH")) {
            stu.setStuBirth(value);
        } else if (key.equals("SEX")) {
            stu.setStuSex(value);
        } else if (key.equals("stu_type")) {
            stu.setStuType(value);
        } else if (key.equals("MOTHERNAME")) {
            stu.setStuMothername(value);
        } else if (key.equals("FATHERNAME")) {
            stu.setStuFathername(value);
        } else if (key.equals("stu_phone")) {
            stu.setStuPhone(value);
        } else if (key.equals("OTHERPHONE")) {
            stu.setStuOtherphone(value);
        } else if (key.equals("FPPDW")) {
            stu.setFPPDW(value);
        } else if (key.equals("FPPDH")) {
            stu.setFPPDH(value);
        } else if (key.equals("B001")) {
            stu.setB001(value);
        } else if (key.equals("B063")) {
            stu.setB063(value);
        } else if (key.equals("B064")) {
            stu.setB064(value);
        } else if (key.equals("B065")) {
            stu.setB065(value);
        } else if (key.equals("B009")) {
            stu.setB009(value);
        } else if (key.equals("B010")) {
            stu.setB010(value);
        } else if (key.equals("B011")) {
            stu.setB011(value);
        } else if (key.equals("B012")) {
            stu.setB012(value);
        } else if (key.equals("B015")) {
            stu.setB015(value);
        } else if (key.equals("B016")) {
            stu.setB016(value);
        } else if (key.equals("B017")) {
            stu.setB017(value);
        } else if (key.equals("B018")) {
            stu.setB018(value);
        } else if (key.equals("B037")) {
            stu.setB037(value);
        } else if (key.equals("B038")) {
            stu.setB038(value);
        } else if (key.equals("B039")) {
            stu.setB039(value);
        } else if (key.equals("B093")) {
            stu.setB093(value);
        } else if (key.equals("B094")) {
            stu.setB094(value);
        } else if (key.equals("B040")) {
            stu.setB040(value);
        } else if (key.equals("B041")) {
            stu.setB041(value);
        } else if (key.equals("B045")) {
            stu.setB045(value);
        } else if (key.equals("B046")) {
            stu.setB046(value);
        } else if (key.equals("B032")) {
            stu.setB032(value);
        } else if (key.equals("B033")) {
            stu.setB033(value);
        } else if (key.equals("B069")) {
            stu.setB069(value);
        } else if (key.equals("B050")) {
            stu.setB050(value);
        } else if (key.equals("B132")) {
            stu.setB132(value);
        } else if (key.equals("B169")) {
            stu.setB169(value);
        } else if (key.equals("B170")) {
            stu.setB170(value);
        } else if (key.equals("B171")) {
            stu.setB171(value);
        } else if (key.equals("B172")) {
            stu.setB172(value);
        } else if (key.equals("B163")) {
            stu.setB163(value);
        } else if (key.equals("B164")) {
            stu.setB164(value);
        } else if (key.equals("B165")) {
            stu.setB165(value);
        } else if (key.equals("B166")) {
            stu.setB166(value);
        } else if (key.equals("B157")) {
            stu.setB157(value);
        } else if (key.equals("B158")) {
            stu.setB158(value);
        } else if (key.equals("B159")) {
            stu.setB159(value);
        } else if (key.equals("B160")) {
            stu.setB160(value);
        } else if (key.equals("B027")) {
            stu.setB027(value);
        } else if (key.equals("B028")) {
            stu.setB028(value);
        } else if (key.equals("B029")) {
            stu.setB029(value);
        } else if (key.equals("B138")) {
            stu.setB138(value);
        } else if (key.equals("B139")) {
            stu.setB139(value);
        } else if (key.equals("B140")) {
            stu.setB140(value);
        } else if (key.equals("B141")) {
            stu.setB141(value);
        } else if (key.equals("B147")) {
            stu.setB147(value);
        } else if (key.equals("B148")) {
            stu.setB148(value);
        } else if (key.equals("B073")) {
            stu.setB073(value);
        } else if (key.equals("B133")) {
            stu.setB133(value);
        } else if (key.equals("B155")) {
            stu.setB155(value);
        } else if (key.equals("B059")) {
            stu.setB059(value);
        } else if (key.equals("B060")) {
            stu.setB060(value);
        } else if (key.equals("B089")) {
            stu.setB089(value);
        } else if (key.equals("B090")) {
            stu.setB090(value);
        } else if (key.equals("B096")) {
            stu.setB096(value);
        } else if (key.equals("B097")) {
            stu.setB097(value);
        } else if (key.equals("B030")) {
            stu.setB030(value);
        } else if (key.equals("B074")) {
            stu.setB074(value);
        } else if (key.equals("B327")) {
            stu.setB327(value);
        }
    }
    public static void dealStudentPlantStatus(Student student) {
        student.setB001(getSpecialDate(student.getB001()));
        student.setB064(getSpecialDate(student.getB064()));
        student.setB065(getSpecialDate(student.getB065()));
        student.setB009(getSpecialDate(student.getB009()));
        student.setB010(getSpecialDate(student.getB010()));
        student.setB011(getSpecialDate(student.getB011()));
        student.setB012(getSpecialDate(student.getB012()));
        student.setB015(getSpecialDate(student.getB015()));
        student.setB016(getSpecialDate(student.getB016()));
        student.setB017(getSpecialDate(student.getB017()));
        student.setB018(getSpecialDate(student.getB018()));
        student.setB037(getSpecialDate(student.getB037()));
        student.setB038(getSpecialDate(student.getB038()));
        student.setB039(getSpecialDate(student.getB039()));
        student.setB093(getSpecialDate(student.getB093()));
        student.setB094(getSpecialDate(student.getB094()));
        student.setB040(getSpecialDate(student.getB040()));
        student.setB041(getSpecialDate(student.getB041()));
        student.setB045(getSpecialDate(student.getB045()));
        student.setB046(getSpecialDate(student.getB046()));
        student.setB032(getSpecialDate(student.getB032()));
        student.setB033(getSpecialDate(student.getB033()));
        student.setB069(getSpecialDate(student.getB069()));
        student.setB050(getSpecialDate(student.getB050()));
        student.setB132(getSpecialDate(student.getB132()));
        student.setB169(getSpecialDate(student.getB169()));
        student.setB170(getSpecialDate(student.getB170()));
        student.setB171(getSpecialDate(student.getB171()));
        student.setB172(getSpecialDate(student.getB172()));
        student.setB163(getSpecialDate(student.getB163()));
        student.setB164(getSpecialDate(student.getB164()));
        student.setB165(getSpecialDate(student.getB165()));
        student.setB166(getSpecialDate(student.getB166()));
        student.setB157(getSpecialDate(student.getB157()));
        student.setB158(getSpecialDate(student.getB158()));
        student.setB159(getSpecialDate(student.getB159()));
        student.setB160(getSpecialDate(student.getB160()));
        student.setB027(getSpecialDate(student.getB027()));
        student.setB028(getSpecialDate(student.getB028()));
        student.setB029(getSpecialDate(student.getB029()));
        student.setB030(getSpecialDate(student.getB030()));
        student.setB138(getSpecialDate(student.getB138()));
        student.setB139(getSpecialDate(student.getB139()));
        student.setB140(getSpecialDate(student.getB140()));
        student.setB141(getSpecialDate(student.getB141()));
        student.setB147(getSpecialDate(student.getB147()));
        student.setB148(getSpecialDate(student.getB148()));
        student.setB073(getSpecialDate(student.getB073()));
        student.setB074(getSpecialDate(student.getB074()));
        student.setB133(getSpecialDate(student.getB133()));
        student.setB327(getSpecialDate(student.getB327()));
        student.setB155(getSpecialDate(student.getB155()));
        student.setB059(getSpecialDate(student.getB059()));
        student.setB060(getSpecialDate(student.getB060()));
        student.setB089(getSpecialDate(student.getB089()));
        student.setB090(getSpecialDate(student.getB090()));
        student.setB096(getSpecialDate(student.getB096()));
        student.setB097(getSpecialDate(student.getB097()));
        student.setB063(getSpecialDate(student.getB063()));

    }
    public static String getSpecialDate(String key) {
        String getStuValue = StringUtils.EMPTY;
        if (StringUtils.isBlank(key)) {
            return StringUtils.EMPTY;
        }
        if (key.contains("-")) {
            return key;
        }
        else if ("已种替代疫苗".equals(key) || "1980".equals(key)) {
            getStuValue = "19800101";
        }
        else if ("不详".equals(key) || "1981".equals(key)) {
            getStuValue = "19810101";
        }
        else if ("父亲/母亲/监护人要求不种".equals(key) || "1982".equals(key)) {
            getStuValue = "19820101";
        }
        else if ("禁忌".equals(key) || "1983".equals(key)) {
            getStuValue = "19830101";
        }
        else if ("超期不种".equals(key) || "1984".equals(key)) {
            getStuValue = "19840101";
        }
        else if ("已种但时间未知".equals(key) || "1985".equals(key)) {
            getStuValue = "19850101";
        }
        else if ("已患".equals(key) || "1986".equals(key)) {
            getStuValue = "19860101";
        }
        else if ("阳性".equals(key) || "1979".equals(key)) {
            getStuValue = "19790101";
        } else {
            getStuValue = key;
        }

        String setValue = StringUtils.EMPTY;
        setValue = DateUtil.formatShort(getStuValue);
        return setValue;
    }
    public static void changeStudentTimeToStrToStr(Student student) {
        student.setB001(changeStudentTimeToStr(student.getB001()));
        student.setB064(changeStudentTimeToStr(student.getB064()));
        student.setB065(changeStudentTimeToStr(student.getB065()));
        student.setB009(changeStudentTimeToStr(student.getB009()));
        student.setB010(changeStudentTimeToStr(student.getB010()));
        student.setB011(changeStudentTimeToStr(student.getB011()));
        student.setB012(changeStudentTimeToStr(student.getB012()));
        student.setB015(changeStudentTimeToStr(student.getB015()));
        student.setB016(changeStudentTimeToStr(student.getB016()));
        student.setB017(changeStudentTimeToStr(student.getB017()));
        student.setB018(changeStudentTimeToStr(student.getB018()));
        student.setB037(changeStudentTimeToStr(student.getB037()));
        student.setB038(changeStudentTimeToStr(student.getB038()));
        student.setB039(changeStudentTimeToStr(student.getB039()));
        student.setB093(changeStudentTimeToStr(student.getB093()));
        student.setB094(changeStudentTimeToStr(student.getB094()));
        student.setB040(changeStudentTimeToStr(student.getB040()));
        student.setB041(changeStudentTimeToStr(student.getB041()));
        student.setB045(changeStudentTimeToStr(student.getB045()));
        student.setB046(changeStudentTimeToStr(student.getB046()));
        student.setB032(changeStudentTimeToStr(student.getB032()));
        student.setB033(changeStudentTimeToStr(student.getB033()));
        student.setB069(changeStudentTimeToStr(student.getB069()));
        student.setB050(changeStudentTimeToStr(student.getB050()));
        student.setB132(changeStudentTimeToStr(student.getB132()));
        student.setB169(changeStudentTimeToStr(student.getB169()));
        student.setB170(changeStudentTimeToStr(student.getB170()));
        student.setB171(changeStudentTimeToStr(student.getB171()));
        student.setB172(changeStudentTimeToStr(student.getB172()));
        student.setB163(changeStudentTimeToStr(student.getB163()));
        student.setB164(changeStudentTimeToStr(student.getB164()));
        student.setB165(changeStudentTimeToStr(student.getB165()));
        student.setB166(changeStudentTimeToStr(student.getB166()));
        student.setB157(changeStudentTimeToStr(student.getB157()));
        student.setB158(changeStudentTimeToStr(student.getB158()));
        student.setB159(changeStudentTimeToStr(student.getB159()));
        student.setB160(changeStudentTimeToStr(student.getB160()));
        student.setB027(changeStudentTimeToStr(student.getB027()));
        student.setB028(changeStudentTimeToStr(student.getB028()));
        student.setB029(changeStudentTimeToStr(student.getB029()));
        student.setB030(changeStudentTimeToStr(student.getB030()));
        student.setB138(changeStudentTimeToStr(student.getB138()));
        student.setB139(changeStudentTimeToStr(student.getB139()));
        student.setB140(changeStudentTimeToStr(student.getB140()));
        student.setB141(changeStudentTimeToStr(student.getB141()));
        student.setB147(changeStudentTimeToStr(student.getB147()));
        student.setB148(changeStudentTimeToStr(student.getB148()));
        student.setB073(changeStudentTimeToStr(student.getB073()));
        student.setB074(changeStudentTimeToStr(student.getB074()));
        student.setB133(changeStudentTimeToStr(student.getB133()));
        student.setB327(changeStudentTimeToStr(student.getB327()));
        student.setB155(changeStudentTimeToStr(student.getB155()));
        student.setB059(changeStudentTimeToStr(student.getB059()));
        student.setB060(changeStudentTimeToStr(student.getB060()));
        student.setB089(changeStudentTimeToStr(student.getB089()));
        student.setB090(changeStudentTimeToStr(student.getB090()));
        student.setB096(changeStudentTimeToStr(student.getB096()));
        student.setB097(changeStudentTimeToStr(student.getB097()));
        student.setB063(changeStudentTimeToStr(student.getB063()));
    }
    public static String changeStudentTimeToStr(String key) {
        if(DateUtil.isDate(key)){
            String showTime="";
            if(key!=null&&key.length()>4){
                showTime=key.substring(0,4);
            }

            if("1980".equals(showTime)){
                showTime  = "已种替代疫苗";
            }else if("1981".equals(showTime)){
                showTime= "不详";
            }else if("1982".equals(showTime)){
                showTime= "父亲/母亲/监护人要求不种";
            }else if("1983".equals(showTime)){
                showTime = "禁忌";
            }else if("1984".equals(showTime)){
                showTime = "超期不种";
            }else if("1985".equals(showTime)){
                showTime = "已种但时间未知";
            }else if("1986".equals(showTime)){
                showTime="已患";
            }else if("1979".equals(showTime)){
                showTime="阳性";
            }else {
                showTime=key;
            }
            return  showTime;
        }
        return key ;

    }


    public List<String> queryClassMess(Map<String, Object> info) {
        String  schId =StringUtils.nullToString(info.get("schId"));
        String  grade = StringUtils.nullToString(info.get("classLevel"));
        String  classYear = StringUtils.nullToString(info.get("classYear"));
        String  lunci = StringUtils.nullToString(info.get("lunci"));
        return  studentInputMapper.queryAllClass(schId,grade,classYear,lunci);
    }
    public List<ClassInfo> queryHighClassMess(Map<String, Object> info) {
        String  schId =StringUtils.nullToString(info.get("schId"));
        String  grade = StringUtils.nullToString(info.get("classLevel"));
        String  classYear = StringUtils.nullToString(info.get("classYear"));
        return  studentInputMapper.queryAllHighClass(schId,grade,classYear);
    }


    public List<ClassInfo> findHighClassYear(Map<String, Object> info) {
        String  schId =StringUtils.nullToString(info.get("schId"));
        String  grade = StringUtils.nullToString(info.get("classLevel"));
        String  classYear = StringUtils.nullToString(info.get("classYear"));
        return  studentInputMapper.findHighClassYear(schId,grade,classYear);
    }

    public List<ClassInfo> findHighClassYears(Map<String, Object> info) {
        String  schId =StringUtils.nullToString(info.get("schId"));
        String[] schIds  =null;
        if(StringUtils.isNotEmpty(schId)){
            schIds=schId.split(",");
        }
        String  grade = StringUtils.nullToString(info.get("classLevel"));
        String[] grades =null;
        if(StringUtils.isNotEmpty(grade)){
            grades=grade.split(",");
        }
        String  classYear = StringUtils.nullToString(info.get("classYear"));
        return  studentInputMapper.findHighClassYears(schIds,grades,classYear);
    }
}
