package com.frame.library.core.control;

import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.retrofit.FrameNullException;

/**
 * @Author: JenkinsZhou on 2019/7/12 10:25
 * @E-Mail: 971613168@qq.com
 * @Function: {@link BaseObserver}错误信息拦截并做其它操作处理配合{@link FrameNullException}以处理解决接口某些情况下无法回调成功问题
 * @Description:
 */
public interface FrameObserverControl {

    /**
     * @param o {@link BaseObserver} 对象用于后续事件逻辑
     * @param e 原始错误
     * @return true 拦截操作不进行原始{@link BaseObserver#onError(Throwable)}后续逻辑
     * false 不拦截继续后续逻辑
     */
    boolean onError(BaseObserver o, Throwable e);
}
