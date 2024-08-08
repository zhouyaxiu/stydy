package com.shensu;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.shensu.service.highStuService.HighStuService;
import com.shensu.service.taskService.HighStuinfoTaskService;
import com.shensu.service.taskService.TaskService;
import com.shensu.util.Sm4Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class HainanSchoolApplicationTests {
    @Autowired
    private  TaskService taskService;
    @Resource
    private HighStuService  service;

    @Resource
    private HighStuinfoTaskService highStuinfoTaskService;


    @Test
    public  void  test2() throws Exception {
      taskService.areaTask();
      taskService.stationTask();
      taskService.SchUserInfoTask();
    }
    @Test
    public  void  test() throws Exception {
        JSONObject  obj = new JSONObject();
        obj.put("nowYear","2022");
        obj.put("vaccName","加强麻疹");
        String str  = Sm4Util.encryptEcb(obj.toString());
        System.out.println(str);//0d7e9d2590f879007cd7e4f7b772ac90ba800c5524d8973c977e57a41ea9a4550e828ecc6ce534ecbc75c8cdcc23c276
    }


    @Test
    public  void  test1() throws Exception {
        highStuinfoTaskService.checkHighStudent("0");
    }

}
