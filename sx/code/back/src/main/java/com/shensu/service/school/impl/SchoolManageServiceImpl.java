package com.shensu.service.school.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.school.SchUserBean;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.mybatis.mapper.school.SchUserMapper;
import com.shensu.mybatis.mapper.school.SchoolMapper;
import com.shensu.service.school.SchoolManageService;
import com.shensu.util.Constants;
import com.shensu.util.ExcelUtil;
import com.shensu.util.MD5Util;
import com.shensu.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName SchoolManageServiceImpl
 * @Description
 * @createTime 2023/4/4
 */
@Service
public class SchoolManageServiceImpl implements SchoolManageService {

    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private SchUserMapper schUserMapper;

    @Override
    public PageInfo<SchoolManageBean> schoolList(Map<String, Object> info) {
        // 查询条件
        String areaCode = info.get("areaCode").toString();  // 地区编码
        String areaLevelStr = StringUtils.nullToString(info.get("areaLevel")); // 地区等级
        String schType = StringUtils.nullToString(info.get("schType")); // 学校类型
        String schName = StringUtils.nullToString(info.get("schName"));    // 学校名称
        String schCode = StringUtils.nullToString(info.get("schCode"));    // 学校编码
        String pageNum = info.get("pageNum").toString();    // 页码
        String pageSize = info.get("pageSize").toString();    //

        String columnSql = "";
        int areaLevel = Integer.parseInt(areaLevelStr);

        switch (areaLevel) {
            case 0: columnSql = "area.CITYCODE is not null "; break;
            case 1: columnSql = "area.CITYCODE='" + areaCode + "'"; break;
            case 2: columnSql = "area.countycode='" + areaCode + "'"; break;
            case 3: columnSql = "area.supcode='" + areaCode + "'"; break;
            case 4: columnSql = "area.stationcode='" + areaCode + "'"; break;
        }
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<SchoolManageBean> schoolList = schoolMapper.getSchoolList(columnSql, schType, schCode, schName);
        return new PageInfo<>(schoolList);
    }

    @Override
    public String addSchool(SchoolManageBean schoolBean, String supCode) {
        String areaCode = schoolBean.getSchStationCode();
        String schCode = schoolBean.getSchCode();
        // 校验地区编码是否存在
        Map<String, Object> schCounts = schoolMapper.getSchCounts(areaCode, schCode);
        String count = schCounts.get("schNum").toString();
        if (!count.equals("0")) {
            String addMsg = "新增失败，学校编码已存在!";
            String invalidStatus = schCounts.get("invalid_status").toString();
            if (invalidStatus.equals("1")) {
                addMsg = "该学校已被删除，如需恢复请联系管理人员!";
            }
            return addMsg;
        }
        String areaCodeSub = areaCode.substring(0, areaCode.length() - 2);
        // 10位地区编码+3位学校编码
        String fullCode = areaCode.substring(0,areaCode.length()-2) + "00" +schCode;
        schoolBean.setFullCode(fullCode);
        Integer idCounts = schoolMapper.getIdCounts(areaCode);
        // 12位父级地区编码+10位地区编码+序列号
        String schId = supCode + areaCodeSub + "000" + (idCounts+1);
        schoolBean.setSchId(schId);
        schoolBean.setInvalidCount("0");
        schoolBean.setSchIsAccess("1");
        schoolMapper.insertSchool(schoolBean);
        return "新增成功！";
    }

    @Override
    public SchoolManageBean detail(String schId) {
        return schoolMapper.getSchoolById(schId);
    }

    @Override
    public String modifySchool(Map<String, Object> info) {
        String msg = "修改成功！";
        // 修改信息
        String schId = info.get("schId").toString();
        String schName = info.get("schName").toString();
        String type = info.get("schType").toString();
        String schAddress = info.get("schAddress").toString();
        String schPhone = info.get("schPhone").toString();
        String schLinkMan = info.get("schLinkMan").toString();
        if (!"234".contains(type)) {
            return "无法修改该类型学校信息！";
        }
        // 是否修改了学校类型
        SchoolManageBean bean = schoolMapper.getSchoolById(schId);
        String schType = bean.getSchType();
        if (!schType.equals(type)) {
            // 学校类型发生改变时，判断学校下面是否存在学生
            Integer stuCounts = schoolMapper.getStuCounts(schId);
            if (stuCounts>0) {
                msg = "该学校下存在学生，无法修改学校类型！";
            } else {
                schoolMapper.updateSchoolById(schId, schName, type, schAddress, schPhone, schLinkMan);
            }
        } else {
            schoolMapper.updateSchoolById(schId, schName, type, schAddress, schPhone, schLinkMan);
        }
        return msg;
    }

    @Override
    public String delSchool(List<String> schIds) {
        schoolMapper.delSchoolS(schIds);
        return "删除成功！";
    }

    @Override
    public List<SchUserBean> schUserList(String schId) {
        return schUserMapper.getSchUserBySchId(schId);
    }

    @Override
    public String addSchUser(String schId, String fullCode) {
        Integer userNum = schUserMapper.getSchUserNumBySchId(schId);
        String loginName = fullCode + (userNum +1);
        String password = Constants.SCH_DEFAULT_PWD;
        String encode = MD5Util.MD5Encode(password);
        schUserMapper.insertOneSchUser(schId,loginName,encode);
        return "新增账号成功！";
    }

    @Override
    public String resetSchUser(String id) {
        String password = MD5Util.MD5Encode(Constants.SCH_DEFAULT_PWD);
        schUserMapper.resetSchUser(id,password);
        return "重置密码成功！";
    }

    @Override
    public void outPut(ExcelUtil excelUtil, Map<String, Object> info, List<SchoolManageBean> list) {
        //设置查询条件
        String areaName = StringUtils.nullToString(info.get("areaName")).trim();
        String areaCode = StringUtils.nullToString(info.get("areaCode")).trim();
        String schName= StringUtils.nullToString(info.get("schName")).trim();
        String schCode = StringUtils.nullToString(info.get("schCode")).trim();
        String schType2 = StringUtils.nullToString(info.get("schType")).trim();

        StringBuffer condition = new StringBuffer();
        condition.append("选择地区：" + areaName + " " + areaCode);
        if (StringUtils.isNotEmpty(schType2)) {
            condition.append(" 学校类型："+SchoolManageBean.getSchTypeCn(schType2));
        }
        if (StringUtils.isNotEmpty(schCode)) {
            condition.append(" 学校编码："+schCode);
        }
        if (StringUtils.isNotEmpty(schName)) {
            condition.append(" 学校名称："+schName);
        }
        excelUtil.fillCellData(1, (short) 0, condition);


        // 创建新行
        int firstRowNum = 4;//第一行记录在excel文件中的位置
        int firstPageCount = 1;//第一页的记录行个数
        int pageCount = 1;//每页显示的记录行个数
        int newRowCount = excelUtil.getNewRowCount(firstPageCount, list.size(), pageCount);//需要创建的行数
        int organLastRowNum = 4; //原始模板的最后一行
        int lastRowNum = organLastRowNum + newRowCount; //创建新行后的最后一行
        if (newRowCount > 0) {
            List<Integer> colnumlist = new ArrayList<>();
            for (int i = 0; i < 142; i++) {
                colnumlist.add(i);
            }

            //填表人、备注等占用的行下移到指定位置
            excelUtil.createNewRow(organLastRowNum + 1, colnumlist, lastRowNum + 1);
            excelUtil.fillCellData(lastRowNum + 1, (short) 0, excelUtil.getCellData(organLastRowNum + 1, (short) 0));

            //删除填表人、备注等占用的行
            excelUtil.removeOldRow(organLastRowNum + 1);

            excelUtil.createNewRowAll(organLastRowNum, colnumlist, newRowCount);//创建新行
        }

        //格式
        HSSFSheet sheet = excelUtil.getWb().getSheetAt(0);
        HSSFCellStyle style = sheet.getRow(firstRowNum).getCell(1).getCellStyle();

        int i = 0, rownum = 0;
        for (SchoolManageBean bean : list) {
            rownum = firstRowNum + i;
            excelUtil.fillCellData(rownum, (short) 0, bean.getCityName(),style);     //学校编码
            excelUtil.fillCellData(rownum, (short) 1, bean.getCountyName(),style);     //学校编码
            excelUtil.fillCellData(rownum, (short) 2, bean.getSchCode(),style);     //学校编码
            excelUtil.fillCellData(rownum, (short) 3, bean.getSchName(),style);     //学校名称
            String schType = bean.getSchType();
            excelUtil.fillCellData(rownum, (short) 4, SchoolManageBean.getSchTypeCn(schType),style);     //学校类型
            excelUtil.fillCellData(rownum, (short) 5, bean.getSupCode(),style);     //地址
            excelUtil.fillCellData(rownum, (short) 6, bean.getSupName(),style);     //电话
            excelUtil.fillCellData(rownum, (short) 7, bean.getSchStationCode(),style);     //接种单位编码
            excelUtil.fillCellData(rownum, (short) 8, bean.getSchStationName(),style);     //接种单位名称
            i++;
        }
    }
    //查询符合要求的全部学校
    @Override
    public List<SchoolManageBean> findAllSchool(Map<String, Object> info) {
        // 查询条件
        String areaCode =info.get("areaCode").toString();  // 地区编码
        String areaLevelStr = StringUtils.nullToString(info.get("areaLevel")); // 地区等级
        String schType = StringUtils.nullToString(info.get("schType")); // 学校类型
        String columnSql = "";
        int areaLevel = Integer.parseInt(areaLevelStr);
        switch (areaLevel) {
            case 0: columnSql = "area.CITYCODE is not null "; break;
            case 1: columnSql = "area.CITYCODE='" + areaCode + "'"; break;
            case 2: columnSql = "area.countycode='" + areaCode + "'"; break;
            case 3: columnSql = "area.supcode='" + areaCode + "'"; break;
            case 4: columnSql = "area.stationcode='" + areaCode + "'"; break;
        }
        List<SchoolManageBean> schoolList = schoolMapper.getSchoolList(columnSql, schType, "", "");
        return schoolList;
    }

    @Override
    public List<SchoolManageBean> findAllSchools() {
        return schoolMapper.findAllSchool();
    }

}
