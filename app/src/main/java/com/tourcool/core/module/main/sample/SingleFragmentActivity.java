package com.tourcool.core.module.main.sample;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.TypedValue;

import com.aries.library.fast.demo.R;
import com.tourcool.core.constant.ApiConstant;
import com.frame.library.core.module.activity.FrameTitleActivity;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:22
 * @E-Mail: 971613168@qq.com
 * @Function: Fragment单独嵌套--校验Fragment懒加载问题
 * @Description:
 */
public class SingleFragmentActivity extends FrameTitleActivity {
    private Fragment mFragment;

    @Override
    public int getContentLayout() {
        return R.layout.activity_test_fragment;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("Activity直接嵌套Fragment懒加载效果")
                .setTitleMainTextSize(TypedValue.COMPLEX_UNIT_DIP,16)
                .setTitleMainTextMarquee(true);
    }

    @Override
    public void loadData() {
        super.loadData();
        mFragment = SingleFragment.newInstance(ApiConstant.API_MOVIE_IN_THEATERS);
        //此处设置先隐藏再显示方能进入Fragment类的显示隐藏回调--懒加载
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fLayout_containerTestFragment, mFragment)
                .commit();
    }
}
