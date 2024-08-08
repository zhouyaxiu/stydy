package com.shensu.service.highStuService;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.highStudent.HighStuInfoAndCheck;
import com.shensu.mybatis.entity.highStudent.HighStudCheck;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.highStudent.NoticeContent;
import com.shensu.util.ExcelUtil;

import java.util.List;

public interface HighStuService {
    public List<HighStudent>  findCheckStuds(String schId,String claId,String startBirth,String endBirth) throws  Exception;
    public  int  insertStuCheckResult(List<HighStudCheck> list );
    int  deleteCheckResultById(String roundId);
    PageInfo<HighStuInfoAndCheck> findCheckResult(String cid, String schId, String nowYear, String vaccName, String roundName, String claYear, String claLevel, String claId, String stuName, int pageNum, int pageSize);
    void outPutMaSaiFengCheckResult(ExcelUtil excelUtil, StringBuilder builder, List<HighStuInfoAndCheck> list);
    void outPutCheckResult(ExcelUtil excelUtil, StringBuilder builder, List<HighStuInfoAndCheck> list);
    void outPutLiuGanCheckResult(ExcelUtil excelUtil, StringBuilder builder, List<HighStuInfoAndCheck> list);
    int  deleteCheckResult(String roundId, String stuNos);
    List<NoticeContent>  queryBuZhongMess(String roundId, String stuNos);
}
