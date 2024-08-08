package com.shensu.jsjm.util;

import com.shensu.jsjm.constant.Constants;
import com.shensu.jsjm.exception.BaseException;
import com.shensu.jsjm.model.AreaBean;
import com.shensu.jsjm.model.LabelValueBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义通用方法类
 *
 * @author puzm
 */
public class CommonUtil {

    /**
     * 验证邮箱
     *
     * @param str
     * @return 如果是符合的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isEmail(String str) {
//        String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        String regex = "^[a-zA-Z0-9.-_\u4e00-\u9fa5]+@[a-zA-Z0-9-_]+(\\.[A-Za-z0-9-_]+)+$";
        return match(regex, str);
    }

    /**
     * 验证IP地址
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIP(String str) {
        String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
        return match(regex, str);
    }

    /**
     * 验证网址Url
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
//	public static boolean IsUrl(String str) {
//		String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
//		return match(regex, str);
//	}
    public static boolean IsUrl(String str) {
        String regex = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        return match(regex, str);
    }

    /**
     * 验证电话号码
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsTelephone(String str) {
        String regex = "^(\\d{3,4}-)?\\d{6,8}$";
        return match(regex, str);
    }

    /**
     * 验证输入密码条件(字符与数据同时出现)
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPassword(String str) {
        String regex = "[A-Za-z]+[0-9]";
        return match(regex, str);
    }

    /**
     * 验证输入密码长度 (6-18位)
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPasswLength(String str) {
        String regex = "^\\d{6,18}$";
        return match(regex, str);
    }

    /**
     * 验证输入邮政编号
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPostalcode(String str) {
        String regex = "^\\d{6}$";
        return match(regex, str);
    }

    /**
     * 验证输入手机号码
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsHandset(String str) {
        String regex = "^[1]+[3,5]+\\d{9}$";
        return match(regex, str);
    }

    /**
     * 验证输入身份证号
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsIDcard(String str) {
        String regex = "(^\\d{18}$)|(^\\d{15}$)";
        return match(regex, str);
    }

    /**
     * 验证输入两位小数
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsDecimal(String str) {
        String regex = "^[0-9]+(.[0-9]{2})?$";
        return match(regex, str);
    }

    /**
     * 验证输入一年的12个月
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsMonth(String str) {
        String regex = "^(0?[[1-9]|1[0-2])$";
        return match(regex, str);
    }

    /**
     * 验证输入一个月的31天
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsDay(String str) {
        String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
        return match(regex, str);
    }

    /**
     * 验证日期时间
     *
     * @param str
     * @return 如果是符合网址格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isDate(String str) {
        // 严格验证时间格式的(匹配[2002-01-31], [1997-04-30],
        // [2004-01-01])不匹配([2002-01-32], [2003-02-29], [04-01-01])
        // String regex =
        // "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((01,3-9])|(1[0-2]))-(29|30)))))$";
        // 没加时间验证的YYYY-MM-DD
        // String regex =
        // "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";
        // 加了时间验证的YYYY-MM-DD 00:00:00
        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
        return match(regex, str);
    }

    /**
     * 验证数字输入
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsNumber(String str) {
        String regex = "^[0-9]*$";
        return match(regex, str);
    }

    /**
     * 验证非零的正整数
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsIntNumber(String str) {
        String regex = "^\\+?[1-9][0-9]*$";
        return match(regex, str);
    }

    /**
     * 验证大写字母
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsUpChar(String str) {
        String regex = "^[A-Z]+$";
        return match(regex, str);
    }

    /**
     * 验证小写字母
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLowChar(String str) {
        String regex = "^[a-z]+$";
        return match(regex, str);
    }

    /**
     * 验证验证输入字母
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLetter(String str) {
        String regex = "^[A-Za-z]+$";
        return match(regex, str);
    }

    /**
     * 验证验证输入汉字
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsChinese(String str) {
        String regex = "^[\u4e00-\u9fa5]+$";
        return match(regex, str);
    }

    /**
     * 验证验证输入字符串
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLength(String str) {
        String regex = "^.{8,}$";
        return match(regex, str);
    }

    /**
     * 验证是否中英文数字
     *
     * @param str
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsText(String str) {
        String regex = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
        return match(regex, str);
    }

    /**
     * 校验特殊字符
     *
     * @param str
     * @return
     */
    public static boolean isConSpeChar(String str) {

        String regex = "[`~!@#$%^&*()+=|{}':;'\\[\\]<>/?~！@#￥%……&*——+|{}【】‘；：”“’。，、？]";
        return match(regex, str);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    // 3. 检查字符串重复出现的词
    //
    // private void btnWord_Click(object sender, EventArgs e)
    // {
    // System.Text.RegularExpressions.MatchCollection matches =
    // System.Text.RegularExpressions.Regex.Matches(label1.Text,
    //
    // @"\b(?<word>\w+)\s+(\k<word>)\b",
    // System.Text.RegularExpressions.RegexOptions.Compiled |
    // System.Text.RegularExpressions.RegexOptions.IgnoreCase);
    // if (matches.Count != 0)
    // {
    // foreach (System.Text.RegularExpressions.Match match in matches)
    // {
    // string word = match.Groups["word"].Value;
    // MessageBox.Show(word.ToString(),"英文单词");
    // }
    // }
    // else { MessageBox.Show("没有重复的单词"); }
    //
    //
    // }
    //
    // 4. 替换字符串
    //
    // private void button1_Click(object sender, EventArgs e)
    // {
    //
    // string strResult =
    // System.Text.RegularExpressions.Regex.Replace(textBox1.Text,
    // @"[A-Za-z]\*?", textBox2.Text);
    // MessageBox.Show("替换前字符:" + "\n" + textBox1.Text + "\n" + "替换的字符:" + "\n"
    // + textBox2.Text + "\n" +
    //
    // "替换后的字符:" + "\n" + strResult,"替换");
    //
    // }
    //
    // 5. 拆分字符串
    //
    // private void button1_Click(object sender, EventArgs e)
    // {
    // //实例: 甲025-8343243乙0755-2228382丙029-32983298389289328932893289丁
    // foreach (string s in
    // System.Text.RegularExpressions.Regex.Split(textBox1.Text,@"\d{3,4}-\d*"))
    // {
    // textBox2.Text+=s; //依次输出 "甲乙丙丁"
    // }
    //
    // }

//    /**
//     * 从session获取User信息
//     *
//     */
//    public static User getUserInfo(HttpSession session) {
//        Object obj = session.getAttribute(Constants.SESSION_KEY_USER);
//
//        JSONObject json = JSONObject.fromObject(obj);
//        if (json.isNullObject() || json.isEmpty() || !json.has("loginName")) {
//            throw new BusinessException(Constants.STATUS_524, "登录失效");
//        }
//        try {
//            User user = (User) obj;
//            if (user == null) {
//                throw new BusinessException(Constants.STATUS_524, "登录失效");
//            }
//            return user;
//        } catch (Exception e) {
//            throw new BusinessException(Constants.STATUS_524, "登录失效");
//        }
//    }

    /**
     * 设置respone返回
     */
    public static void responseProcess(HttpServletResponse response, HttpServletRequest request, int code, String msg,
                                       boolean encrypt, boolean samesite) throws IOException {
//		response.setHeader("Access-Control-Methods", "*");
//		response.setHeader("Access-Control-Allow-Headers", "*");
//		response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Transfer-Encoding", "chunked");
        response.setContentType("application/json;charset=UTF-8");
        if (!samesite) {
            String origin = request.getHeader("Origin");
            if (origin == null) {
                origin = request.getHeader("Referer");
            }
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, PATCH, DELETE");
            response.setHeader("Access-Control-Allow-Headers",
                    "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setStatus(code);
        response.getOutputStream().write(respMsg(code, msg, encrypt).getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    /**
     *
     */
    public static String respMsg(int status, String msg, boolean encrypt) {
        if (!encrypt || status == Constants.STATUS_530) {
            return msg;
        }
        String parameters = AESUtil.encrypt(msg);
        String sign;
        try {
            sign = MD5Util.md5(parameters);
        } catch (Exception e1) {
            sign = "";
        }
        JSONObject json = new JSONObject();
        json.put("parameters", parameters);
        json.put("sign", sign);
        return json.toString();
    }

    /**
     *
     */
    public static Object respMsg(int status, JSONObject msg, boolean encrypt) {
        if (!encrypt || status == Constants.STATUS_530) {
            return msg;
        }
        String parameters = AESUtil.encrypt(msg.toString());
        String sign;
        try {
            sign = MD5Util.md5(parameters);
        } catch (Exception e1) {
            sign = "";
        }
        JSONObject json = new JSONObject();
        json.put("parameters", parameters);
        json.put("sign", sign);
        return json;
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static String sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }


    /**
     * 有小数位保留一位，没有小数位显示整数
     */
    public static String div2(String v) {
        BigDecimal bigDecimal = new BigDecimal(v);
        bigDecimal = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP);
        String resultStr = bigDecimal.toString();
        if (resultStr.contains(".0")) {
            resultStr = resultStr.substring(0, resultStr.lastIndexOf("."));
        }
        return resultStr;
    }

    /**
     * 有小数位保留2位，没有小数位显示整数
     */
    public static String div4(String v) {
        BigDecimal bigDecimal = new BigDecimal(v);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        String resultStr = bigDecimal.toString();
//        if (resultStr.contains(".00")) {
//            resultStr = resultStr.substring(0, resultStr.lastIndexOf("."));
//        }
        return div3(resultStr);
    }

    /**
     * 去除小数尾部的多余的0
     */
    public static String div3(String v) {
        return new BigDecimal(v).stripTrailingZeros().toPlainString();
    }

    /**
     * 提供精确的除法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String div(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2).toString();
    }

    /**
     * 提供精确的除法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 四舍五入
     */
    public static String div(String v1, String v2, int scale) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        if (!StringUtils.hasText(v2) || b2 == BigDecimal.ZERO || "0".equals(v2)) {
            b1 = new BigDecimal(0);
            b2 = new BigDecimal(1);
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }


    /**
     * 四舍五入
     *
     * @param scale 保留小数位数
     */
    public static String RoundNoOf(String str, int scale) {
        // 取得数值
        BigDecimal b = new BigDecimal(str);
        // 取得数值1
        BigDecimal one = new BigDecimal("1");
        // 原始值除以1，保留scale位小数，进行四舍五入
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 取整
     *
     * @param scale 保留小数位数
     */
    public static String RoundNoOf2(String str, int scale) {
        // 取得数值
        BigDecimal b = new BigDecimal(str);
        // 取得数值1
        BigDecimal one = new BigDecimal("1");
        // 原始值除以1，保留scale位小数，直接截取保留位
        return b.divide(one, scale, BigDecimal.ROUND_DOWN).toString();
    }

    /**
     * 地区展示处理
     *
     */
//    public static String getAreaName(CommonMapper commonMapper, String areaCode, String areaName) {
//        if (!"0000000000".equals(areaCode.substring(2))) {
//            if ("00000000".equals(areaCode.substring(4))) {
//                String Code1 = areaCode.substring(0, 2) + "0000000000";
//                String areaName1 = commonMapper.queryAreaNameByCode(Code1);
//                areaName = areaName1 + "-" + areaName;
//            } else if ("000000".equals(areaCode.substring(6))) {
//                String Code1 = areaCode.substring(0, 2) + "0000000000";
//                String areaName1 = commonMapper.queryAreaNameByCode(Code1);
//                String Code2 = areaCode.substring(0, 4) + "00000000";
//                String areaName2 = commonMapper.queryAreaNameByCode(Code2);
//                areaName = areaName1 + "-" + areaName2 + "-" + areaName;
//            } else {
//                String Code1 = areaCode.substring(0, 2) + "0000000000";
//                String areaName1 = commonMapper.queryAreaNameByCode(Code1);
//                String Code2 = areaCode.substring(0, 4) + "00000000";
//                String areaName2 = commonMapper.queryAreaNameByCode(Code2);
//                String Code3 = areaCode.substring(0, 6) + "000000";
//                String areaName3 = commonMapper.queryAreaNameByCode(Code3);
//                areaName = areaName1 + "-" + areaName2 + "-" + areaName3 + "-" + areaName;
//            }
//        }
//        return areaName;
//    }

    /**
     * 分页
     */
    public static List<?> pageList(List<?> list, List<?> pageList, Integer pageNo, Integer pageSize, Integer count) {
        if (count > 0) {
            int fromIndex = (pageNo - 1) * pageSize;
            int toIndex = (pageNo) * pageSize;
            if (toIndex > count) {
                toIndex = count;
            }
            pageList = list.subList(fromIndex, toIndex);
        }
        return pageList;
    }

    /**
     * list处理-String
     */
    @SuppressWarnings("unchecked")
    public static List<String> setStringList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>(jsonArray.size());
        jsonArray.forEach(data -> list.add((String) data));
        return list;
    }

    /**
     * list处理-int
     */
    public static List<Integer> setIntegerList(JSONArray jsonArray) {
        List<String> listString = setStringList(jsonArray);
        List<Integer> listInteger = new ArrayList<>(listString.size());
        listString.forEach(data -> listInteger.add(Integer.parseInt(data)));
        return listInteger;
    }

    /**
     * list处理-int
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> setIntegerList2(JSONArray jsonArray) {
        List<Integer> list = new ArrayList<>(jsonArray.size());
        jsonArray.forEach(data -> list.add((Integer) data));
        return list;
    }

    /**
     * inputvolist校验
     */
    public static boolean inputvoListCheck(Object vo) {
        return vo != null && StringUtils.hasText(vo.toString()) && JSONArray.fromObject(vo).size() > 0
                && !"null".equals(JSONArray.fromObject(vo).get(0).toString());
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request HttpServletRequest
     */
    public static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * String转date yyyy-MM-dd
     */
    public static Date StringToDate(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(str, formatter);
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * String转dateTime yyyy-MM-dd
     */
    public static Date StringToDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 身份证取生日
     */
    public static String getBirthday(String certificateNo) {
        return certificateNo.length() == 15
                ? ("19" + certificateNo.substring(6, 8) + "-" + certificateNo.substring(8, 10) + "-"
                + certificateNo.substring(10, 12))
                : (certificateNo.substring(6, 10) + "-" + certificateNo.substring(10, 12) + "-"
                + certificateNo.substring(12, 14));
    }

    /**
     * 身份证取生日
     */
    public static String getSex(String card) {
        return card.length() == 15
                ? Integer.parseInt(card.substring(14, 15)) % 2 == 0 ? "2" : "1"
                : Integer.parseInt(card.substring(16, 17)) % 2 == 0 ? "2" : "1";
    }

    /**
     * 生成比对模板
     *
     * @throws BaseException
     */
    public static String[] productTemplate(String code) throws BaseException {
        String level = getZoneLevelByCode(code);
        String codeTemplate1 = null;//sql语句code条件
        String codeTemplate2 = null;//sql语句code条件
        //０－省，１－市，２－县，３－乡，４－点，5 - 村 -1 - 国家
        if ("-1".equalsIgnoreCase(level)) {
            codeTemplate1 = "[0-9]{8}[0-9][1-9][0]{2}";
            codeTemplate2 = "[0-9]{8}[1-9][0-9][0]{2}";
        }
        if ("0".equalsIgnoreCase(level)) {
            codeTemplate1 = code.substring(0, 2) + "[0-9]{6}[0-9][1-9][0]{2}";
            codeTemplate2 = code.substring(0, 2) + "[0-9]{6}[1-9][0-9][0]{2}";
        }
        if ("1".equalsIgnoreCase(level)) {
            codeTemplate1 = code.substring(0, 4) + "[0-9]{4}[0-9][1-9][0]{2}";
            codeTemplate2 = code.substring(0, 4) + "[0-9]{4}[1-9][0-9][0]{2}";
        }
        if ("2".equalsIgnoreCase(level)) {
            codeTemplate1 = code.substring(0, 6) + "[0-9]{2}[0-9][1-9][0]{2}";
            codeTemplate2 = code.substring(0, 6) + "[0-9]{2}[1-9][0-9][0]{2}";
        }
        if ("3".equalsIgnoreCase(level)) {
            codeTemplate1 = code.substring(0, 8) + "[0-9][1-9][0]{2}";
            codeTemplate2 = code.substring(0, 8) + "[1-9][0-9][0]{2}";
        }
        if ("4".equalsIgnoreCase(level)) {
            codeTemplate1 = "none";
            codeTemplate2 = "none";
        }
        if ("5".equalsIgnoreCase(level)) {
            codeTemplate1 = "none";
            codeTemplate2 = "none";
        }

        return new String[]{codeTemplate1, codeTemplate2};
    }

    /**
     * 生成比对模板
     *
     * @throws BaseException
     */
    public static String[] productNewBaseCodeTemplate(String code) throws BaseException {
        String level = getZoneLevelByCode(code);
        String codeTemplate1 = null;//sql语句code条件
        String codeTemplate2 = null;//sql语句code条件
        //０－省，１－市，２－县，３－乡，４－点，5 - 村 -1 - 国家
        if ("-1".equalsIgnoreCase(level)) {
            codeTemplate1 = "[0-9]{8}[0-9][1-9]";
            codeTemplate2 = "[0-9]{8}[1-9][0-9]";
        }
        if ("0".equalsIgnoreCase(level)) {
            codeTemplate1 = code.substring(0, 2) + "[0-9]{6}[0-9][1-9]";
            codeTemplate2 = code.substring(0, 2) + "[0-9]{6}[1-9][0-9]";
        }
        if ("1".equalsIgnoreCase(level)) {
            codeTemplate1 = code.substring(0, 4) + "[0-9]{4}[0-9][1-9]";
            codeTemplate2 = code.substring(0, 4) + "[0-9]{4}[1-9][0-9]";
        }
        if ("2".equalsIgnoreCase(level)) {
            codeTemplate1 = code.substring(0, 6) + "[0-9]{2}[0-9][1-9]";
            codeTemplate2 = code.substring(0, 6) + "[0-9]{2}[1-9][0-9]";
        }
        if ("3".equalsIgnoreCase(level)) {
            codeTemplate1 = code.substring(0, 8) + "[0-9][1-9]";
            codeTemplate2 = code.substring(0, 8) + "[1-9][0-9]";
        }
        if ("4".equalsIgnoreCase(level)) {
            codeTemplate1 = "none";
            codeTemplate2 = "none";
        }
        if ("5".equalsIgnoreCase(level)) {
            codeTemplate1 = "none";
            codeTemplate2 = "none";
        }

        return new String[]{codeTemplate1, codeTemplate2};
    }

    public static String getZoneLevelByCode(String zoneCode) {
        if (zoneCode.contains(",")) {
            zoneCode = zoneCode.split(",")[0];
        }
        return getZoneLevelInt(zoneCode) + "";
    }

    public static int getZoneLevelInt(String zoneCode) {
        int l = zoneCode.length() / 2 - 1;
        if (zoneCode.length() >= 2 && zoneCode.endsWith("00")) {
            zoneCode = zoneCode.substring(0, zoneCode.length() - 2);
            return getZoneLevelInt(zoneCode);
        }
        return l;
    }

    public static void setAreaCodes(List<String> areaCodes, AreaBean parentArea) {
        areaCodes.add(parentArea.getCode());
        List<AreaBean> children = parentArea.getChildren();
        List<AreaBean> stations = parentArea.getStations();
        List<AreaBean> cyss = parentArea.getCys();
        //地区
        if (children != null && children.size() > 0) {
            for (AreaBean areaBean : children) {
                setAreaCodes(areaCodes, areaBean);
            }
        }
        //门诊
        if (stations != null && stations.size() > 0) {
            for (AreaBean areaBean : stations) {
                setAreaCodes(areaCodes, areaBean);
            }
        }
        //接种室
        if (cyss != null && cyss.size() > 0) {
            for (AreaBean areaBean : cyss) {
                setAreaCodes(areaCodes, areaBean);
            }
        }
    }

    public static List<AreaBean> getAreaList(Map<String, AreaBean> areaMapCode, String areaCode) throws BaseException {
        List<AreaBean> areaList = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(areaCode)) {
            String[] areaCodeArr = areaCode.split(",");
            AreaBean areaBean;
            for (String code : areaCodeArr) {
                areaBean = areaMapCode.get(code);
                if (areaBean == null) {
                    continue;
                }
            }
        }
        return areaList;
    }

    public static List<AreaBean> getChildAreaList(Map<String, AreaBean> areaMapCode, String areaCode) throws BaseException {
        List<AreaBean> areaList = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(areaCode)) {
            areaList = areaMapCode.get(areaCode).getChildren();
        }
        return areaList;
    }

    public static String subDecimal(double value) {
        BigDecimal b = new BigDecimal(value);
        double result = b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(result);
    }


    public static String getTodayStr () {
        LocalDate today = LocalDate.now();

        String startTime = today.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return startTime;
    }

    public static List<LabelValueBean> getAgeMonthList() {
        List<LabelValueBean> TYPES = new ArrayList<>();
        {
            TYPES.add(new LabelValueBean("2-3岁", "2"));
            TYPES.add(new LabelValueBean("3-4岁", "3"));
            TYPES.add(new LabelValueBean("4-5岁", "4"));
            TYPES.add(new LabelValueBean("5-6岁", "5"));
            TYPES.add(new LabelValueBean("6-7岁", "6"));
            TYPES.add(new LabelValueBean("7-8岁", "7"));
        }
        return TYPES;
    }

    public static void main(String[] args) {
        System.out.println(CommonUtil.getZoneLevelByCode("370000000000"));
        System.out.println(CommonUtil.getZoneLevelByCode("37010000000"));
        System.out.println(CommonUtil.getZoneLevelByCode("371311090000"));
    }
}
