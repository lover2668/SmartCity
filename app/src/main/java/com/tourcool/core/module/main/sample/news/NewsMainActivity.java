package com.tourcool.core.module.main.sample.news;

import android.os.Bundle;

import com.frame.library.core.entity.FrameTabEntity;
import com.frame.library.core.module.activity.FrameMainActivity;
import com.aries.ui.view.tab.CommonTabLayout;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:01
 * @E-Mail: 971613168@qq.com
 * Function: 腾讯新闻
 * Description:
 */
public class NewsMainActivity extends FrameMainActivity {
    String[] titles;

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public List<FrameTabEntity> getTabList() {
        titles = getResources().getStringArray(R.array.arrays_tab_news);
        ArrayList<FrameTabEntity> list = new ArrayList<>();
        list.add(new FrameTabEntity(titles[0], R.drawable.ic_tab_news_main_normal, R.drawable.ic_tab_news_main_selected, NewsItemFragment.newInstance(0)));
        list.add(new FrameTabEntity(titles[1], R.drawable.ic_tab_news_recommend_normal, R.drawable.ic_tab_news_recommend_selected, NewsItemFragment.newInstance(1)));
        list.add(new FrameTabEntity(titles[2], R.drawable.ic_tab_news_live_normal, R.drawable.ic_tab_news_live_selected, NewsItemFragment.newInstance(2)));
        list.add(new FrameTabEntity(titles[3], R.drawable.ic_tab_news_mine_normal, R.drawable.ic_tab_news_mine_selected, NewsItemFragment.newInstance(3)));
        return list;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
//        tabLayout.setTextSize(10f)
//                .setIconMargin(SizeUtil.dp2px(2))
//                .setIconWidth(SizeUtil.dp2px(22))
//                .setIconHeight(SizeUtil.dp2px(22))
//                .setTextSelectColor(ContextCompat.getColor(mContext, R.color.colorMainNews));
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.setBackgroundResource(R.color.colorWhite);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
