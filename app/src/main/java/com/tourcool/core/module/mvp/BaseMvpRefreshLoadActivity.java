package com.tourcool.core.module.mvp;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.frame.library.core.module.activity.FrameRefreshLoadActivity;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;

/**
 * @author :JenkinsZhou
 * @description :mvp模式下的下拉刷新与加载更多的activity基类
 * @company :翼迈科技股份有限公司
 * @date 2019年08月15日23:02
 * @Email: 971613168@qq.com
 */
public abstract class BaseMvpRefreshLoadActivity<P extends BasePresenter, T> extends FrameRefreshLoadActivity<T> implements IBaseView {
    protected P presenter;



    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        loadPresenter();
    }


    protected abstract void loadPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }

    }


    //***************************************IBaseView方法实现*************************************
    @Override
    public void showLoading() {
        showLoading("");
    }

    public void showLoading(String msg) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            if (!TextUtils.isEmpty(msg)) {
                loadingDialog.setLoadingText(msg);
            }
            loadingDialog.show();
        }
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onEmpty(Object tag) {

    }

    @Override
    public void onError(Object tag, String errorMsg) {

    }

    @Override
    public Context getContext() {
        return mContext;
    }


    //***************************************IBaseView方法实现*************************************

    /**
     * 创建Presenter
     *
     * @return p
     */
    protected abstract P createPresenter();

    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }
}
