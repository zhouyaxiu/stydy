package com.shensu.service.studentInputService;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.check.StudentBase;

import java.util.Map;

/**
 * @author zcy
 * @ClassName ManualEntryService
 * @Description
 * @createTime 2023/4/14
 */
public interface ManualEntryService {
    /**
     *  查询已录入但未建档学生
     * */
    PageInfo<StudentBase> queryList(String schId, String nameYear, String grade, String round, String className, int pageNum, int pageSize);

    Map<String,Object> pageEntry(Map<String, Object> info);

    boolean  ifNotHasCard(String cid);
}
