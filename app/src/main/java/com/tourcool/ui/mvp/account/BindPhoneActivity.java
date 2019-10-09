package com.tourcool.ui.mvp.account;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.smartcity.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetCode:
                //验证码发送成功开始，倒计时
                showLoading("发送中...");
                mHandler.postDelayed(() -> {
                    closeLoading();
                    ToastUtil.showSuccess("发送成功");
                    countDownTime();
                }, 1000);
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
}
