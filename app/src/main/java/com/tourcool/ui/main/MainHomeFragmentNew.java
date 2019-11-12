package com.tourcool.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aries.ui.util.StatusBarUtil;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :主页
 * @company :途酷科技
 * @date 2019年11月12日9:51
 * @Email: 971613168@qq.com
 */
public class MainHomeFragmentNew extends BaseTitleFragment implements View.OnClickListener {
    private LinearLayout llContainer;
    @Override
    public int getContentLayout() {
        return R.layout.fragment_home_new;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        llContainer = mContentView.findViewById(R.id.llContainer);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {
        initSearchView();
    }

    public static MainHomeFragmentNew newInstance() {
        Bundle args = new Bundle();
        MainHomeFragmentNew fragment = new MainHomeFragmentNew();
        fragment.setArguments(args);
        return fragment;
    }




    private void initSearchView() {
        RelativeLayout rlSearch = mContentView.findViewById(R.id.rlSearch);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlSearch.getLayoutParams();
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(), 0, 0);
        rlSearch.setLayoutParams(params);
        rlSearch.post(() -> {
            int width = rlSearch.getMeasuredWidth();
            int height = rlSearch.getMeasuredHeight();
            TourCooLogUtil.i(TAG, "搜索框宽:" + width);
            TourCooLogUtil.i(TAG, "搜索框高:" + height);
        });
        rlSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
