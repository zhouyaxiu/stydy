package com.shensu.service.studentInputService.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.check.InfoBean;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.check.StudentBase;
import com.shensu.mybatis.mapper.student.StudentMapper;
import com.shensu.service.check.CheckInfoService;
import com.shensu.service.check.SaasInfoService;
import com.shensu.service.studentInputService.ManualEntryService;
import com.shensu.util.DateUtils;
import com.shensu.util.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zcy
 * @ClassName ManualEntryServiceImpl
 * @Description
 * @createTime 2023/4/14
 */
@Service
public class ManualEntryServiceImpl implements ManualEntryService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private SaasInfoService saasInfoService;
    @Autowired
    private CheckInfoService checkInfoService;
    @Override
    public PageInfo<StudentBase> queryList(String schId, String nameYear, String grade, String round, String className, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<StudentBase> list = studentMapper.getManualStuSchIdAndYearAndGradeAndRoundAndClass(schId, nameYear, grade, round, className);
        return new PageInfo<>(list);
    }

    @Override
    @DSTransactional
    public Map<String,Object> pageEntry(Map<String, Object> info) {
        String message = "添加成功！";
        Map<String, Object> map = new HashMap<>();

        String schId = info.get("schId").toString();
        String schstationCode = info.get("schstationCode").toString();
        String year = info.get("year").toString();
        String grade = info.get("grade").toString();
        String round = info.get("round").toString();
        String className = info.get("className").toString();
        String stuName = info.get("stuName").toString();
        String stuBirthStr = info.get("stuBirth").toString();
        String stuBirth = DateUtils.formatDateFullStr(stuBirthStr, "yyyy-MM-dd");
        String stuSex = info.get("stuSex").toString();
        String stuPhone = info.get("stuPhone").toString();
        String cid = info.get("cid").toString();
        String stuParentname = info.get("stuParentname").toString();

        //根据身份证件号码匹配
        List<Student> list = new ArrayList<>();
        List<Student> updateList = new ArrayList<>();
        List<String> stuNolist = new ArrayList<>();
        List<InfoBean> infoBeans = null;
        if (infoBeans.size() > 1) {
            map.put("message","该身份证件号码匹配到多个学生，无法添加！");
            return map;
        }
        Student oldStu = studentMapper.getOneStuByCidAndSchId(cid, schId);
        // saas库查询不到，新增
        if (infoBeans.isEmpty() ) {
            // 未匹配到属于未建档学生
            Student bean = new Student();
            bean.setSchid(schId);
            bean.setStuStationcode(schstationCode);
            bean.setAcademicYear(year);
            bean.setGrade(grade);
            bean.setRounds(round);
            bean.setClazzName(className);
            bean.setStuName(stuName);
            bean.setStuBirth(stuBirth);
            bean.setStuSex(stuSex);
            bean.setStuPhone(stuPhone);
            bean.setCid(cid);
            bean.setStuParentname(stuParentname);
            bean.setStuIsjz("2");
            bean.setStuSourceid("2");
            bean.setStuIsjzOrigin("2");
            //没匹配到sass库的都是没有接种证的
            bean.setStuIsjzz("1");
            bean.setFirstStuSmrq(DateUtils.getDate());
            list.add(bean);
            // 查询选中学校下是否存在该学生，存在则更新（数据全部更新，直接先删除再新增）
            if (oldStu != null) {
                studentMapper.deleteStuByRowkey(oldStu.getRowkey());
            }
            studentMapper.batchInsertStudent(list);
        }  else if (infoBeans.size() == 1){
            // 匹配到则从saas库获取接种记录
            InfoBean infoBean = infoBeans.get(0);
            Student student = null;
            student.setStuSourceid("2");
            student.setSchid(schId);
            student.setStuStationcode(schstationCode);
            student.setAcademicYear(year);
            student.setGrade(grade);
            student.setRounds(round);
            student.setClazzName(className);
            student.setFirstStuSmrq(DateUtils.getDate());
            // 本校存在，就更新记录，不存在则新增
            if (oldStu != null) {
                // 赋值 且忽略null值
                String[] nullPropertyNames = Util.getNullPropertyNames(student);
                BeanUtils.copyProperties(student,oldStu,nullPropertyNames);
                Student checkStu = checkInfoService.excuteCheck(oldStu, false);
                updateList.add(checkStu);
                studentMapper.updateBatchByIds(updateList);
            } else {
                stuNolist.add(infoBean.getFchildno());
                list.add(student);
                studentMapper.batchInsertStudent(list);
                map.put("insertStuNo",stuNolist);
            }
        }
        map.put("message",message);
        return map;
    }

   /**
    * 保存前查看下sass库是否存在该接汇者档案
    * */
    @Override
    public boolean ifNotHasCard(String cid) {
        List<InfoBean> infoBeans = null;
        if(infoBeans.isEmpty()){
            return true ;
        }
        return false;
    }

}
