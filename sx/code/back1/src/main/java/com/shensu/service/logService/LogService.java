package com.shensu.service.logService;


import com.shensu.mybatis.mapper.systemmanage.LogMapper;
import com.shensu.util.DateUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class LogService {
    @Resource
     private LogMapper logMapper;
    //增加日志
    public void  insertSysLog(String loginname, String  modelName, String operateType, String accountName,String modelObject,String areaCode,String areaLevel) {

        logMapper.insertLog(operateType,modelName,loginname,accountName, DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),modelObject,areaCode,areaLevel);
    }



}
