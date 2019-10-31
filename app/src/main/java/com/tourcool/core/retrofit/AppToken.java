package com.tourcool.core.retrofit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月28日16:32
 * @Email: 971613168@qq.com
 */
//这个注解是应用在方法上
@Target(ElementType.METHOD)
//注解会在class中存在，运行时可通过反射获取
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppToken {
    boolean needToken() default true;
}
