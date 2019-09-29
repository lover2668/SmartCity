package com.tourcool.ui.main;


import android.os.Bundle;

import com.frame.library.core.basis.BaseFragment;
import com.tourcool.library.frame.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月29日16:55
 * @Email: 971613168@qq.com
 */
public class LazyLoadFragmentTest extends BaseFragment {
    @Override
    public int getContentLayout() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }


    public static LazyLoadFragmentTest newInstance() {
        Bundle args = new Bundle();
        LazyLoadFragmentTest fragment = new LazyLoadFragmentTest();
        fragment.setArguments(args);
        return fragment;
    }



}
