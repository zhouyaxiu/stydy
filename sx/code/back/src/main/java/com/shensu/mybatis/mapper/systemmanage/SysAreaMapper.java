package com.shensu.mybatis.mapper.systemmanage;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.shensu.mybatis.entity.systemmanage.AreaCodeAndName;
import com.shensu.mybatis.entity.systemmanage.SysArea;
import com.shensu.mybatis.entity.systemmanage.SysStation;
import com.shensu.mybatis.entity.systemmanage.VSysStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysAreaMapper {

    VSysStation findStationBySchId(@Param("schId") String schId);

    VSysStation findStationByStationCode(@Param("stationCode") String stationCode);

    //通过地区编码查找地区
    @DS("ningbo")
    List<SysArea> findAllArea();

    @DS("ningbo")
    int findAllAreaCount();

    @DS("ningbo")
    List<SysStation> findAllStation();

    @DS("ningbo")
    int findAllStationCount();

    //通过地区编码查找地区
    SysArea findByAreaCode(String areacode);
    //通过地区id查找 市 区 县 街道
    List<SysArea> listChildByAreaID(String areaId);
    //通过查询接种单位信息
    List<SysStation> listByAreaId(String areaID);
    //根据地区编码查找接种单位信息
    SysArea findStationByAreaCode(String areacode);
    //根据下级地区的id查找上一级地区的信息
    SysArea findSupAreaByStationId(String systionId);
    //根据接种编码查询接种单位信息
  /* @DS("saas")
    @Select(" select fMzShortName organName ,fAddress organAddress,fTelePhone linkPhone " +
            " from xtMzConfig where concat(ustationcode,'00')= #{areaCode}")
    SysStation findStationMess(String areaCode);*/

   List<AreaCodeAndName>   findCodeAndName(String areaCode,String areaLevel);

   void  bashInsertAllArea(List<SysArea> list );

    void bashInsertAllStation(List<SysStation> list );
}
