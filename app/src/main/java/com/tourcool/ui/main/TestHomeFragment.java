package com.tourcool.ui.main;

import android.os.Bundle;

import com.frame.library.core.basis.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tourcool.library.frame.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月27日10:15
 * @Email: 971613168@qq.com
 */
public class TestHomeFragment extends BaseFragment {
    private SmartRefreshLayout smartRefreshCommon;
    @Override
    public int getContentLayout() {
        return R.layout.fragment_home_yi_xing_new;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        smartRefreshCommon = mContentView.findViewById(R.id.smartRefreshCommon);
        smartRefreshCommon.setRefreshHeader(new ClassicsHeader(mContext));
    }


    public static TestHomeFragment newInstance() {
        Bundle args = new Bundle();
        TestHomeFragment fragment = new TestHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
