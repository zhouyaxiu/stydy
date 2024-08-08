package com.shensu.service.studentInputService;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.studentInput.ExcelImportResult;
import com.shensu.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.util.List;
import java.util.Map;

/**
 * @author zcy
 * @ClassName BatchExcelService
 * @Description
 * @createTime 2023/4/12
 */
public interface BatchExcelService {
    Map<String,Object> excelImport(HSSFSheet sheet, String schId, String classId,String className,String schStationCode);

    PageInfo<ExcelImportResult> queryList(String schId, String claId, int pageSize, int pageNum);

    void outPut(ExcelUtil excelUtil, Map<String,Object> info, List<ExcelImportResult> list);
}
