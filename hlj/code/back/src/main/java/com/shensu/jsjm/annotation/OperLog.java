package com.shensu.jsjm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义操作日志注解
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperLog {
    String operModulId() default ""; // 操作模块Id
    String operType() default "";  // 操作类型
    String operDesc() default "";  // 操作说明
    boolean paramsFlag() default true; // 是否取参数
    int paramsOrder() default 0; // 取参数位置

}
