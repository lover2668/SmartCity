package com.tourcool.ui.kitchen;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月06日18:02
 * @Email: 971613168@qq.com
 */
public class LivePlayer extends StandardGSYVideoPlayer {
    public LivePlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LivePlayer(Context context) {
        super(context);
    }

    public LivePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return  R.layout.live_player_layout;
    }

    @Override
    protected void setStateAndUi(int state) {
        super.setStateAndUi(state);
    }
}
