package com.tourcool.ui.mvp.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.ToastUtil;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.config.RequestConfig.MSG_SEND_SUCCESS;
import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_FORGET_PASS;
import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_LOGIN;
import static com.tourcool.core.constant.TimeConstant.COUNT;
import static com.tourcool.core.constant.TimeConstant.ONE_SECOND;

/**
 * @author :JenkinsZhou
 * @description :忘记密码
 * @company :途酷科技
 * @date 2019年10月30日17:42
 * @Email: 971613168@qq.com
 */
@Route(path = ACTIVITY_URL_FORGET_PASS)
public class ForgetPasswordActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private int timeCount = COUNT;
    private List<Disposable> disposableList = new ArrayList<>();
    private EditText etPhoneNumber;
    private EditText etVcode;
    private EditText etNewPass;
    private EditText etPasswordConfirm;
    private TextView tvGetCode;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvConfirm:
                doResetPass();
                break;
            case R.id.tvGetCode:
                //验证码发送成功开始，倒计时
                sendVCodeAndCountDownTime(getTextValue(etPhoneNumber));
                break;
            default:
                break;
        }
    }

    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etVcode = findViewById(R.id.etVcode);
        etNewPass = findViewById(R.id.etNewPass);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        tvGetCode = findViewById(R.id.tvGetCode);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
    }


    private void requestResetPass(String phone,String vCode,String pass,String confirmPass) {
        ApiRepository.getInstance().requestResetPass(phone,vCode,pass,confirmPass).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            TourCooLogUtil.i(TAG, entity);
                            //处理返回的实体数据
                            ToastUtil.showSuccess("密码重置成功");
                            finish();
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        ToastUtil.show("服务器异常:" + e.toString());
                    }
                });
    }



    private void doResetPass(){
        if (TextUtils.isEmpty(getTextValue(etPhoneNumber))) {
            ToastUtil.show("请输入手机号");
            return;
        }
        if (!TourCooUtil.isMobileNumber(getTextValue(etPhoneNumber))) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(getTextValue(etVcode))) {
            ToastUtil.show("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(getTextValue(etNewPass))) {
            ToastUtil.show("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(getTextValue(etPasswordConfirm))) {
            ToastUtil.show("请确认新密码");
            return;
        }
        if (!getTextValue(etPasswordConfirm).equals(getTextValue(etNewPass))) {
            ToastUtil.show("两次密码不一致");
            return;
        }
        requestResetPass(getTextValue(etPhoneNumber),getTextValue(etVcode),getTextValue(etNewPass),getTextValue(etPasswordConfirm));
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

}
