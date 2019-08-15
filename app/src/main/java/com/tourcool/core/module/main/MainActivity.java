package com.tourcool.core.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.aries.library.fast.demo.BuildConfig;
import com.aries.library.fast.demo.R;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.module.activity.ActivityFragment;
import com.tourcool.core.module.mine.MineFragment;
import com.tourcool.core.module.web.WebAppFragment;
import com.frame.library.core.entity.FrameTabEntity;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.module.activity.FrameMainActivity;
import com.frame.library.core.retrofit.BaseObserver;
import com.aries.ui.view.tab.CommonTabLayout;
import com.didichuxing.doraemonkit.util.PermissionUtil;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:00
 * @E-Mail: 971613168@qq.com
 * Function: 示例主页面
 * Description:
 */
public class MainActivity extends FrameMainActivity {

    @BindView(R.id.tabLayout_commonFastLib)
    CommonTabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public List<FrameTabEntity> getTabList() {
        ArrayList<FrameTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new FrameTabEntity(R.string.home, R.drawable.ic_home_normal, R.drawable.ic_home_selected, HomeFragment.newInstance()));
        tabEntities.add(new FrameTabEntity(R.string.web_app, R.drawable.ic_app_normal, R.drawable.ic_app_selected, WebAppFragment.newInstance()));
        tabEntities.add(new FrameTabEntity(R.string.activity, R.drawable.ic_activity_normal, R.drawable.ic_activity_selected, ActivityFragment.newInstance()));
        tabEntities.add(new FrameTabEntity(R.string.mine, R.drawable.ic_mine_normal, R.drawable.ic_mine_selected, MineFragment.newInstance()));
        return tabEntities;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            WebViewActivity.start(mContext, url);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            WebViewActivity.start(mContext, url);
        }
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        RxJavaManager.getInstance().setTimer(2000)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    public void _onNext(Long entity) {
                        requestPermission();
                    }
                });
    }

    /**
     * 检查悬浮窗权限-哆啦A梦
     */
    private void requestPermission() {
        if (BuildConfig.DEBUG && !PermissionUtil.canDrawOverlays(mContext)) {
            new AlertDialog.Builder(mContext)
                    .setTitle("FastLib温馨提示")
                    .setMessage("哆啦A梦研发助手需求请求悬浮窗权限,如果需要使用研发助手,请到设置页面开启悬浮窗权限")
                    .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PermissionUtil.requestDrawOverlays(mContext);
                        }
                    })
                    .create()
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoggerManager.i(TAG, "onDestroy");
    }
}
