package com.shensu.service.check.util;


import com.shensu.service.check.util.model.EpiJzjl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RxrtUtil {
    public static List<EpiJzjl> getRxruList() {
        List<EpiJzjl> lst = new ArrayList<EpiJzjl>();

        lst.add(new EpiJzjl("乙肝疫苗(HepB)", "B063", "第1剂次", "-", "3"));
        lst.add(new EpiJzjl("乙肝疫苗(HepB)", "B064", "第2剂次", "-", null));
        lst.add(new EpiJzjl("乙肝疫苗(HepB)", "B065", "第3剂次", "-", null));
        lst.add(new EpiJzjl("卡介苗(BCG)", "B001", "第1剂次", "-", "1"));

        lst.add(new EpiJzjl("脊灰疫苗(PV)", "B009", "第1剂次", "-", "4"));
        lst.add(new EpiJzjl("脊灰疫苗(PV)", "B010", "第2剂次", "-", null));
        lst.add(new EpiJzjl("脊灰疫苗(PV)", "B011", "第3剂次", "-", null));
        lst.add(new EpiJzjl("脊灰疫苗(PV)", "B012", "第4剂次", "-", null));

        lst.add(new EpiJzjl("百白破疫苗(DPT)", "B015", "第1剂次", "-", "4"));
        lst.add(new EpiJzjl("百白破疫苗(DPT)", "B016", "第2剂次", "-", null));
        lst.add(new EpiJzjl("百白破疫苗(DPT)", "B017", "第3剂次", "-", null));
        lst.add(new EpiJzjl("百白破疫苗(DPT)", "B018", "第4剂次", "-", null));

        lst.add(new EpiJzjl("白破疫苗(DT)", "B037", "第1剂次", "-", "1"));

        lst.add(new EpiJzjl("麻腮风疫苗(MMR)", "B093", "第1剂次", "-", "2"));

        lst.add(new EpiJzjl("麻腮风疫苗(MMR)", "B089", "第2剂次", "-", null));

        lst.add(new EpiJzjl("乙脑减毒活疫苗(JE-l)", "B032", "第1剂次", "-", "2"));
        lst.add(new EpiJzjl("乙脑减毒活疫苗(JE-l)", "B033", "第2剂次", "-", null));

        lst.add(new EpiJzjl("A群流脑疫苗(MenA)", "B040", "第1剂次", "-", "2"));
        lst.add(new EpiJzjl("A群流脑疫苗(MenA)", "B041", "第2剂次", "-", null));

        lst.add(new EpiJzjl("A+C群流脑疫苗(MenAC)", "B045", "第1剂次", "-", "2"));
        lst.add(new EpiJzjl("A+C群流脑疫苗(MenAC)", "B046", "第2剂次", "-", null));

        lst.add(new EpiJzjl("甲肝灭活疫苗(HepA-I)", "B073", "第1剂次", "-", "2"));
        lst.add(new EpiJzjl("甲肝灭活疫苗(HepA-I)", "B074", "第2剂次", "-", null));


        lst.add(new EpiJzjl("水痘(VZV)", "B050", "第1剂次", "-", "2"));
        lst.add(new EpiJzjl("水痘(VZV)", "B132", "第2剂次", "-", null));
        return lst;
    }

    public static Map<String, String> getBactJcMonth() {
        Map<String, String> bactMonthMap = new HashMap<>();

        bactMonthMap.put("B050","12");
        bactMonthMap.put("B132","36");
        bactMonthMap.put("B169","2");
        bactMonthMap.put("B063","0");
        bactMonthMap.put("B064","1");
        bactMonthMap.put("B065","6");
        bactMonthMap.put("B001","0");
        bactMonthMap.put("B009","2");
        bactMonthMap.put("B010","3");
        bactMonthMap.put("B011","4");
        bactMonthMap.put("B012","48");
        bactMonthMap.put("B015","3");
        bactMonthMap.put("B016","4");
        bactMonthMap.put("B017","5");
        bactMonthMap.put("B018","18");
        bactMonthMap.put("B037","72");
        bactMonthMap.put("B093","8");
        bactMonthMap.put("B032","8");
        bactMonthMap.put("B033","24");
        bactMonthMap.put("B040","6");
        bactMonthMap.put("B041","9");
        bactMonthMap.put("B045","36");
        bactMonthMap.put("B046","72");
        bactMonthMap.put("B073","18");
        bactMonthMap.put("B074","24");
        bactMonthMap.put("B089","18");
        bactMonthMap.put("B094","18");

        return bactMonthMap;
    }
}
