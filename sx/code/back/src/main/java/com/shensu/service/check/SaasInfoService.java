package com.shensu.service.check;


import com.shensu.mybatis.entity.check.BactInfo;
import com.shensu.mybatis.entity.check.InfoBean;
import com.shensu.mybatis.entity.check.JzjlBean;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.highStudent.HighStudent;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName SaasService.java
 * @Description 信息和接种记录查询
 * @createTime 2023年4月10日 14:27
 */
public interface SaasInfoService {
    /**
     * 个案信息查询-身份证件号码
     */
    List<InfoBean> getInfoByCid(String cid);

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
    HighStudent sassBeanToStudent(InfoBean infoBean);

    BactInfo getBactInfoByFchildno(String fchildno);
}
