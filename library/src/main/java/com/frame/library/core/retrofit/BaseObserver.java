package com.frame.library.core.retrofit;

import com.frame.library.core.UiManager;
import com.frame.library.core.control.IHttpRequestControl;

import io.reactivex.observers.DefaultObserver;

/**
 * @Author: JenkinsZhou on 2018/7/23 14:21
 * @E-Mail: 971613168@qq.com
 * Function: Retrofit快速观察者-观察者基类用于错误全局设置
 * Description:
 * 1、2017-11-16 11:35:12 JenkinsZhou增加返回错误码全局控制
 * 2、2018-6-20 15:15:45 重构
 * 3、2018-7-9 14:27:03 删除IHttpRequestControl判断避免http错误时无法全局控制BUG
 */
public abstract class BaseObserver<T> extends DefaultObserver<T> {

    public IHttpRequestControl mHttpRequestControl;

    public BaseObserver() {
        this(null);
    }

    public BaseObserver(IHttpRequestControl httpRequestControl) {
        this.mHttpRequestControl = httpRequestControl;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        //错误全局拦截控制
        boolean isIntercept = UiManager.getInstance().getFastObserverControl() != null && UiManager.getInstance().getFastObserverControl().onError(this, e);
        if (isIntercept) {
            return;
        }
        if (e instanceof FrameNullException) {
            onNext(null);
            return;
        }
        if (UiManager.getInstance().getHttpRequestControl() != null) {
            UiManager.getInstance().getHttpRequestControl().httpRequestError(mHttpRequestControl, e);
        }
        _onError(e);
    }

    @Override
    public void onNext(T entity) {
        _onNext(entity);
    }

    /**
     * 获取成功后数据展示
     *
     * @param entity 可能为null
     */
    public abstract void _onNext(T entity);

    public void _onError(Throwable e) {
    }
}
