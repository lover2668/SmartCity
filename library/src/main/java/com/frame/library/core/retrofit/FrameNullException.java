package com.frame.library.core.retrofit;

/**
 * @Author: JenkinsZhou on 2019/7/11 21:55
 * @E-Mail: 971613168@qq.com
 * @Function: 特定空对象Exception 用于解决接口某些情况下
 * 数据null无法回调{@link BaseObserver#_onNext(Object)}的情况
 * @Description:
 */
public class FrameNullException extends Exception {

    public int errorCode;

    public FrameNullException() {
        this("");
    }

    public FrameNullException(String message) {
        this(message, -1);
    }

    public FrameNullException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public FrameNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameNullException(Throwable cause) {
        super(cause);
    }
}
