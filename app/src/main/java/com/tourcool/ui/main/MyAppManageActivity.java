package com.tourcool.ui.main;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.ModuleListAdapter;
import com.tourcool.adapter.drag.GridModuleAdapter;
import com.tourcool.bean.home.HomeChildBean;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.core.module.activity.BaseTitleActivity;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :应用管理
 * @company :途酷科技
 * @date 2019年08月28日16:07
 * @Email: 971613168@qq.com
 */
public class MyAppManageActivity extends BaseTitleActivity {
    private ModuleListAdapter moduleListAdapter;
    private Handler handler = new Handler();



    @Override
    public int getContentLayout() {
        return R.layout.activity_app_manager;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        RecyclerView rvContentFastLib = findViewById(R.id.rv_contentFastLib);
        rvContentFastLib.setLayoutManager(new LinearLayoutManager(mContext));
        HomeChildItem homeChildItem = new HomeChildItem();
        homeChildItem.setTitle("测试00");
        homeChildItem.setIcon("http://img1.imgtn.bdimg.com/it/u=1785199001,3375299815&fm=214&gp=0.jpg");
        HomeChildItem homeChildItem1 = new HomeChildItem();
        homeChildItem1.setTitle("测试01");
        homeChildItem1.setIcon("http://img1.imgtn.bdimg.com/it/u=1785199001,3375299815&fm=214&gp=0.jpg");
        List<HomeChildItem> homeChildItems  = new ArrayList<>();
        homeChildItems.add(homeChildItem);
        homeChildItems.add(homeChildItem1);
        moduleListAdapter = new ModuleListAdapter(homeChildItems);
        moduleListAdapter.bindToRecyclerView(rvContentFastLib);
        moduleListAdapter.setNewData(getBeanList());

     handler.postDelayed(new Runnable() {
         @Override
         public void run() {


         }
     },2000);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("应用管理");
    }

    private List<HomeChildBean> getBeanList() {
        List<HomeChildBean> beanList = new ArrayList<>();
        List<HomeChildItem> childBeanList;
        int size = 3;
        int childSize = 5;
        HomeChildBean homeChildBean;
        HomeChildItem homeChildItem;
        for (int i = 0; i < size; i++) {
            homeChildBean = new HomeChildBean();
            childBeanList = new ArrayList<>();
            homeChildBean.setTitle("交通" + i);
            for (int j = 0; j < childSize; j++) {
                homeChildItem = new HomeChildItem();
                homeChildItem.setTitle("测试" + j+i);
                homeChildItem.setIcon("http://img1.imgtn.bdimg.com/it/u=1785199001,3375299815&fm=214&gp=0.jpg");
                homeChildItem.setParentGroup(true);
                childBeanList.add(homeChildItem);
            }
            homeChildBean.setChildList(childBeanList);
            beanList.add(homeChildBean);
        }
        return beanList;
    }



}
