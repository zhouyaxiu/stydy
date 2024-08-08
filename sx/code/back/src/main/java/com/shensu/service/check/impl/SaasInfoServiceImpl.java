package com.shensu.service.check.impl;

import cn.hutool.core.bean.BeanUtil;
import com.shensu.mybatis.entity.check.BactInfo;
import com.shensu.mybatis.entity.check.InfoBean;
import com.shensu.mybatis.entity.check.JzjlBean;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.mapper.check.SaasDataMapper;
import com.shensu.mybatis.mapper.check.SaasInfoMapper;
import com.shensu.service.check.SaasInfoService;
import com.shensu.util.DTOConvertUtil;
import com.shensu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName QueryServiceImpl.java
 * @Description 信息和接种记录查询
 * @createTime 2022年12月14日 14:32
 */
@Service
public class SaasInfoServiceImpl implements SaasInfoService {

    @Autowired
    private SaasInfoMapper saasInfoMapper;

    @Autowired
    private SaasDataMapper saasDataMapper;


    @Override
    public List<InfoBean> getInfoByCid(String cid) {
        List<InfoBean> infoBeans = saasInfoMapper.queryInfoByCid(cid);
        return infoBeans;
    }

    @Override
    public List<JzjlBean> getJzjlByFchildno(String fchildno) {
        List<JzjlBean> jzjlBeans = saasInfoMapper.queryJzjlByFchildno(fchildno);
        return jzjlBeans;
    }

    @Override
    public List<JzjlBean> getJzjlByCid(String cid) {
        List<JzjlBean> jzjlBeans = saasInfoMapper.queryJzjlByCid(cid);
        return jzjlBeans;
    }

    /**
     * 将查验库中的个案信息转为学生表信息
     */
    public HighStudent sassBeanToStudent(InfoBean infoBean) {
        if (BeanUtil.isEmpty(infoBean)){
            return null;
        }

        HighStudent highStudent = new HighStudent();

        if (!BeanUtil.isEmpty(infoBean)){
            DTOConvertUtil.infoBeanToStudent(infoBean,highStudent);
        }

        if (StringUtils.isNotEmpty(highStudent.getStuMothername())) {
            highStudent.setStuParentname(highStudent.getStuMothername().trim());
        } else if (StringUtils.isNotEmpty(highStudent.getStuFathername())) {
            highStudent.setStuParentname(highStudent.getStuFathername().trim());
        } else {
            highStudent.setStuParentname("");
        }
        // 获取接种记录
        if (null != highStudent.getStuNo()) {
            BactInfo bactInfo = this.getBactInfoByFchildno(highStudent.getStuNo());
            // 麻疹
            highStudent.setMmr(bactInfo.getMeasles());
            highStudent.setMmrId(bactInfo.getMeaslesId());
            // 流感1
            highStudent.setFluOne(bactInfo.getFluFir());
            highStudent.setFluOneId(bactInfo.getFluFirId());
            // 水痘1
            highStudent.setVarOne(bactInfo.getVarFir());
            highStudent.setVarOneId(bactInfo.getVarFirId());
            // 水痘2
            highStudent.setVarTwo(bactInfo.getVarSec());
            highStudent.setVarTwoId(bactInfo.getVarSecId());
        }
        return highStudent;
    }

    public BactInfo getBactInfoByFchildno(String fchildno) {
        BactInfo bactInfo = new BactInfo();
        List<JzjlBean> jzjlList = saasDataMapper.queryJzjlByFchildnoOrdered(fchildno);
        int fluCount = 0;
        int varCount = 0;
        int mmrCount = 0;
        for (JzjlBean jzjlBean : jzjlList) {
            //流感
            if ("Flu".equals(jzjlBean.getFBactType()) && fluCount < 2) {
                if (fluCount == 0) {
                    bactInfo.setFluFir(jzjlBean.getJzrq());
                    bactInfo.setFluFirId(jzjlBean.getFbactid());
                    bactInfo.setFluFirName(jzjlBean.getFBactName());
                } else {
                    bactInfo.setFluSec(jzjlBean.getJzrq());
                    bactInfo.setFluSecId(jzjlBean.getFbactid());
                    bactInfo.setFluSecName(jzjlBean.getFBactName());
                }
                fluCount++;
            }
            //水痘
            if ("Var".equals(jzjlBean.getFBactType()) && varCount < 2) {
                if (varCount == 0) {
                    bactInfo.setVarFir(jzjlBean.getJzrq());
                    bactInfo.setVarFirId(jzjlBean.getFbactid());
                    bactInfo.setVarFirName(jzjlBean.getFBactName());
                } else {
                    bactInfo.setVarSec(jzjlBean.getJzrq());
                    bactInfo.setVarSecId(jzjlBean.getFbactid());
                    bactInfo.setVarSecName(jzjlBean.getFBactName());
                }
                varCount++;
            }
            //麻疹
            if ("MMR".equals(jzjlBean.getFBactType()) && mmrCount < 1) {
                bactInfo.setMeasles(jzjlBean.getJzrq());
                bactInfo.setMeaslesId(jzjlBean.getFbactid());
                bactInfo.setMeaslesName(jzjlBean.getFBactName());
                mmrCount++;
            }
        }

        return bactInfo;
    }

    private void getStudentVal(String key, String value, HighStudent stu) {
        if (key.equals("stu_no")) {
            stu.setStuNo(value);
        } else if (key.equals("stu_streetcode")) {
            stu.setStuStreetcode(value);
        } else if (key.equals("stu_imuno")) {
            stu.setStuImuno(value);
        } else if (key.equals("NAME")) {
            stu.setStuName(value);
        } else if (key.equals("BIRTH")) {
            stu.setStuBirth(value);
        } else if (key.equals("SEX")) {
            stu.setStuSex(value);
        } else if (key.equals("stu_type")) {
            stu.setStuType(value);
        } else if (key.equals("MOTHERNAME")) {
            stu.setStuMothername(value);
        } else if (key.equals("FATHERNAME")) {
            stu.setStuFathername(value);
        } else if (key.equals("stu_phone")) {
            stu.setStuPhone(value);
        }
    }
}
