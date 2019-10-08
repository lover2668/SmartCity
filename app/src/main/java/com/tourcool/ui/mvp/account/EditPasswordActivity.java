package com.tourcool.ui.mvp.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :修改密码
 * @company :途酷科技
 * @date 2019年10月08日18:01
 * @Email: 971613168@qq.com
 */
public class EditPasswordActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_phone_editpass;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("修改密码");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.llBindPhone).setOnClickListener(this);
        findViewById(R.id.llClearCache).setOnClickListener(this);
        findViewById(R.id.llEditPhone).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBindPhone:
                ToastUtil.show("绑定手机号");
                break;
            case R.id.llClearCache:
                ToastUtil.show("清除缓存");
                break;

            default:
                break;
        }
    }
}
