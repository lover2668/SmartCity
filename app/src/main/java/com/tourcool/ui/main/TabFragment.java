package com.tourcool.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.threadpool.ThreadPoolManager;
import com.frame.library.core.util.SizeUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.gyf.immersionbar.ImmersionBar;
import com.tourcool.bean.home.HomeBean;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.library.frame.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月30日11:00
 * @Email: 971613168@qq.com
 */
public class TabFragment extends BaseTitleFragment {
    public static final String TAG = "TabFragment";

    @Override
    public int getContentLayout() {
        return R.layout.fragment_tab_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        showLoading("加载中");
      baseHandler.postDelayed(() -> {
          long startTime = System.currentTimeMillis();
          setViewToBelowStatusBar(mContentView.findViewById(R.id.llContainer));
          loadTab(null);
          long endTime = System.currentTimeMillis();
          TourCooLogUtil.i("数据加载耗时:"+(endTime-startTime));
          closeLoading();
      },2500);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("瀑布流");
        titleBar.setVisibility(View.GONE);
    }


    public static TabFragment newInstance() {
        Bundle args = new Bundle();
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        if (isVisibleToUser) {
            setStatusBarModeWhite(TabFragment.this);
        }
    }

    private void loadTab(HomeBean homeBean) {
        LinearLayout llContainer = mContentView.findViewById(R.id.llContainer);
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_module_tab_layout, null);
        llContainer.addView(rootView);
        ViewPager viewPager = rootView.findViewById(R.id.vpContainer);
        MagicIndicator magicIndicator = rootView.findViewById(R.id.magicIndicator);
        List<String> titleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();
        int size = 10;
        for (int i = 0; i < size; i++) {
            fragmentList.add(NewsFragment.newInstance());
        }
        for (int i = 0; i < fragmentList.size(); i++) {
            titleList.add("热点资讯" + i);
        }


        ThreadPoolManager.getThreadPoolProxy().execute(() -> {
            viewPager.setOffscreenPageLimit(size);
            MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragmentList);
            viewPager.setAdapter(pagerAdapter);
            initTabLayout(magicIndicator, viewPager, titleList);
        });

    }


    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


    private void initTabLayout(MagicIndicator mMagicIndicator, ViewPager viewPager, List<String> mDataList) {
        mMagicIndicator.setBackgroundColor(TourCooUtil.getColor(R.color.whiteCommon));
        CommonNavigator mCommonNavigator = new CommonNavigator(mContext);
        mCommonNavigator.setSkimOver(true);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(index));
                clipPagerTitleView.setTextColor(TourCooUtil.getColor(R.color.black171717));
                clipPagerTitleView.setClipColor(TourCooUtil.getColor(R.color.blue5E8FEB));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
//                return null;
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineWidth(SizeUtil.dp2px(20));
                linePagerIndicator.setColors(TourCooUtil.getColor(R.color.blue4287FF));
                linePagerIndicator.setRoundRadius(UIUtil.dip2px(context, 5));
                linePagerIndicator.setStartInterpolator(new AccelerateInterpolator());
                linePagerIndicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                return linePagerIndicator;
            }
        });
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, viewPager);
    }

    private long computeConsuming(long startTime, long endTime) {
        return endTime - startTime;
    }



    private void loadViewPager(ViewPager viewPager, int limitSize) {
    }
}
