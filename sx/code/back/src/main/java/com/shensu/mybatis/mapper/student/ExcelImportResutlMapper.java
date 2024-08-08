package com.shensu.mybatis.mapper.student;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
///import sun.reflect.generics.tree.VoidDescriptor;

import java.util.List;

/**
 * @author zcy
 * @ClassName ExcelImportResutlMapper
 * @Description
 * @createTime 2023/4/13
 */
@Mapper
public interface ExcelImportResutlMapper{
    void saveBatchList(List<ExcelImportResult> list);

    List<ExcelImportResult> getListBySchIdAndYearAndGradeAndRound(@Param("schId") String schId,@Param("claId") String claId);

    void saveCheckAgainList(List<ExcelImportResult> beans);

    List<ExcelImportResult> getAllStuAgain();

    void  delAgainByCids(List<String> cids);
}
