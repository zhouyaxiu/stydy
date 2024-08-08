package com.shensu.mybatis.mapper.school;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

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

    List<SchoolManageBean> getSchoolList(@Param("columnSql") String columnSql, @Param("schType") String schType,
                                         @Param("schCode") String schCode, @Param("schName") String schName);

    Map getSchCounts(@Param("stationCode") String stationCode, @Param("schCode") String schCode);

    // 获取指定地区下的学校总数
    Integer getIdCounts(String stationCode);

    void insertSchool(SchoolManageBean bean);

    SchoolManageBean getSchoolById(String id);

    void updateSchoolById(@Param("schId") String schId, @Param("schName") String schName, @Param("schType") String schType,
                             @Param("schAddress") String schAddress, @Param("schPhone") String schPhone, @Param("schLinkMan") String schLinkMan);

    void delSchoolS(List<String> schIds);

    Integer getStuCounts(String schId);

    //通过地区编码查询所有学校信息
    @DS("ningbo")
    List<SchoolManageBean> findAllSchoolByAreaCode(String areaCode);

    List<SchoolManageBean> findAllSchool();

    void batchInsertSch(List<SchoolManageBean> beans);

    void delSchoolBySch(List<SchoolManageBean> beans);
    List<SchoolManageBean> getSchsForHealthPlatform(@Param("updateTime") String updateTime);
    @DS("healthPlatform")
    int getSchCount();
    @DS("healthPlatform")
    void delSchoolBySchForHealthPlatform(List<SchoolManageBean> beans);
    @DS("healthPlatform")
    void batchInsertSchForHealthPlatform(List<SchoolManageBean> beans);
    public  List<String> findSchoolByIds(String[] shcId );
    public  List<String> findClasByIds(String[] claIds);
}
