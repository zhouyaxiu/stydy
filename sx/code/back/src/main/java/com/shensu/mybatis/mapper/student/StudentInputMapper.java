package com.shensu.mybatis.mapper.student;

import com.baomidou.dynamic.datasource.annotation.DS;
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
     List<StudentInfo> findStuInfoByNos(String schId);

     int insertStuInfo(StudentInfo stundet);

     int updateStuInfo(StudentInfo student);


     public List<Student>  findStusByNo(String stuNo);
     public Student   findStuByNo(String stuNo,String schId);
     public   void updateCheckInfo(Student student );
     public int insertStudent(Student stundet);
     List<String> queryAllClass(String schId,String grade,String classYear,String lunci);

     List<ClassInfo> queryAllHighClass(@Param("schId") String schId, @Param("claLevel") String claLevel, @Param("claYear") String claYear);

     List<ClassInfo> findHighClassYear(@Param("schId") String schId, @Param("claLevel") String claLevel, @Param("claYear") String claYear);

     List<ClassInfo> findHighClassYears(@Param("schIds") String[] schIds, @Param("claLevels") String[] claLevels, @Param("claYear") String claYear);


     @DS("ningbo")
     List<Student>  findStudentBySchId(String schId);
     @DS("ningbo")
     long  findStudentCountBySchId(String schId);

     void bachInsertStudents(List<Student> list);

     List<Student>  queryStudentBySchIdAnySource(String schId);
     @DS("healthPlatform")
     void bachInsertHealthPlatformStus(List<Student> list);
}
