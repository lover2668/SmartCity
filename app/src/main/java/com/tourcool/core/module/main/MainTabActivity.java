package com.tourcool.core.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;

import androidx.appcompat.app.AlertDialog;

import com.aries.ui.view.tab.BuildConfig;
import com.frame.library.core.log.TourCooLogUtil;
import com.tourcool.core.module.WebViewActivity;
import com.frame.library.core.entity.FrameTabEntity;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.module.activity.FrameMainActivity;
import com.frame.library.core.retrofit.BaseObserver;
import com.aries.ui.view.tab.CommonTabLayout;
import com.didichuxing.doraemonkit.util.PermissionUtil;
import com.tourcool.core.module.web.WebAppFragment;
import com.tourcool.smartcity.R;
import com.tourcool.ui.main.MainHomeFragment;
import com.tourcool.ui.main.MainHomeFragmentNew;
import com.tourcool.ui.main.MainMineFragment;
import com.tourcool.ui.main.MainServiceFragment;
import com.tourcool.ui.main.TabFragment;
import com.tourcool.ui.main.TestFragment;
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
public class MainTabActivity extends FrameMainActivity {

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
        tabEntities.add(new FrameTabEntity(R.string.home, R.mipmap.icon_tab_home_un_selected, R.mipmap.ic_home_select, MainHomeFragment.newInstance()));
        tabEntities.add(new FrameTabEntity("资讯", R.drawable.ic_app_normal, R.drawable.ic_app_selected, MainHomeFragmentNew.newInstance()));
        tabEntities.add(new FrameTabEntity("服务", R.mipmap.ic_service_unselect, R.mipmap.ic_service_select, TestFragment.newInstance()));
        tabEntities.add(new FrameTabEntity("我的", R.mipmap.ic_mine_unselect, R.mipmap.ic_mine_select, MainMineFragment.newInstance()));
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
                    public void onRequestNext(Long entity) {
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
                    .setTitle("温馨提示")
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


    public interface MyTouchListener {
        boolean dispatchTouchEvent(MotionEvent event);
    }

    /*
     * 保存MyTouchListener接口的列表
     */
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            if (listener != null) {
                return listener.dispatchTouchEvent(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }


}
