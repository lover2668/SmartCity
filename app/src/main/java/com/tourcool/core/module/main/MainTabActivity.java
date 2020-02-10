package com.tourcool.core.module.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.view.tab.BuildConfig;
import com.frame.library.core.util.ToastUtil;
import com.tourcool.core.module.WebViewActivity;
import com.frame.library.core.entity.FrameTabEntity;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.module.activity.FrameMainActivity;
import com.frame.library.core.retrofit.BaseObserver;
import com.aries.ui.view.tab.CommonTabLayout;
import com.didichuxing.doraemonkit.util.PermissionUtil;
import com.tourcool.core.permission.PermissionHelper;
import com.tourcool.core.permission.PermissionInterface;
import com.tourcool.core.widget.InputDialog;
import com.tourcool.core.widget.IosAlertDialog;
import com.tourcool.smartcity.R;
import com.tourcool.ui.main.MainHomeFragmentNew;
import com.tourcool.ui.main.MainMineFragment;
import com.tourcool.ui.main.TestFragment;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:00
 * @E-Mail: 971613168@qq.com
 * Function: 示例主页面
 * Description:
 */
public class MainTabActivity extends FrameMainActivity  implements EasyPermissions.PermissionCallbacks  {
    @BindView(R.id.tabLayout_commonFastLib)
    CommonTabLayout mTabLayout;
    /**
     * 随便赋值的一个唯一标识码
     */
    public static final int REQUEST_PERMISSION_STORAGE = 100;
    private boolean isFirst = false;
    //权限参数
    private String[] params = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = true;
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public List<FrameTabEntity> getTabList() {
        ArrayList<FrameTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new FrameTabEntity(R.string.home, R.mipmap.icon_tab_home_un_selected, R.mipmap.ic_home_select, MainHomeFragmentNew.newInstance()));
//        tabEntities.add(new FrameTabEntity("资讯", R.drawable.ic_app_normal, R.drawable.ic_app_selected, MainHomeFragmentNew.newInstance()));
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
    /*    RxJavaManager.getInstance().setTimer(2000)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    public void onRequestNext(Long entity) {
                        requestPermission();
                    }
                });*/

        checkPermission();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        //将结果传入EasyPermissions中
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }




    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //这个方法有个前提是，用户点击了“不再询问”后，才判断权限没有被获取到
            baseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSetting();
                }
            },500);
        } else if (!EasyPermissions.hasPermissions(this, params)) {
            //这里响应的是除了AppSettingsDialog这个弹出框，剩下的两个弹出框被拒绝或者取消的效果
            exitApp();
        }
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


    /**
     * 检查权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_STORAGE)
    private void checkPermission() {
        baseHandler.postDelayed(() -> {
            if (!EasyPermissions.hasPermissions(MainTabActivity.this, params)) {
                EasyPermissions.requestPermissions(MainTabActivity.this, "需要获取相关权限", REQUEST_PERMISSION_STORAGE, params);
            }
        }, 300);

    }

    private void skipDetailSettingIntent(){
        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        try {
           startActivityForResult(intent,REQUEST_PERMISSION_STORAGE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_PERMISSION_STORAGE ==requestCode ) {
            if (!EasyPermissions.hasPermissions(MainTabActivity.this, params)) {
                ToastUtil.show("您未授予权限 应用已退出");
            }
        }
    }

    private void showSetting(){
        new IosAlertDialog(mContext)
                .init()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("权限申请")
                .setMsg("应用需要您授予相关权限 请前往授权管理页面授权")
                .setPositiveButton("前往授权", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipDetailSettingIntent();
                    }
                })
                .setNegativeButton("退出应用", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitApp();
                    }
                }).show();
    }

    private void exitApp(){
        ToastUtil.show("您未授予相关权限 应用已退出");
        finish();
    }
}
