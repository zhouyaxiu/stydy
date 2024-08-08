package com.shensu.service.check;


import com.shensu.mybatis.entity.check.*;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName SaasService.java
 * @Description 信息和接种记录查询
 * @createTime 2023年4月10日 14:27
 */
public interface SaasInfoService {

    //String fchildno, String imuno, String cid, String birth, String name, String sex, String fathername, String mothername

    /**
     * 个案信息查询-个案编码
     */
    List<InfoBean> getInfoByFchildno(String fchildno);

    /**
     * 个案信息查询-个案编码
     */
    Student getStuInfoByFchildno(String fchildno);

    /**
     * 个案信息查询-身份证
     */
    Student getStuInfoByCid(String cid);

    /**
     * 个案信息查询-条码
     */
    List<InfoBean> getInfoByImuno(String imuno);

    /**
     * 个案信息查询-身份证件号码
     */
    List<InfoBean> getInfoByCid(String cid);

    List<InfoBeanSaas> getInfoSaasByCid(String cid);

    /**
     * 使用个案编码查询接种记录
     */
    List<JzjlBean> getJzjlByFchildno(String fchildno);

    /**
     * 使用身份证查询接种记录
     */
    List<JzjlBean> getJzjlByCid(String cid);

    /**
     * 将查验库中的个案信息转为学生表信息
     */
    HighStudent sassBeanToStudent(InfoBeanSaas infoBean);


    /**
     * 根据个案编码获得疫苗时间
     */
    BactInfo getBactInfoByFchildno(String fchildno);


}
