package com.tourcool.ui.kitchen;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.adapter.kitchen.KitchenDeviceAdapter;
import com.tourcool.bean.kitchen.KitchenGroup;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.smartcity.R;
import com.tourcool.ui.base.BaseCommonTitleActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.List;

import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月08日16:21
 * @Email: 971613168@qq.com
 */
public class TestListActivity extends BaseCommonTitleActivity implements OnRefreshListener {
    private KitchenDeviceAdapter adapter;
    private RecyclerView rvCommon;
    private SmartRefreshLayout mSmartRefresh;

    private StatusLayoutManager mStatusManager ;

    private String groupName;
    private TextView tvGroupName;


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("全部");
    }

    private void showSelectTitle( String group) {
        tvGroupName.setText(group);

    }
    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        groupName = getIntent().getStringExtra(DeviceListActivity.EXTRA_GROUP_NAME);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_bright_kitchen_device_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        rvCommon = findViewById(R.id.rvCommon);
        tvGroupName = findViewById(R.id.tvGroupName);
        rvCommon.setLayoutManager(new LinearLayoutManager(mContext));
        mSmartRefresh = findViewById(R.id.mSmartRefresh);
        adapter = new KitchenDeviceAdapter();
        adapter.bindToRecyclerView(rvCommon);
        mSmartRefresh.setOnRefreshListener(this);
        mSmartRefresh.setRefreshHeader(new ClassicsHeader(mContext));
        setStatusManager();
        requestVideoList();
    }


    private void  requestVideoList() {
       /* if (!NetworkUtil.isConnected(mContext)) {
//            mStatusManager.showLoadingLayout();
            baseHandler.postDelayed(() -> mStatusManager.showCustomLayout(R.layout.common_status_layout_no_network,R.id.llNoNetwok),300);
            return;
        }*/
        ApiRepository.getInstance().requestKitchenList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(
                new BaseLoadingObserver<BaseResult<List<KitchenGroup>>>() {
                    @Override
                    public void onRequestNext(BaseResult<List<KitchenGroup>> entity) {
                        if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                            loadDeviceList(entity.data);
                            mSmartRefresh.finishRefresh(true);
                            if (entity.data.isEmpty()) {
                                mStatusManager.showEmptyLayout();
                            } else {
                                mStatusManager.showSuccessLayout();
                            }
                        } else {
                            ToastUtil.show(entity.errorMsg);
                            mStatusManager.showErrorLayout();
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        mSmartRefresh.finishRefresh(false);
                        mStatusManager.showErrorLayout();
                    }
                }

        );
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestVideoList();
    }


    private void setStatusManager() {
        View llContentView = findViewById(R.id.llContentView);
        mStatusManager  = new StatusLayoutManager.Builder(llContentView)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setEmptyLayout(R.layout.common_status_layout_empty)
                .setErrorLayout(R.layout.view_error_layout)
                .setErrorClickViewID(R.id.llErrorRequest)
                .setDefaultEmptyText(com.tourcool.library.frame.demo.R.string.fast_multi_empty)
                .setDefaultLoadingText(com.tourcool.library.frame.demo.R.string.fast_multi_loading)
                .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {

                    }

                    @Override
                    public void onErrorChildClick(View view) {
                        mStatusManager.showLoadingLayout();
                        requestVideoList();
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                        mStatusManager.showLoadingLayout();
                        requestVideoList();
                    }
                }).build();
        mStatusManager.showLoadingLayout();
    }

    private int findSelectItemIndex( List<KitchenGroup> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGroupName() .equals(groupName) ) {
                return i;
            }
        }
        return -1;
    }


    private void loadDeviceList(List<KitchenGroup> list) {
        if (list != null) {
            int selectPosition = findSelectItemIndex(list);
            if (groupName != null) {
                showSelectTitle(groupName);
            }
            if (selectPosition >= 0) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelect(selectPosition == i);
                }
                rvCommon.scrollToPosition(selectPosition);
            }
            adapter.setNewData(list);
        }
//        mStatusManager.showLoadingLayout();

    }

}
