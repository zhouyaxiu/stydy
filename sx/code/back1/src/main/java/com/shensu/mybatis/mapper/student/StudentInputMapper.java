package com.shensu.mybatis.mapper.student;

import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.check.StudentInfo;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentInputMapper {

     List<StudentInfo> findStuInfoListByNo(String stuNo);

     StudentInfo findStuInfoByNo(String stuNo, String schId);

     int insertStuInfo(StudentInfo stundet);

     int updateStuInfo(StudentInfo student);



     List<Student> findStusByNo(String stuNo);

     Student findStuByNo(String stuNo,String schId);

     void updateCheckInfo(Student student );

     int insertStudent(Student stundet);

     List<String> queryAllClass(String schId,String grade,String classYear,String lunci);

     List<ClassInfo> queryAllHighClass(@Param("schId") String schId, @Param("claLevel") String claLevel, @Param("claYear") String claYear);

     List<ClassInfo> findHighClassYear(@Param("schId") String schId, @Param("claLevel") String claLevel, @Param("claYear") String claYear);
     List<ClassInfo> findHighClassYears(@Param("schIds") String[] schId, @Param("claLevels") String[] claLevel, @Param("claYear") String claYear);

}
