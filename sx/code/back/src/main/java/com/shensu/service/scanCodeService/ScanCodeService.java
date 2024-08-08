package com.shensu.service.scanCodeService;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shensu.constant.Constant;
import com.shensu.mybatis.entity.check.*;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.mybatis.entity.systemmanage.SysStation;
import com.shensu.mybatis.entity.systemmanage.VSysStation;
import com.shensu.mybatis.mapper.check.SaasDataMapper;
import com.shensu.mybatis.mapper.highStu.HighStuMapper;
import com.shensu.mybatis.mapper.nurClass.NurClassMapper;
import com.shensu.mybatis.mapper.student.StudentInputMapper;
import com.shensu.mybatis.mapper.systemmanage.SysAreaMapper;
import com.shensu.service.check.SaasInfoService;
import com.shensu.service.nurClass.ClassService;
import com.shensu.service.taskService.TaskService;
import com.shensu.util.Constants;
import com.shensu.util.DTOConvertUtil;
import com.shensu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ScanCodeService {


    @Autowired
    private SaasDataMapper saasDataMapper;
    @Autowired
    private SaasInfoService saasInfoService;
    @Resource
    private StudentInputMapper studentInputMapper;
    @Resource
    private SysAreaMapper sysAreaMapper;
    @Resource
    private TaskService taskService;
    @Resource
    private NurClassMapper  nurClassMapper;
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
        String stationCode = info.getString("stationCode");
        String stationName = info.getString("stationName");
        //根据身份证取查验库查询到用户的信息
        List<ScanInfoBean> infoBeanList = null;
        if (type == 1) { //儿童编码
            infoBeanList = saasDataMapper.queryInfoByFchildno(childno);
            /*BactInfo bactInfo = saasInfoService.getBactInfoByFchildno(childno);
            System.out.println(bactInfo);*/
        }
        if (type == 2) { //儿童条码 (多条)
            infoBeanList = saasDataMapper.queryInfoByImuno(imuno);
        }
        if (type == 3) { //身份证件号码
            infoBeanList = saasDataMapper.queryInfoByCid(cid);
        }
        if (type == 4) { //儿童姓名、性别、出生日期  (多条)
            infoBeanList = saasDataMapper.queryInfoByThreeCondition(name, gender, birth);
        }

        //将查验结果进行处理
        if (CollectionUtils.isEmpty(infoBeanList)) {
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入" + Constant.SCAN_CODE_TYPE_MAP.get(type) + "！");
        } else if (infoBeanList.size() == 1) {
            ScanInfoBean infoBean = infoBeanList.get(0);
            //测试身份证: 330102010120150186
            //流感2：Flu
            //水痘2：Var
            //麻疹1：MMR
            //List<JzjlBean> jzjlList = saasInfoMapper.queryJzjlByFchildnoOrdered(infoBean.getFchildno());
            //infoBean = saasInfoService.initBactInfo(infoBean, jzjlList);
            StudentBrief studentBrief = sassBeanToStudentBrief(infoBean);
            //处理下学生归属地接种单位编码
            if(StringUtils.isNotEmpty(studentBrief.getStuBelongStationCode())){
                SysStation station = taskService.findStationMess(studentBrief.getStuBelongStationCode());
                if(station!=null) {
                    studentBrief.setStuBelongStationName(station.getOrganName());
                }
            }

            /*studentBrief.setStuBelongStationCode(stationCode);
            studentBrief.setStuBelongStationName(stationName);*/
            /*VSysStation station = sysAreaMapper.findStationBySchId(schid);
            if (station != null) {
                studentBrief.setStuBelongStationCode(station.getStationcode());
                studentBrief.setStuBelongStationName(station.getStationname());
            }*/
            object.put("code", Constants.ResultSuccessCode);
            object.put("data", studentBrief);
        } else {
            infoBeanList.forEach(infoBean->{
                infoBean.setSex(Constants.STU_SEX.get(infoBean.getSex()));
                infoBean.setRegtype(Constants.STU_TYPE.get(infoBean.getRegtype()));
            });
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
        List<ScanInfoBean> list = saasDataMapper.queryInfoByFchildno(childNo);
        if (CollectionUtils.isEmpty(list)) {
            object.put("code", Constants.ResultFailCode);
            object.put("data","信息系统不存在该受种者,请重新输入儿童编码！");
        } else if (list.size() == 1) {
            //将这个学生进行查验后结果返回
            ScanInfoBean infoBean = list.get(0);
            //查验前的学生
            StudentInfo stuInfo = sassBeanToStuInfo(infoBean);
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
                    List<JzjlBean> jzjlList = saasDataMapper.queryJzjlByFchildnoOrdered(stuInfo.getStuNo());
                    stuInfo = initBactInfo(stuInfo, jzjlList);
                    VSysStation station = sysAreaMapper.findStationByStationCode(stationCode);
                    if (station != null) {
                        //stuInfo.setStuStationcode(station.getStationcode());
                        stuInfo.setStuStreetcode(station.getSupcode());
                        stuInfo.setStuCountycode(station.getCountycode());
                        stuInfo.setStuCitycode(station.getCitycode());
                        //stuInfo.setStuBelongStationCode(station.getStationcode());
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
                            student.setStuBelongStationCode(student.getStuBelongStationCode());
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
        String stationCode =  info.getString("stationCode");
        String stateCode =  info.getString("stateCode");
        String oldSchId = info.getString("oldSchId");
        //获取sass端学生信息
        ScanInfoBean infoBean = saasDataMapper.queryInfoByChildno(childNo);
        //将sass学生信息转换成查验库学生信息对象
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
            //stuInfo.setStuBelongStationCode(station.getStationcode());
        }
        //初始化接种记录
        List<JzjlBean> jzjlList = saasDataMapper.queryJzjlByFchildnoOrdered(stuInfo.getStuNo());
        stuInfo = initBactInfo(stuInfo, jzjlList);

        StudentInfo oldStudent = studentInputMapper.findStuInfoByNo(stuInfo.getStuNo(), oldSchId);
        //查看在状态码是更新还是插入 204 说明这个学生在之前的判断中在其他学校中存在，所以直接插入当前的学校
        /*if ("204".equals(stateCode)) {
            studentInputMapper.insertStuInfo(stuInfo);
        } else*/
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

    /**
     * 将查验库中的个案信息转为学生表信息
     */
    public StudentBrief sassBeanToStudentBrief(ScanInfoBean infoBean) {
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
    public StudentInfo sassBeanToStuInfo(ScanInfoBean infoBean) {
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
