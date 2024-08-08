package com.shensu.util;

import com.alibaba.fastjson.JSONObject;
import com.shensu.mybatis.entity.check.*;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.systemmanage.SysStation;
import org.springframework.beans.factory.annotation.Value;


/**
 *
 */
public class DTOConvertUtil {

    @Value("${downPath}")
    private  String path;

    public static void infoBeanToStudent(InfoBean infoBean, HighStudent student){
        if (infoBean.getFchildno()!=null){
            student.setStuNo(infoBean.getFchildno());
        }

        if (infoBean.getCid() != null) {
            student.setCid(infoBean.getCid());
        }

        if (infoBean.getStationcode() != null) {
            student.setStuBelongStationcode(infoBean.getStationcode() + "00");
        }

        if (infoBean.getImuno()!=null){
            student.setStuImuno(infoBean.getImuno());
        }

        if (infoBean.getRegtype()!=null){
            student.setStuType(infoBean.getRegtype());

        }
        if (infoBean.getPhone() != null) {
            student.setStuPhone(infoBean.getPhone());
        }
        //母亲电话
        if (infoBean.getMotherPhone()!=null){
            student.setStuMotherPhone(infoBean.getMotherPhone());
        }
        //父亲电话
        if (infoBean.getFatherPhone()!=null){
            student.setStuFatherPhone(infoBean.getFatherPhone());
        }
        if (infoBean.getSex()!=null){
            student.setStuSex(infoBean.getSex());

        }

        if (infoBean.getMotherName()!=null){
            student.setStuMothername(infoBean.getMotherName());
        }

        if (infoBean.getFatherName()!=null){
            student.setStuFathername(infoBean.getFatherName());

        }
        if (infoBean.getName()!=null){
            student.setStuName(infoBean.getName());

        }

        if (infoBean.getBirth()!=null){
            String birth = infoBean.getBirth();
            String dateFullStr = DateUtils.formatDateFullStr(birth, "yyyy-mm-dd");
            student.setStuBirth(dateFullStr);

        }

        if (infoBean.getRegtype()!=null){
            student.setStuType(infoBean.getRegtype());
        }
        if (infoBean.getFResideAddr()!=null){
            student.setFResideAddr(infoBean.getFResideAddr());
        }
    }

    public static void infoBeanToStudentBrief(ScanInfoBean infoBean, StudentBrief student){
        if (infoBean.getFchildno() != null){
            student.setStuNo(infoBean.getFchildno());
        }

        if (infoBean.getCid() != null) {
            student.setCid(infoBean.getCid());
        }

        if (StringUtils.isNotBlank(infoBean.getStationcode())) {
            //student.setStuBelongStationCode(infoBean.getStationcode() + "00");
            student.setStuStationcode(infoBean.getStationcode());//saas库提供12位所属预防接种单位编码
            student.setStuBelongStationCode(infoBean.getStationcode());
        }


        /*if (StringUtils.isNotBlank(infoBean.getUnitname())) {
            student.setStuStationName(infoBean.getUnitname());//saas库提供的所属预防接种单位名称
            student.setStuBelongStationName(infoBean.getUnitname());
        }*/

        if (infoBean.getImuno()!=null){
            student.setStuImuno(infoBean.getImuno());
        }

        if (infoBean.getRegtype()!=null){
            student.setStuType(infoBean.getRegtype());

        }
        //母亲电话
        if (infoBean.getMobphone()!=null){
            student.setStuMotherPhone(infoBean.getMobphone());
            student.setStuPhone(infoBean.getMobphone());
        }
        //父亲电话
        if (infoBean.getOtherphone()!=null){
            student.setStuFatherPhone(infoBean.getOtherphone());
            if(StringUtils.isEmpty(student.getStuPhone())){
                student.setStuPhone(infoBean.getOtherphone());
            }
        }
        if (infoBean.getSex()!=null){
            student.setStuSex(infoBean.getSex());
            student.setStuSex(Constants.STU_SEX.get(student.getStuSex()));
        }
        if (infoBean.getMothername()!=null){
            student.setStuMothername(infoBean.getMothername());
        }

        if (infoBean.getFathername()!=null){
            student.setStuFathername(infoBean.getFathername());

        }
        if (infoBean.getOtherLinkName()!=null){
            student.setStuOtherLinkMan(infoBean.getOtherLinkName());
        }
        if (infoBean.getName()!=null){
            student.setStuName(infoBean.getName());
        }
        if (StringUtils.isNotBlank(infoBean.getDwelladdr())) {
            student.setFResideAddr(infoBean.getDwelladdr());
        }
        if (infoBean.getBirth()!=null){
            String birth = infoBean.getBirth();
            String dateFullStr = DateUtils.formatDateFullStr(birth, "yyyy-mm-dd");
            student.setStuBirth(dateFullStr);
        }

        if (infoBean.getRegtype()!=null){
            student.setStuType(infoBean.getRegtype());
        }
        if (infoBean.getFResideAddr()!=null){
            student.setFResideAddr(infoBean.getFResideAddr());
        }
    }

    public static void infoBeanToStudentInfo(ScanInfoBean infoBean, StudentInfo student){
        if (infoBean.getFchildno() != null){
            student.setStuNo(infoBean.getFchildno());
            student.setFchildno(infoBean.getFchildno());
        }

        if (infoBean.getCid() != null) {
            student.setCid(infoBean.getCid());
        }

        if (StringUtils.isNotBlank(infoBean.getStationcode())) {
            //student.setStuBelongStationCode(infoBean.getStationcode() + "00");
            student.setStuStationcode(infoBean.getStationcode());//saas库提供12位所属预防接种单位编码
            student.setStuBelongStationCode(infoBean.getStationcode());
        }

        if (StringUtils.isNotBlank(infoBean.getUnitname())) {
            student.setStuStationName(infoBean.getUnitname());//saas库提供的所属预防接种单位名称
            student.setStuBelongStationName(infoBean.getUnitname());
        }

        if (infoBean.getImuno()!=null){
            student.setStuImuno(infoBean.getImuno());
        }

        if (infoBean.getRegtype()!=null){
            student.setStuType(infoBean.getRegtype());

        }
        //母亲电话
        if (infoBean.getMobphone()!=null){
            student.setStuMotherPhone(infoBean.getMobphone());
            student.setStuPhone(infoBean.getMobphone());
        }
        //父亲电话
        if (infoBean.getOtherphone()!=null){
            student.setStuFatherPhone(infoBean.getOtherphone());
            if(StringUtils.isEmpty(student.getStuPhone())){
                student.setStuPhone(infoBean.getOtherphone());
            }
        }
        if (infoBean.getSex()!=null){
            student.setStuSex(infoBean.getSex());
        }
        if (infoBean.getMothername()!=null){
            student.setStuMothername(infoBean.getMothername());
        }

        if (infoBean.getFathername()!=null){
            student.setStuFathername(infoBean.getFathername());

        }
        if (infoBean.getOtherLinkName()!=null){
            student.setStuOtherLinkMan(infoBean.getOtherLinkName());
        }
        if (infoBean.getName()!=null){
            student.setStuName(infoBean.getName());
        }
        if (StringUtils.isNotBlank(infoBean.getDwelladdr())) {
            student.setFResideAddr(infoBean.getDwelladdr());
        }
        if (infoBean.getBirth()!=null){
            String birth = infoBean.getBirth();
            String dateFullStr = DateUtils.formatDateFullStr(birth, "yyyy-mm-dd");
            student.setStuBirth(dateFullStr);
        }

        if (infoBean.getRegtype()!=null){
            student.setStuType(infoBean.getRegtype());
        }
        if (infoBean.getFResideAddr()!=null){
            student.setFResideAddr(infoBean.getFResideAddr());
        }
    }

}
