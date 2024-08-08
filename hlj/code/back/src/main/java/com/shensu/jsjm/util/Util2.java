package com.shensu.jsjm.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class Util2 {
	
	public final static int BUFFER = 2048;
	
    //性别
    public static final String sex_man="1";//男
    public static final String sex_women="2";//女
    
    //状态
    public static final Integer status_ls = 3;//临时
    public static final Integer status_save = 0;//保存
    public static final Integer status_submit = 1;//提交未审核
    public static final Integer status_pass = 2;//审核通过
    public static final Integer status_reject = -1;//审核未通过
    
    public static final String city ="1";//市
    public static final String country ="2";//县
    public static final String sup ="3";//乡
    public static final String station ="4";//接种单位
    public static final String unit ="5";//村
    
    //日志操作状态
    /*public static final String moduleId = "2"; // AEFI报告卡ID 【后期如果多个模块此处要修改】*/
    public static String moduleName = "AEFI报告卡";
    
    public static final String logCountName = "系统日志统计";
    public static final String logQueryName = "系统日志查看";
    public static final String roleManageName = "系统角色管理";
    
    public static final String insert = "1";  //添加
    public static final String update = "2";  //修改和同步的操作都在这个类型中
    public static final String delete = "3";  //删除
    public static final String export = "4";  //导出
    public static final String state = "5";   //审核
    public static final String print = "6";   //打印
    public static final String down = "7";    //数据下载
    public static final String query = "8";  //查询
    public static final String login = "9";  //查询
    
    public static final String SYSTEM_USER_ROLE = "9";
    
    
    public static final String success = "success";
    public static final String fail = "fail";
    
    public final static String message8 = "读取Excel模板文件失败！";
	public final static String message9 = "EXCEL报表文件写入失败！";

    public static final String cyFbcgBactid = "B001";
    public static final String cyFhepbBactid = "B063";
    public static final String cyFhepbigBactid = "B110";

	public static final int interfacePageSize = 10;
    
    public static String getZoneLevelByCode(String zoneCode)
    {
        String zoneLevel = "-1";
        
        if(zoneCode.substring(2,10).equals("00000000")) //省 == 处理上等同市级 例
        {
            zoneLevel = "0";
        }   
        else if(zoneCode.substring(4,10).equals("000000")) //市
        {
            zoneLevel = "1";
        }
        else if(zoneCode.substring(6,10).equals("0000")) //县
        {
            zoneLevel = "2";
        }
        else if(zoneCode.substring(8,10).equals("00")) //乡
        {
            zoneLevel = "3";
        }
        else //村
        {
            zoneLevel = "4";
        }
        
        return zoneLevel;
    }

    public static String GetWhereInValuesSql(String column, String str,int num) {
        if (str==null||str.isEmpty()) {
            return " 1=1 ";
        }
        List<String> values = Arrays.asList(str.split(","));
        // sql语句
        String sql = " 1=1 AND (";
        // 值的个数
        int valueSize = values.size();
        // 批次数
        int batchSize = valueSize / num + (valueSize % num == 0 ? 0 : 1);
        for (int i = 0; i < batchSize; i++) {
            if (i > 0) {
                sql += ") or ";
            }
            sql += column+" in (";
            for (int j = i * num; ( j < (i + 1) * num) && j < valueSize; j++) {
                if (j > i * num) {
                    sql += ",";
                }
                // sql += "'" + values.get(j) + "'";
                sql += "" + values.get(j) + "";
            }
        }
        sql += ")) ";
        return sql;
    }
    
	public static String removeSpace(String name) {
		while (name.startsWith("　")) {// 这里判断是不是全角空格
			name = name.substring(1, name.length()).trim();
		}
		while (name.endsWith("　")) {
			name = name.substring(0, name.length() - 1).trim();
		}
		return name.trim();
	}
	
	/**
	  *  判断对象或字符串是否为空
	 * @param o
	 * @return
	 */
	public static boolean isNull(Object o) {
		boolean bReslut = false;
		if (o == null) {
			bReslut = true;
		} else if (o instanceof String) {
			if (((String) o).trim().length() == 0)
				bReslut = true;
		} else
			bReslut = false;

		return bReslut;
	}
	
	public static byte[] getBytesFromInputStream(InputStream inputStream) throws Exception {
		int len = 0;
		byte[] bBuffer = new byte[BUFFER];
		byte[] bIn = null;
	
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		while ((len = inputStream.read(bBuffer, 0, bBuffer.length)) != -1) {
			baout.write(bBuffer, 0, len);
		}
		inputStream.close();
		bIn = baout.toByteArray();
		return bIn;
	}
	
	public static String escapeQueryChars(String s) {
		if (null==s||s.isEmpty()) {
			return s;
		}
		StringBuilder sb = new StringBuilder();
		//查询字符串一般不会太长，挨个遍历也花费不了多少时间
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// These characters are part of the query syntax and must be escaped
			if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' 
				|| c == ':' || c == '^'	|| c == '[' || c == ']' || c == '\"' 
				|| c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
				|| c == '|' || c == '&' || c == ';' || c == '/' || c == '.' 
				|| c == '$' || c == '%' || Character.isWhitespace(c)) {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}

    public static Map<String,Object> xmlStr2Map(String xmlStr){
        Map<String,Object> map = new HashMap<String,Object>();
        Document doc;
        try {
            doc = DocumentHelper.parseText(xmlStr);
            Element root = doc.getRootElement();
            List children = root.elements();
            if(children != null && children.size() > 0) {
                for(int i = 0; i < children.size(); i++) {
                    Element child = (Element)children.get(i);
                    map.put(child.getName(),child.getTextTrim());
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return map;
    }


	 public static void main(String[] args) {
	       for (int i = 0; i < 6;) {
			System.out.println("sadasdsda\n");
			i=i+2;
	       }
	    }
}
