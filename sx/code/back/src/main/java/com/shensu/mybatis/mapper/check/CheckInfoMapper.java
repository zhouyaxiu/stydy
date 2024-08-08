package com.shensu.mybatis.mapper.check;

import com.shensu.mybatis.entity.check.AreaCdcUserVacData;
import com.shensu.mybatis.entity.check.ReplantVacStatBean;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.check.StudentBase;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName CheckInfoMapper.java
 * @Description TODO
 * @createTime 2023年04月07日 08:56
 */
@Mapper
public interface CheckInfoMapper {

    List<StudentBase> queryStudentList(Integer level, String areaCode, String schType, String schId, String year, String rounds, String grade, String clazz, String isjz, String status, String name, String idcard, String code);

    StudentBase queryOneById(String rowkey);

    List<Student> queryStuInfoByCode(List<String> childnoList);

    List<Student> queryStuInfoByRowkey(List<Long> rowkeyList);

    int batchDeleteByIds(List<String> rowkeyList);

    int batchUpdate(List<String> rowkeyList, String year, String grade, String clazz, String rounds);

    int updateCheckInfo(Student student);

    List<Student> queryReplantRosterList(Integer level, String areaCode, String schType, String schId, String year, String rounds, String grade,String className);

    ReplantVacStatBean queryReplantVaccinumStat(Integer level, String areaCode, String schType, String year, String rounds, String birthStart, String birthEnd, String replantStart, String replantEnd, String isFull,String schId,String grade,String className);

    List<AreaCdcUserVacData> queryVacCertCheckStat(String areaLevel, String areaCode, String schType, String year, String rounds);

    List<Student> queryReplateMess(Integer level, String areaCode, String schType, String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList);


}
