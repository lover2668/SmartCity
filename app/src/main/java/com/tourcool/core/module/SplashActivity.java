package com.tourcool.core.module;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.module.activity.FrameTitleActivity;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.util.StackUtil;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.StatusBarUtil;
import com.tourcool.core.module.main.MainTabActivity;
import com.tourcool.library.frame.R;
import com.trello.rxlifecycle3.android.ActivityEvent;

import androidx.core.content.ContextCompat;

import butterknife.BindView;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:25
 * @E-Mail: 971613168@qq.com
 * @Function: 欢迎页
 * @Description:
 */
public class SplashActivity extends FrameTitleActivity {

    @BindView(R.id.tv_appSplash)
    TextView tvApp;
    @BindView(R.id.tv_versionSplash)
    TextView tvVersion;
    @BindView(R.id.tv_copyRightSplash)
    TextView tvCopyRight;

    @Override
    public void beforeSetContentView() {
        LoggerManager.i(TAG, "isTaskRoot:" + isTaskRoot() + ";getCurrent:" + StackUtil.getInstance().getCurrent());
        //防止应用后台后点击桌面图标造成重启的假象---MIUI及Flyme上发现过(原生未发现)
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.beforeSetContentView();
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setStatusBarLightMode(false)
                .setVisibility(View.GONE);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            return;
        }
        if (!StatusBarUtil.isSupportStatusBarFontChange()) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        tvApp.setCompoundDrawablesWithIntrinsicBounds(null,
                DrawableUtil.setTintDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_launcher).mutate(), Color.WHITE)
                , null, null);
        mContentView.setBackgroundResource(R.drawable.img_bg_login);
        tvVersion.setText("V" + FrameUtil.getVersionName(mContext));
        tvVersion.setTextColor(Color.WHITE);
        tvCopyRight.setTextColor(Color.WHITE);
        RxJavaManager.getInstance().setTimer(500)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        FrameUtil.startActivity(mContext, MainTabActivity.class);
                        finish();
                    }

                    @Override
                    public void onRequestNext(Long entity) {

                    }
                });
    }
}
