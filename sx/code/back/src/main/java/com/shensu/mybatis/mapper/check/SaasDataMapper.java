package com.shensu.mybatis.mapper.check;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.shensu.mybatis.entity.check.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName SaasDataMapper.java
 * @Description TODO
 * @createTime 2023年09月20日 09:38
 */
@Mapper
public interface SaasDataMapper {

    /**
     * 户籍类型映射
     * 本县: 1
     * 本市: 3
     * 本省: 4
     * 外省: 5
     * 国外: 6
     * 个案信息查询-个案编码
     */
    @DS("saas")
    @Select("select name, fchildno, imuno, mobphone, cid, date_format(birth,'%Y-%m-%d') birth, fathername, mothername, dwelladdr, stationcode, unitname, stationCode," +
            "case when sex='未知的性别' then '0' " +
            "when sex='男性' then '1' " +
            "when sex='女性' then '2' " +
            "when sex='未说明的性别' then '9' end as sex, " +
            "case when regtype='本县' then '1' " +
            "when regtype='本市' then '3' " +
            "when regtype='本省' then '4' " +
            "when regtype='外省' then '5' " +
            "when regtype='国外' then '6' end as regtype " +
            "from epi_childinfo where fchildno=#{fchildno}")
    List<ScanInfoBean> queryInfoByFchildno(@Param("fchildno") String fchildno);

    @DS("saas")
    @Select("select name, fchildno, imuno, mobphone, cid, date_format(birth,'%Y-%m-%d') birth, fathername, mothername, dwelladdr, stationcode, unitname, stationCode," +
            "case when sex='未知的性别' then '0' " +
            "when sex='男性' then '1' " +
            "when sex='女性' then '2' " +
            "when sex='未说明的性别' then '9' end as sex, " +
            "case when regtype='本县' then '1' " +
            "when regtype='本市' then '3' " +
            "when regtype='本省' then '4' " +
            "when regtype='外省' then '5' " +
            "when regtype='国外' then '6' end as regtype " +
            "from epi_childinfo where fchildno=#{fchildno} limit 1")
    ScanInfoBean queryInfoByChildno(@Param("fchildno") String fchildno);

    /**
     * 个案信息查询-条码
     */
    @DS("saas")
    @Select("select name, fchildno, imuno, mobphone, cid, date_format(birth,'%Y-%m-%d') birth, fathername, mothername, dwelladdr, stationcode, unitname, stationCode," +
            "case when sex='未知的性别' then '0' " +
            "when sex='男性' then '1' " +
            "when sex='女性' then '2' " +
            "when sex='未说明的性别' then '9' end as sex, " +
            "case when regtype='本县' then '1' " +
            "when regtype='本市' then '3' " +
            "when regtype='本省' then '4' " +
            "when regtype='外省' then '5' " +
            "when regtype='国外' then '6' end as regtype " +
            "from epi_childinfo where imuno=#{imuno}")
    List<ScanInfoBean> queryInfoByImuno(@Param("imuno") String imuno);

    @DS("saas")
    @Select("select name, fchildno, imuno, mobphone, cid, date_format(birth,'%Y-%m-%d') birth, fathername, mothername, dwelladdr, stationcode, unitname, stationCode," +
            "case when sex='未知的性别' then '0' " +
            "when sex='男性' then '1' " +
            "when sex='女性' then '2' " +
            "when sex='未说明的性别' then '9' end as sex, " +
            "case when regtype='本县' then '1' " +
            "when regtype='本市' then '3' " +
            "when regtype='本省' then '4' " +
            "when regtype='外省' then '5' " +
            "when regtype='国外' then '6' end as regtype " +
            "from epi_childinfo where cid=#{cid} limit 1")
    ScanInfoBean queryOneInfoByCid(@Param("cid") String cid);

    @DS("saas")
    @Select("select name, fchildno, imuno, mobphone, cid, date_format(birth,'%Y-%m-%d') birth, fathername, mothername, dwelladdr, stationcode, unitname, stationCode," +
            "case when sex='未知的性别' then '0' " +
            "when sex='男性' then '1' " +
            "when sex='女性' then '2' " +
            "when sex='未说明的性别' then '9' end as sex, " +
            "case when regtype='本县' then '1' " +
            "when regtype='本市' then '3' " +
            "when regtype='本省' then '4' " +
            "when regtype='外省' then '5' " +
            "when regtype='国外' then '6' end as regtype " +
            "from epi_childinfo where cid=#{cid}")
    List<ScanInfoBean> queryInfoByCid(@Param("cid") String cid);


    /**
     * 个案信息查询-条码
     */
    @DS("saas")
    @Select("select name, fchildno, imuno, mobphone, cid, date_format(birth,'%Y-%m-%d') birth, fathername , mothername, dwelladdr, stationcode, unitname,stationCode ," +
            "case when sex='未知的性别' then '0' " +
            "when sex='男性' then '1' " +
            "when sex='女性' then '2' " +
            "when sex='未说明的性别' then '9' end as sex, " +
            "case when regtype='本县' then '1' " +
            "when regtype='本市' then '3' " +
            "when regtype='本省' then '4' " +
            "when regtype='外省' then '5' " +
            "when regtype='国外' then '6' end as regtype " +
            "from epi_childinfo where name=#{childName} and sex=#{sex} " +
            "and birth >= date_format(concat(#{brith}, ' 00:00:00'), '%Y-%m-%d %T') and birth <= date_format(concat(#{brith}, ' 23:59:59'), '%Y-%m-%d %T')")
    List<ScanInfoBean> queryInfoByThreeCondition(@Param("childName") String childName , @Param("sex") String sex ,@Param("brith") String birth);

    /**
     * 接种记录查询
     */
    @DS("saas")
    @Select("select fbactid, fshotdated jzrq from epi_jzjl where fchildno=#{fchildno}")
    List<JzjlBean> queryJzjlByFchildno(@Param("fchildno") String fchildno);

    /**
     * 接种记录查询
     * @Select("select FBactID fbactid, date_format(FShotDate,'%Y-%m-%d') jzrq from ymshot where FChildNo=(select FChildNo from chchild where FChildNo=#{fchildno} or FIDCard=#{cid} limit 1)")
     */
    @DS("saas")
    @Select("select fbactid, fshotdated jzrq from epi_jzjl where fchildno=(select fchildno from epi_childinfo where cid=#{cid} limit 1)")
    List<JzjlBean> queryJzjlByCid(@Param("cid") String cid);

    /**
     * @param fchildno
     * @return
     */
    @DS("saas")
    @Select("select name, fchildno, imuno, mobphone, cid, sex, date_format(birth,'%Y-%m-%d') birth, regtype, fathername, mothername, dwelladdr, stationcode, unitname from epi_childinfo where fchildno=#{fchildno} limit 1")
    Student queryStuInfoByFchildno(String fchildno);

    /**
     * @param cid
     * @return
     */
    @DS("saas")
    @Select("select name, fchildno, imuno, mobphone, cid, sex, date_format(birth,'%Y-%m-%d') birth, regtype, fathername, mothername, dwelladdr, stationcode, unitname from epi_childinfo where cid=#{cid} limit 1" )
    Student queryStuInfoByCid(String cid);

    @DS("saas")
    @Select("select * from nur_vaccin_info")
    List<VaccinInfo> queryVaccinInfo();

    /**
     * 接种记录查询
     */
    @DS("saas")
    @Select("SELECT s.fbactid, fshotdated jzrq, fbacttype, concat(fbactshortname, fbactseq) fbactname " +
            "FROM epi_jzjl s " +
            "INNER JOIN vaccin_info v ON v.fbactid = s.fbactid " +
            "WHERE length(fshotdated) > 0 " +
            "AND fchildno=#{fchildno} " +
            "ORDER BY v.FBactType, fshotdated desc")
    List<JzjlBean> queryJzjlByFchildnoOrdered(@Param("fchildno") String fchildno);

    /**
     * 接种记录查询
     */
    @DS("saas")
    @Select("SELECT s.fbactid, fshotdated jzrq, fbacttype, concat(fbactshortname, fbactseq) fbactname " +
            "FROM epi_jzjl s " +
            "INNER JOIN vaccin_info v ON v.fbactid = s.fbactid " +
            "WHERE length(fshotdated) > 0 " +
            "AND fchildno=(SELECT fchildno FROM epi_childinfo WHERE cid=#{cid} limit 1) " +
            "ORDER BY v.fbacttype, fshotdated desc")
    List<JzjlBean> queryJzjlByCidOrdered(@Param("cid") String cid);
}
