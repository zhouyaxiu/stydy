package com.shensu.service.check.util;

import com.alibaba.fastjson2.JSON;

public class Test {
    public static void main(String[] args) {
        RxrtService rxrtService = new RxrtService();
        String json="{" +
                "        name:\"测试\"," +
                "        birth:'2022-02-18'," +
                "        jzjlList:[{" +
                "            jzrq:'2022-02-18'," +
                "            fbactid:'B063'," +
                "        }," +
                "        {" +
                "            jzrq:'2022-02-18'," +
                "            fbactid:'B001'," +
                "        }]" +
                "    }";
        System.out.println(JSON.toJSONString(rxrtService.getChildRxrtToPt(JSON.parseObject(json))));
    }
}
