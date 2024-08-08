package com.shensu.jsjm.controller;

import com.shensu.jsjm.model.SimpleAreaBean;
import com.shensu.jsjm.redis.RedisAreaService;
import com.shensu.jsjm.util.*;
import com.shensu.jsjm.vo.SingleLoginInputVo;
import com.shensu.jsjm.vo.SingleLoginOutputVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system")
@Api(tags = "0000.登录相关接口")
public class SystemMgtController {

    private static final Logger logger = LoggerFactory.getLogger(SystemMgtController.class);

    @Resource
    private PropertyUtil propertyUtil;

    @Resource
    private RedisAreaService redisAreaService;

    //单点登入
    @GetMapping("/singleLogin")
    @ApiOperation("单点登入")
    public SingleLoginOutputVo singleLogin(SingleLoginInputVo inputVo) {
//    public String singleLogin(SingleLoginInputVo inputVo, RedirectAttributes redirectAttributes) {
        SingleLoginOutputVo outputVo = new SingleLoginOutputVo();
        try {
            String newToken = inputVo.getToken();
            String AccessTokenUrl = propertyUtil.getAccessTokenUrl();
            String AccessToken = HttpUtil2.postHttpXml("", AccessTokenUrl + "?TOKEN=" + newToken);
            if ("-100".equals(AccessToken.trim())) {
                outputVo.setCode("1");
                outputVo.setMsg("用户登录异常请重试！");
                return outputVo;
            }
            if ("-101".equals(AccessToken.trim())) {
                outputVo.setCode("1");
                outputVo.setMsg("用户登录失效！");
                return outputVo;
            }
            String AccessUserInfoUrl = propertyUtil.getAccessUserUrl();
            String userStr = HttpUtil2.postHttpXml("", AccessUserInfoUrl + "?ACCESS_TOKEN=" + AccessToken);
            if ("-200".equals(userStr.trim())) {
                outputVo.setCode("1");
                outputVo.setMsg("TOKEN不能为空！");
                return outputVo;
            }
            if ("-201".equals(userStr.trim())) {
                outputVo.setCode("1");
                outputVo.setMsg("TOKEN错误或者已失效！");
                return outputVo;
            }
            Map<String, Object> map = Util2.xmlStr2Map(DESPlus.getDencryptByKey(AccessToken, userStr));
            String areaCode = String.valueOf(map.get("AC"));
            if (StringUtils.isNotBlank(areaCode)) {
                String level = CommonUtil.getZoneLevelByCode(areaCode);
                if ("0".equals(level) || "1".equals(level)) {
                    SimpleAreaBean simpleAreaBean = redisAreaService.getArea(null);
                    if (areaCode.equals(simpleAreaBean.getCode())) {
                        outputVo.setAreaName(simpleAreaBean.getName());
                    } else {
                        List<SimpleAreaBean> children = simpleAreaBean.getChildren();
                        for (SimpleAreaBean simpleAreaBean1 : children) {
                            if (areaCode.equals(simpleAreaBean1.getCode())) {
                                outputVo.setAreaName(simpleAreaBean1.getCode());
                                break;
                            }
                        }
                    }
                    outputVo.setCode("0");
                    outputVo.setMsg("SUCCESS");
                    outputVo.setAreaCode(areaCode);
                    outputVo.setLevel(level);
                } else {
                    outputVo.setLevel(level);
                    outputVo.setAreaCode(areaCode);
                    outputVo.setCode("1");
                    outputVo.setMsg("需要省市级用户登录！");
                }
            } else {
                outputVo.setCode("1");
                outputVo.setMsg("登录用户地区编码不为空! ");
            }
            return outputVo;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            outputVo.setCode("1");
            outputVo.setMsg(e.getMessage());
            return outputVo;
        }
    }

    @PostMapping("/singleLogin2")
    @ApiOperation("单点登入")
//    public SingleLoginOutputVo singleLogin(SingleLoginInputVo inputVo) {
    public void singleLogin2(@RequestParam("TOKEN")String TOKEN, HttpServletResponse respon, HttpServletRequest request) {
        try {
            logger.info("singleLogin2 TOEKN:"+TOKEN);
            TOKEN = request.getParameter("TOKEN");
            logger.info("singleLogin2 TOEKN:"+TOKEN);
            respon.sendRedirect(propertyUtil.getSingleLogin2()+TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





