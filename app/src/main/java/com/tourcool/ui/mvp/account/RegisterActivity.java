package com.tourcool.ui.mvp.account;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.module.main.MainTabActivity;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;
import com.tourcool.ui.mvp.account.contract.RegisterContract;
import com.tourcool.ui.mvp.account.presenter.RegisterPresenter;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.config.RequestConfig.MSG_SEND_SUCCESS;
import static com.tourcool.core.constant.TimeConstant.COUNT;
import static com.tourcool.core.constant.TimeConstant.ONE_SECOND;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月24日11:09
 * @Email: 971613168@qq.com
 */
public class RegisterActivity extends BaseMvpTitleActivity<RegisterPresenter> implements RegisterContract.LoginView, View.OnClickListener {
    private TextView tvGetCode;
    private int timeCount = COUNT;
    private List<Disposable> disposableList = new ArrayList<>();
    private Handler mHandler = new Handler();
    private EditText etPhone;
    private EditText etVcode;
    private EditText etPassword;
    private EditText etPasswordConfirm;

    @Override
    protected void loadPresenter() {

    }

    @Override
    protected RegisterPresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.tvRegister).setOnClickListener(this);
        etVcode = findViewById(R.id.etVcode);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        tvGetCode = findViewById(R.id.tvGetCode);
        findViewById(R.id.tvRegister).setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        ImageView ivClearPhone = findViewById(R.id.ivClearPhone);
        ImageView ivClearPass = findViewById(R.id.ivClearPass);
        ImageView ivClearPassConfirm = findViewById(R.id.ivClearPassConfirm);
        EditText etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);

        EditText etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        ImageView ivPhoneValid = findViewById(R.id.ivPhoneValid);
        listenInput(etPhone, ivClearPhone);
        listenInput(etPassword, ivClearPass);
        listenInput(etPasswordConfirm, ivClearPassConfirm);
        listenInputPhoneValid(etPhone, ivPhoneValid);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        setMarginTop(titleBar);
        titleBar.setTitleMainText("注册");
    }


    @Override
    public void clearPassword(View view) {

    }

    @Override
    public ImageView getClearIcon() {
        return null;
    }

    @Override
    public boolean showClearIcon() {
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegister:
                doRegister();
                break;
            case R.id.tvGetCode:
                //验证码发送成功开始，倒计时
                sendVCodeAndCountDownTime(getTextValue(etPhone));
                break;
            default:
                break;
        }
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


    private void setClickEnable(boolean clickEnable) {
        tvGetCode.setEnabled(clickEnable);
    }

    private void setText(String text) {
        tvGetCode.setText(text);
    }

    private void reset() {
        setClickEnable(true);
        timeCount = COUNT;
        setText("发送验证码");
        tvGetCode.setTextColor(FrameUtil.getColor(R.color.blue55A9FF));
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
                subscribe(new BaseLoadingObserver<BaseResult>() {
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


    private void doRegister() {
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
        if (TextUtils.isEmpty(getTextValue(etPassword))) {
            ToastUtil.show("请输入密码");
            return;
        }
        if (!getTextValue(etPassword).equals(getTextValue(etPasswordConfirm))) {
            ToastUtil.show("两次输入密码不一致");
            return;
        }
        //执行登录
        register();
    }


    /**
     * 注册请求
     */
    private void register() {
        ApiRepository.getInstance().register(getTextValue(etPhone), getTextValue(etVcode), getTextValue(etPassword), getTextValue(etPasswordConfirm)).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            ToastUtil.showSuccess("注册成功");
                            //注册成功后 由于用户信息没有直接返回所以需要再调用登录接口
                            loginByPassword(getTextValue(etPhone),getTextValue(etPassword));
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }
                });
    }




    /**
     * 账号密码登录
     *
     * @param phone
     * @param password
     */
    private void loginByPassword(String phone, String password) {
        showLoading("登录中...");
        ApiRepository.getInstance().loginByPassword(phone, password).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        closeLoading();
                        if (entity == null) {
                            ToastUtil.showFailed("服务器异常");
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            TourCooLogUtil.i(TAG,entity);
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

}
