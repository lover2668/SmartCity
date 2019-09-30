package com.tourcool.ui.mvp.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.smartcity.R;
import com.tourcool.ui.WebViewActivityTest;
import com.tourcool.ui.mvp.recharge.RechargeActivity;
import com.tourcool.ui.mvp.weather.WeatherActivity;

/**
 * @author :JenkinsZhou
 * @description :实名认证
 * @company :途酷科技
 * @date 2019年09月26日11:04
 * @Email: 971613168@qq.com
 */
public class CertificationRealNameActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private TextView tvlevle2;
    private TextView tvBankCard;
    private TextView tvFace;

    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_real_name_certification;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvlevle2 = findViewById(R.id.tvlevle2);
        tvlevle2.setOnClickListener(this);
        tvBankCard = findViewById(R.id.tvBankCard);
        tvBankCard.setOnClickListener(this);
        tvFace = findViewById(R.id.tvFace);
        tvFace.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("实名认证");
        titleBar.setBgColor(FrameUtil.getColor(R.color.transparent));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvlevle2:
                Intent intent = new Intent();
                intent.setClass(mContext, WeatherActivity.class);
                startActivity(intent);
                break;
            case R.id.tvBankCard:
                Intent rechargeIntent = new Intent();
                rechargeIntent.setClass(mContext, RechargeActivity.class);
                startActivity(rechargeIntent);
                break;
            case R.id.tvFace:
                Intent webIntent = new Intent();
                webIntent.setClass(mContext, WebViewActivityTest.class);
                startActivity(webIntent);
//                WebViewActivity.start(mContext, "http://www.baidu.com/");
                break;
            default:
                break;
        }
    }


}
