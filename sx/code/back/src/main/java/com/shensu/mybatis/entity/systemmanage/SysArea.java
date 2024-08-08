package com.shensu.mybatis.entity.systemmanage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author hogans
 * @Date 2021/6/2 9:39
 **/
@NoArgsConstructor
@Data
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class SysArea {
    private String id;
    private String areacode;
    private String areaname;
    private String newBaseCode;
    private String supid;
    private Date updatedate;
    private String updateuser;
    private String tablestatus;
    //同步数据用到的信息
    private  String lng;
    private String lat;
    private String cnCode;
    private String cnName;
    private String fullCnName;

}
