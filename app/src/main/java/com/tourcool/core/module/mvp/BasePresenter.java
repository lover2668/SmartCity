package com.tourcool.core.module.mvp;

import com.alibaba.fastjson.JSON;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author :JenkinsZhou
 * @description :Presenter基类
 * @company :途酷科技
 * @date 2019年07月01日10:27
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public abstract class BasePresenter<T extends IBaseModel, V extends IBaseView> {
     public static final String TAG = "BasePresenter";
    protected V mProxyView;
    private T module;
    private WeakReference<V> weakReference;

    /**
     * 生成Module实例
     *
     * @return
     */
    protected abstract T createModule();


    /**
     * 初始化方法
     */
    public abstract void start();

    /**
     * 绑定View
     */
    public void attachView(V view) {
        weakReference = new WeakReference<>(view);
        mProxyView = (V) Proxy.newProxyInstance(
                view.getClass().getClassLoader(),
                view.getClass().getInterfaces(),
                new MvpViewHandler(weakReference.get()));
        if (this.module == null) {
            this.module = createModule();
        }
    }


    /**
     * 解绑View
     */
    public void detachView() {
        this.module = null;
        if (isViewAttached()) {
            weakReference.clear();
            weakReference = null;
        }
    }

    /**
     * 是否与View建立连接
     */
    protected boolean isViewAttached() {
        return weakReference != null && weakReference.get() != null;
    }


    protected V getView() {
        return mProxyView;
    }

    protected T getModule() {
        return module;
    }

    /**
     * View代理类  防止 页面关闭P异步操作调用V 方法 空指针问题
     */
    private class MvpViewHandler implements InvocationHandler {

        private IBaseView mvpView;

        MvpViewHandler(IBaseView mvpView) {
            this.mvpView = mvpView;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //如果V层没被销毁, 执行V层的方法.
            if (isViewAttached()) {
                return method.invoke(mvpView, args);
            } //P层不需要关注V层的返回值
            return null;
        }
    }


    protected  <T> T parseJavaBean(Object data, Class<T> tClass) {
        try {
            return JSON.parseObject(JSON.toJSONString(data), tClass);
        } catch (Exception e) {
            return null;
        }
    }
}
