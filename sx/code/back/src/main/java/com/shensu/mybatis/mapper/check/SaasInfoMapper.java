package com.shensu.mybatis.mapper.check;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.shensu.mybatis.entity.check.InfoBean;
import com.shensu.mybatis.entity.check.JzjlBean;
import com.shensu.mybatis.entity.check.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName EpiInfoMapper.java
 * @Description TODO
 * @createTime 2022年12月14日 14:38
 */
@Mapper
public interface SaasInfoMapper {

    /**
     * 户籍类型映射
     * '1':县区内流动'
     * '3':市地内流动'
     * '4':本省内流动'
     * '5':省际内流动'
     * '6':国际流动'
     * 个案信息查询-个案编码
     */
    @DS("saas")
    @Select("select FChildNo fchildno, FIDCard cid, FProofNo imuno, FChildName name, FBirthday birth, FSex sex, " +
            "FMobile mobphone,FTelePhone otherphone, FFatherName fathername, FMotherName mothername, FMoveKind regtype, UStationCode stationcode,FResideAddr fResideAddr,fOtherGuard otherLinkName ,fOtherGuardMobile otherLinkPhone  " +
//            "case when FMoveKind='1' then '县区内流动' " +
//            "when FMoveKind='3' then '市地内流动' " +
//            "when FMoveKind='4' then '本省内流动' " +
//            "when FMoveKind='5' then '省际内流动' " +
//            "when FMoveKind='6' then '国际流动' end as regtype " +
            "from chchild where FChildNo=#{fchildno}")
    List<InfoBean> queryInfoByFchildno(@Param("fchildno") String fchildno);

    /**
     * 个案信息查询-条码
     */
    @DS("saas")
    @Select("select FChildNo fchildno, FIDCard cid, FProofNo imuno, FChildName name, FBirthday birth, FSex sex, " +
            "FMobile mobphone,FTelePhone otherphone, FFatherName fathername, FMotherName mothername, FMoveKind regtype, UStationCode stationcode,FResideAddr fResideAddr ,fOtherGuard otherLinkName ,fOtherGuardMobile otherLinkPhone " +
            "from chchild where FProofNo=#{imuno}")
    List<InfoBean> queryInfoByImuno(@Param("imuno") String imuno);


    @DS("saas")
    List<InfoBean> queryInfoByCid(@Param("cid") String cid);


    /**
     * 个案信息查询-条码
     */
    @DS("saas")
    @Select("select FChildNo fchildno, FIDCard cid, FProofNo imuno, FChildName name, FBirthday birth, FSex sex, " +
            "FMobile mobphone, FTelePhone otherphone,FFatherName fathername, FMotherName mothername, FMoveKind regtype, UStationCode stationcode,FResideAddr fResideAddr,fOtherGuard otherLinkName ,fOtherGuardMobile otherLinkPhone " +
            "from chchild where FChildName=#{childName} and FSex =#{sex} and FBirthdayD =#{brith}")
    List<InfoBean> queryInfoByThreeCondition(@Param("childName") String childName , @Param("sex") String sex ,@Param("brith") String birth   );

    /**
     * 接种记录查询
     */
    @DS("saas")
    @Select("select FBactID fbactid, date_format(FShotDate,'%Y-%m-%d') jzrq ,FShotKind fShotKind  from ymshot where FChildNo=#{fchildno}")
    List<JzjlBean> queryJzjlByFchildno(@Param("fchildno") String fchildno);

    /**
     * 接种记录查询
     * @Select("select FBactID fbactid, date_format(FShotDate,'%Y-%m-%d') jzrq from ymshot where FChildNo=(select FChildNo from chchild where FChildNo=#{fchildno} or FIDCard=#{cid} limit 1)")
     */
    @DS("saas")
    @Select("select FBactID fbactid, date_format(FShotDate,'%Y-%m-%d') jzrq ,FShotKind fShotKind from ymshot where  FChildNo=(select FChildNo from chchild where ( FIDCard=#{cid}  and  FIDCardType='01') limit 1)")
    List<JzjlBean> queryJzjlByCid(@Param("cid") String cid);

    @DS("saas")
    @Select("SELECT s.FBactID fbactid, date_format(FShotDate,'%Y-%m-%d') jzrq, FShotKind fShotKind, FBactType fBactType, CONCAT(FBactShortName, FBactSeq) fBactName " +
            "FROM ymshot s " +
            "INNER JOIN vaccin_info v ON v.FBactID = s.FBactID " +
            "WHERE length(FShotDate) > 0 AND length(FShotKind) > 0 " +
            "AND FChildNo=#{fchildno} " +
            "ORDER BY v.FBactType, FShotDate ASC")
    List<JzjlBean> queryJzjlByFchildnoOrdered(@Param("fchildno") String fchildno);
}
