package com.tourcool.ui.mvp.account;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcool.core.constant.TimeConstant.COUNT;
import static com.tourcool.core.constant.TimeConstant.ONE_SECOND;

/**
 * @author :JenkinsZhou
 * @description :认证level1
 * @company :途酷科技
 * @date 2019年09月25日14:42
 * @Email: 971613168@qq.com
 */
public class IdentifyLevel1Activity extends BaseMvpTitleActivity implements View.OnClickListener {
    private List<Disposable> disposableList = new ArrayList<>();
    private int timeCount = COUNT;
    private TextView tvGetCode;
    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_identify_level1;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvGetCode = findViewById(R.id.tvGetCode);
        tvGetCode.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
            super.setTitleBar(titleBar);
        titleBar.setTitleMainText("Lv1认证");
    }



    /**
     * 倒计时
     */
    private void countDownTime(TextView textView) {
        reset(textView);
        setClickEnable(textView,false);
        mHandler.postDelayed(() -> textView.setTextColor(FrameUtil.getColor(R.color.grayA2A2A2)), ONE_SECOND);
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
                reset(tvGetCode);
                cancelTime();
            }
        });
    }


    private void reset(TextView textView) {
        setClickEnable(textView,true);
        timeCount = COUNT;
        setText("发送验证码");
        textView.setTextColor(FrameUtil.getColor(R.color.blue55A9FF));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetCode:
                //验证码发送成功开始，倒计时
                showLoading("发送中...");
                mHandler.postDelayed(() -> {
                    closeLoading();
                    ToastUtil.showSuccess("发送成功");
                    countDownTime(tvGetCode);
                }, 1000);
                break;
            default:
                break;
        }
    }
}
