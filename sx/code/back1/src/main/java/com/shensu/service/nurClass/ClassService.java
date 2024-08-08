package com.shensu.service.nurClass;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.nurClass.ClassInfo;
import com.shensu.util.ExcelUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName ClassService
 * @Description
 * @createTime 2023/9/5
 */
public interface ClassService {
    PageInfo<ClassInfo> classList(Map<String, Object> info);

    String insertClass(ClassInfo classInfo);

    ClassInfo getClass(String classId);

    String updateClass(String classId,String claTotal,String claLinkman,String claPhone);

    String deleteClass(String classIds);

    void outPut(ExcelUtil excelUtil , Map<String,Object> info , List<ClassInfo> list);
}
