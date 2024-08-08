package com.shensu.service.school;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.school.SchUserBean;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.util.ExcelUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName SchoolManageService
 * @Description 学校管理
 * @createTime 2023/4/4
 */
public interface SchoolManageService {
    /**
     * 学校列表查询
     * */
    PageInfo<SchoolManageBean> schoolList(Map<String, Object> info);

    String addSchool(SchoolManageBean schoolBean,String supCode);

    SchoolManageBean detail(String schId);

    String modifySchool(Map<String, Object> info);

    String delSchool(List<String> schIds);

    List<SchUserBean> schUserList(String schId);

    String addSchUser(String schId, String fullCode);

    String resetSchUser(String id);

    void outPut(ExcelUtil excelUtil , Map<String,Object> info , List<SchoolManageBean> list);


    List<SchoolManageBean> findAllSchool(Map<String, Object> info);

    List<SchoolManageBean> findAllSchools();
}
