package com.tourcool.core.base;

/**
 * @Author: JenkinsZhou on 2019/7/11 22:00
 * @E-Mail: 971613168@qq.com
 * @Function:
 * @Description:
 */
public class BaseResult<T> {

    public boolean success;
    public int code;
    public String message;
    public T data;
}
