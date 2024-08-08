package com.shensu.service.check.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shensu.service.check.util.model.EpiJzjl;
import com.shensu.service.check.util.model.ResponseBean;
import com.shensu.service.check.util.model.RxrtChild;
import com.shensu.service.check.util.model.RxrtJzjl;
import com.shensu.util.Util;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class RxrtService {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ResponseBean getChildRxrtToPt(JSONObject childInfo) {
        RxrtChild rxrtChild=new RxrtChild();
        rxrtChild.setName(childInfo.getString("name"));
        List<EpiJzjl> jzjlList = this.getHNChildJzjlCheckList(childInfo);
        Map<String, Object> result = new HashMap<>();
        // boolean isPassCheck = true;
        //isPassCheck 现在分为三种状态  ：已种全：0   未种全不需补种-1   未种全需补种1
        String  isPassCheck="";
        int one =0;
        int two =0;
        int three=0;

        Iterator<EpiJzjl> jzjlIterator = jzjlList.iterator();
        while (jzjlIterator.hasNext()) {
            EpiJzjl epiJzjl = jzjlIterator.next();
            if ("B050".equals(epiJzjl.getFbactid()) || "B132".equals(epiJzjl.getFbactid())) {
                continue;
            }
            String sd = epiJzjl.getShotdate();
            if ("到期未种".equals(sd) || "不详".equals(sd)
                    || "父亲/母亲/监护人要求不种".equals(sd) || "未接种".equals(sd)) {
                one++;
            }else if("禁忌".equals(sd)||"超期不种".equals(sd)||"已患".equals(sd)){
                two++;
            }else{
                three++;
            }
        }
        if(one>0){
            isPassCheck="1";
        }else{
            if(two>0){
                isPassCheck="-1";
            }else if(three>0){
                isPassCheck="0";
            }
        }

        result.put("isPassCheck", isPassCheck);
        result.put("jzjlList", jzjlList);
        result.put("name", rxrtChild.getName());
        result.put("imuno", rxrtChild.getImuno());
        return new ResponseBean(0, "获取成功！", result);
    }

    private List<EpiJzjl> getHNChildJzjlCheckList(JSONObject childInfo){
        List<EpiJzjl> lst = RxrtUtil.getRxruList();
        //jzjlMap：存在接种日期的集合
        Map<String, RxrtJzjl> jzjlMap =new HashMap<>();
        //获取接种记录
        JSONArray jzjlList=childInfo.getJSONArray("jzjlList");
        for(int i=0;i<jzjlList.size();i++) {
            JSONObject jzjl=jzjlList.getJSONObject(i);
            //获取剂次名称
            String fbactid = jzjl.getString("fbactid");
            //获取剂次时间
            String fshotdate = jzjl.getString("jzrq");
            //如果接种日期不是空，并且接种日期的长度是xxxx-xx-xx长度为10
            if(StringUtils.isNotBlank(fshotdate) && fshotdate.length()==10) {
                RxrtJzjl epiJzjl = new RxrtJzjl();
                epiJzjl.setJzrq(fshotdate);
                epiJzjl.setFbactid(fbactid);
                jzjlMap.put(fbactid, epiJzjl);
            }
        }
        //乙脑减毒
        String [] ynArray=new String[]{
                "B032",
                "B033",
                "B034",
                "B035",
                "B036"
        };
        //将不符合时间要求的疫苗信息删除掉，并且把符合要求的从B032开始放  主苗
        setNewJzjl(jzjlMap,ynArray,false);

        //将不符合时间要求的疫苗信息删除掉，并且把符合要求的从B027开始放 替代苗
        String [] ynmhArray=new String[]{
                "B027",
                "B028",
                "B029",
                "B030",
                "B031"
        };
        setNewJzjl(jzjlMap,ynmhArray,true);

        /*
         如果都接种了 乙脑减毒第一剂次和灭活第一剂次都接种了
        * */
        if(jzjlMap.containsKey("B032")&&jzjlMap.containsKey("B027")){
            List<RxrtJzjl> ynmhList=new ArrayList<>();
            //把接种记录中的灭火剂次都删除，并且把已经接种的灭火剂次保存
            for(String jc:ynmhArray){
                if(jzjlMap.containsKey(jc)){
                    ynmhList.add(jzjlMap.get(jc));
                    jzjlMap.remove(jc);
                }
            }
            //如果
            if(jzjlMap.get("B032").getJzrq().compareTo(ynmhList.get(0).getJzrq())<=0){
                jzjlMap.put("B029",ynmhList.get(0));
                if(ynmhList.size()>1) {
                    jzjlMap.put("B030", ynmhList.get(1));
                }
            }else{
                if(ynmhList.size()>1){
                    jzjlMap.put("B027",ynmhList.get(0));
                    jzjlMap.put("B028", ynmhList.get(1));
                    jzjlMap.put("B033",jzjlMap.get("B032"));
                    jzjlMap.remove("B032");
                }else{
                    jzjlMap.put("B027",ynmhList.get(0));
                }
            }
        }
        //甲肝活苗
        String [] jgArray=new String[]{
                "B069",
                "B070",
                "B071",
                "B072"
        };
        setNewJzjl(jzjlMap,jgArray,true);
      //灭活活苗
        String [] jgmhArray=new String[]{
                "B073",
                "B074",
                "B075",
                "B076"
        };
        setNewJzjl(jzjlMap,jgmhArray,false);

        RxrtChild rxrtChild = new RxrtChild();
        rxrtChild.setName(childInfo.getString("name"));
        rxrtChild.setBirth(childInfo.getString("birth"));
        rxrtChild.setCityCode(childInfo.getString("330200000000"));
        try {
            this.getChildRXRT(rxrtChild,lst,jzjlMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }

     //jzjlMap:接种信息      jcArray:剂次信息

    private void setNewJzjl(Map<String, RxrtJzjl> jzjlMap,String [] jcArray,boolean isTd){
        List<RxrtJzjl> jcJzjlList=new ArrayList<>(5);
        for(String jc:jcArray){
            //如果用户接种的剂次中包含了jcArray中的剂次信息
            if(jzjlMap.containsKey(jc)){
                RxrtJzjl jzrq=jzjlMap.get(jc);
                if(isTd){
                    //如果接种日期比1987年大
                    if("1987-01-01".compareTo(jzrq.getJzrq())<=0){
                        jcJzjlList.add(jzrq);
                    }
                }else {
                    if("1981-01-01".compareTo(jzrq.getJzrq())<=0){
                        jcJzjlList.add(jzrq);
                    }
                }

                jzjlMap.remove(jc);
            }
        }
        int i=0;
        for(RxrtJzjl jcJz:jcJzjlList){
            jzjlMap.put(jcArray[i++],jcJz);
        }
    }

    private void setYmString(Map<String, RxrtJzjl> jzjlMap, String[] ymArray, List<String>... ymList) {
        for (String s : ymArray) {
            RxrtJzjl o1 = jzjlMap.get(s);
            if (o1 != null && StringUtils.isNotBlank(o1.getJzrq()) && (o1.getJzrq().startsWith("1985")||o1.getJzrq().compareTo("1987-01-01")>0)) {
                for (List<String> ym : ymList) {
                    ym.add(o1.getJzrq());
                }
            }
        }
    }

    private String checkJzjlMonth(RxrtChild rxrtChild, String month) {
        if(rxrtChild==null){
            return "到期未种";
        }
        String birth = rxrtChild.getBirth();
        Calendar now = Calendar.getInstance();
        if (StringUtils.isNotBlank(month)) {
            now.add(Calendar.MONTH, 0 - Integer.valueOf(month));
        }
        if (this.dateFormat.format(now.getTime()).compareTo(birth) < 0) {
            return "未到接种时间";
        }
        return "到期未种";
    }

    private String maxString(String... datas) {
        String maxString = "";
        for (String data : datas) {
            if (data != null) {
                if (maxString.compareTo(data) < 0) {
                    maxString = data;
                }
            }
        }
        return maxString;
    }

    private void getChildRXRT(RxrtChild rxrtChild,List<EpiJzjl> lst,Map<String, RxrtJzjl> jzjlMap) throws Exception {
        //额外特殊判断的城市
        String checkCityCode="330200000000";
        //每个疫苗首次接种时间
        Map<String, String> bactMonthMap = RxrtUtil.getBactJcMonth();
        //有替代疫苗的四个剂次
        List<String> jhList = new ArrayList<String>();
        List<String> bbpList = new ArrayList<String>();
        List<String> acHib = new ArrayList<String>();
        TreeSet<String> mz = new TreeSet<>();


        String[] wulian = new String[]{"B163", "B164", "B165", "B166"};
        this.setYmString(jzjlMap, wulian, jhList, bbpList);


        TreeSet<String> jh = new TreeSet<>();
        jh.addAll(jhList);
        String[] silian = new String[]{"B157", "B158", "B159", "B160"};
        this.setYmString(jzjlMap, silian, bbpList);


        TreeSet<String> bbp = new TreeSet<>();
        bbp.addAll(bbpList);

        String[] acHibYm = new String[]{"B427", "B428", "B429"};
        this.setYmString(jzjlMap, acHibYm, acHib);

        String[] mzYm = new String[]{"B093", "B094", "B089", "B090", "B059", "B060", "B096", "B097"};
        int specialDateCout=1;

        int aQunNum=2;
        //处理下138 139 140 141 需要
        List<String> ac = new ArrayList<String>();
        String[] acYm = new String[]{"B138", "B139", "B140","B141"};
        this.setYmString(jzjlMap, acYm, ac);
        TreeSet<String> acComb = new TreeSet<>();
        acComb.addAll(ac);


        for (String s : mzYm) {
            RxrtJzjl o1 = jzjlMap.get(s);
            if (o1 != null && StringUtils.isNotBlank(o1.getJzrq()) && !(o1.getJzrq().startsWith("1980")
                    || o1.getJzrq().startsWith("1981") || o1.getJzrq().startsWith("1982"))) {
                if(o1.getJzrq().startsWith("1983")
                        || o1.getJzrq().startsWith("1984") || o1.getJzrq().startsWith("1985")|| o1.getJzrq().startsWith("1986")){
                    if(specialDateCout<10){
                        o1.setJzrq(o1.getJzrq().substring(0,o1.getJzrq().length()-1)+(specialDateCout++));
                    }else{
                        o1.setJzrq(o1.getJzrq().substring(0,o1.getJzrq().length()-2)+(specialDateCout++));
                    }
                }
                mz.add(o1.getJzrq());
            }
        }
        Map<String, String> jzrqMap = new HashMap<>();

        List<String> jhbpList=Arrays.asList("B009","B010","B011","B012","B015","B016","B017","B018");
        int dtapShotNum=0;
        String maxDtapShotTime=null;
        //循环处理疫苗进行查验
        for (int jzjlIndex =0;jzjlIndex<lst.size();jzjlIndex++) {
            EpiJzjl jzjl =lst.get(jzjlIndex);
            String shotDateShowResult = null;
            Calendar now = Calendar.getInstance();
            String fbactId = jzjl.getFbactid();
            //处理麻疹类的疫苗  分为2019年10月1日 之前出生的和2019年之后出生的
            if(rxrtChild.getBirth().compareTo("2019-10-01")>=0){
                //如果大于2019年10-01的用户必须判断两针都为麻疹疫苗
                if("B093".equals(fbactId)){
                    //看看麻腮风第一剂次接种信息
                    RxrtJzjl B089 = jzjlMap.get("B089");
                    if (B089 != null  && StringUtils.isNotBlank(B089.getJzrq())) {
                        shotDateShowResult=B089.getJzrq();
                        jzjl.setShotdate(B089.getJzrq());
                    } else {
                        shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                    }
                }
                if("B089".equals(fbactId)){
                    RxrtJzjl B090 = jzjlMap.get("B090");
                    if (B090 != null  && StringUtils.isNotBlank(B090.getJzrq())) {
                        shotDateShowResult=B090.getJzrq();
                        jzjl.setShotdate(B090.getJzrq());
                    }else {
                        RxrtJzjl B093 = jzjlMap.get("B089");
                        if (B093 != null && StringUtils.isNotBlank(B093.getJzrq())) {
                            now.add(Calendar.MONTH, -1);
                            if (this.dateFormat.format(now.getTime()).compareTo(B093.getJzrq()) <= 0) {
                                shotDateShowResult = "未到接种时间";
                            } else {
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                            }
                        } else {
                            shotDateShowResult = "未到接种时间";
                        }
                    }
                }
            }else{
                if ("B093".equals(fbactId)) {
                    if (!mz.isEmpty()) {
                        shotDateShowResult = mz.pollFirst();
                        jzrqMap.put("B093", shotDateShowResult);
                    } else {
                        shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                    }
                }
                if ("B089".equals(fbactId)) {
                    if (!mz.isEmpty()) {
                        shotDateShowResult = mz.pollFirst();
                    } else {
                        String jzrq = jzrqMap.get("B093");
                        if (StringUtils.isNotBlank(jzrq)) {
                            now.add(Calendar.MONTH, -1);
                            if (this.dateFormat.format(now.getTime()).compareTo(jzrq) <= 0) {
                                shotDateShowResult = "未到接种时间";
                            } else {
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                            }
                        } else {
                            shotDateShowResult = "未到接种时间";
                        }
                    }
                }
            }

            RxrtJzjl jzjlShow = jzjlMap.get(fbactId);
            String nJzrq = shotDateShowResult;
            if (nJzrq == null && jzjlShow != null && StringUtils.isNotBlank(jzjlShow.getJzrq())) {
                nJzrq = jzjlShow.getJzrq();
                jzrqMap.put(fbactId, nJzrq);
            }
            if (nJzrq != null) {
                if (nJzrq != null) {
                    if (nJzrq.indexOf("1980") != -1) {
                        nJzrq = "已种替代疫苗";
                    }else if (nJzrq.indexOf("1981") != -1){
                        nJzrq = "不详";
                    }else if (nJzrq.indexOf("1982") != -1){
                        nJzrq = "父亲/母亲/监护人要求不种";
                    }else if (nJzrq.indexOf("1983") != -1){
                        nJzrq = "禁忌";
                    }else if (nJzrq.indexOf("1984") != -1){
                        nJzrq = "超期不种";
                    }else if (nJzrq.indexOf("1985") != -1){
                        nJzrq = "已种但时间未知";
                    }else if (nJzrq.indexOf("1986") != -1){
                        nJzrq = "已患";
                    }
                    jzjl.setShotdate(nJzrq);
                }
            }
            if (StringUtils.isNotBlank(jzjl.getShotdate())) {
                if("B052".equals(fbactId)||"B051".equals(fbactId)){
                    if("到期未种".equals(shotDateShowResult) || "未到接种时间".equals(shotDateShowResult)
                            ||"不详".equals(jzjl.getShotdate()) || "父亲/母亲/监护人要求不种".equals(jzjl.getShotdate())){
                        Calendar now1=Calendar.getInstance();
                        now1.add(Calendar.YEAR, -2);
                        if (this.dateFormat.format(now1.getTime()).compareTo(rxrtChild.getBirth()) > 0) {
                            shotDateShowResult="超期不种";
                            jzjl.setShotdate(shotDateShowResult);
                        }
                    }
                }

                if (jzjl.getShotdate().indexOf("-") > 1 || shotDateShowResult != null) {
                    continue;
                }
                if ("已种替代疫苗".equals(jzjl.getShotdate())){
                    if(!jhbpList.contains(fbactId)){
                        continue;
                    }
                    jzjl.setShotdate(null);
                    shotDateShowResult=null;
                    jzrqMap.remove(fbactId);
                }
            }

            if ("B001".equals(fbactId) || "B063".equals(fbactId)) {
                //卡介苗1 乙肝1
                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
            } else if ("B064".equals(fbactId)) {
                //乙肝2
                RxrtJzjl b063 = jzjlMap.get("B063");
                if (b063 != null && StringUtils.isNotBlank(b063.getJzrq())) {
                    now.add(Calendar.MONTH, -1);
                    if (this.dateFormat.format(now.getTime()).compareTo(b063.getJzrq()) <= 0) {
                        shotDateShowResult = "未到接种时间";
                    } else {
                        shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                    }
                } else {
                    shotDateShowResult = "未到接种时间";
                }
            } else if ("B065".equals(fbactId)) {
                //乙肝3
                RxrtJzjl b064 = jzjlMap.get("B064");
                if (b064 != null && StringUtils.isNotBlank(b064.getJzrq())) {
                    now.add(Calendar.MONTH, -2);
                    if (this.dateFormat.format(now.getTime()).compareTo(b064.getJzrq()) <= 0) {
                        shotDateShowResult = "未到接种时间";
                    } else {
                        shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                    }
                } else {
                    shotDateShowResult = "未到接种时间";
                }
            } else if ("B009".equals(fbactId)) {
                //脊灰1
                RxrtJzjl B169 = jzjlMap.get("B169");
                if (B169 != null && StringUtils.isNotBlank(B169.getJzrq()) && (B169.getJzrq().startsWith("1985")||B169.getJzrq().compareTo("1987-01-01")>0)) {
                    shotDateShowResult = B169.getJzrq();
                    jzrqMap.put("B009", B169.getJzrq());
                } else {
                    if (!jh.isEmpty()) {
                        shotDateShowResult = jh.pollFirst();
                        jzrqMap.put("B009", shotDateShowResult);
                    } else {
                        shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                    }
                }

            } else if ("B010".equals(fbactId)) {
                //脊灰2
                RxrtJzjl B170 = jzjlMap.get("B170");
                if (B170 != null && StringUtils.isNotBlank(B170.getJzrq()) && (B170.getJzrq().startsWith("1985")||B170.getJzrq().compareTo("1987-01-01")>0)) {
                    shotDateShowResult = B170.getJzrq();
                    jzrqMap.put("B010", shotDateShowResult);
                } else {
                    if (!jh.isEmpty()) {
                        shotDateShowResult = jh.pollFirst();
                        jzrqMap.put("B010", shotDateShowResult);
                    } else {
                        String B009Jzrq = jzrqMap.get("B009");
                        if (StringUtils.isNotBlank(B009Jzrq)) {
                            now.add(Calendar.MONTH, -1);
                            if (this.dateFormat.format(now.getTime()).compareTo(B009Jzrq) <= 0) {
                                shotDateShowResult = "未到接种时间";
                            } else {
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                            }
                        } else {
                            shotDateShowResult = "未到接种时间";
                        }
                    }
                }
            } else if ("B011".equals(fbactId)) {
                //脊灰3
                RxrtJzjl B171 = jzjlMap.get("B171");
                if (B171 != null && StringUtils.isNotBlank(B171.getJzrq()) && (B171.getJzrq().startsWith("1985")||B171.getJzrq().compareTo("1987-01-01")>0)) {
                    shotDateShowResult = B171.getJzrq();
                    jzrqMap.put("B011", shotDateShowResult);
                } else {
                    if (!jh.isEmpty()) {
                        shotDateShowResult = jh.pollFirst();
                        jzrqMap.put("B011", shotDateShowResult);
                    } else {
                        String B010Jzrq = jzrqMap.get("B010");
                        if (StringUtils.isNotBlank(B010Jzrq)) {
                            now.add(Calendar.MONTH, -1);
                            if (this.dateFormat.format(now.getTime()).compareTo(B010Jzrq) <= 0) {
                                shotDateShowResult = "未到接种时间";
                            } else {
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                            }
                        } else {
                            shotDateShowResult = "未到接种时间";
                        }

                    }
                }
            } else if ("B012".equals(fbactId)) {
                //脊灰4
                RxrtJzjl B172 = jzjlMap.get("B172");
                if (B172 != null && StringUtils.isNotBlank(B172.getJzrq()) && (B172.getJzrq().startsWith("1985")||B172.getJzrq().compareTo("1987-01-01")>0)) {
                    shotDateShowResult = B172.getJzrq();
                    jzrqMap.put("B012", shotDateShowResult);
                } else {
                    if (!jh.isEmpty()) {
                        shotDateShowResult = jh.pollFirst();
                        jzrqMap.put("B012", shotDateShowResult);
                    } else {
                        String B011Jzrq = jzrqMap.get("B011");
                        if (StringUtils.isNotBlank(B011Jzrq)) {
                            now.add(Calendar.MONTH, -1);
                            if (this.dateFormat.format(now.getTime()).compareTo(B011Jzrq) <= 0) {
                                shotDateShowResult = "未到接种时间";
                            } else {
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                            }
                        } else {
                            shotDateShowResult = "未到接种时间";
                        }
                    }
                }
            } else if ("B015".equals(fbactId)) {
                //百白破1
                if (!bbp.isEmpty()) {
                    shotDateShowResult = bbp.pollFirst();
                    jzrqMap.put("B015", shotDateShowResult);
                } else {
                    shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                }
            } else if ("B016".equals(fbactId)) {
                //百白破2
                if (!bbp.isEmpty()) {
                    shotDateShowResult = bbp.pollFirst();
                    jzrqMap.put("B016", shotDateShowResult);
                } else {
                    String B015Jzrq = jzrqMap.get("B015");
                    if (StringUtils.isNotBlank(B015Jzrq)) {
                        now.add(Calendar.MONTH, -1);
                        if (this.dateFormat.format(now.getTime()).compareTo(B015Jzrq) <= 0) {
                            shotDateShowResult = "未到接种时间";
                        } else {
                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                        }
                    } else {
                        shotDateShowResult = "未到接种时间";
                    }
                }
            } else if ("B017".equals(fbactId)) {
                //百白破3
                if (!bbp.isEmpty()) {
                    shotDateShowResult = bbp.pollFirst();
                    jzrqMap.put("B017", shotDateShowResult);
                } else {
                    String B016Jzrq = jzrqMap.get("B016");
                    if (StringUtils.isNotBlank(B016Jzrq)) {
                        now.add(Calendar.MONTH, -1);
                        if (this.dateFormat.format(now.getTime()).compareTo(B016Jzrq) <= 0) {
                            shotDateShowResult = "未到接种时间";
                        } else {
                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                        }
                    } else {
                        shotDateShowResult = "未到接种时间";
                    }
                }
            } else if ("B018".equals(fbactId)) {
                //百白破4
                if (!bbp.isEmpty()) {
                    shotDateShowResult = bbp.pollFirst();
                    jzrqMap.put("B018", shotDateShowResult);
                } else {
                    String B017Jzrq = jzrqMap.get("B017");
                    if (StringUtils.isNotBlank(B017Jzrq)) {
                        now.add(Calendar.MONTH, -6);
                        if (this.dateFormat.format(now.getTime()).compareTo(B017Jzrq) <= 0) {
                            shotDateShowResult = "未到接种时间";
                        } else {
                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                        }
                    } else {
                        shotDateShowResult = "未到接种时间";
                    }
                }
                if("到期未种".equals(this.checkJzjlMonth(rxrtChild, "72"))){
                    String []  dtapCodeArray=new String[]{"B015","B016","B017","B018"};
                    for(String dtap:dtapCodeArray){
                        if(jzrqMap.containsKey(dtap)){
                            dtapShotNum++;
                            maxDtapShotTime=jzrqMap.get(dtap);
                        }
                    }
                    if(dtapShotNum==0){
                        lst.add(jzjlIndex+2,new EpiJzjl("白破疫苗(DT)", "B038", "第2剂次", "-", null));
                        lst.add(jzjlIndex+3,new EpiJzjl("白破疫苗(DT)", "B039", "第3剂次", "-", null));
                        jzjl.setColspan("3");
                    }else if(dtapShotNum==1){
                        lst.add(jzjlIndex+2,new EpiJzjl("白破疫苗(DT)", "B038", "第2剂次", "-", null));
                        jzjl.setColspan("2");
                    }
                }
            } else if ("B037".equals(fbactId)) {
                //白破疫苗
                RxrtJzjl B155 = jzjlMap.get("B155");
                if (B155 != null && StringUtils.isNotBlank(B155.getJzrq()) &&
                        !(B155.getJzrq().startsWith("1981") || B155.getJzrq().startsWith("1982") || B155.getJzrq().startsWith("1983")
                                || B155.getJzrq().startsWith("1984") || B155.getJzrq().startsWith("1986"))) {
                    shotDateShowResult = B155.getJzrq();
                    jzrqMap.put("B037", shotDateShowResult);
                } else {
                    if("未到接种时间".equals(this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId)))){
                        shotDateShowResult="未到接种时间";
                    }else {
                        if (dtapShotNum == 4) {
                            shotDateShowResult=this.checkJzjlMonth(rxrtChild, "72");
                        }else if (dtapShotNum == 3) {
                            now.add(Calendar.MONTH, -6);
                            if (this.dateFormat.format(now.getTime()).compareTo(maxDtapShotTime) <= 0) {
                                shotDateShowResult = "未到接种时间";
                            } else {
                                shotDateShowResult = "到期未种";
                            }
                        }else {
                            if(maxDtapShotTime!=null){
                                now.add(Calendar.MONTH, -1);
                                if (this.dateFormat.format(now.getTime()).compareTo(maxDtapShotTime) <= 0) {
                                    shotDateShowResult = "未到接种时间";
                                } else {
                                    shotDateShowResult = "到期未种";
                                }
                            }else {
                                shotDateShowResult = "到期未种";
                            }
                        }
                    }
                }
            }else if ("B038".equals(fbactId)) {
                String B037Jzrq = jzrqMap.get("B037");
                if (StringUtils.isNotBlank(B037Jzrq)) {
                    now.add(Calendar.MONTH, -1);
                    if (this.dateFormat.format(now.getTime()).compareTo(B037Jzrq) <= 0) {
                        shotDateShowResult = "未到接种时间";
                    } else {
                        shotDateShowResult = "到期未种";
                    }
                } else {
                    shotDateShowResult = "未到接种时间";
                }
            }else if ("B039".equals(fbactId)) {
                String B038Jzrq = jzrqMap.get("B038");
                if (StringUtils.isNotBlank(B038Jzrq)) {
                    now.add(Calendar.MONTH, -6);
                    if (this.dateFormat.format(now.getTime()).compareTo(B038Jzrq) <= 0) {
                        shotDateShowResult = "未到接种时间";
                    } else {
                        shotDateShowResult = "到期未种";
                    }
                } else {
                    shotDateShowResult = "未到接种时间";
                }
            } else if ("B040".equals(fbactId)) {
                String  B138="";
                //A群流脑1
                if(!acComb.isEmpty()){
                    B138 = acComb.pollFirst();
                    if(Util.getMonthByADate(rxrtChild.getBirth(),B138)<=24){
                        if (B138 != null && StringUtils.isNotBlank(B138) &&
                                !(B138.startsWith("1981") || B138.startsWith("1982") || B138.startsWith("1983")
                                        || B138.startsWith("1984") || B138.startsWith("1986"))) {
                            shotDateShowResult = B138;
                            jzrqMap.put("B040", shotDateShowResult);
                            jzrqMap.put("B138", shotDateShowResult);
                        }
                    }else{
                        //如果B138不是null但是接种时间已经大于2岁了，就不能替代A群1了，所以把这个剂次归还然后判断其他替代疫苗
                        acComb.add(B138);
                        if (!acHib.isEmpty()) {
                            shotDateShowResult = acHib.get(0);
                            jzrqMap.put("B040", shotDateShowResult);
                        } else {
                            //判断B699
                            RxrtJzjl B699 = jzjlMap.get("B699");
                            if (B699 != null && StringUtils.isNotBlank(B699.getJzrq()) &&
                                    !(B699.getJzrq().startsWith("1981") || B699.getJzrq().startsWith("1982") || B699.getJzrq().startsWith("1983")
                                            || B699.getJzrq().startsWith("1984") || B699.getJzrq().startsWith("1986"))) {
                                shotDateShowResult = B699.getJzrq();
                                System.out.println(Util.getMonthByADate(rxrtChild.getBirth(),shotDateShowResult));
                                if(Util.getMonthByADate(rxrtChild.getBirth(),shotDateShowResult)>=3 && Util.getMonthByADate(rxrtChild.getBirth(),shotDateShowResult)<=23) {
                                    jzrqMap.put("B040", shotDateShowResult);
                                    jzrqMap.put("B699", shotDateShowResult);
                                    jzjlMap.remove("B699");
                                }else{
                                    shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                                }
                            }else {
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                            }
                        }
                    }
                }
                else {
                    if (!acHib.isEmpty()) {
                        shotDateShowResult = acHib.get(0);
                        jzrqMap.put("B040", shotDateShowResult);
                    } else {
                        //判断B699
                        RxrtJzjl B699 = jzjlMap.get("B699");
                        if (B699 != null && StringUtils.isNotBlank(B699.getJzrq()) &&
                                !(B699.getJzrq().startsWith("1981") || B699.getJzrq().startsWith("1982") || B699.getJzrq().startsWith("1983")
                                        || B699.getJzrq().startsWith("1984") || B699.getJzrq().startsWith("1986"))) {
                            shotDateShowResult = B699.getJzrq();
                            System.out.println(Util.getMonthByADate(rxrtChild.getBirth(),shotDateShowResult));
                            if(Util.getMonthByADate(rxrtChild.getBirth(),shotDateShowResult)>=3 && Util.getMonthByADate(rxrtChild.getBirth(),shotDateShowResult)<=23) {
                                jzrqMap.put("B040", shotDateShowResult);
                                jzrqMap.put("B699", shotDateShowResult);
                                jzjlMap.remove("B699");
                            }else{
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                            }
                        }else {
                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                        }
                    }
                }
            } else if ("B041".equals(fbactId)) {
                //A群流脑2
                String  B139 = "";
                if(!acComb.isEmpty()) {
                    B139 = acComb.pollFirst();
                    if(Util.getMonthByADate(rxrtChild.getBirth(),B139)<=24){
                        if (B139 != null && StringUtils.isNotBlank(B139) &&
                                !(B139.startsWith("1981") || B139.startsWith("1982") || B139.startsWith("1983")
                                        || B139.startsWith("1984") || B139.startsWith("1986"))) {
                            shotDateShowResult = B139;
                            jzrqMap.put("B041", shotDateShowResult);
                        }
                    }else{
                        acComb.add(B139);
                        //如果大于2岁了需要吧数据再归还，然后判断其他替代苗
                        {
                            if (!acHib.isEmpty()) {
                                if (acHib.get(0).startsWith("1980") || acHib.get(0).startsWith("1985")) {
                                    jzjl.setShotdate(acHib.get(0));
                                } else {
                                    Calendar c1 = Calendar.getInstance();
                                    if (acHib.size() == 3) {
                                        shotDateShowResult = acHib.get(1);
                                    } else {
                                        c1.setTime(this.dateFormat.parse(rxrtChild.getBirth()));
                                        c1.add(Calendar.MONTH, 2);
                                        if (this.dateFormat.format(c1.getTime()).compareTo(acHib.get(0)) > 0) {
                                            String acHibDate = acHib.get(acHib.size() - 1);
                                            now.add(Calendar.MONTH, -1);
                                            if (this.dateFormat.format(now.getTime()).compareTo(acHibDate) <= 0) {
                                                shotDateShowResult = "未到接种时间";
                                            } else {
                                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                                            }
                                        } else {
                                            //AC流脑Hib联合疫苗第一剂次在6月龄之后12月龄之前接种，第二剂次接种替代A群流脑2，第二剂次接种，则A群流脑2显示“√”
                                            c1.setTime(this.dateFormat.parse(rxrtChild.getBirth()));
                                            c1.add(Calendar.MONTH, 12);
                                            if (this.dateFormat.format(c1.getTime()).compareTo(acHib.get(0)) > 0) {
                                                if (acHib.size() >= 2) {
                                                    jzjl.setShotdate(acHib.get(1));
                                                } else {
                                                    String acHibDate = acHib.get(acHib.size() - 1);
                                                    now.add(Calendar.MONTH, -1);
                                                    if (this.dateFormat.format(now.getTime()).compareTo(acHibDate) <= 0) {
                                                        shotDateShowResult = "未到接种时间";
                                                    } else {
                                                        shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                                                    }
                                                }
                                            } else {
                                                //AC流脑Hib联合疫苗第一剂次在12月龄之后接种，第一剂次同时替代A群流脑1和A群流脑2
                                                shotDateShowResult = acHib.get(0);
                                            }
                                        }
                                    }
                                }

                            } else {
                                //判断B700是不是已经接种
                                RxrtJzjl B700 = jzjlMap.get("B700");
                                if (B700 != null && StringUtils.isNotBlank(B700.getJzrq()) &&
                                        !(B700.getJzrq().startsWith("1981") || B700.getJzrq().startsWith("1982") || B700.getJzrq().startsWith("1983")
                                                || B700.getJzrq().startsWith("1984") || B700.getJzrq().startsWith("1986"))) {
                                    shotDateShowResult = B700.getJzrq();
                                }else{
                                    String B040Jzrq = jzrqMap.get("B040");
                                    String B138Jzrq = "";
                                    String B699Jzrq = "";
                                    String  B138 = jzrqMap.get("B138");
                                    String  B699 = jzrqMap.get("B699");
                                    if (B138 != null && StringUtils.isNotBlank(B138) &&
                                            !(B138.startsWith("1981") || B138.startsWith("1982") || B138.startsWith("1983")
                                                    || B138.startsWith("1984") || B138.startsWith("1986"))) {
                                        B138Jzrq = B138;
                                    }
                                    if (B699 != null && StringUtils.isNotBlank(B699) &&
                                            !(B699.startsWith("1981") || B699.startsWith("1982") || B699.startsWith("1983")
                                                    || B699.startsWith("1984") || B699.startsWith("1986"))) {
                                        B699Jzrq = B699;
                                    }
                                    String jzrq = maxString(B040Jzrq, B138Jzrq,B699Jzrq);
                                    if (StringUtils.isNotBlank(jzrq)) {
                                        now.add(Calendar.MONTH, -3);
                                        if (this.dateFormat.format(now.getTime()).compareTo(jzrq) <= 0) {
                                            shotDateShowResult = "未到接种时间";
                                        } else {
                                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                                        }
                                    } else {
                                        shotDateShowResult = "未到接种时间";
                                    }
                                }
                            }
                        }
                    }
                }else {
                    if (!acHib.isEmpty()) {
                        if (acHib.get(0).startsWith("1980") || acHib.get(0).startsWith("1985")) {
                            jzjl.setShotdate(acHib.get(0));
                        } else {
                            Calendar c1 = Calendar.getInstance();
                            if (acHib.size() == 3) {
                                shotDateShowResult = acHib.get(1);
                            } else {
                                c1.setTime(this.dateFormat.parse(rxrtChild.getBirth()));
                                c1.add(Calendar.MONTH, 2);
                                if (this.dateFormat.format(c1.getTime()).compareTo(acHib.get(0)) > 0) {
                                    String acHibDate = acHib.get(acHib.size() - 1);
                                    now.add(Calendar.MONTH, -1);
                                    if (this.dateFormat.format(now.getTime()).compareTo(acHibDate) <= 0) {
                                        shotDateShowResult = "未到接种时间";
                                    } else {
                                        shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                                    }
                                } else {
                                    //AC流脑Hib联合疫苗第一剂次在6月龄之后12月龄之前接种，第二剂次接种替代A群流脑2，第二剂次接种，则A群流脑2显示“√”
                                    c1.setTime(this.dateFormat.parse(rxrtChild.getBirth()));
                                    c1.add(Calendar.MONTH, 12);
                                    if (this.dateFormat.format(c1.getTime()).compareTo(acHib.get(0)) > 0) {
                                        if (acHib.size() >= 2) {
                                            jzjl.setShotdate(acHib.get(1));
                                        } else {
                                            String acHibDate = acHib.get(acHib.size() - 1);
                                            now.add(Calendar.MONTH, -1);
                                            if (this.dateFormat.format(now.getTime()).compareTo(acHibDate) <= 0) {
                                                shotDateShowResult = "未到接种时间";
                                            } else {
                                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                                            }
                                        }
                                    } else {
                                        //AC流脑Hib联合疫苗第一剂次在12月龄之后接种，第一剂次同时替代A群流脑1和A群流脑2
                                        shotDateShowResult = acHib.get(0);
                                    }
                                }
                            }
                        }

                    } else {
                        //判断B700是不是已经接种
                        RxrtJzjl B700 = jzjlMap.get("B700");
                        if (B700 != null && StringUtils.isNotBlank(B700.getJzrq()) &&
                                !(B700.getJzrq().startsWith("1981") || B700.getJzrq().startsWith("1982") || B700.getJzrq().startsWith("1983")
                                        || B700.getJzrq().startsWith("1984") || B700.getJzrq().startsWith("1986"))) {
                            shotDateShowResult = B700.getJzrq();
                        }else{
                            String B040Jzrq = jzrqMap.get("B040");
                            String B138Jzrq = "";
                            String B699Jzrq = "";
                            String  B138 = jzrqMap.get("B138");
                            String  B699 = jzrqMap.get("B699");
                            if (B138 != null && StringUtils.isNotBlank(B138) &&
                                    !(B138.startsWith("1981") || B138.startsWith("1982") || B138.startsWith("1983")
                                            || B138.startsWith("1984") || B138.startsWith("1986"))) {
                                B138Jzrq = B138;
                            }
                            if (B699 != null && StringUtils.isNotBlank(B699) &&
                                    !(B699.startsWith("1981") || B699.startsWith("1982") || B699.startsWith("1983")
                                            || B699.startsWith("1984") || B699.startsWith("1986"))) {
                                B699Jzrq = B699;
                            }
                            String jzrq = maxString(B040Jzrq, B138Jzrq,B699Jzrq);
                            if (StringUtils.isNotBlank(jzrq)) {
                                now.add(Calendar.MONTH, -3);
                                if (this.dateFormat.format(now.getTime()).compareTo(jzrq) <= 0) {
                                    shotDateShowResult = "未到接种时间";
                                } else {
                                    shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                                }
                            } else {
                                shotDateShowResult = "未到接种时间";
                            }
                        }
                    }
                }
                //查看是否已经种全了
                if("到期未中".equals(this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId)))){
                    for(int index =0;index<lst.size();index++){
                        EpiJzjl  jzjld =   lst.get(index);
                        if("B040".equals(jzjld.getFbactid())){
                            if("到期未中".equals(jzjld.getShotdate())){
                                aQunNum++;
                            }
                        }
                        if("B041".equals(jzjld.getFbactid())){
                            if("到期未中".equals(jzjld.getShotdate())){
                                aQunNum++;
                            }
                        }
                    }}
            } else if ("B045".equals(fbactId)) {
                //A+C群流脑1  B140
                String B140="";
                if(!acComb.isEmpty()) {
                    B140= acComb.pollFirst();
                }
                if (B140 != null && StringUtils.isNotBlank(B140) &&
                        !(B140.startsWith("1981") || B140.startsWith("1982") || B140.startsWith("1983")
                                || B140.startsWith("1984") || B140.startsWith("1986")) &&Util.getMonthByADate(rxrtChild.getBirth(),B140)>=24) {
                    //如果B140作为替代苗已经接种了那么当前显示为B140的接种时间 并且把这个时间赋值给B045和B147存储到jzrqMap中
                    shotDateShowResult = B140;
                    jzrqMap.put("B045", shotDateShowResult);
                    jzrqMap.put("B147", shotDateShowResult);
                    jzrqMap.put("B699", shotDateShowResult);
                }else {
                    //如果B140没有接种的话看看B147接种了没有 如果有的话将显示B147的接种时间存储到B045和B147中
                    RxrtJzjl B147 = jzjlMap.get("B147");
                    if (B147 != null && StringUtils.isNotBlank(B147.getJzrq()) &&
                            !(B147.getJzrq().startsWith("1981") || B147.getJzrq().startsWith("1982") || B147.getJzrq().startsWith("1983")
                                    || B147.getJzrq().startsWith("1984") || B147.getJzrq().startsWith("1986"))) {
                        shotDateShowResult = B147.getJzrq();
                        jzrqMap.put("B045", shotDateShowResult);
                        jzrqMap.put("B147", shotDateShowResult);
                        jzrqMap.put("B699", shotDateShowResult);
                    } else {
                        //这里需要再额外判断一针B699
                        RxrtJzjl B699 = jzjlMap.get("B699");
                        if (B699 != null && StringUtils.isNotBlank(B699.getJzrq()) &&
                                !(B699.getJzrq().startsWith("1981") || B699.getJzrq().startsWith("1982") || B699.getJzrq().startsWith("1983")
                                        || B699.getJzrq().startsWith("1984") || B699.getJzrq().startsWith("1986"))) {
                            shotDateShowResult = B699.getJzrq();
                            jzrqMap.put("B045", shotDateShowResult);
                        }else {
                            //根据A群的接种情况来判断是否到了接种时间
                            if(aQunNum>1) {
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                            }else{
                                shotDateShowResult = this.checkJzjlMonth(rxrtChild, "24");
                            }
                        }
                    }
                }
            } else if ("B046".equals(fbactId)) {
                //A+C群流脑2
                String B141="";
                if(!acComb.isEmpty()) {
                    B141= acComb.pollFirst();
                }

                if (B141 != null && StringUtils.isNotBlank(B141) &&
                        !(B141.startsWith("1981") || B141.startsWith("1982") || B141.startsWith("1983")
                                || B141.startsWith("1984") || B141.startsWith("1986")) &&Util.getMonthByADate(rxrtChild.getBirth(),B141)>=24) {
                    shotDateShowResult = B141;
                    jzrqMap.put("B148", shotDateShowResult);
                    jzrqMap.put("B699", shotDateShowResult);
                }else {
                    //如果第二针替代苗没有接种  则需要根据第一针接种的替代苗来判断是未到接种时间还是到期未中
                    RxrtJzjl B148 = jzjlMap.get("B148");
                    if (B148 != null && StringUtils.isNotBlank(B148.getJzrq()) &&
                            !(B148.getJzrq().startsWith("1981") || B148.getJzrq().startsWith("1982") || B148.getJzrq().startsWith("1983")
                                    || B148.getJzrq().startsWith("1984") || B148.getJzrq().startsWith("1986"))) {
                        shotDateShowResult = B148.getJzrq();
                        jzrqMap.put("B148", shotDateShowResult);
                    } else {
                        String B045Jzrq = jzrqMap.get("B045");
                        if(StringUtils.isEmpty(B045Jzrq)){
                            B045Jzrq="";
                        }
                        String B147Jzrq = "";
                        String B699Jzrq = "";
                        RxrtJzjl B147 = jzjlMap.get("B147");
                        RxrtJzjl B699 = jzjlMap.get("B699");
                        if (B147 != null && StringUtils.isNotBlank(B147.getJzrq()) &&
                                !(B147.getJzrq().startsWith("1981") || B147.getJzrq().startsWith("1982") || B147.getJzrq().startsWith("1983")
                                        || B147.getJzrq().startsWith("1984") || B147.getJzrq().startsWith("1986"))) {
                            B147Jzrq = B147.getJzrq();
                        }
                        if (B699 != null && StringUtils.isNotBlank(B699.getJzrq()) &&
                                !(B699.getJzrq().startsWith("1981") || B699.getJzrq().startsWith("1982") || B699.getJzrq().startsWith("1983")
                                        || B699.getJzrq().startsWith("1984") || B699.getJzrq().startsWith("1986"))) {
                            B699Jzrq = B699.getJzrq();
                        }
                        String jzrq = maxString(B045Jzrq, B147Jzrq,B699Jzrq);
                        if (StringUtils.isNotBlank(jzrq)) {
                            now.add(Calendar.MONTH, -36);
                            if (this.dateFormat.format(now.getTime()).compareTo(jzrq) <= 0) {
                                shotDateShowResult = "未到接种时间";
                            } else {
                                if(aQunNum>1) {
                                    shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                                }else{
                                    shotDateShowResult = this.checkJzjlMonth(rxrtChild, "60");
                                }
                            }
                        } else {
                            shotDateShowResult = "未到接种时间";
                        }
                    }
                }
            } else if ("B032".equals(fbactId)) {
                //乙脑减毒1
                RxrtJzjl B027 = jzjlMap.get("B027");
                RxrtJzjl B028 = jzjlMap.get("B028");
                if (B027 != null && StringUtils.isNotBlank(B027.getJzrq()) &&
                        !(B027.getJzrq().startsWith("1981") || B027.getJzrq().startsWith("1982") || B027.getJzrq().startsWith("1983")
                                || B027.getJzrq().startsWith("1984") || B027.getJzrq().startsWith("1986"))) {
                    if (B028 != null && StringUtils.isNotBlank(B028.getJzrq()) &&
                            !(B028.getJzrq().startsWith("1981") || B028.getJzrq().startsWith("1982") || B028.getJzrq().startsWith("1983")
                                    || B028.getJzrq().startsWith("1984") || B028.getJzrq().startsWith("1986"))) {
                        shotDateShowResult = B028.getJzrq();
                        jzrqMap.put("B028", shotDateShowResult);
                        jzrqMap.put("B032", shotDateShowResult);
                    } else {
                        now.add(Calendar.DATE, -7);
                        if (this.dateFormat.format(now.getTime()).compareTo(B027.getJzrq()) <= 0) {
                            shotDateShowResult = "未到接种时间";
                        } else {
                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                        }
                    }
                } else {
                    shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                }
            } else if ("B033".equals(fbactId)) {
                //乙脑减毒2
                RxrtJzjl B029 = jzjlMap.get("B029");
                RxrtJzjl B030 = jzjlMap.get("B030");
                if (B029 != null && StringUtils.isNotBlank(B029.getJzrq()) &&
                        !(B029.getJzrq().startsWith("1981") || B029.getJzrq().startsWith("1982") || B029.getJzrq().startsWith("1983")
                                || B029.getJzrq().startsWith("1984") || B029.getJzrq().startsWith("1986"))) {
                    if (B030 != null && StringUtils.isNotBlank(B030.getJzrq()) &&
                            !(B030.getJzrq().startsWith("1981") || B030.getJzrq().startsWith("1982") || B030.getJzrq().startsWith("1983")
                                    || B030.getJzrq().startsWith("1984") || B030.getJzrq().startsWith("1986"))) {
                        shotDateShowResult = B030.getJzrq();
                        jzrqMap.put("B028", shotDateShowResult);
                    } else {
                        now.add(Calendar.MONTH, -36);
                        if (this.dateFormat.format(now.getTime()).compareTo(B029.getJzrq()) <= 0) {
                            shotDateShowResult = "未到接种时间";
                        } else {
                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, (6 * 12) + "");
                        }
                    }
                } else {
                    String B032Jzrq = jzrqMap.get("B032");
                    RxrtJzjl B027 = jzjlMap.get("B027");
                    RxrtJzjl B028 = jzjlMap.get("B028");
                    String B027Jzrq = "";
                    String B028Jzrq = "";
                    if (B027 != null && StringUtils.isNotBlank(B027.getJzrq()) &&
                            !(B027.getJzrq().startsWith("1981") || B027.getJzrq().startsWith("1982") || B027.getJzrq().startsWith("1983")
                                    || B027.getJzrq().startsWith("1984") || B027.getJzrq().startsWith("1986"))) {
                        B027Jzrq = B027.getJzrq();
                    }
                    if (B028 != null && StringUtils.isNotBlank(B028.getJzrq()) &&
                            !(B028.getJzrq().startsWith("1981") || B028.getJzrq().startsWith("1982") || B028.getJzrq().startsWith("1983")
                                    || B028.getJzrq().startsWith("1984") || B028.getJzrq().startsWith("1986"))) {
                        B028Jzrq = B028.getJzrq();
                    }
                    String jzrq = "";
                    if (StringUtils.isNotBlank(B032Jzrq) || (StringUtils.isNotBlank(B027Jzrq) && StringUtils.isNotBlank(B028Jzrq))) {
                        jzrq = maxString(B032Jzrq, B027Jzrq, B028Jzrq);
                    }
                    if (StringUtils.isNotBlank(jzrq)) {
                        now.add(Calendar.MONTH, -12);
                        if (this.dateFormat.format(now.getTime()).compareTo(jzrq) <= 0) {
                            shotDateShowResult = "未到接种时间";
                        } else {
                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                        }
                    } else {
                        shotDateShowResult = "未到接种时间";
                    }
                }
            } else   if ("B073".equals(fbactId)) {
                //甲肝灭活第一剂次状态
                RxrtJzjl B069 = jzjlMap.get("B069");
                if(B069!=null && StringUtils.isNotBlank(B069.getJzrq())){
                    shotDateShowResult=B069.getJzrq();
                }else{
                    shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                }
            }  else if ("B074".equals(fbactId)) {
                //甲肝减毒1
                RxrtJzjl B073 = jzjlMap.get("B073");
                RxrtJzjl B069 = jzjlMap.get("B069");
                if(B069!=null && StringUtils.isNotBlank(B069.getJzrq())) {
                    shotDateShowResult=B069.getJzrq();
                } else {
                    if (B073 != null && StringUtils.isNotBlank(B073.getJzrq())) {
                        now.add(Calendar.MONTH, -6);
                        if (this.dateFormat.format(now.getTime()).compareTo(B073.getJzrq()) <= 0) {
                            shotDateShowResult = "未到接种时间";
                        } else {
                            shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                        }
                    } else {
                        shotDateShowResult = "未到接种时间";
                    }
                }
            } else if ("B050".equals(fbactId)) {
                shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
            } else if ("B132".equals(fbactId)) {
                //水痘2
                RxrtJzjl B050 = jzjlMap.get("B050");
                if (B050 != null && StringUtils.isNotBlank(B050.getJzrq())) {
                    int month=-3;
                    if(checkCityCode.equals(rxrtChild.getCityCode())){
                        if(rxrtChild.getBirth().compareTo("2018-07-01")<0){
                            month=-12;
                        }else {
                            month=-36;
                        }
                    }
                    now.add(Calendar.MONTH, month);
                    if (this.dateFormat.format(now.getTime()).compareTo(B050.getJzrq()) <= 0) {
                        shotDateShowResult = "未到接种时间";
                    } else {
                        shotDateShowResult = this.checkJzjlMonth(rxrtChild, bactMonthMap.get(fbactId));
                    }
                } else {
                    shotDateShowResult = "未到接种时间";
                }
            }
            if ("已种替代疫苗".equals(shotDateShowResult) || (shotDateShowResult != null && shotDateShowResult.indexOf("-") > 1)) {
                jzjl.setShotdate(shotDateShowResult);
            } else {
                if (StringUtils.isBlank(jzjl.getShotdate()) || "-".equals(jzjl.getShotdate())) {
                    jzjl.setShotdate(shotDateShowResult);
                }
            }
            if (StringUtils.isBlank(jzjl.getShotdate())
                    || "到期未种".equals(jzjl.getShotdate()) || "未到接种时间".equals(jzjl.getShotdate())
                    || "不详".equals(jzjl.getShotdate()) || "父亲/母亲/监护人要求不种".equals(jzjl.getShotdate())) {
                Calendar now1 = Calendar.getInstance();
                if ("B001".equals(jzjl.getFbactid())) {
                    //4周岁超期不种
                    now1.add(Calendar.YEAR, -4);
                    if (this.dateFormat.format(now1.getTime()).compareTo(rxrtChild.getBirth()) > 0) {
                        jzjl.setShotdate("超期不种");
                    }
                } else if ("B015".equals(jzjl.getFbactid()) || "B016".equals(jzjl.getFbactid())
                        || "B017".equals(jzjl.getFbactid()) || "B018".equals(jzjl.getFbactid())) {
                    now1.add(Calendar.YEAR, -6);
                    if (this.dateFormat.format(now1.getTime()).compareTo(rxrtChild.getBirth()) > 0) {
                        jzjl.setShotdate("超期不种");
                    }
                } else if ("B040".equals(jzjl.getFbactid()) || "B041".equals(jzjl.getFbactid())) {
                    now1.add(Calendar.YEAR, -2);
                    if (this.dateFormat.format(now1.getTime()).compareTo(rxrtChild.getBirth()) > 0) {
                        jzjl.setShotdate("超期不种");
                    }
                } else if ("B037".equals(jzjl.getFbactid())||"B038".equals(jzjl.getFbactid())||"B039".equals(jzjl.getFbactid())) {
                    now1.add(Calendar.YEAR, -12);
                    if (this.dateFormat.format(now1.getTime()).compareTo(rxrtChild.getBirth()) > 0) {
                        jzjl.setShotdate("超期不种");
                    }
                }else if("B050".equals(jzjl.getFbactid()) || "B132".equals(jzjl.getFbactid())){
                    if(checkCityCode.equals(rxrtChild.getCityCode())){
                        now1.add(Calendar.YEAR, -7);
                        if (this.dateFormat.format(now1.getTime()).compareTo(rxrtChild.getBirth()) > 0) {
                            jzjl.setShotdate("超期不种");
                        }
                    }
                }
            }
        }
        //处理下甲肝的两个剂次:如果甲肝 灭活只接种了一针或者没有接种但是甲肝活苗接种了
        //获取B073 B074  B069
        RxrtJzjl     B069 = jzjlMap.get("B069");
        RxrtJzjl     B073 = jzjlMap.get("B073");
        RxrtJzjl     B074 = jzjlMap.get("B074");
        String B069Date="";
        if(B069!=null && StringUtils.isNotBlank(B069.getJzrq())){
            B069Date=B069.getJzrq();
        }
        int count=0;
        if(B073!=null && StringUtils.isNotBlank(B073.getJzrq())){
            count++;
        }
        if(B074!=null && StringUtils.isNotBlank(B074.getJzrq())){
            count++;
        }
        if(StringUtils.isNotBlank(B069Date) && count<=1) {
            for (int jzjlIndex = 0; jzjlIndex < lst.size(); jzjlIndex++) {
                EpiJzjl jzjl = lst.get(jzjlIndex);
                String shotDateShowResult = null;
                Calendar now = Calendar.getInstance();
                String fbactId = jzjl.getFbactid();
                if ("B073".equals(fbactId) || "B074".equals(fbactId)) {
                    jzjl.setShotdate(B069Date);
                }
            }
        }
        //处理百白破
        //记录B0338和B039的位置
        EpiJzjl B038Index =null;
        EpiJzjl B039Index=null;
        EpiJzjl B037Index=null;
        System.out.println(lst.get(0).getFbactid());
        for(int jzjlIndex = 0; jzjlIndex < lst.size(); jzjlIndex++){
            EpiJzjl jzjl = lst.get(jzjlIndex);
            if("B039".equals(jzjl.getFbactid())){
                B039Index=lst.get(jzjlIndex);
            }else if("B038".equals(jzjl.getFbactid())){
                B038Index=lst.get(jzjlIndex);
            }else if("B037".equals(jzjl.getFbactid())){
                B037Index=lst.get(jzjlIndex);
            }
        }
        if(dtapShotNum==0){
            //如果第一针接种了
            if(B037Index.getShotdate().contains("-")){
                if(B038Index.getShotdate().contains("-")&&B039Index.getShotdate().contains("-")||B038Index.getShotdate().contains("-")){
                    B037Index.setShotdate(B039Index.getShotdate());
                }else{
                    B037Index.setShotdate(B038Index.getShotdate());
                }
            }
            lst.remove(B038Index);
            lst.remove(B039Index);
        }else if(dtapShotNum==1){
            if(B037Index.getShotdate().contains("-")) {
                B037Index.setShotdate(B038Index.getShotdate());
            }
            lst.remove(B038Index);
        }
    }


}
