package com.shensu.mybatis.entity.studentInput;

import lombok.Data;

import java.util.Date;

/**
 * @author zcy
 * @ClassName ExcelImportResult
 * @Description  Excel批量导入，信息返回类
 * @createTime 2023/4/13
 */
@Data
public class ExcelImportResult {
    private String id;

    private String schId;

    private String claId;

    private String name;

    private String cid;

    private String className;
    //失败原因
    private String msg;

    private Date addTime;

    private String stationCode;
}
