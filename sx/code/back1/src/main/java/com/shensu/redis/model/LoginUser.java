package com.shensu.redis.model;


//import com.shensu.mybatis.entity.systemmanage.SysModule;

import com.shensu.mybatis.entity.systemmanage.SysModule;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权：(C) 版权所有 2019-2022  苏州沈苏自动化有限公司
 * <简述>redis保存的 用户基本信息
 * <详细描述>
 *
 * @author gelongyu
 * @version $Id$
 * @see
 * @since
 */
@Data
public class LoginUser implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(目的和意义)
     */
    private static final long serialVersionUID = -7465252041132313235L;

    /**
     * @Fields token : token  用于验证
     */
    private String token;

    /**
     * @Fields loginName : 登录姓名
     */
    private String loginName;

    private String userName;

    /**
     * @Fields userId : 登录人的ID
     */
    private String userId;

    /**
     * @Fields sessionId : 服务器的session
     */
    private String sessionId;


    /**
     * @Fields loginTime : 登录时间
     */
    private String loginTime;


    /**
     * @Fields uuid : 随机数
     */
    private String uuid;

    /**
     * @Fields empCode : 人员编码
     */
    private String empCode;


    private Map<String, Object> areaInfo = new HashMap<>();

    /**
     * @Fields path : 用户的目录结构
     */
    private List<SysModule> path = new ArrayList<SysModule>();

    /**
     * @Fields pathCheck : 直接用来验证前端传过来的地址是否存在
     */
    private List<String> checkPath = new ArrayList<String>();

    /**
     * @Fields userArea : 用户所属的所有区划
     */
    private List<Map<String, Object>> userArea = new ArrayList<Map<String, Object>>();

    private Integer messageCount;


}
