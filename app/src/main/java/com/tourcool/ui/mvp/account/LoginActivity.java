package com.tourcool.ui.mvp.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.TokenInfo;
import com.tourcool.bean.account.UserInfo;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.constant.RouteConstance;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.event.service.ServiceEvent;
import com.tourcool.smartcity.R;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.config.RequestConfig.MSG_SEND_SUCCESS;
import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_LOGIN;
import static com.tourcool.core.constant.TimeConstant.COUNT;
import static com.tourcool.core.constant.TimeConstant.ONE_SECOND;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月24日17:42
 * @Email: 971613168@qq.com
 */
@Route(path = ACTIVITY_URL_LOGIN)
public class LoginActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private int timeCount = COUNT;
    private TextView tvGetCode;
    private TextView tvChangeLoginType;
    private boolean loginByVcode = false;
    private LinearLayout llLoginVcode;
    private LinearLayout llLoginPass;
    private Handler mHandler = new Handler();
    private List<Disposable> disposableList = new ArrayList<>();
    private EditText etPhone;
    private EditText etVcode;
    private EditText etPassword;
    /**
     * 用户是否设置过密码
     */
    private boolean hasPassword;

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
        findViewById(R.id.tvSkipRegister).setOnClickListener(this);
        findViewById(R.id.tvForgetPass).setOnClickListener(this);
        ImageView ivClearPhone = findViewById(R.id.ivClearPhone);
        ImageView ivClearPass = findViewById(R.id.ivClearPass);
        ImageView ivPhoneValid = findViewById(R.id.ivPhoneValid);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etVcode = findViewById(R.id.etVcode);
        listenInput(etPhone, ivClearPhone);
        listenInput(etPassword, ivClearPass);
        listenInputPhoneValid(etPhone, ivPhoneValid);
        showLoginByType();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        setMarginTop(titleBar);
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
                if (loginByVcode) {
                    loginByVcode = false;
                } else {
                    loginByVcode = true;
                }
                showLoginByType();
                break;
            case R.id.tvGetCode:
                //验证码发送成功开始，倒计时
                sendVCodeAndCountDownTime(getTextValue(etPhone));

                break;
            case R.id.tvSkipRegister:
                Intent intent = new Intent();
                intent.setClass(mContext, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tvLogin:
                if (loginByVcode) {
                    doLoginByVcode();
                } else {
                    doLoginByPhoneNumber();
                }
                break;
            case R.id.tvForgetPass:
                Intent intent1 = new Intent();
                intent1.setClass(mContext,ForgetPasswordActivity.class);
                startActivity(intent1);
//                skipActivity(RouteConstance.ACTIVITY_URL_FORGET_PASS);
                break;
            default:
                break;
        }
    }


    private void showLoginByType() {
        if (loginByVcode) {
            setViewGone(llLoginPass, false);
            setViewGone(llLoginVcode, true);
            tvChangeLoginType.setText("密码登录");
        } else {
            setViewGone(llLoginPass, true);
            setViewGone(llLoginVcode, false);
            tvChangeLoginType.setText("验证码登录");
        }
    }


    private void reset() {
        setClickEnable(true);
        timeCount = COUNT;
        setText("发送验证码");
        tvGetCode.setTextColor(FrameUtil.getColor(R.color.blue55A9FF));
    }


    private void setClickEnable(boolean clickEnable) {
        tvGetCode.setEnabled(clickEnable);
    }

    private void setText(String text) {
        tvGetCode.setText(text);
    }


    private void cancelTime() {
        if (disposableList != null && !disposableList.isEmpty()) {
            Disposable disposable;
            for (int i = 0; i < disposableList.size(); i++) {
                disposable = disposableList.get(i);
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    disposableList.remove(disposable);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        cancelTime();
        super.onDestroy();
    }


    private void listenInputPhoneValid(EditText editText, ImageView imageView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setViewGone(imageView, StringUtil.isPhoneNumber(s.toString()));
            }
        });
    }


    @Override
    public boolean isStatusBarDarkMode() {
        return true;
    }


    /**
     * 账号密码登录
     *
     * @param phone
     * @param password
     */
    private void loginByPassword(String phone, String password) {
        ApiRepository.getInstance().loginByPassword(phone, password).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            ToastUtil.showFailed("服务器异常");
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            TokenInfo tokenInfo = parseJavaBean(entity.data, TokenInfo.class);
                            if (tokenInfo != null) {
                                saveTokenInfo(tokenInfo);
                                //获取用户信息
                                requestUserInfoAndSendEvent(loginByVcode);
                            }
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                            closeLoading();
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        closeLoading();
                    }
                });
    }


    /**
     * 根据手机账号密码登录
     */
    private void doLoginByPhoneNumber() {
        if (TextUtils.isEmpty(getTextValue(etPhone))) {
            ToastUtil.show("请先输入手机号");
            return;
        }
        if (!TourCooUtil.isMobileNumber(getTextValue(etPhone))) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }

        if (TextUtils.isEmpty(getTextValue(etPassword))) {
            ToastUtil.show("请输入密码");
            return;
        }
        showLoading("登录中...");
        loginByPassword(getTextValue(etPhone), getTextValue(etPassword));
    }


    /**
     * 根据手机账号密码登录
     */
    private void doLoginByVcode() {
        if (TextUtils.isEmpty(getTextValue(etPhone))) {
            ToastUtil.show("请先输入手机号");
            return;
        }
        if (!TourCooUtil.isMobileNumber(getTextValue(etPhone))) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }

        if (TextUtils.isEmpty(getTextValue(etVcode))) {
            ToastUtil.show("请输入验证码");
            return;
        }
        showLoading("登录中...");
        loginByVcode(getTextValue(etPhone), getTextValue(etVcode));
    }

    private void saveTokenInfo(TokenInfo tokenInfo) {
        if (tokenInfo == null) {
            return;
        }
        TourCooLogUtil.i(TAG, "保存的访问token：" + tokenInfo.getAccess_token());
        AccountHelper.getInstance().setAccessToken(tokenInfo.getAccess_token());
        AccountHelper.getInstance().setRefreshToken(tokenInfo.getRefresh_token());
        TourCooLogUtil.i(TAG, "获取到保存的访问token：" + AccountHelper.getInstance().getAccessToken());
    }

    /**
     * 请求用户信息并发送事件
     */
    private void requestUserInfoAndSendEvent(boolean loginByVcode) {
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
                            //用户使用验证码登录 并且没有设置过密码
                            if (loginByVcode && !hasPassword) {
                                skipSettingPassAndFinish();
                            }else {
                                finish();
                            }
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

    /**
     * 账号密码登录
     *
     * @param phone
     * @param password
     */
    private void loginByVcode(String phone, String password) {
        ApiRepository.getInstance().loginByVcode(phone, password).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            ToastUtil.showFailed("服务器异常");
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            TokenInfo tokenInfo = parseJavaBean(entity.data, TokenInfo.class);
                            if (tokenInfo != null) {
                                //保存获取到的用户信息
                                saveTokenInfo(tokenInfo);
                                hasPassword = tokenInfo.isHasPassword();
                                requestUserInfoAndSendEvent(loginByVcode);
                            }
                        } else {
                            closeLoading();
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

    /**
     * 倒计时
     */
    private void countDownTime() {
        reset();
        setClickEnable(false);
        mHandler.postDelayed(() -> tvGetCode.setTextColor(FrameUtil.getColor(R.color.grayA2A2A2)), ONE_SECOND);
        RxJavaManager.getInstance().doEventByInterval(ONE_SECOND, new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableList.add(d);
            }


            @Override
            public void onNext(Long aLong) {
                --timeCount;
                setText("还有" + timeCount + "秒");
                if (aLong >= COUNT - 1) {
                    onComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                cancelTime();
            }

            @Override
            public void onComplete() {
                reset();
                cancelTime();
            }
        });
    }


    /**
     * 验证码发送接口并倒计时
     *
     * @param phone
     */
    private void sendVCodeAndCountDownTime(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号");
            return;
        }
        if (!TourCooUtil.isMobileNumber(phone)) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        ApiRepository.getInstance().getVcode(phone).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>("发送中") {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            ToastUtil.showFailed("服务器异常");
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            ToastUtil.showSuccess(MSG_SEND_SUCCESS);
                            //验证码发送成功开始，倒计时
                            countDownTime();
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


    private void notitfyRefreshUserInfo(UserInfo userInfo) {
        UserInfoEvent userInfoEvent = new UserInfoEvent(userInfo);
        EventBus.getDefault().post(userInfoEvent);
        EventBus.getDefault().post(new ServiceEvent());
    }


    private void skipSettingPassAndFinish() {
        Intent intent = new Intent();
        intent.setClass(mContext, SettingPassActivity.class);
        startActivity(intent);
        finish();
    }
}
