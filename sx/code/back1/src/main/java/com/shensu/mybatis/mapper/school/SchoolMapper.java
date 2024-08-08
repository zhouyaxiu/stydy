package com.shensu.mybatis.mapper.school;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName SchoolMapper
 * @Description
 * @createTime 2023/4/4
 */
@Mapper
public interface SchoolMapper {
    public  List<String> findSchoolByIds(String shcId );
    public  List<String> findClasByIds(String[] claIds);
}
