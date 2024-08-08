//package com.shensu.jsjm.aspect;
//
//import com.shensu.jsjm.annotation.OperLog;
//import com.shensu.jsjm.model.syslog.OperationLog;
//import com.shensu.jsjm.util.CommonUtil;
//import net.sf.json.JSONObject;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//public class OperLogAspect {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(OperLogAspect.class);
//
//    @Resource
//    private SysLogService sysLogService;
//
//    /**
//     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
//     */
//    @Pointcut("@annotation(com.shensu.jsjm.annotation.OperLog)")
//    public void operLogPoinCut() {
//    }
//
//    //正常返回记录日志
//    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
//    public void saveOperLog(JoinPoint joinPoint, Object keys) {
//        try {
//            // 获取RequestAttributes
//            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//            // 从获取RequestAttributes中获取HttpServletRequest的信息
//            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
//            String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
//            // 从切面织入点处通过反射机制获取织入点处的方法
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();// 获取切入点所在的方法
//            Method method = signature.getMethod();// 获取操作
//            OperLog opLog = method.getAnnotation(OperLog.class);
//            //请求的方法名
//            String className = joinPoint.getTarget().getClass().getName();
//            String methodName = "请求方法：【" + className + "." + signature.getName() + "()】, ";
//            String reqPath = "请求方式：【" + request.getMethod() + "】, 请求路径：【" + path + "】, ";
//            String content = methodName + reqPath + "请求参数：";
//            //请求的参数
//            Object[] args = joinPoint.getArgs();
//            if (opLog.paramsFlag() && args.length > 0) {
//                content = content + JSONObject.fromObject(JSONObject.fromObject(args[opLog.paramsOrder()]));
//            }
//            //获取登录名
//            HttpSession httpSession = request.getSession();
//            String loginName = CommonUtil.getUserInfo(httpSession).getLoginName();
//            //获取客户端IP
//            String userIpAddr = CommonUtil.getIpAddress(request);
//
//            OperationLog operationLog = new OperationLog();
//            operationLog.setOperType(opLog.operType());
//            operationLog.setOperMouleId(Integer.valueOf(opLog.operModulId()));
//            operationLog.setOperContent(content);
//            operationLog.setOperIP(userIpAddr);
//            operationLog.setOperResult("操作成功");
//            operationLog.setOperUser(loginName);
//            sysLogService.insertSysLog(operationLog);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOGGER.info("记录日志异常");
//        }
//
//    }
//
//    //异常返回记录日志
//    @AfterThrowing(value = "operLogPoinCut()", throwing = "e")
//    public void saveOperLog(JoinPoint joinPoint, Throwable e) {
//        try {
//            // 获取RequestAttributes
//            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//            // 从获取RequestAttributes中获取HttpServletRequest的信息
//            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
//            String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
//            // 从切面织入点处通过反射机制获取织入点处的方法
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();// 获取切入点所在的方法
//            Method method = signature.getMethod();// 获取操作
//            OperLog opLog = method.getAnnotation(OperLog.class);
//            //请求的方法名
//            String className = joinPoint.getTarget().getClass().getName();
//            String methodName = "请求方法：【" + className + "." + signature.getName() + "()】, ";
//            String reqPath = "请求方式：【" + request.getMethod() + "】, 请求路径：【" + path + "】, ";
//            String content = methodName + reqPath + "请求参数：";
//            //获取请求参数
////                Map<String, String> rtnMap = converMap(request.getParameterMap());
////                String parameters = rtnMap.get("parameters").toString().replace(" ", "+");
////                Map<Object, Object> decryptParameterMap = (Map<Object, Object>) JSONObject.fromObject(AESUtil.decrypt(parameters));
////                content = content + JSONObject.fromObject(decryptParameterMap);
//            //请求的参数
//            //请求的参数
//            Object[] args = joinPoint.getArgs();
//            if (opLog.paramsFlag() && args.length > 0) {
//                content = content + JSONObject.fromObject(JSONObject.fromObject(args[opLog.paramsOrder()]));
//            }
//            //获取登录名
//            HttpSession httpSession = request.getSession();
//            String loginName = CommonUtil.getUserInfo(httpSession).getLoginName();
//
//            //获取客户端IP
//            String userIpAddr = CommonUtil.getIpAddress(request);
//
//            OperationLog operationLog = new OperationLog();
//            operationLog.setOperType(opLog.operType());
//            operationLog.setOperMouleId(Integer.valueOf(opLog.operModulId()));
//            operationLog.setOperContent(content);
//            operationLog.setOperIP(userIpAddr);
//            operationLog.setOperResult("操作失败");
//            operationLog.setOperUser(loginName);
//            sysLogService.insertSysLog(operationLog);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            LOGGER.info("记录日志异常");
//        }
//
//    }
//
//    /**
//     * 转换request 请求参数
//     *
//     * @param paramMap request获取的参数数组
//     */
//    public Map<String, String> converMap(Map<String, String[]> paramMap) {
//        Map<String, String> rtnMap = new HashMap<String, String>();
//        for (String key : paramMap.keySet()) {
//            rtnMap.put(key, paramMap.get(key)[0]);
//        }
//        return rtnMap;
//    }
//
//}
