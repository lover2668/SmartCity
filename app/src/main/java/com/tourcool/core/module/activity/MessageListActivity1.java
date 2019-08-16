package com.tourcool.core.module.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.tourcool.core.adapter.SystemMsgAdapter;
import com.tourcool.core.entity.BasePageBean;
import com.tourcool.core.entity.BaseResult;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.frame.library.core.UiManager;
import com.aries.library.fast.demo.R;
import com.frame.library.core.module.activity.FrameRefreshLoadActivity;
import com.frame.library.core.retrofit.BaseObserver;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月14日17:53
 * @Email: 971613168@qq.com
 */
public class MessageListActivity1 extends FrameRefreshLoadActivity<MessageBean> {

    private SystemMsgAdapter adapter;

    @Override
    public int getContentLayout() {
        return R.layout.frame_layout_title_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public BaseQuickAdapter<MessageBean, BaseViewHolder> getAdapter() {
        adapter = new SystemMsgAdapter();
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    public LoadMoreView getLoadMoreView() {
        return null;
    }


    @Override
    public boolean isLoadMoreEnable() {
        return true;
    }

    @Override
    public boolean isItemClickEnable() {
        return false;
    }


    @Override
    public void loadData(int page) {
        test(3, page);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("普通MVC模式");
    }

    private void test(int userId, int page) {
        ApiRepository.getInstance().requestMsgList1(userId, page).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseResult<BasePageBean<MessageBean>>>(getIHttpRequestControl()) {
                    @Override
                    public void onRequestNext(BaseResult<BasePageBean<MessageBean>> entity) {
                        UiManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), entity.data == null ? new ArrayList<>() : entity.data.getElements(), null);
                    }
                });
    }

    @Override
    public View getMultiStatusContentView() {
        return null;
    }

    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView) {

    }

    @Override
    public void setMultiStatusView(StatusLayoutManager manager) {

    }

    @Override
    public View.OnClickListener getEmptyClickListener() {
        return null;
    }

    @Override
    public View.OnClickListener getErrorClickListener() {
        return null;
    }

    @Override
    public View.OnClickListener getCustomerClickListener() {
        return null;
    }
}
