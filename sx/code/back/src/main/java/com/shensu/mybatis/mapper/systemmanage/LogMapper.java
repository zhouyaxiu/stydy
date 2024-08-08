package com.shensu.mybatis.mapper.systemmanage;

import com.shensu.mybatis.entity.systemmanage.SysLog;
import com.shensu.util.DateUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {

    public  void  insertLog(String operaType, String modelName, String userName,String accountName,String addTime,String modelObject,String areaCode,String areaLevel);

    List<SysLog> findLogs(String opertimeStrat, String opertimeEnd, String[] operaTypes, String operaObject,String areaCode ,String areaLevel,String temp );
}
