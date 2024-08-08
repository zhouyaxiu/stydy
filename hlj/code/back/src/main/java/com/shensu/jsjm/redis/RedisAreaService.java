package com.shensu.jsjm.redis;

import com.shensu.jsjm.model.AreaBean;
import com.shensu.jsjm.model.OpdServeCount;
import com.shensu.jsjm.model.SimpleAreaBean;
import com.shensu.jsjm.vo.AreaInputVo;
import com.shensu.jsjm.vo.TotalOutputVo;

import java.util.Map;

public interface RedisAreaService {

    SimpleAreaBean getArea(AreaInputVo inputVo);

    Map<String, AreaBean> getAllArea();


}
