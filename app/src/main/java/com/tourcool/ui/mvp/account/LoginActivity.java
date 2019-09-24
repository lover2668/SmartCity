package com.tourcool.ui.mvp.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.library.frame.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月24日17:42
 * @Email: 971613168@qq.com
 */
public class LoginActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private TextView tvGetCode;
    private TextView tvChangeLoginType;
    private boolean loginByPass = true;
    private LinearLayout llLoginVcode;
    private LinearLayout llLoginPass;

    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.tvLogin).setOnClickListener(this);
        tvGetCode = findViewById(R.id.tvGetCode);
        tvGetCode.setOnClickListener(this);
        tvChangeLoginType = findViewById(R.id.tvChangeLoginType);
        tvChangeLoginType.setOnClickListener(this);
        llLoginPass = findViewById(R.id.llLoginPass);
        llLoginVcode = findViewById(R.id.llLoginVcode);
        ImageView ivClearPhone = findViewById(R.id.ivClearPhone);
        ImageView ivClearPass = findViewById(R.id.ivClearPass);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etPhone = findViewById(R.id.etPhone);
        listenInput(etPhone, ivClearPhone);
        listenInput(etPassword, ivClearPass);
        changeLoginType();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("登录");
    }


    private void listenInput(EditText editText, ImageView imageView) {
        setViewGone(imageView, !TextUtils.isEmpty(editText.getText().toString()));
        imageView.setOnClickListener(v -> editText.setText(""));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setViewGone(imageView, s.length() != 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChangeLoginType:
                changeLoginType();
                break;
            default:
                break;
        }
    }


    private void changeLoginType() {
        if (loginByPass) {
            setViewGone(llLoginPass, true);
            setViewGone(llLoginVcode, false);
            loginByPass = false;
            tvChangeLoginType.setText("验证码登录");
        } else {
            setViewGone(llLoginPass, false);
            setViewGone(llLoginVcode, true);
            loginByPass = true;
            tvChangeLoginType.setText("密码登录");
        }
    }


}
