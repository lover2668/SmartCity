package com.tourcool.ui.mvp.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.UserInfo;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.event.service.ServiceEvent;
import com.tourcool.smartcity.R;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :设置密码
 * @company :途酷科技
 * @date 2019年11月11日16:28
 * @Email: 971613168@qq.com
 */
public class SettingPassActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private EditText etPass;
    private EditText etPassConfirm;
    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_setting_password;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.tvSave).setOnClickListener(this);
        etPass = findViewById(R.id.etPass);
        etPassConfirm = findViewById(R.id.etPassConfirm);
        ImageView ivValid = findViewById(R.id.ivValid);
        ImageView ivClearPass = findViewById(R.id.ivClearPass);
        ImageView ivClearPassConfirm = findViewById(R.id.ivClearPassConfirm);
        listenInput(etPass,ivClearPass);
        listenInput(etPassConfirm,ivClearPassConfirm);
        listenInputValid(etPassConfirm,ivValid,etPass);
        listenInputValid(etPass,ivValid,etPassConfirm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSave:
                doSetPass();
                break;
            default:
                break;
        }
    }



    private void requestSetPass(String pass, String confirmPass) {
        ApiRepository.getInstance().requestSetPass(pass, confirmPass).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>("设置中...") {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            ToastUtil.showSuccess("设置成功");
                            requestUserInfoAndSendEvent();
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                    }
                });
    }

    private void doSetPass(){
      if(TextUtils.isEmpty(getTextValue(etPass))){
          ToastUtil.show("请输入密码");
      return;
      }
        if(TextUtils.isEmpty(getTextValue(etPassConfirm))){
            ToastUtil.show("请确认密码");
            return;
        }
        if(!getTextValue(etPass).equals(getTextValue(etPassConfirm))){
            ToastUtil.show("两次密码输入不一致");
            return;
        }
        requestSetPass(getTextValue(etPass),getTextValue(etPassConfirm));
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        setMarginTop(titleBar);
        titleBar.setTitleMainText("设置密码");
    }



    private void listenInputValid(EditText editText, ImageView imageView,EditText etPass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setViewGone(imageView, !TextUtils.isEmpty(getTextValue(etPass)) && (getTextValue(editText).equals(getTextValue(etPass)))) ;
            }
        });
    }

    @Override
    public boolean isStatusBarDarkMode() {
        return true;
    }


    /**
     * 请求用户信息并发送事件
     */
    private void requestUserInfoAndSendEvent() {
        ApiRepository.getInstance().requestUserInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        closeLoading();
                        if (entity == null) {
                            ToastUtil.showFailed("服务器异常");
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            UserInfo userInfo = parseJavaBean(entity.data, UserInfo.class);
                            if (userInfo == null) {
                                ToastUtil.showFailed("登录失败");
                                return;
                            }
                            //保存用户信息到本地
//                            ToastUtil.showSuccess("登录成功");
                            AccountHelper.getInstance().saveUserInfoToDisk(userInfo);
                            notitfyRefreshUserInfo(userInfo);
                            finish();
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        closeLoading();
                    }
                });
    }


    private void notitfyRefreshUserInfo(UserInfo userInfo) {
        UserInfoEvent userInfoEvent = new UserInfoEvent(userInfo);
        EventBus.getDefault().post(userInfoEvent);
        EventBus.getDefault().post(new ServiceEvent());
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
}
