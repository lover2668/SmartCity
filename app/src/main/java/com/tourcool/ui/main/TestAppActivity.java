package com.tourcool.ui.main;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.TestMultipleAdapter;
import com.tourcool.bean.home.HomeChildBean;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.core.module.activity.BaseTitleActivity;
import com.tourcool.smartcity.R;

import java.util.ArrayList;
import java.util.List;

import static com.tourcool.bean.home.HomeChildItem.ITEM_TYPE_CONTENT;
import static com.tourcool.bean.home.HomeChildItem.ITEM_TYPE_TITLE;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月02日14:10
 * @Email: 971613168@qq.com
 */
public class TestAppActivity extends BaseTitleActivity {
    private TestMultipleAdapter adapter;
    private RecyclerView rvContentFrameLib;

    @Override
    public int getContentLayout() {
        return R.layout.frame_layout_title_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        rvContentFrameLib = findViewById(R.id.rvContentFrameLib);
        adapter = new TestMultipleAdapter(new ArrayList<>());
        adapter.bindToRecyclerView(rvContentFrameLib);
        adapter.setNewData(getHomeData());
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                int spanSize = viewType == ITEM_TYPE_CONTENT ? 3 : 1;
                TourCooLogUtil.i("返回的spanSize =" + spanSize);
                return spanSize;
            }
        });
        rvContentFrameLib.setLayoutManager(manager);

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("测试");
    }


    private List<HomeChildBean> getHomeData( ){
        int size = 5;
        List<HomeChildBean> data = new ArrayList<>();
        HomeChildBean childBean;
        for (int i = 0; i < size; i++) {
            childBean = new HomeChildBean();
            if (i % 2 == 0) {
                //标题部分
                childBean.setItemType(ITEM_TYPE_TITLE);
            } else {
                childBean.setItemType(ITEM_TYPE_CONTENT);
            }
            childBean.setTitle("标题"+i);
            childBean.setChildList(getHomeListItem(childBean));
            data.add(childBean);
        }
        return data;
    }
    private List<HomeChildItem> getHomeListItem(HomeChildBean homeChildBean) {
        List<HomeChildItem> list = new ArrayList<>();
        HomeChildItem homeChildItem;
        int size = 10;
        for (int i = 0; i < size; i++) {
            homeChildItem = new HomeChildItem();
            homeChildItem.setTitle(homeChildBean.getTitle());
            homeChildItem.setIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567416767454&di=3b1d5e0af6640b3a3a43b91bf00b13fc&imgtype=0&src=http%3A%2F%2Fdongying.dzwww.com%2Fyl%2F201908%2FW020190805297215210540.jpg");
            homeChildItem.setGroupTitle(homeChildBean.getTitle());
            if (i == 0) {
                homeChildItem.setParentGroup(false);
            }
            if (i % 2 == 0) {
                //标题部分
                homeChildItem.setItemType(ITEM_TYPE_TITLE);
            } else {
                homeChildItem.setItemType(ITEM_TYPE_CONTENT);
            }
            list.add(homeChildItem);
        }
        return list;
    }

}
