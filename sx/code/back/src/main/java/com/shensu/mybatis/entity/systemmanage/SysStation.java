package com.shensu.mybatis.entity.systemmanage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 对应接种单位数据库表字段
 * @author zhangtt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysStation implements Serializable {
    /**
     * 接种单位ID
     */
    private String id;
    /**
     * 上级行政区划ID
     */
    private String areaId;
    //
    private String supAreaName;
    /**
     * 接种单位编码
     */
    private String organCode;
    /**
     * 接种单位名称
     */
    private String organName;
    //定时同步使用
    private String organSortName;
    /**
     * 启用日期
     */
    private String beginDate;
    /**
     * 停用日期
     */
    private String endDate;
    /**
     * 上传密码
     */
    private String password;
    /**
     * 是否允许上传 1 -- 表示允许上传  0 -- 表示不允许  默认 1
     */
    private String mark = "1";
    //??????
    private String fsysini;
    private String fshoworder ;
    /**
     * 地址
     */
    private String organAddress;

    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 电话
     */
    private String linkPhone;
    /**
     * 门诊日
     */
    private String clinicDay;
    //???
    private String ifSupervise;
    /**
     * 客户端识别码
     */
    private String newBaseCode;

    private String organPhoneTicize;
    /**
     * 更新日期
     */
    private String updateDate;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 数据状态
     */
    private String tableStatus;
    private String account ;
    private String baseCode;
    private String examFlag;
    //停用标识
    private String nouse;
    //客户端门诊号
    private  String clientVer;

    // 管理类型
    private String manageType;
    // 业务类型
    private String businessType;
    private String isbs;

    // 是否为城市
    private String isCity;

    private String ywclass ;
    private String  glclass;
    /**
     * 接种单位面积
     */
    private String jzdwArea;
    private String jzdwywlx ;
    private String cnCode;
    private String cnName;
    private String fullCnName;
    /**
     * 服务类型
     */
    private String fwType;
    /**
     * 工作类型
     */
    private String workType;
    /**
     * 门诊类型
     */
    private String clinicType;

    private String lgn;
    private String lat;


    // 是否为狂犬门诊
    private String isRab;

    private String stationClass;
 //是否新冠
    private String ifXdjz;
    //老人接种
    private String ifLgjz;
   //国标码
    private String unitGbCOde;
    private String supCode;



    /**
     * 上级行政区划
     */

    private AdministrativeBean administrativeBean;


}
