package com.shensu.mybatis.mapper.taskMapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.notice.NoticeMess;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 备份学生表
 * @author  ztt
 * */
@Mapper
public interface TaskMapper {
    //备份表的清空
    public void truncatStuInfoBak(String tableName);
    //备份学生表
    public int insertBakStuInfo();
    //学生表错误信息保存
    public int insertStuLogData(String schId ,String stationCode ,String errMess,String isSuccess );
    //备份通知表
    public int insertBakNotice( );

    void  insertNoticeError(String schId ,String errMess,String isSuccess);

    int getSchCount();

    //地区表日志新增

    public int insertAreaLog(String issuccess,String errMess );





    @DS("healthPlatform")
    void truncatStuForHealthPlatform(String tableName);
    @DS("healthPlatform")
    int insertBakStuForhealthPlatform();
}
