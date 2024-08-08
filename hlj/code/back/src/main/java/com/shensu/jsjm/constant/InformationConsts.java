package com.shensu.jsjm.constant;

import java.util.HashMap;
import java.util.Map;

public class InformationConsts {

    public static final String        xl_bs         = "1";
    public static final String        xl_ss         = "2";
    public static final String        xl_bk         = "3";
    public static final String        xl_dz         = "4";
    public static final String        xl_gz         = "5";
    public static final String        xl_zz         = "6";
    public static final String        xl_cz         = "7";

    public static final String        zc_no         = "0";
    public static final String        zc_cj         = "1";
    public static final String        zc_zj         = "2";
    public static final String        zc_fg         = "3";
    public static final String        zc_gj         = "4";


    public static Map<String, String> xlMap         = new HashMap<>();
    public static Map<String, String> zcMap         = new HashMap<>();

    static {
        xlMap.put(xl_bs, "博士研究生");
        xlMap.put(xl_ss, "硕士研究生");
        xlMap.put(xl_bk, "本科");
        xlMap.put(xl_dz, "大专");
        xlMap.put(xl_gz, "高中");
        xlMap.put(xl_zz, "中专");
        xlMap.put(xl_cz, "初中及以下");

        zcMap.put(zc_no, "无");
        zcMap.put(zc_cj, "初级");
        zcMap.put(zc_zj, "中级");
        zcMap.put(zc_fg, "副高");
        zcMap.put(zc_gj, "高级");
    }
}
