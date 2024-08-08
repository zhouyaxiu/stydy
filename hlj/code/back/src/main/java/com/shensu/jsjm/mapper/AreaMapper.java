package com.shensu.jsjm.mapper;

import com.shensu.jsjm.model.AreaBean;
import com.shensu.jsjm.model.SimpleAreaBean;
import com.shensu.jsjm.vo.TotalOutputVo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AreaMapper {

    List<AreaBean> getChildrenStations(String code);

    List<AreaBean> getChildrenCys(String code);

    List<AreaBean> getChildrenVillages(String id);

    List<SimpleAreaBean> getSimpleChildrenAreas(String code);
    List<AreaBean> getChildrenAreas(String code);

    TotalOutputVo getTotal(@Param("template1") String template1, @Param("template2")String template2);

    Integer getYDJZNum(@Param("code")String code);
}
