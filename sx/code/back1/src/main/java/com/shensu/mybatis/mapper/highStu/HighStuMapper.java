package com.shensu.mybatis.mapper.highStu;

import com.shensu.mybatis.entity.highStudent.HighStuInfoAndCheck;
import com.shensu.mybatis.entity.highStudent.HighStudCheck;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.highStudent.NoticeContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;

@Mapper
public interface HighStuMapper {
 public List<HighStudent> findCheckStuds(String schId, String[] claIds, String startBirth, String endBirth);
 public List<HighStudent> findAllCheckStuds(String roundId);

 public int insertCheckResult(List<HighStudCheck> list );

 int deleteStuCheckResult(String roundId);
 List<HighStuInfoAndCheck> findStuCheckResult(String cid,String schId, String nowYear, String vaccName, String roundName,  List<String> claIds, String stuName);

 void updateCheckResult(HighStudCheck stuCheck, String stuNo, String roundId);

 int  deleteByRoundIdAndStuNos(String roundId, String[] stuNo);
 List<NoticeContent> queryBuZhongMess(String roundId, String[] stuNos);
 HighStudent getStuByCid(@Param("cid") String cid);

 HighStudent getLoseStuByCid(@Param("cid") String cid);

 void batchInsertStu(List<HighStudent> highStudents);
 int  findStuNumByClaId(String claId);
}
