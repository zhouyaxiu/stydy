package com.shensu.mybatis.mapper.student;

import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zcy
 * @ClassName ExcelImportResutlMapper
 * @Description
 * @createTime 2023/4/13
 */
@Mapper
public interface ExcelImportResutlMapper {
    void saveBatchList(List<ExcelImportResult> list);

    List<ExcelImportResult> getListBySchIdAndYearAndGradeAndRound(@Param("schId") String schId,@Param("claId") String claId);

    void saveCheckAgainList(List<ExcelImportResult> beans);
}
