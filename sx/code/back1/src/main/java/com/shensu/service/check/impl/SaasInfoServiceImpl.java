package com.shensu.service.check.impl;

import cn.hutool.core.bean.BeanUtil;
import com.shensu.mybatis.entity.check.*;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.mapper.check.SaasInfoMapper;
import com.shensu.service.check.SaasInfoService;
import com.shensu.util.DTOConvertUtil;
import com.shensu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<InfoBean> getInfoByFchildno(String fchildno) {
        List<InfoBean> infoBeans = saasInfoMapper.queryInfoByFchildno(fchildno);
        return infoBeans;
    }

    @Override
    public Student getStuInfoByFchildno(String fchildno) {
        return saasInfoMapper.queryStuInfoByFchildno(fchildno);
    }

    @Override
    public Student getStuInfoByCid(String cid) {
        return saasInfoMapper.queryStuInfoByCid(cid);
    }

    @Override
    public List<InfoBean> getInfoByImuno(String imuno) {
        List<InfoBean> infoBeans = saasInfoMapper.queryInfoByImuno(imuno);
        return infoBeans;
    }

    @Override
    public List<InfoBean> getInfoByCid(String cid) {
        List<InfoBean> infoBeans = saasInfoMapper.queryInfoByCid(cid);
        return infoBeans;
    }

    @Override
    public List<InfoBeanSaas> getInfoSaasByCid(String cid) {
        List<InfoBeanSaas> infoBeans = saasInfoMapper.queryInfoSaasByCid(cid);
        return infoBeans;
    }

    @Override
    public List<JzjlBean> getJzjlByFchildno(String fchildno) {
        List<JzjlBean> jzjlBeans = saasInfoMapper.queryJzjlByFchildno(fchildno);
        return jzjlBeans;
    }

    //@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public List<JzjlBean> getJzjlByCid(String cid) {
        List<JzjlBean> jzjlBeans = saasInfoMapper.queryJzjlByCid(cid);
        return jzjlBeans;
    }

    /**
     * 将查验库中的个案信息转为学生表信息
     */
    public HighStudent sassBeanToStudent(InfoBeanSaas infoBean) {
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

    @Override
    public BactInfo getBactInfoByFchildno(String fchildno) {
        BactInfo bactInfo = new BactInfo();
        List<JzjlBean> jzjlList = saasInfoMapper.queryJzjlByFchildnoOrdered(fchildno);
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

    private void getStudentVal(String key, String value, Student stu) {
        if (key.equals("stu_no")) {
            stu.setStuNo(value);
        } else if (key.equals("UNITNAME")) {
            stu.setStuStationname(value);
        } else if (key.equals("FStatus")) {
            stu.setStuStatus(value);
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
        } else if (key.equals("OTHERPHONE")) {
            stu.setStuOtherphone(value);
        } else if (key.equals("FPPDW")) {
            stu.setFPPDW(value);
        } else if (key.equals("FPPDH")) {
            stu.setFPPDH(value);
        } else if (key.equals("B001")) {
            stu.setB001(value);
        } else if (key.equals("B063")) {
            stu.setB063(value);
        } else if (key.equals("B064")) {
            stu.setB064(value);
        } else if (key.equals("B065")) {
            stu.setB065(value);
        } else if (key.equals("B009")) {
            stu.setB009(value);
        } else if (key.equals("B010")) {
            stu.setB010(value);
        } else if (key.equals("B011")) {
            stu.setB011(value);
        } else if (key.equals("B012")) {
            stu.setB012(value);
        } else if (key.equals("B015")) {
            stu.setB015(value);
        } else if (key.equals("B016")) {
            stu.setB016(value);
        } else if (key.equals("B017")) {
            stu.setB017(value);
        } else if (key.equals("B018")) {
            stu.setB018(value);
        } else if (key.equals("B037")) {
            stu.setB037(value);
        } else if (key.equals("B038")) {
            stu.setB038(value);
        } else if (key.equals("B039")) {
            stu.setB039(value);
        } else if (key.equals("B093")) {
            stu.setB093(value);
        } else if (key.equals("B094")) {
            stu.setB094(value);
        } else if (key.equals("B040")) {
            stu.setB040(value);
        } else if (key.equals("B041")) {
            stu.setB041(value);
        } else if (key.equals("B045")) {
            stu.setB045(value);
        } else if (key.equals("B046")) {
            stu.setB046(value);
        } else if (key.equals("B032")) {
            stu.setB032(value);
        } else if (key.equals("B033")) {
            stu.setB033(value);
        } else if (key.equals("B069")) {
            stu.setB069(value);
        } else if (key.equals("B050")) {
            stu.setB050(value);
        } else if (key.equals("B132")) {
            stu.setB132(value);
        } else if (key.equals("B169")) {
            stu.setB169(value);
        } else if (key.equals("B170")) {
            stu.setB170(value);
        } else if (key.equals("B171")) {
            stu.setB171(value);
        } else if (key.equals("B172")) {
            stu.setB172(value);
        } else if (key.equals("B163")) {
            stu.setB163(value);
        } else if (key.equals("B164")) {
            stu.setB164(value);
        } else if (key.equals("B165")) {
            stu.setB165(value);
        } else if (key.equals("B166")) {
            stu.setB166(value);
        } else if (key.equals("B157")) {
            stu.setB157(value);
        } else if (key.equals("B158")) {
            stu.setB158(value);
        } else if (key.equals("B159")) {
            stu.setB159(value);
        } else if (key.equals("B160")) {
            stu.setB160(value);
        } else if (key.equals("B027")) {
            stu.setB027(value);
        } else if (key.equals("B028")) {
            stu.setB028(value);
        } else if (key.equals("B029")) {
            stu.setB029(value);
        } else if (key.equals("B138")) {
            stu.setB138(value);
        } else if (key.equals("B139")) {
            stu.setB139(value);
        } else if (key.equals("B140")) {
            stu.setB140(value);
        } else if (key.equals("B141")) {
            stu.setB141(value);
        } else if (key.equals("B147")) {
            stu.setB147(value);
        } else if (key.equals("B148")) {
            stu.setB148(value);
        } else if (key.equals("B073")) {
            stu.setB073(value);
        } else if (key.equals("B133")) {
            stu.setB133(value);
        } else if (key.equals("B155")) {
            stu.setB155(value);
        } else if (key.equals("B059")) {
            stu.setB059(value);
        } else if (key.equals("B060")) {
            stu.setB060(value);
        } else if (key.equals("B089")) {
            stu.setB089(value);
        } else if (key.equals("B090")) {
            stu.setB090(value);
        } else if (key.equals("B096")) {
            stu.setB096(value);
        } else if (key.equals("B097")) {
            stu.setB097(value);
        } else if (key.equals("B030")) {
            stu.setB030(value);
        } else if (key.equals("B074")) {
            stu.setB074(value);
        } else if (key.equals("B327")) {
            stu.setB327(value);
        } /*else if (key.equals("B427")) {
            stu.setB427(value);
        } else if (key.equals("B428")) {
            stu.setB428(value);
        } else if (key.equals("B429")) {
            stu.setB429(value);
        }*/
    }
}
