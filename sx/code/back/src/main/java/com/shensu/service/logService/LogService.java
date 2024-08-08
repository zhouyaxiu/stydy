package com.shensu.service.logService;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.systemmanage.SysLog;
import com.shensu.mybatis.mapper.systemmanage.LogMapper;
import com.shensu.redis.model.LoginUser;
import com.shensu.util.DateUtil;
import com.shensu.util.DateUtils;
import com.shensu.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogService {
    @Resource
     private LogMapper logMapper;
    //增加日志
       public void  insertSysLog(String loginname, String  modelName, String operateType, String accountName,String modelObject,String areaCode,String areaLevel) {

           logMapper.insertLog(operateType,modelName,loginname,accountName, DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),modelObject,areaCode,areaLevel);
    }


    public PageInfo<SysLog> findLogs(String opertimeStrat, String opertimeEnd, String operaType, String operaObject,String areaCode,String areaLevel, int pageIndex, int pageSize) {
          if(StringUtils.isNotEmpty(opertimeStrat)){
              opertimeStrat=opertimeStrat+" 00:00:00";
          }
        if(StringUtils.isNotEmpty(opertimeEnd)){
            opertimeEnd=opertimeEnd+" 23:59:59";
        }
        String[] operaTypes  =null;
        if(StringUtils.isNotEmpty(operaType)){
            operaTypes=operaType.split(",");
        }
        PageHelper.startPage(pageIndex,pageSize);
        //处理关联表
        StringBuilder  build  = new StringBuilder();
        //处理一下查询是通过什么进行查询
        String codeName ="";
        if("1".equals(areaLevel)){
            codeName="citycode";
        }else if("2".equals(areaLevel)){
            codeName="countycode";
        }else if("3".equals(areaLevel)){
            codeName="supcode";
        }else if("4".equals(areaLevel)){
            codeName="stationcode";
        }
        if(StringUtils.isNotEmpty(areaCode) && StringUtils.isNotEmpty(areaLevel)){
            build.append("(");
                switch (areaLevel) {
                    case "1":
                        build.append("select citycode  as areaCode from  v_sys_station vs  where  "+codeName+"='" + areaCode + "' group by citycode union all ");
                    case "2":
                        build.append("select countycode as areaCode from v_sys_station vs  where  "+codeName+"='"+areaCode+"' group by countycode union all ");
                    case "3":
                        build.append("select supcode as areaCode from v_sys_station vs  where  "+codeName+"='"+areaCode+"' group by supcode union all  ");
                    case "4":
                        build.append("select stationcode as areaCode from v_sys_station vs where   "+codeName+" ='"+areaCode+"'");
                }
            build.append(") temp");

        }

        List<SysLog> list  = logMapper.findLogs(opertimeStrat,opertimeEnd,operaTypes,operaObject,areaCode,areaLevel,build.toString());


        return  new PageInfo<>(list);
    }
}
