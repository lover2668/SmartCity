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

import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.smartcity.R;
import com.tourcool.ui.mvp.account.contract.RegisterContract;
import com.tourcool.ui.mvp.account.presenter.RegisterPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
        tvGetCode = findViewById(R.id.tvGetCode);
        tvGetCode.setOnClickListener(this);
        ImageView ivClearPhone = findViewById(R.id.ivClearPhone);
        ImageView ivClearPass = findViewById(R.id.ivClearPass);
        ImageView ivClearPassConfirm = findViewById(R.id.ivClearPassConfirm);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etPhone = findViewById(R.id.etPhone);
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
            case R.id.tvLogin:
                ToastUtil.show("登录");
                break;
            case R.id.tvGetCode:
                //验证码发送成功开始，倒计时
                countDownTime();
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
}
