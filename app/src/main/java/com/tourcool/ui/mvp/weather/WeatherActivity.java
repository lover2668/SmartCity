package com.tourcool.ui.mvp.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.util.SizeUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.library.frame.R;
import com.tourcool.ui.base.BaseTitleTransparentActivity;

/**
 * @author :JenkinsZhou
 * @description :天气
 * @company :途酷科技
 * @date 2019年09月27日13:57
 * @Email: 971613168@qq.com
 */
public class WeatherActivity extends BaseTitleTransparentActivity implements View.OnClickListener {



    @Override
    public int getContentLayout() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvlevle2:
                Intent intent = new Intent();
                intent.setClass(mContext,WeatherActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("天气");
    }


}
