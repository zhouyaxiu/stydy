package com.shensu.mybatis.mapper.student;

import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.check.StudentBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zcy
 * @ClassName StudentMapper
 * @Description
 * @createTime 2023/4/13
 */
@Mapper
public interface StudentMapper {
    void batchInsertStudent(List<Student> students);


    /**
     * 查询未建档页面学生信息
     * @param schId 学校主键
     * @param year 学年
     * @param grade 年级
     * @param round 轮次
     * @param className 班级名称
     * */
    List<StudentBase> getManualStuSchIdAndYearAndGradeAndRoundAndClass(@Param("schId") String schId, @Param("year") String year, @Param("grade") String grade,
                                                                    @Param("round") String round, @Param("className") String className);

    StudentBase getStuBySchIdAndStuNoOrCid(@Param("schId") String schId,@Param("stuNo") String stuNo,@Param("cid") String cid);

    void  updateBatchByIds(List<Student> list);

    Student getOneStuByStuNoAndSchId(@Param("stuNo") String stuNo,@Param("schId") String schId);

    Student getOneStuByCidAndSchId(@Param("cid") String cid,@Param("schId") String schId);

    void deleteStuByRowkey(Long rowkey);
}
