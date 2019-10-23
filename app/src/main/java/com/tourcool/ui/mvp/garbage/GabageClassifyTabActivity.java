package com.tourcool.ui.mvp.garbage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.frame.library.core.widget.EmiViewPager;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.smartcity.R;

import java.util.ArrayList;
import java.util.List;

import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_GABAGE_CLASSIFY;

/**
 * @author :JenkinsZhou
 * @description :垃圾分类
 * @company :途酷科技
 * @date 2019年10月23日10:48
 * @Email: 971613168@qq.com
 */
@Route(path = ACTIVITY_URL_GABAGE_CLASSIFY)
public class GabageClassifyTabActivity extends BaseMvpTitleActivity implements  ViewPager.OnPageChangeListener {
    private EmiViewPager emiViewPager;
    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_tab_gabage_classify;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        List<Fragment> fragmentList = new ArrayList<>();
        emiViewPager = findViewById(R.id.emiViewPager);
        fragmentList.add(GabageClassifyFragment.newInstance());
        fragmentList.add(GabageClassifyFragment.newInstance());
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        emiViewPager.setOffscreenPageLimit(2);
        emiViewPager.addOnPageChangeListener(this);
        emiViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

}
