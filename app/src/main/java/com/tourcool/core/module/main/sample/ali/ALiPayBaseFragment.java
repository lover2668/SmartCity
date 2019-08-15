package com.tourcool.core.module.main.sample.ali;

import android.graphics.Color;
import android.util.TypedValue;

import com.aries.library.fast.demo.R;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:19
 * @E-Mail: 971613168@qq.com
 * @Function: 支付宝-BaseFragment
 * @Description:
 */
public abstract class ALiPayBaseFragment extends BaseTitleFragment {
    String[] titles;
    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);
        titles = getResources().getStringArray(R.array.arrays_tab_ali);
        titleBar.setStatusAlpha(75)
                .setStatusBarLightMode(false)
                .setLeftTextColor(Color.WHITE)
                .setRightTextColor(Color.WHITE)
                .setLeftTextSize(TypedValue.COMPLEX_UNIT_DIP, 16)
                .setRightTextSize(TypedValue.COMPLEX_UNIT_DIP, 16)
                .setBackgroundResource(R.color.colorMainAli);
    }
}
