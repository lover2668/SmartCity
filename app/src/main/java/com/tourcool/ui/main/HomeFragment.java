package com.tourcool.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.demo.R;
import com.aries.ui.view.title.TitleBarView;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.bean.MatrixBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2019年08月19日21:26
 * @Email: 971613168@qq.com
 */
public class HomeFragment extends BaseTitleFragment {
    private MatrixAdapter matrixAdapter;
    private RecyclerView rvMatrix;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home_yi_xing;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        rvMatrix = mContentView.findViewById(R.id.rvMatrix);
        matrixAdapter = new MatrixAdapter();
        rvMatrix.setLayoutManager(new GridLayoutManager(mContext, 5));
        matrixAdapter.bindToRecyclerView(rvMatrix);
        matrixAdapter.setNewData(getMatrixList());
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }


    private List<MatrixBean> getMatrixList() {
        List<MatrixBean> matrixBeanList = new ArrayList<>();
        MatrixBean matrixBean = new MatrixBean("", "巴士管家");
        matrixBeanList.add(matrixBean);
        MatrixBean matrixBean1 = new MatrixBean("", "话费充值");
        matrixBeanList.add(matrixBean1);
        MatrixBean matrixBean2 = new MatrixBean("", "智慧停车");
        matrixBeanList.add(matrixBean2);
        MatrixBean matrixBean3 = new MatrixBean("", "城市公交");
        matrixBeanList.add(matrixBean3);
        MatrixBean matrixBean4 = new MatrixBean("", "违章查询");
        matrixBeanList.add(matrixBean4);
        return matrixBeanList;
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
       HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
