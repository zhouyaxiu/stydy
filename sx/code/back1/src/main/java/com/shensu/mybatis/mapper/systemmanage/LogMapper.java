package com.shensu.mybatis.mapper.systemmanage;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {
    public  void  insertLog(String operaType, String modelName, String userName,String accountName,String addTime,String modelObject,String areaCode,String areaLevel);
}
