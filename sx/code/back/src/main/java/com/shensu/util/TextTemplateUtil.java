package com.shensu.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 版权：(C) 版权所有 2019-2022  苏州沈苏自动化有限公司
 * <简述>配置文件读取
 * <详细描述>
 *
 * @author gelongyu
 * @version $Id$
 * @see
 * @since
 */
@Component
public class TextTemplateUtil {

    private static Properties props;
    private static Properties defaultProps;
    private static Properties childInfoProps;
    private static Properties ymShotInfoProps;
    private static Properties ymBespeakInfoProps;
    private static Properties chChildTabu31Props;
    private static Properties dogInjuryProps;
    private static Properties textconstans;

    public TextTemplateUtil() {

        try {
            Resource resource = new ClassPathResource("/base/TextTemplate.properties");//
            //props = PropertiesLoaderUtils.loadProperties(resource);
            //StaticConfigProperties staticProperties = new StaticConfigProperties();
            //props.setProperty("front.server.url", staticProperties.getFrontUrl());
            //initApplicationPropByName(props, "front.server.url");
            //initApplicationPropByName(props, "gjUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取属性
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {

        return props == null ? null : props.getProperty(key);
    }

    /**
     * 获取区免疫平台的相关字段映射
     *
     * @param key
     * @param prefix 前缀
     * @return
     */
    public static String getPropertyForYmMypt(String key, String prefix) {
        if (prefix.equals("childInfo")) {
            return childInfoProps == null ? null : childInfoProps.getProperty(key);
        }
        if (prefix.equals("ymShotInfo")) {
            return ymShotInfoProps == null ? null : ymShotInfoProps.getProperty(key);
        }
        if (prefix.equals("ymBespeakInfo")) {
            return ymBespeakInfoProps == null ? null : ymBespeakInfoProps.getProperty(key);
        }
        if (prefix.equals("chChildTabu31")) {
            return chChildTabu31Props == null ? null : chChildTabu31Props.getProperty(key);
        }
        if (prefix.equals("dogInjury")) {
            return dogInjuryProps == null ? null : dogInjuryProps.getProperty(key);
        }
        return null;

    }

    /**
     * 获取属性
     *
     * @param key          属性key
     * @param defaultValue 属性value
     * @return
     */
    public static String getProperty(String key, String defaultValue) {

        return props == null ? null : props.getProperty(key, defaultValue);

    }

    public static void setProperty(String key, String value) {
        props.setProperty(key, value);
    }


    /**
     * 获取properyies属性
     *
     * @return
     */
    public static Properties getProperties() {
        return props;
    }

    /**
     * 获取主域名，即URL头
     * @param urlStr urlStr
     * @return host
     */
    public static String getDomainHost(String urlStr){
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println("protocol = " + url.getProtocol());
        System.out.println("authority = " + url.getAuthority()); System.out.println("host = " + url.getHost());
        System.out.println("port = " + url.getPort());
        System.out.println("path = " + url.getPath());
        System.out.println("query = " + url.getQuery());
        System.out.println("filename = " + url.getFile());
        System.out.println("ref = " + url.getRef());
        return url.getHost();
    }

    /**
     * 获取区免疫平台的相关字段映射 的 Properties
     *
     * @param prefix 前缀
     * @return
     */
    public static Properties getPropertiesForYmMypt(String prefix) {
        if (prefix.equals("childInfo")) {
            return childInfoProps;
        }
        if (prefix.equals("ymShotInfo")) {
            return ymShotInfoProps;
        }
        if (prefix.equals("ymBespeakInfo")) {
            return ymBespeakInfoProps;
        }
        if (prefix.equals("chChildTabu31")) {
            return chChildTabu31Props;
        }
        if (prefix.equals("dogInjury")) {
            return dogInjuryProps;
        }
        return null;
    }

    /**
     * 〈简述〉反射自动赋值，用于测试表的插入
     * 〈详细描述〉
     *
     * @param obj 类的名称
     * @return 类对象
     * @throws IllegalAccessException
     * @author gelongyu
     */
    public static Object getProperty(Object obj) throws IllegalAccessException {
        //利用反射获取类属性
        Field[] field = obj.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            Field f = field[i];
            f.setAccessible(true);
            // key:得到属性名
            String key = f.getName();
            //获取属性名
            String name = field[i].getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            //获取属性类型
            String type = field[i].getGenericType().toString();
            Method method = null;
            try {
                try {
                    if (type.equals("class java.lang.String")) {
                        method = obj.getClass().getMethod("set" + name, String.class);
                        //给对象的这个属性赋值 String类型 举例 此处赋值为空字符串
                        method.invoke(obj, String.valueOf(2));
                    }
                    if (type.equals("class java.lang.Integer")) {
                        method = obj.getClass().getMethod("set" + name, Integer.class);
                        //给对象的这个属性赋值 Integer类型 此处赋值为1
                        method.invoke(obj, 1);
                    }
                    if (type.equals("int")) {
                        method = obj.getClass().getMethod("set" + name, int.class);
                        //给对象的这个属性赋值 int类型
                        method.invoke(obj, 2);
                    }
                    if (type.equals("class java.util.Date")) {
                        method = obj.getClass().getMethod("set" + name, Date.class);
                        //给对象的这个属性赋值 int类型
                        method.invoke(obj, new Date());
                    }
                    //其他类型还未测试，所以没有写出
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }


    /**
     * 〈简述〉反射自动赋值，用于测试表的插入
     * 〈详细描述〉
     *
     * @param obj 类的名称
     * @return 类对象
     * @throws IllegalAccessException
     * @author gelongyu
     */
    public static Map getPropertyToMap(Object obj) throws IllegalAccessException {
        //利用反射获取类属性
        Field[] field = obj.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < field.length; i++) {
            Field f = field[i];
            f.setAccessible(true);
            // key:得到属性名
            String key = f.getName();
            //获取属性名
            String name = field[i].getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            //获取属性类型
            String type = field[i].getGenericType().toString();
            Method method = null;
            try {
                try {
                    method = obj.getClass().getMethod("get" + name, null);

                    map.put(name, method.invoke(obj));
//                    if(type.equals("class java.lang.String")){
//                        method = obj.getClass().getMethod("get"+name,String.class);
//                        //给对象的这个属性赋值 String类型 举例 此处赋值为空字符串
//                        method.invoke();
//                    }
//                    if(type.equals("class java.lang.Integer")){
//                        method = obj.getClass().getMethod("get"+name,Integer.class);
//                        //给对象的这个属性赋值 Integer类型 此处赋值为1
//                        method.invoke(obj, 1);
//                    }
//                    if(type.equals("int")){
//                        method = obj.getClass().getMethod("get"+name,int.class);
//                        //给对象的这个属性赋值 int类型
//                        method.invoke(obj, 2);
//                    }
//                    if(type.equals("class java.util.Date")){
//                        method = obj.getClass().getMethod("get"+name,Date.class);
//                        //给对象的这个属性赋值 int类型
//                        method.invoke(obj, new Date());
//                    }
                    //其他类型还未测试，所以没有写出
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static void CopySameBean(Object fromBean, Object toBean) throws NullPointerException, IllegalArgumentException, IllegalAccessException {
        if (fromBean == toBean) {
            return;
        }
        if (fromBean != null) {
            // 得到类对象
            Class fromBeanClass = (Class) fromBean.getClass();
            Class toBeanClass = (Class) toBean.getClass();

            /**
             * 得到类中的所有属性集合
             */
            Field[] fbc = fromBeanClass.getDeclaredFields();
            Field[] tbc = toBeanClass.getDeclaredFields();
            for (int i = 0; i < fbc.length; i++) {
                Field f = fbc[i];

                f.setAccessible(true); // 设置些属性是可以访问的
                Object fVal = f.get(fromBean);// 得到此属性的值
                System.out.println("name:" + f.getName() + "\t value = " + fVal);
                for (int j = 0; j < tbc.length; j++) {
                    Field t = tbc[i];

                    t.setAccessible(true); // 设置些属性是可以访问的
                    // 属性名称和属性类型必须全部相同，才能赋值
                    if (f.getName().equals(t.getName()) && f.getType().toString().equals(t.getType().toString())) {
                        t.set(toBean, fVal);
                    }
                }
            }
        } else {
            throw new NullPointerException("FromBean is null");
        }
    }


    /**
     * 〈简述〉对请求的回应 统一 重新 响应
     * 〈详细描述〉
     *
     * @param comm     json数据
     * @param response 响应
     * @author gelongyu
     */
    @SuppressWarnings("deprecation")
    public static void responseStutas(CommonRespBody comm, HttpServletResponse response) {
        if (comm.isSuccess()) {
            response.setStatus(Constants.ResultSuccessCode);
        } else {
            response.setStatus(comm.getCode() != null ? comm.getCode() : Constants.ResultFailCode);
        }
    }

    /**
     * 〈简述〉对请求的回应 统一 重新 响应
     * 〈详细描述〉
     * @param comm     json数据
     * @param response 响应
     * @author gelongyu
     */
    @SuppressWarnings("deprecation")
    public static void responseStatus(MultipleRespBody<?> comm, HttpServletResponse response) {
        if (comm.isSuccess()) {
            response.setStatus(Constants.ResultSuccessCode);
        } else {
            response.setStatus(comm.getCode() != null ? comm.getCode() : Constants.ResultFailCode);
        }
    }

    /**
     * 〈简述〉page封装成map 前端参数接收标准
     * 〈详细描述〉
     *
     * @param pageInfo page
     * @return map
     * @author gelongyu
     */
    public static Map<String, Object> getMapInfo(PageInfo<Map<String, Object>> pageInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageInfo.getTotal());
        List<Map<String, Object>> list = pageInfo.getList();
        map.put("data", list);
        return map;
    }


    public static Map<String, Object> getMapInfo(PageInfo<Map<String, Object>> pageInfo,int total) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageInfo.getTotal());
        if (pageInfo.getTotal() > total) {
            map.put("total", total);
        }
        int pageNum = pageInfo.getPageNum();
        int pageSize = pageInfo.getPageSize();

        int currentPageTotal = pageSize;

        if (pageNum * pageSize > total) {
            currentPageTotal = total - (pageNum - 1) * pageSize;
        }

        List<Map<String, Object>> list = pageInfo.getList();
        if (list.size() <= currentPageTotal) {
            map.put("data", list);
        } else {
            map.put("data", list.subList(0, currentPageTotal));
        }
        return map;
    }

    public static Map<String, Object> getListInfo(PageInfo<?> pageInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageInfo.getTotal());
        List<?> list = pageInfo.getList();
        map.put("data", list);
        return map;
    }

    public static Map<String, Object> parseListInfo(PageInfo<?> pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        List<?> list = pageInfo.getList();
        JSONArray result = new JSONArray();
        JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
        for (Object obj : array) {
            JSONObject jsonObj = (JSONObject) obj;
            JSONObject newObj = new JSONObject();
            for (String key : jsonObj.keySet()) {
                newObj.put(key.toUpperCase(), jsonObj.get(key));
            }
            result.add(newObj);
        }
        map.put("data", result);
        return map;
    }

    public static Map<String, Object> getListInfo(PageInfo<?> pageInfo,int total) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageInfo.getTotal());
        if (pageInfo.getTotal() > total) {
            map.put("total", total);
        }
        int pageNum = pageInfo.getPageNum();
        int pageSize = pageInfo.getPageSize();

        int currentPageTotal = pageSize;

        if (pageNum * pageSize > total) {
            currentPageTotal = total - (pageNum - 1) * pageSize;
        }

        List<?> list = pageInfo.getList();
        if (list.size() <= currentPageTotal) {
            map.put("data", list);
        } else {
            map.put("data", list.subList(0, currentPageTotal));
        }

        return map;
    }


    /**
     * 〈简述〉检验字符串是否是 数字组成
     * 〈详细描述〉
     *
     * @param str 参数
     * @return true  or false
     * @author gelongyu
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 〈简述〉返回前端接收值
     * 〈详细描述〉
     */
    public static Object setResponse(CommonRespBody comm, HttpServletResponse response) {
        if (comm.isSuccess()) {
            response.setStatus(Constants.ResultSuccessCode);
            return comm.getData();
        } else {
            response.setStatus(comm.getCode() == null ? Constants.ResultFailCode : comm.getCode());
            return comm.getPopMsg();
        }
    }

    /**
     * 〈简述〉根据路径请求来判断 返回 成人 还是儿童
     * 〈详细描述〉
     *
     * @param request 请求
     * @return 0  或 1
     * @author gelongyu
     */
    public static String getIsadult(HttpServletRequest request) {
        if (request.getServletPath().contains("bactrule")) {
            return "0";
        } else {
            return "1";
        }

    }



    /**
     * 〈简述〉一个字符串  按照一定长度分割
     * 〈详细描述〉
     *
     * @param inputString 字符串
     * @param length      长度
     * @return list
     * @author gelongyu
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStr(inputString, length, size);
    }

    /**
     * 〈简述〉
     * 〈详细描述〉
     *
     * @param inputString 值
     * @param length      长度
     * @param size        一共几组
     * @return lsit
     * @author gelongyu
     */
    public static List<String> getStr(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String str = substring(inputString, index * length, (index + 1) * length);
            list.add(str);
        }
        return list;

    }

    /**
     * 〈简述〉
     * 〈详细描述〉
     *
     * @param str 值
     * @param f   开头截取位
     * @param t   末尾截取位
     * @return 结果
     * @author gelongyu
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length()) {
            return null;
        }
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }


    /**
     * 〈简述〉获取用户真实IP地址
     * 〈详细描述〉
     *
     * @param request 请求
     * @return IP
     * @author gelongyu
     */
    public static String getIpAddress(HttpServletRequest request) {
        /**
         * 获取距离服务器最远的那个ip
         */
        String ip = request.getHeader("x-forwarded-for");
        if (ipIsNullOrEmpty(ip)) {
            /**
             * apache http服务代理加上的ip
             */
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ipIsNullOrEmpty(ip)) {
            /**
             * weblogic插件加上的头
             */
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipIsNullOrEmpty(ip)) {
            /**
             * 真实ip
             */
            ip = request.getHeader("X-Real-IP");
        }
        if (ipIsNullOrEmpty(ip)) {
            /**
             * 最后真实的ip
             */
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 当前ip是否为空
     *
     * @param ip
     * @return
     */
    public static boolean ipIsNullOrEmpty(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return true;
        }
        return false;
    }

    /**
     * 〈简述〉list 转成 String
     * 〈详细描述〉
     *
     * @param list 集合
     * @return String
     * @author gelongyu
     */
    public static String changeListToStr(List<String> list) {
        return list == null ? "" : list.stream().collect(Collectors.joining(","));
    }

    /**
     * 〈简述〉对象转map
     * 〈详细描述〉
     *
     * @param obj 对象
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @author gelongyu
     */
    public static Map<String, Object> modelToMap(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            map.put(fields[i].getName(), fields[i].get(obj));
        }
        return map;
    }

    /**
     * 〈简述〉x.x.x  转成具体多少天
     * 〈详细描述〉
     *
     * @param arr   数组
     * @param index 元素下标
     * @return
     * @author gelongyu
     */
    public static Integer arrToInt(String[] arr, int index) {
        if (arr.length < index) {
            return 0;
        }
        String value = arr[index];
        String[] toInt = value.split("[.]");
        int result = Integer.parseInt(toInt[0]) * 365 + Integer.parseInt(toInt[1]) * 30 + Integer.parseInt(toInt[2]);
        return result;
    }

    public static Map<String, Object> resetMapKey(Map<String, Object> origin) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (String key : origin.keySet()) {
            key = key.toString();
            if (key.equals(key.toUpperCase())) {
                map.put(key.toLowerCase(), origin.get(key));
            } else {
                map.put(key, origin.get(key));
            }
        }
        return map;
    }

    public static List resetListMapKey(List<Object> origin) {
        List list = new ArrayList();
        for (Object obj : origin) {
            if (obj instanceof Map) {
                list.add(resetMapKey((Map<String, Object>) obj));
            } else {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 获取属性
     *
     * @param key
     * @return
     */
    public static String getTextConstans(String key) {

        return textconstans == null ? null : textconstans.getProperty(key);

    }

    /**
     * 获取属性
     *
     * @param key          属性key
     * @param defaultValue 属性value
     * @return
     */
    public static String getTextConstans(String key, String defaultValue) {

        return textconstans == null ? null : textconstans.getProperty(key, defaultValue);

    }
}
