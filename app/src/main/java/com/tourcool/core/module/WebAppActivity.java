package com.tourcool.core.module;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.aries.ui.util.StatusBarUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tourcool.smartcity.R;

/**
 * @Author: JenkinsZhou on 2019/4/24 13:41
 * @E-Mail: 971613168@qq.com
 * @Function: 加载WebApp的Activity
 * @Description:
 */
public class WebAppActivity extends WebViewActivity {

    private static int mColor;

    public static void start(Context mActivity, String url) {
        start(mActivity, url, Color.WHITE);
    }

    public static void start(Context mActivity, String url, int color) {
        mColor = color;
        start(mActivity, WebAppActivity.class, url);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setHeight(0)
                .setStatusBarLightMode(mColor == Color.WHITE)
                .setStatusAlpha(mColor == Color.WHITE && !StatusBarUtil.isSupportStatusBarFontChange() ? 60 : 0)
                .setBgColor(mColor)
                .setVisibility(View.VISIBLE);
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        super.setRefreshLayout(refreshLayout);
        refreshLayout.setRefreshHeader(new MaterialHeader(mContext).setColorSchemeColors(Color.MAGENTA, Color.BLUE))
                .setPrimaryColorsId(R.color.colorTextBlack)
                .setEnableHeaderTranslationContent(false);
    }
}
