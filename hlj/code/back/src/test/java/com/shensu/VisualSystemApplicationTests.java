package com.shensu;

import com.shensu.jsjm.VisualSystemApplication;
import com.shensu.jsjm.controller.VaccinationController;
import com.shensu.jsjm.service.CommonService;
import com.shensu.jsjm.util.PropertyUtil;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

//指定web环境，随机端口
@SpringBootTest(classes = VisualSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class VisualSystemApplicationTests {


    @Resource
    private VaccinationController vaccinationController;

    @Resource
    private PropertyUtil propertyUtil;

    @Resource
    private CommonService commonService;




}
