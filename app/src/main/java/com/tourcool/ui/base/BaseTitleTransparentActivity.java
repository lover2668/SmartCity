package com.tourcool.ui.base;


import com.frame.library.core.module.activity.FrameTitleActivity;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.library.frame.R;

import static com.tourcool.core.config.AppConfig.TITLE_MAIN_TITLE_SIZE;


/**
 * @author :JenkinsZhou
 * @description :透明白字状态栏
 * @company :途酷科技
 * @date 2019年09月27日14:50
 * @Email: 971613168@qq.com
 */
public abstract class BaseTitleTransparentActivity extends FrameTitleActivity {
    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (mTitleBar != null) {
            mTitleBar.setBackgroundColor(FrameUtil.getColor(R.color.transparent));
            mTitleBar.setLeftTextDrawable(R.drawable.ic_back_white);
            mTitleBar.setTitleMainTextColor(FrameUtil.getColor(R.color.whiteCommon));
            mTitleBar.getMainTitleTextView().setTextSize(TITLE_MAIN_TITLE_SIZE);
        }
    }
}
