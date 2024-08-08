package com.shensu.jsjm.model;

import java.io.Serializable;

import com.shensu.jsjm.util.CommonUtil;
import io.swagger.annotations.ApiModelProperty;

public class QualifiedBean implements Serializable {
    private static final long serialVersionUID = -7953822871671351047L;
    private String            year;

    private long              childCount;

    private long              sz_050;                                  //水痘1
    private long              sz_132;                                  //水痘2

    private long              sz_001;
    @ApiModelProperty("五连")
    private String            sz_rate_001      = "0.00";

    private long              sz_009;
    private String            sz_rate_009      = "0.00";

    private long              sz_010;
    private String            sz_rate_010      = "0.00";

    private long              sz_011;
    private String            sz_rate_011      = "0.00";

    private long              sz_012;
    private String            sz_rate_012      = "0.00";

    private long              sz_015;
    private String            sz_rate_015      = "0.00";

    private long              sz_016;
    private String            sz_rate_016      = "0.00";

    private long              sz_017;
    private String            sz_rate_017      = "0.00";

    private long              sz_018;
    private String            sz_rate_018      = "0.00";

    private long              sz_059;
    private String            sz_rate_059      = "0.00";

    private long              sz_060;
    private String            sz_rate_060      = "0.00";

    private long              sz_061;
    private String            sz_rate_061      = "0.00";

    private long              sz_063;
    private String            sz_rate_063      = "0.00";

    private long              sz_064;
    private String            sz_rate_064      = "0.00";

    private long              sz_065;
    private String            sz_rate_065      = "0.00";

    private long              sz_032;
    private String            sz_rate_032      = "0.00";

    private long              sz_033;
    private String            sz_rate_033      = "0.00";

    private long              sz_040;
    private String            sz_rate_040      = "0.00";

    private long              sz_041;
    private String            sz_rate_041      = "0.00";

    private long              sz_045;
    private String            sz_rate_045      = "0.00";

    private long              sz_046;
    private String            sz_rate_046      = "0.00";

    private long              sz_069;
    private String            sz_rate_069      = "0.00";

    private long              sz_037;
    private String            sz_rate_037      = "0.00";

    private long              sz_009_all;
    private String            sz_rate_009_all  = "0.00";

    private long              sz_015_all;
    private String            sz_rate_015_all  = "0.00";

    private long              sz_059_all;
    private String            sz_rate_059_all  = "0.00";

    private long              sz_063_all;
    private String            sz_rate_063_all  = "0.00";

    private long              sz_032_all;
    private String            sz_rate_032_all  = "0.00";

    private long              sz_040_all;
    private String            sz_rate_040_all  = "0.00";

    private long              sz_045_all;
    private String            sz_rate_045_all  = "0.00";

    private long              sz_five;
    private String            sz_rate_five     = "0.00";

    private long              sz_eight;
    private String            sz_rate_eight    = "0.00";

    public QualifiedBean() {
    }

    public QualifiedBean(int which) {
        this.sz_012 = 0;
        this.sz_018 = 0;
        this.sz_060 = 0;
        this.sz_061 = 0;
        this.sz_033 = 0;
        this.sz_045 = 0;
        this.sz_046 = 0;
        this.sz_069 = 0;
        this.sz_037 = 0;
        this.sz_045_all = 0;
        this.sz_eight = 0;
    }

    public void calcRate() {
        if (this.getChildCount() > 0) {
            this.setSz_rate_009(CommonUtil.subDecimal((double) this.getSz_009() / (double) this.childCount * 100));//脊灰 1
            this.setSz_rate_010(CommonUtil.subDecimal((double) this.getSz_010() / (double) this.childCount * 100));//脊灰 2
            this.setSz_rate_011(CommonUtil.subDecimal((double) this.getSz_011() / (double) this.childCount * 100));//脊灰 3
            if (this.getSz_012() >= 0) {
                this.setSz_rate_012(CommonUtil.subDecimal((double) this.getSz_012() / (double) this.childCount * 100));//脊灰 4
            }
            this.setSz_rate_015(CommonUtil.subDecimal((double) this.getSz_015() / (double) this.childCount * 100));//百白破 1
            this.setSz_rate_016(CommonUtil.subDecimal((double) this.getSz_016() / (double) this.childCount * 100));//百白破 2
            this.setSz_rate_017(CommonUtil.subDecimal((double) this.getSz_017() / (double) this.childCount * 100));//百白破 3
            if (this.getSz_018() >= 0) {
                this.setSz_rate_018(CommonUtil.subDecimal((double) this.getSz_018() / (double) this.childCount * 100));//百白破 4
            }
            this.setSz_rate_059(CommonUtil.subDecimal((double) this.getSz_059() / (double) this.childCount * 100));//麻疹 1
            if (this.getSz_060() >= 0) {
                this.setSz_rate_060(CommonUtil.subDecimal((double) this.getSz_060() / (double) this.childCount * 100));//麻疹 2
            }
            if (this.getSz_061() >= 0) {
                this.setSz_rate_061(CommonUtil.subDecimal((double) this.getSz_061() / (double) this.childCount * 100));//麻疹 3
            }
            this.setSz_rate_063(CommonUtil.subDecimal((double) this.getSz_063() / (double) this.childCount * 100));//乙肝 1
            this.setSz_rate_064(CommonUtil.subDecimal((double) this.getSz_064() / (double) this.childCount * 100));//乙肝 2
            this.setSz_rate_065(CommonUtil.subDecimal((double) this.getSz_065() / (double) this.childCount * 100));//乙肝 3
            this.setSz_rate_032(CommonUtil.subDecimal((double) this.getSz_032() / (double) this.childCount * 100));//乙脑减毒活 1
            if (this.getSz_033() >= 0) {
                this.setSz_rate_033(CommonUtil.subDecimal((double) this.getSz_033() / (double) this.childCount * 100));//乙脑减毒活 2
            }
            this.setSz_rate_040(CommonUtil.subDecimal((double) this.getSz_040() / (double) this.childCount * 100));//A群流脑 1
            this.setSz_rate_041(CommonUtil.subDecimal((double) this.getSz_041() / (double) this.childCount * 100));//A群流脑 2
            if (this.getSz_045() >= 0) {
                this.setSz_rate_045(CommonUtil.subDecimal((double) this.getSz_045() / (double) this.childCount * 100));//A+C群流脑 1
            }
            if (this.getSz_046() >= 0) {
                this.setSz_rate_046(CommonUtil.subDecimal((double) this.getSz_046() / (double) this.childCount * 100));//A+C群流脑 2
            }
            this.setSz_rate_001(CommonUtil.subDecimal((double) this.getSz_001() / (double) this.childCount * 100));//卡介
            if (this.getSz_069() >= 0) {
                this.setSz_rate_069(CommonUtil.subDecimal((double) this.getSz_069() / (double) this.childCount * 100));//甲肝减毒
            }
            if (this.getSz_037() >= 0) {
                this.setSz_rate_037(CommonUtil.subDecimal((double) this.getSz_037() / (double) this.childCount * 100));//白破
            }
            this.setSz_rate_five(CommonUtil.subDecimal((double) this.getSz_five() / (double) this.childCount * 100));//五苗
            if (this.getSz_eight() >= 0) {
                this.setSz_rate_eight(CommonUtil.subDecimal((double) this.getSz_eight() / (double) this.childCount * 100));//八苗
            }
            this.setSz_rate_009_all(CommonUtil.subDecimal((double) this.getSz_009_all() / (double) this.childCount * 100));//脊灰
            this.setSz_rate_015_all(CommonUtil.subDecimal((double) this.getSz_015_all() / (double) this.childCount * 100));//百白破
            this.setSz_rate_032_all(CommonUtil.subDecimal((double) this.getSz_032_all() / (double) this.childCount * 100));//乙脑减毒活
            this.setSz_rate_040_all(CommonUtil.subDecimal((double) this.getSz_040_all() / (double) this.childCount * 100));//A群流脑
            if (this.getSz_045_all() >= 0) {
                this.setSz_rate_045_all(CommonUtil.subDecimal((double) this.getSz_045_all() / (double) this.childCount * 100));//A+C群流脑
            }
            this.setSz_rate_059_all(CommonUtil.subDecimal((double) this.getSz_059_all() / (double) this.childCount * 100));//麻疹
            this.setSz_rate_063_all(CommonUtil.subDecimal((double) this.getSz_063_all() / (double) this.childCount * 100));//乙肝
        }
    }

    public long getChildCount() {
        return childCount;
    }

    public void setChildCount(long childCount) {
        this.childCount = childCount;
    }

    public long getSz_001() {
        return sz_001;
    }

    public void setSz_001(long sz_001) {
        this.sz_001 = sz_001;
    }

    public String getSz_rate_001() {
        return sz_rate_001;
    }

    public void setSz_rate_001(String sz_rate_001) {
        this.sz_rate_001 = sz_rate_001;
    }

    public long getSz_009() {
        return sz_009;
    }

    public void setSz_009(long sz_009) {
        this.sz_009 = sz_009;
    }

    public String getSz_rate_009() {
        return sz_rate_009;
    }

    public void setSz_rate_009(String sz_rate_009) {
        this.sz_rate_009 = sz_rate_009;
    }

    public long getSz_010() {
        return sz_010;
    }

    public void setSz_010(long sz_010) {
        this.sz_010 = sz_010;
    }

    public String getSz_rate_010() {
        return sz_rate_010;
    }

    public void setSz_rate_010(String sz_rate_010) {
        this.sz_rate_010 = sz_rate_010;
    }

    public long getSz_011() {
        return sz_011;
    }

    public void setSz_011(long sz_011) {
        this.sz_011 = sz_011;
    }

    public String getSz_rate_011() {
        return sz_rate_011;
    }

    public void setSz_rate_011(String sz_rate_011) {
        this.sz_rate_011 = sz_rate_011;
    }

    public long getSz_012() {
        return sz_012;
    }

    public void setSz_012(long sz_012) {
        this.sz_012 = sz_012;
    }

    public String getSz_rate_012() {
        return sz_rate_012;
    }

    public void setSz_rate_012(String sz_rate_012) {
        this.sz_rate_012 = sz_rate_012;
    }

    public long getSz_015() {
        return sz_015;
    }

    public void setSz_015(long sz_015) {
        this.sz_015 = sz_015;
    }

    public String getSz_rate_015() {
        return sz_rate_015;
    }

    public void setSz_rate_015(String sz_rate_015) {
        this.sz_rate_015 = sz_rate_015;
    }

    public long getSz_016() {
        return sz_016;
    }

    public void setSz_016(long sz_016) {
        this.sz_016 = sz_016;
    }

    public String getSz_rate_016() {
        return sz_rate_016;
    }

    public void setSz_rate_016(String sz_rate_016) {
        this.sz_rate_016 = sz_rate_016;
    }

    public long getSz_017() {
        return sz_017;
    }

    public void setSz_017(long sz_017) {
        this.sz_017 = sz_017;
    }

    public String getSz_rate_017() {
        return sz_rate_017;
    }

    public void setSz_rate_017(String sz_rate_017) {
        this.sz_rate_017 = sz_rate_017;
    }

    public long getSz_018() {
        return sz_018;
    }

    public void setSz_018(long sz_018) {
        this.sz_018 = sz_018;
    }

    public String getSz_rate_018() {
        return sz_rate_018;
    }

    public void setSz_rate_018(String sz_rate_018) {
        this.sz_rate_018 = sz_rate_018;
    }

    public long getSz_059() {
        return sz_059;
    }

    public void setSz_059(long sz_059) {
        this.sz_059 = sz_059;
    }

    public String getSz_rate_059() {
        return sz_rate_059;
    }

    public void setSz_rate_059(String sz_rate_059) {
        this.sz_rate_059 = sz_rate_059;
    }

    public long getSz_060() {
        return sz_060;
    }

    public void setSz_060(long sz_060) {
        this.sz_060 = sz_060;
    }

    public String getSz_rate_060() {
        return sz_rate_060;
    }

    public void setSz_rate_060(String sz_rate_060) {
        this.sz_rate_060 = sz_rate_060;
    }

    public long getSz_061() {
        return sz_061;
    }

    public void setSz_061(long sz_061) {
        this.sz_061 = sz_061;
    }

    public String getSz_rate_061() {
        return sz_rate_061;
    }

    public void setSz_rate_061(String sz_rate_061) {
        this.sz_rate_061 = sz_rate_061;
    }

    public long getSz_063() {
        return sz_063;
    }

    public void setSz_063(long sz_063) {
        this.sz_063 = sz_063;
    }

    public String getSz_rate_063() {
        return sz_rate_063;
    }

    public void setSz_rate_063(String sz_rate_063) {
        this.sz_rate_063 = sz_rate_063;
    }


    public long getSz_064() {
        return sz_064;
    }

    public void setSz_064(long sz_064) {
        this.sz_064 = sz_064;
    }

    public String getSz_rate_064() {
        return sz_rate_064;
    }

    public void setSz_rate_064(String sz_rate_064) {
        this.sz_rate_064 = sz_rate_064;
    }

    public long getSz_065() {
        return sz_065;
    }

    public void setSz_065(long sz_065) {
        this.sz_065 = sz_065;
    }

    public String getSz_rate_065() {
        return sz_rate_065;
    }

    public void setSz_rate_065(String sz_rate_065) {
        this.sz_rate_065 = sz_rate_065;
    }

    public long getSz_032() {
        return sz_032;
    }

    public void setSz_032(long sz_032) {
        this.sz_032 = sz_032;
    }

    public String getSz_rate_032() {
        return sz_rate_032;
    }

    public void setSz_rate_032(String sz_rate_032) {
        this.sz_rate_032 = sz_rate_032;
    }

    public long getSz_033() {
        return sz_033;
    }

    public void setSz_033(long sz_033) {
        this.sz_033 = sz_033;
    }

    public String getSz_rate_033() {
        return sz_rate_033;
    }

    public void setSz_rate_033(String sz_rate_033) {
        this.sz_rate_033 = sz_rate_033;
    }

    public long getSz_040() {
        return sz_040;
    }

    public void setSz_040(long sz_040) {
        this.sz_040 = sz_040;
    }

    public String getSz_rate_040() {
        return sz_rate_040;
    }

    public void setSz_rate_040(String sz_rate_040) {
        this.sz_rate_040 = sz_rate_040;
    }

    public long getSz_041() {
        return sz_041;
    }

    public void setSz_041(long sz_041) {
        this.sz_041 = sz_041;
    }

    public String getSz_rate_041() {
        return sz_rate_041;
    }

    public void setSz_rate_041(String sz_rate_041) {
        this.sz_rate_041 = sz_rate_041;
    }
    public long getSz_045() {
        return sz_045;
    }

    public void setSz_045(long sz_045) {
        this.sz_045 = sz_045;
    }

    public String getSz_rate_045() {
        return sz_rate_045;
    }

    public void setSz_rate_045(String sz_rate_045) {
        this.sz_rate_045 = sz_rate_045;
    }


    public long getSz_046() {
        return sz_046;
    }

    public void setSz_046(long sz_046) {
        this.sz_046 = sz_046;
    }

    public String getSz_rate_046() {
        return sz_rate_046;
    }

    public void setSz_rate_046(String sz_rate_046) {
        this.sz_rate_046 = sz_rate_046;
    }


    public long getSz_069() {
        return sz_069;
    }

    public void setSz_069(long sz_069) {
        this.sz_069 = sz_069;
    }


    public String getSz_rate_069() {
        return sz_rate_069;
    }

    public void setSz_rate_069(String sz_rate_069) {
        this.sz_rate_069 = sz_rate_069;
    }

    public long getSz_037() {
        return sz_037;
    }

    public void setSz_037(long sz_037) {
        this.sz_037 = sz_037;
    }

    public String getSz_rate_037() {
        return sz_rate_037;
    }

    public void setSz_rate_037(String sz_rate_037) {
        this.sz_rate_037 = sz_rate_037;
    }

    public long getSz_009_all() {
        return sz_009_all;
    }

    public void setSz_009_all(long sz_009_all) {
        this.sz_009_all = sz_009_all;
    }

    public String getSz_rate_009_all() {
        return sz_rate_009_all;
    }

    public void setSz_rate_009_all(String sz_rate_009_all) {
        this.sz_rate_009_all = sz_rate_009_all;
    }

    public long getSz_015_all() {
        return sz_015_all;
    }

    public void setSz_015_all(long sz_015_all) {
        this.sz_015_all = sz_015_all;
    }

    public String getSz_rate_015_all() {
        return sz_rate_015_all;
    }

    public void setSz_rate_015_all(String sz_rate_015_all) {
        this.sz_rate_015_all = sz_rate_015_all;
    }

    public long getSz_059_all() {
        return sz_059_all;
    }

    public void setSz_059_all(long sz_059_all) {
        this.sz_059_all = sz_059_all;
    }

    public String getSz_rate_059_all() {
        return sz_rate_059_all;
    }

    public void setSz_rate_059_all(String sz_rate_059_all) {
        this.sz_rate_059_all = sz_rate_059_all;
    }

    public long getSz_063_all() {
        return sz_063_all;
    }

    public void setSz_063_all(long sz_063_all) {
        this.sz_063_all = sz_063_all;
    }

    public String getSz_rate_063_all() {
        return sz_rate_063_all;
    }

    public void setSz_rate_063_all(String sz_rate_063_all) {
        this.sz_rate_063_all = sz_rate_063_all;
    }

    public long getSz_032_all() {
        return sz_032_all;
    }

    public void setSz_032_all(long sz_032_all) {
        this.sz_032_all = sz_032_all;
    }

    public String getSz_rate_032_all() {
        return sz_rate_032_all;
    }

    public void setSz_rate_032_all(String sz_rate_032_all) {
        this.sz_rate_032_all = sz_rate_032_all;
    }

    public long getSz_040_all() {
        return sz_040_all;
    }

    public void setSz_040_all(long sz_040_all) {
        this.sz_040_all = sz_040_all;
    }

    public String getSz_rate_040_all() {
        return sz_rate_040_all;
    }

    public void setSz_rate_040_all(String sz_rate_040_all) {
        this.sz_rate_040_all = sz_rate_040_all;
    }

    public long getSz_045_all() {
        return sz_045_all;
    }

    public void setSz_045_all(long sz_045_all) {
        this.sz_045_all = sz_045_all;
    }

    public String getSz_rate_045_all() {
        return sz_rate_045_all;
    }

    public void setSz_rate_045_all(String sz_rate_045_all) {
        this.sz_rate_045_all = sz_rate_045_all;
    }

    public long getSz_five() {
        return sz_five;
    }

    public void setSz_five(long sz_five) {
        this.sz_five = sz_five;
    }

    public String getSz_rate_five() {
        return sz_rate_five;
    }

    public void setSz_rate_five(String sz_rate_five) {
        this.sz_rate_five = sz_rate_five;
    }

    public long getSz_eight() {
        return sz_eight;
    }

    public void setSz_eight(long sz_eight) {
        this.sz_eight = sz_eight;
    }

    public String getSz_rate_eight() {
        return sz_rate_eight;
    }

    public void setSz_rate_eight(String sz_rate_eight) {
        this.sz_rate_eight = sz_rate_eight;
    }

    public long getSz_050() {
        return sz_050;
    }

    public void setSz_050(long sz_050) {
        this.sz_050 = sz_050;
    }

    public long getSz_132() {
        return sz_132;
    }

    public void setSz_132(long sz_132) {
        this.sz_132 = sz_132;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
