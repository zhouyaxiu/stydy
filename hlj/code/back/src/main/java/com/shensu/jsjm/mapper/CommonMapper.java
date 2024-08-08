package com.shensu.jsjm.mapper;

import com.shensu.jsjm.model.VaccineType;
import com.shensu.jsjm.model.WisdomBirthMonth;
import com.shensu.jsjm.model.WisdomBirthYear;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CommonMapper
 **/
public interface CommonMapper {

    List<VaccineType> getVaccineType();

    void saveNeonateYearCount(@Param("yearStr") String yearStr);

    void saveNeonateMonthCount(@Param("dateStartStr") String dateStartStr,@Param("dateEndStr") String dateEndStr);

    void deleteNeonateMonth();

    void deleteNeonateYear();

    List<WisdomBirthYear> getWisdomBirthYear(@Param("areaCode") String areaCode);

    List<WisdomBirthMonth> getWisdomBirthMonth(@Param("areaCode") String areaCode);
}
