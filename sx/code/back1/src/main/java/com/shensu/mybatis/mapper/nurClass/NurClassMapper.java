package com.shensu.mybatis.mapper.nurClass;

import com.shensu.mybatis.entity.nurClass.ClassInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zcy
 * @ClassName NurClassMapper
 * @Description
 * @createTime 2023/9/5
 */
@Mapper
public interface NurClassMapper {

    List<ClassInfo> getClassList(@Param("schId")String schId, @Param("claNameYear") String claNameYear,
                                 @Param("claLevel") String claLevel, @Param("claShortName") String claShortName);

    void insertClass(ClassInfo classInfo);

    ClassInfo getClassById(String classId);

    int getStudentNumByClassId(String clode);

    void updateClass(@Param("classId") String classId,@Param("claTotal") String claTotal,
                     @Param("claLinkman") String claLinkman,@Param("claPhone") String claPhone);

    int getStudentNumByClassIds(List<String> ids);
    void deleteClassByIds(List<String> ids);
    void deleteStudentByClaCode(List<String> ids);
}
