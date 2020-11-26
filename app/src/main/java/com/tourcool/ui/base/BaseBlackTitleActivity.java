package com.tourcool.ui.base;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.frame.library.core.module.activity.FrameTitleActivity;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.smartcity.R;

import static com.tourcool.core.config.AppConfig.TITLE_MAIN_TITLE_SIZE;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月16日14:58
 * @Email: 971613168@qq.com
 */
public abstract class BaseBlackTitleActivity extends FrameTitleActivity {

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (titleBar != null) {
            setMarginTop(titleBar);
            titleBar.setBgResource(R.color.white);
            titleBar.setLeftTextDrawable(R.drawable.ic_back);
            titleBar.setTitleMainTextColor(FrameUtil.getColor(R.color.black231717));
            titleBar.getMainTitleTextView().setTextSize(TITLE_MAIN_TITLE_SIZE);
        }
    }


    protected void setMarginTop(TitleBarView titleBarView) {
        if (titleBarView == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = titleBarView.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(0, getMaginTop(), 0, 0);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).setMargins(0, getMaginTop(), 0, 0);
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).setMargins(0, getMaginTop(), 0, 0);
        }
    }


    protected  void  skipActivity(String activityUrl){
        ARouter.getInstance().build(activityUrl).navigation();
    }

    @Override
    public boolean isStatusBarDarkMode() {
        return true;
    }
}
