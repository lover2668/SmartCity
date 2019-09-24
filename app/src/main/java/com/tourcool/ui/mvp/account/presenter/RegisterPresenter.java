package com.tourcool.ui.mvp.account.presenter;

import android.view.View;

import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.ui.mvp.account.contract.RegisterContract;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月24日14:45
 * @Email: 971613168@qq.com
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.LoginModel, RegisterContract.LoginView> implements RegisterContract.LoginPresenter {
    @Override
    protected RegisterContract.LoginModel createModule() {
        return null;
    }

    @Override
    public void start() {

    }


    @Override
    public void showClearIcon(boolean visible) {
        if (getView().getClearIcon() == null) {
            return;
        }
        getView().getClearIcon().setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
