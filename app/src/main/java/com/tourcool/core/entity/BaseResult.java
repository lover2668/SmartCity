package com.tourcool.core.entity;

/**
 * @author :JenkinsZhou
 * @description :实体对象基类
 * @company :途酷科技
 * @date 2019年06月28日9:25
 * @Email: 971613168@qq.com
 */
public class BaseResult<T> {
    public int code;
    public String message;
    public T data;
}
