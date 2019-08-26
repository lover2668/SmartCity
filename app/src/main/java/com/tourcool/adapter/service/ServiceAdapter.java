package com.tourcool.adapter.service;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.adapter.MatrixOldAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.TwoLevelBean;
import com.tourcool.bean.TwoLevelChildBean;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月21日16:37
 * @Email: 971613168@qq.com
 */
public class ServiceAdapter extends BaseQuickAdapter<TwoLevelBean, BaseViewHolder> {

    public ServiceAdapter() {
        super(R.layout.item_service_group_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TwoLevelBean item) {
        if (item == null) {
            return;
        }
        ImageView imageView = helper.getView(R.id.ivGroupIcon);
        GlideManager.loadImg(R.mipmap.icon_service_group, imageView);
        RecyclerView childRecyclerView = helper.getView(R.id.rvItemGrid);
        MatrixOldAdapter adapter = new MatrixOldAdapter();
        //二级布局为网格布局
        childRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter.bindToRecyclerView(childRecyclerView);
        adapter.setNewData(parseMatrixList(item.getChildBeans()));

    }


    private List<MatrixBean> parseMatrixList(List<TwoLevelChildBean> twoLevelChildBeanList) {
        List<MatrixBean> matrixBeanList = new ArrayList<>();
        if (twoLevelChildBeanList == null) {
            return matrixBeanList;
        }
        for (TwoLevelChildBean twoLevelChildBean : twoLevelChildBeanList) {
            if (twoLevelChildBean == null) {
                continue;
            }
            MatrixBean matrixBean = new MatrixBean(twoLevelChildBean.getChildItemIcon(), twoLevelChildBean.getChildItemTitle());
            matrixBeanList.add(matrixBean);
        }
        return matrixBeanList;
    }
}
