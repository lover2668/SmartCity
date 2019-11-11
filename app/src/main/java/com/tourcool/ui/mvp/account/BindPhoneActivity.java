package com.tourcool.ui.mvp.account;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.UserInfo;
import com.tourcool.bean.weather.WeatherEntity;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.smartcity.R;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

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
 * @description :绑定手机
 * @company :途酷科技
 * @date 2019年10月09日10:28
 * @Email: 971613168@qq.com
 */
public class BindPhoneActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private TextView tvGetCode;
    private Handler mHandler = new Handler();
    private List<Disposable> disposableList = new ArrayList<>();
    private int timeCount = COUNT;
    private EditText etVcode;
    private EditText etPhoneNumber;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetCode:
                sendVCodeAndCountDownTime(getTextValue(etPhoneNumber));
                break;
            case R.id.tvConfirm:
                doBindPhone();
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
        return R.layout.activity_phone_bind;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvGetCode = findViewById(R.id.tvGetCode);
        tvGetCode.setOnClickListener(this);
        etVcode = findViewById(R.id.etVcode);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("绑定手机");
    }


    /**
     * 倒计时
     */
    private void countDownTime() {
        reset();
        setClickEnable(tvGetCode, false);
        mHandler.postDelayed(() -> tvGetCode.setTextColor(FrameUtil.getColor(R.color.grayA2A2A2)), ONE_SECOND);
        RxJavaManager.getInstance().doEventByInterval(ONE_SECOND, new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableList.add(d);
            }


            @Override
            public void onNext(Long aLong) {
                --timeCount;
                setText(tvGetCode, "还有" + timeCount + "秒");
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


    private void reset() {
        setClickEnable(tvGetCode, true);
        timeCount = COUNT;
        setText(tvGetCode, "发送验证码");
        tvGetCode.setTextColor(FrameUtil.getColor(R.color.blue55A9FF));
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


    private void requestBindPhone(String bindPhone, String vCode) {
        ApiRepository.getInstance().requestBindPhone(bindPhone, vCode).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>("绑定中...") {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            ToastUtil.showSuccess("绑定成功");
                            syncUserInfo();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
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
                subscribe(new BaseLoadingObserver<BaseResult>("发送中...") {
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


    private void doBindPhone() {
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
        requestBindPhone(getTextValue(etPhoneNumber), getTextValue(etVcode));
    }


    private void syncUserInfo(){
        UserInfo userInfo = AccountHelper.getInstance().getUserInfo();
        if (userInfo != null) {
            userInfo.setPhoneNumber(getTextValue(etPhoneNumber));
            AccountHelper.getInstance().setUserInfo(userInfo);
            EventBus.getDefault().post(new UserInfoEvent(userInfo));
        }
    }
}
