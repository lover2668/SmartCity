package com.tourcool.ui.mvp.account;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.library.frame.R;

/**
 * @author :JenkinsZhou
 * @description :Level3
 * @company :途酷科技
 * @date 2019年09月26日9:49
 * @Email: 971613168@qq.com
 */
public class IdentifyLevel3Activity extends BaseMvpTitleActivity implements  View.OnClickListener {

    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_identify_level3;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("Lv3认证");
    }


    @Override
    public void onClick(View v) {

    }
}
