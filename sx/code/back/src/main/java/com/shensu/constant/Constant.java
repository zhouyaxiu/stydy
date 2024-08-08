package com.shensu.constant;

import com.shensu.mybatis.entity.check.FullExcelField;
import com.shensu.mybatis.entity.check.VaccinInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {

    public static final String        yes                = "1";
    public static final String        no                 = "0";

    public static final int           zone_level_shen    = 0;
    public static final int           zone_level_shi     = 1;
    public static final int           zone_level_xian    = 2;
    public static final int           zone_level_xiang   = 3;
    public static final int           zone_level_station = 4;

    public static final String        sex_man            = "1";
    public static final String        sex_woman          = "2";

    public static final String        adult_all          = "-1";
    public static final String        adult_yes          = "1";
    public static final String        adult_no           = "0";

    public static final String        hk_address         = "1";                          //户籍地
    public static final String        unit_address       = "0";                          //建当地
    public static final String        reg_address        = "2";                          //居住地

    public static Map<String, String> ym_map             = new HashMap<>();

    static {
        ym_map.put("IPV", "脊灰灭活疫苗");
        ym_map.put("OPV", "脊灰减毒活疫苗");
        ym_map.put("HepB", "乙肝疫苗");
        ym_map.put("MV", "麻疹疫苗");
        ym_map.put("MMR", "麻腮风疫苗");
        ym_map.put("MR", "麻风疫苗");
        ym_map.put("DPT", "百白破疫苗");
    }

    public static List<FullExcelField> fullList = new ArrayList<FullExcelField>() {
        {
            add(new FullExcelField("B063State", 12, (short) 2, String.class));
            add(new FullExcelField("B064State", 13, (short) 2, String.class));
            add(new FullExcelField("B065State", 14, (short) 2, String.class));
            add(new FullExcelField("B001State", 22, (short) 5, String.class));
            add(new FullExcelField("B009State", 15, (short) 2, String.class));
            add(new FullExcelField("B010State", 16, (short) 2, String.class));
            add(new FullExcelField("B011State", 17, (short) 2, String.class));
            add(new FullExcelField("B012State", 18, (short) 2, String.class));
            add(new FullExcelField("B015State", 19, (short) 2, String.class));
            add(new FullExcelField("B016State", 20, (short) 2, String.class));
            add(new FullExcelField("B017State", 21, (short) 2, String.class));
            add(new FullExcelField("B018State", 22, (short) 2, String.class));
            add(new FullExcelField("B037State", 21, (short) 5, String.class));
            add(new FullExcelField("B059State", 12, (short) 5, String.class));
            add(new FullExcelField("B060State", 13, (short) 5, String.class));
            add(new FullExcelField("B040State", 17, (short) 5, String.class));
            add(new FullExcelField("B041State", 18, (short) 5, String.class));
            add(new FullExcelField("B045State", 19, (short) 5, String.class));
            add(new FullExcelField("B046State", 20, (short) 5, String.class));
            add(new FullExcelField("B032State", 15, (short) 5, String.class));
            add(new FullExcelField("B033State", 16, (short) 5, String.class));
            add(new FullExcelField("B073State", 14, (short) 5, String.class));
            add(new FullExcelField("B074State", 14, (short) 5, String.class));
           /* add(new FullExcelField("B050State", 16, (short) 5, String.class));
            add(new FullExcelField("B132State", 14, (short) 5, String.class));*/
        }
    };

    public static Map<String, String> ymStr = new HashMap<>();
    static {
        ymStr.put("B001State", "卡介苗");
        ymStr.put("B009State", "脊灰疫苗1");
        ymStr.put("B010State", "脊灰疫苗2");
        ymStr.put("B011State", "脊灰疫苗3");
        ymStr.put("B012State", "脊灰疫苗4");
        ymStr.put("B015State", "百白破疫苗1");
        ymStr.put("B016State", "百白破疫苗2");
        ymStr.put("B017State", "百白破疫苗3");
        ymStr.put("B018State", "百白破疫苗4");
        ymStr.put("B032State", "乙脑疫苗1");
        ymStr.put("B033State", "乙脑疫苗2");
        ymStr.put("B037State", "白破疫苗1");
        ymStr.put("B038State", "白破疫苗2");
        ymStr.put("B039State", "白破疫苗3");
        ymStr.put("B040State", "A群流脑疫苗1");
        ymStr.put("B041State", "A群流脑疫苗2");
        ymStr.put("B045State", "A+C群流脑疫苗1");
        ymStr.put("B046State", "A+C群流脑疫苗2");
        ymStr.put("B059State", "麻腮风疫苗1");
        ymStr.put("B060State", "麻腮风疫苗2");
        ymStr.put("B063State", "乙肝疫苗1");
        ymStr.put("B064State", "乙肝疫苗2");
        ymStr.put("B065State", "乙肝疫苗3");
        ymStr.put("B073State", "甲肝灭活疫苗1");
        ymStr.put("B074State", "甲肝灭活疫苗2");
      /*  ymStr.put("B050State", "水痘1");
        ymStr.put("B132State", "水痘2");*/
    }

    public static Map<String, String> VACCIN_MAP = new HashMap<>();
    static {
        VACCIN_MAP.put("B001State", "D001");
        VACCIN_MAP.put("B009State", "D009");
        VACCIN_MAP.put("B010State", "D010");
        VACCIN_MAP.put("B011State", "D011");
        VACCIN_MAP.put("B012State", "D012");
        VACCIN_MAP.put("B015State", "D015");
        VACCIN_MAP.put("B016State", "D016");
        VACCIN_MAP.put("B017State", "D017");
        VACCIN_MAP.put("B018State", "D018");
        VACCIN_MAP.put("B032State", "D032");
        VACCIN_MAP.put("B033State", "D033");
        VACCIN_MAP.put("B037State", "D037");
        VACCIN_MAP.put("B038State", "D038");
        VACCIN_MAP.put("B039State", "D039");
        VACCIN_MAP.put("B040State", "D040");
        VACCIN_MAP.put("B041State", "D041");
        VACCIN_MAP.put("B045State", "D045");
        VACCIN_MAP.put("B046State", "D046");
        VACCIN_MAP.put("B059State", "D059");
        VACCIN_MAP.put("B060State", "D060");
        VACCIN_MAP.put("B063State", "D063");
        VACCIN_MAP.put("B064State", "D064");
        VACCIN_MAP.put("B065State", "D065");
        VACCIN_MAP.put("B073State", "D073");
        VACCIN_MAP.put("B074State", "D074");
      /*  ymStr.put("B050State", "水痘1");
        ymStr.put("B132State", "水痘2");*/
    }

    /**
     * 查验状态
     */
    public static Map<String, String> CHECK_STATE_MAP = new HashMap<>();
    static {
        //全部/已种全/未种全/未建档
        CHECK_STATE_MAP.put("0", "已种全");
        CHECK_STATE_MAP.put("1", "未种全需补种");
        CHECK_STATE_MAP.put("-1", "未种全无需补种");
        CHECK_STATE_MAP.put("2", "无接种证");
    }

    /**
     * 性别
     */
    public static Map<String, String> GENDER_MAP = new HashMap<>();
    static {
        GENDER_MAP.put("0", "女");
        GENDER_MAP.put("1", "男");
        GENDER_MAP.put("2", "未知");
    }

    /**
     * 户籍类型映射
     */
    public static Map<String, String> REGTYPE_MAP = new HashMap<>();
    static {
        //全部/已种全/未种全/未建档
        REGTYPE_MAP.put("1", "常住");
        REGTYPE_MAP.put("2", "县区内流动");
        REGTYPE_MAP.put("3", "地市内流动");
        REGTYPE_MAP.put("4", "省内流动");
        REGTYPE_MAP.put("5", "国内流动");
        REGTYPE_MAP.put("6", "国外流动");
    }

    /**
     * 年级编码名称映射
     */
    public static Map<String, String> GRADE_MAP = new HashMap<>();
    static {
        GRADE_MAP.put("0", "幼儿园托班");
        GRADE_MAP.put("1", "幼儿园小班");
        GRADE_MAP.put("2", "幼儿园中班");
        GRADE_MAP.put("3", "幼儿园大班");
        GRADE_MAP.put("4", "一年级");
        GRADE_MAP.put("5", "二年级");
        GRADE_MAP.put("6", "三年级");
        GRADE_MAP.put("7", "四年级");
        GRADE_MAP.put("8", "五年级");
        GRADE_MAP.put("9", "六年级");
    }

    /**
     * 轮次编码名称映射
     */
    public static Map<String, String> ROUND_MAP = new HashMap<>();
    static {
        ROUND_MAP.put("0", "秋季");
        ROUND_MAP.put("1", "春季");
    }

    /**
     * 学校编码名称映射
     */
    public static Map<String, String> SCHOOL_TYPE_MAP = new HashMap<>();
    static {
        SCHOOL_TYPE_MAP.put("0", "幼托机构");
        SCHOOL_TYPE_MAP.put("1", "小学");
    }

    /**
     * 扫码类型名称映射
     */
    public static Map<Integer, String> SCAN_CODE_TYPE_MAP = new HashMap<>();
    static {
        //1:儿童编码, 2:儿童条码, 3:身份证件号码, 4:儿童姓名、性别、出生日期
        SCAN_CODE_TYPE_MAP.put(1, "儿童编码");
        SCAN_CODE_TYPE_MAP.put(2, "儿童条码");
        SCAN_CODE_TYPE_MAP.put(3, "身份证件号码");
        SCAN_CODE_TYPE_MAP.put(4, "儿童姓名、性别、出生日期");
    }

}
