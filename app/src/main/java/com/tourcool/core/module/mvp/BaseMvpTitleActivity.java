package com.tourcool.core.module.mvp;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tourcool.ui.base.BaseCommonTitleActivity;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月07日13:37
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public abstract class BaseMvpTitleActivity<P extends BasePresenter> extends BaseCommonTitleActivity implements IBaseView {

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
        if (loadingDialog != null ) {
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
