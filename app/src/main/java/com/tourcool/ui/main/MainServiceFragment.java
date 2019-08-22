package com.tourcool.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.service.CategoryServiceNameAdapter;
import com.tourcool.adapter.service.ServiceAdapter;
import com.tourcool.bean.CategoryServiceBean;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.ServiceGroupItem;
import com.tourcool.bean.TwoLevelBean;
import com.tourcool.bean.TwoLevelChildBean;
import com.tourcool.core.module.main.MainTabActivity;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :服务专栏
 * @company :途酷科技
 * @date 2019年08月21日10:00
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public class MainServiceFragment extends BaseTitleFragment {
    private RecyclerView rvClassify;
    private RecyclerView rvGrid;
    private CategoryServiceNameAdapter categoryServiceNameAdapter;
    private ServiceAdapter serviceAdapter;

    @Override
    public int getContentLayout() {
        return R.layout.frament_main_service;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setStatusBarModeWhite(MainServiceFragment.this);
        initRecyclerView();
//        initAdapter();


        rvClassify.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rvGrid.scrollBy(dx, dy);
                }
            }
        });

        rvGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rvClassify.scrollBy(dx, dy);
                }
            }
        });


    }

    private void initAdapter() {
        categoryServiceNameAdapter = new CategoryServiceNameAdapter();
        categoryServiceNameAdapter.bindToRecyclerView(rvClassify);
        serviceAdapter = new ServiceAdapter();
        categoryServiceNameAdapter.setNewData(getCategoryServiceList());
        serviceAdapter.bindToRecyclerView(rvGrid);
        TourCooLogUtil.i(TAG, "数据长度:" + getTwoLevelList());
        serviceAdapter.setNewData(getTwoLevelList());
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBackgroundColor(TourCooUtil.getColor(R.color.blue4287FF));
        TextView mainText = titleBar.getMainTitleTextView();
        titleBar.setTitleMainText("智慧宜兴");
        mainText.setText("");
        mainText.setTextColor(TourCooUtil.getColor(R.color.white));
        mainText.setCompoundDrawablesWithIntrinsicBounds(null, null, TourCooUtil.getDrawable(R.mipmap.icon_title_name), null);
        titleBar.setBgResource(R.drawable.bg_gradient_title_common);
    }


    public static MainServiceFragment newInstance() {
        Bundle args = new Bundle();
        MainServiceFragment fragment = new MainServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void initRecyclerView() {
        rvClassify = mContentView.findViewById(R.id.rvCategory);
        rvClassify.setLayoutManager(new LinearLayoutManager(mContext));
        rvGrid = mContentView.findViewById(R.id.rvGrid);
        rvGrid.setLayoutManager(new LinearLayoutManager(mContext));

    }


    private List<TwoLevelBean> getTwoLevelList() {
        int size = 5;
        int childSize = 6;
        List<TwoLevelBean> twoLevelBeanList = new ArrayList<>();
        List<TwoLevelChildBean> twoLevelChildBeanList;
        TwoLevelChildBean childBean;
        TwoLevelBean twoLevelBean;
        for (int i = 0; i < size; i++) {
            twoLevelBean = new TwoLevelBean();
            twoLevelBeanList.add(twoLevelBean);
            twoLevelBean.setGroupName("交通" + i);
            twoLevelChildBeanList = new ArrayList<>();
            for (int j = 0; j < childSize; j++) {
                childBean = new TwoLevelChildBean();
                childBean.setChildItemTitle("城市公交" + j);
                twoLevelChildBeanList.add(childBean);
            }
            twoLevelBean.setChildBeans(twoLevelChildBeanList);

        }
        return twoLevelBeanList;
    }


    private List<CategoryServiceBean> getCategoryServiceList() {
        List<TwoLevelBean> levelBeanList = getTwoLevelList();
        List<CategoryServiceBean> categoryServiceBeanList = new ArrayList<>();
        for (int i = 0; i < levelBeanList.size(); i++) {
            TwoLevelBean twoLevelBean = levelBeanList.get(i);
            if (i == 0) {
                twoLevelBean.setSelected(true);
            }
            CategoryServiceBean categoryServiceBean = new CategoryServiceBean();
            categoryServiceBean.setCategoryName(twoLevelBean.getGroupName());
            categoryServiceBeanList.add(categoryServiceBean);
        }
        return categoryServiceBeanList;
    }


    private List<ServiceGroupItem> getGroupItem() {
        List<ServiceGroupItem> itemList = new ArrayList<>();
        MatrixBean matrixBean;
        List<MatrixBean> matrixBeanList;
        ServiceGroupItem.ItemInfo itemInfo;
        int size = 9;
        int childSize = 7;
        ServiceGroupItem groupItem;
        for (int i = 0; i < size; i++) {
            matrixBeanList = new ArrayList<>();
            for (int j = 0; j < childSize; j++) {
                matrixBean = new MatrixBean("", "矩阵" + j);
                matrixBeanList.add(matrixBean);
            }
            itemInfo = new ServiceGroupItem.ItemInfo("标题" + i, "group" + i, matrixBeanList);
            itemInfo.setGroup("group" + i);
            groupItem = new ServiceGroupItem(itemInfo);
            itemList.add(groupItem);
        }
        return itemList;
    }


    private boolean touchEventInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int left = location[0];
        int top = location[1];

        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        if (y >= top && y <= bottom && x >= left && x <= right) {
            return true;
        }

        return false;
    }

//    然后在fragment中注册使用这个方法
    /**
     * Fragment中，注册
     * 接收ChatActivity的Touch回调的对象
     * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
     */
    private MainTabActivity.MyTouchListener mTouchListener = new MainTabActivity.MyTouchListener() {

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            /**
             * 如果用户的手指同时放在屏幕上滑动，不要触发滚动事件。
             *
             */
            if (event.getPointerCount() >= 2) {
                return true;
            }

            /**
             * 如果左侧的RecyclerView1在滚动中，但是此时用户又在RecyclerView2中触发滚动事件，则停止所有滚动，等待新一轮滚动。
             *
             */
            if (rvClassify.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                if (touchEventInView(rvGrid, event.getX(), event.getY())) {
                    rvClassify.stopScroll();
                    rvGrid.stopScroll();
                    return true;
                }
            }

            /**
             * 如果右侧的RecyclerView2在滚动中，但是此时用户又在RecyclerView1中触发滚动事件，则停止所有滚动，等待新一轮滚动。
             *
             */
            if (rvGrid.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                if (touchEventInView(rvClassify, event.getX(), event.getY())) {
                    rvGrid.stopScroll();
                    rvClassify.stopScroll();
                    return true;
                }
            }
            return ((MainTabActivity) mContext).dispatchTouchEvent(event);
        }
    };

    //    注意，要在fragment的onAttach方法中注册回调
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//在该Fragment的构造函数中注册mTouchListener的回调
        if (mTouchListener != null) {
            if (getActivity() != null) {
                ((MainTabActivity) getActivity()).registerMyTouchListener(mTouchListener);
            }
        }
    }
}
