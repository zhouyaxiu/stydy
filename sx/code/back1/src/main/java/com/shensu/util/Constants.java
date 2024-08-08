package com.shensu.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    /*sessionId*/
    public static final String SessionId = "EpicardCloud_SessionId";
    /**
     * @Fields FAILMESSAGE : 后台所有报错 统一 返回 改提示信息
     */
    public static final String FAILE = "=操作失败";
    public static final String FAILMESSAGE = "服务器开小差啦！";

    /*定义接口返回状态码*/
    public static final int ResultSuccessCode = 200; //成功
    public static final int MultesultSuccessCode = 201;    //成功，但是查询到多条信息
    public static final int STUINSCHOOL = 202;    //学生在当前学校存在但是需要转班转年级或者转轮次
    public static final int STUEXIST = 203;    //学生已经存在了提示
    public static final int STUINOTHERCODE= 204;//用户存在于其他学校了


    public static final int ResultFailCode = 521;    //失败，错误信息

    public static final int ResultErrorCode = 522;    //接口调用成功，但有错误提示

    public static final int LOGINOUT = 524;    //登录超时
    /*登录以及验证码相关提示*/
    public static final String LOGGER_ERROR1 = "验证码错误！";

    public static final String LOGGER_ERROR2 = "验证码已失效，请重新输入！";

    public static final String LOGGER_ERROR3 = "账号不能为空！";

    public static final String LOGGER_ERROR4 = "密码不能为空！";

    public static final String LOGGER_ERROR5 = "验证码不能为空！";

    public static final String LOGGER_ERROR6 = "密码错误，请重新输入！";

    public static final String LOGGER_ERROR7 = "账号不存在！";

    public static final String LOGGER_ERROR8 = "账号已被删除！";

    public static final String LOGGER_ERROR12 = "该账号今日已被锁定！";

    public static final String LOGGER_ERROR13 = "该账号所属学校已被删除，无法登录！";

    public static final String LOGGER_ERROR20 = "登录账号不在指定的地区无法登录！";
    /*系统角色相关*/
    public final static String login = "0";//登录
    public final static String insert = "1";//操作类型 新增
    public final static String update = "2";//修改
    public final static String delete = "3";//删除
    public final static String query = "4";//查询
    public final static String print = "5";//打印
    public final static String output = "6";//导出

    /*
     * 页码相关
     * */

    public final  static  int   PAGESIZE=20;
    public  final static  int   EXPORTPAGESIZE=5000;

    /*
     * 学校类型
     * */
    public static final String SCHTYPE0 = "0";  //幼托
    public static final String SCHTYPE1 = "1";  //小学

    public static final String SCH_DEFAULT_PWD = "NBcyxt@123456";

    /*
     * 导出类型*/
    public final static String        EXCEL_SZZCY       = "0";
    public final static String        EXCEL_CYJGCX      = "1";

    //	/**
    //	 * 更改类型，0：修改；1：删除
    //	 */
    public final static String        UPDATETYPE_UPDATE = "0";
    public final static String        UPDATETYPE_DELETE = "1";

    /**
     * 报到时是否有接种证  0有1无
     */
    public static Map<String, String> STU_ISJZZMap         = new HashMap<String, String>() {
        {
            put("0", "有");
            put("1", "无");
        }
    };

    public static Map<String, String> SCH_TYPE         = new HashMap<String, String>() {
        {
            put("0", "幼托机构");
            put("1", "小学");
            put("2", "初中");
            put("3", "高中");
            put("4", "大学");
        }
    };
    public static Map<String, String>  STU_SEX         = new HashMap<String, String>() {
        {
            put("0", "未知的性别");
            put("1", "男性");
            put("2", "女性");
            put("9", "未说明的性别");
        }
    };
}
