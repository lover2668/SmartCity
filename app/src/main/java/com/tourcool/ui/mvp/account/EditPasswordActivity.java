package com.tourcool.ui.mvp.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;
import com.trello.rxlifecycle3.android.ActivityEvent;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :修改密码
 * @company :途酷科技
 * @date 2019年10月08日18:01
 * @Email: 971613168@qq.com
 */
public class EditPasswordActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private EditText etConfirmPass;
    private EditText etNewPass;
    private EditText etOldPass;

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
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etNewPass);
        etConfirmPass = findViewById(R.id.etConfirmPass);
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
            case R.id.tvConfirm:
                if (!AccountHelper.getInstance().isLogin()) {
                    ToastUtil.show("请先登录");
                    return;
                }
                doEditPass();
                break;
            default:
                break;
        }
    }


    private void doEditPass() {
        if (TextUtils.isEmpty(getTextValue(etOldPass))) {
            ToastUtil.show("请输入原密码");
            return;
        }
        if (TextUtils.isEmpty(getTextValue(etNewPass))) {
            ToastUtil.show("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(getTextValue(etConfirmPass))) {
            ToastUtil.show("请再次输入新密码");
            return;
        }
        if (!getTextValue(etNewPass).equals(getTextValue(etConfirmPass))) {
            ToastUtil.show("两次输入密码不一致");
            return;
        }
        //执行修改密码
        editPass();
    }


    /**
     * 修改密码
     */
    private void editPass() {
        ApiRepository.getInstance().requestChangePass(getTextValue(etOldPass), getTextValue(etNewPass), getTextValue(etConfirmPass)).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            ToastUtil.showSuccess("修改成功");
                            finish();
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }
                });
    }




}
