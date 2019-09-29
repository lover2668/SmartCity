package com.tourcool.ui.main;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frame.library.core.basis.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tourcool.adapter.news.NewsAdapter;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月30日11:35
 * @Email: 971613168@qq.com
 */
public class NewsFragment extends BaseFragment {

    private NewsAdapter newsAdapter;
    private RecyclerView recyclerView;
    private SmartRefreshLayout mSmartRefresh;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_news_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private List<HomeChildItem> getNewsList(){
        List<HomeChildItem> homeChildItemList = new ArrayList<>();
        HomeChildItem homeChildItem ;
        int size = 50;
        for (int i = 0; i < size; i++) {
            homeChildItem = new HomeChildItem();
            if(i % 2 == 0){
                homeChildItem.setIcon("http://pic1.win4000.com/wallpaper/c/53cdd1f7c1f21.jpg");
            }
            homeChildItemList.add(homeChildItem);
        }
        return homeChildItemList;
    }

    @Override
    public void loadData() {
        newsAdapter = new NewsAdapter();
        mSmartRefresh = mContentView.findViewById(R.id.mSmartRefresh);
        mSmartRefresh.setRefreshHeader(new ClassicsHeader(mContext));
        recyclerView = mContentView.findViewById(R.id.recyclerNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        newsAdapter.bindToRecyclerView(recyclerView);
        newsAdapter.setNewData(getNewsList());
    }
}
