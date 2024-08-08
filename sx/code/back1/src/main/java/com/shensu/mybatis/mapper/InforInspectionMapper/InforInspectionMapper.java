package com.shensu.mybatis.mapper.InforInspectionMapper;

import com.shensu.mybatis.entity.check.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ztt
 * @ClassName InforInspectionMapper
 * @Description 查验情况汇总:查验后结果以及补种信息的查询接口
 * @createTime 2023/4/4
 */
@Mapper
public interface InforInspectionMapper {

  public List<Student> findStudentReplate(String schId,String classYear,String lunci,String className,String grade);
}
