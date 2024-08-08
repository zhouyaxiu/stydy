package com.shensu.mybatis.mapper.systemmanage;

import com.shensu.mybatis.entity.systemmanage.VSysStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysAreaMapper {

    VSysStation findStationBySchId(@Param("schId") String schId);

    VSysStation findStationByStationCode(@Param("stationCode") String stationCode);

    //根据接种编码查询接种单位信息
    /*
    @DS("saas")
    @Select(" select fMzShortName organName ,fAddress organAddress,fTelePhone linkPhone " +
            " from xtMzConfig where concat(ustationcode,'00')= #{areaCode}")
    SysStation findStationMess(String areaCode);
    */

}
