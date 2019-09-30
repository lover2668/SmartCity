package com.tourcool.ui.mvp.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.smartcity.R;


/**
 * @author :JenkinsZhou
 * @description :Level2认证
 * @company :途酷科技
 * @date 2019年09月26日9:34
 * @Email: 971613168@qq.com
 */
public class IdentifyLevel2Activity extends BaseMvpTitleActivity implements View.OnClickListener {
    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_identify_level2;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.tvConfirm).setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("Lv2认证");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvConfirm:
                Intent intent = new Intent();
                intent.setClass(mContext, IdentifyLevel3Activity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }



}
