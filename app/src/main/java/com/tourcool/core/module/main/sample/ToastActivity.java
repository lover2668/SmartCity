package com.tourcool.core.module.main.sample;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.frame.library.core.module.activity.FrameTitleActivity;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.library.frame.R;

import butterknife.OnClick;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:23
 * @E-Mail: 971613168@qq.com
 * @Function: ToastUtil工具类示例
 * @Description:
 */
public class ToastActivity extends FrameTitleActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_toast;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("ToastUtil工具类示例");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @OnClick({R.id.rtv_system, R.id.rtv_normal, R.id.rtv_success, R.id.rtv_failed, R.id.rtv_warning})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rtv_system:
                Toast.makeText(mContext, R.string.toast_system, Toast.LENGTH_SHORT).show();
                break;
            case R.id.rtv_normal:
                ToastUtil.show(R.string.toast_normal, ToastUtil.newBuilder().setGravity(Gravity.CENTER));
                break;
            case R.id.rtv_success:
                ToastUtil.showSuccess(R.string.toast_success);
                break;
            case R.id.rtv_failed:
                ToastUtil.showFailed(R.string.toast_failed);
                break;
            case R.id.rtv_warning:
                ToastUtil.showWarning(R.string.toast_warning);
                break;
        }
    }
}
