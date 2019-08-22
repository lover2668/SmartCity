package com.tourcool.ui.mvp;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.adapter.SystemMsgAdapter;

import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.module.mvp.BaseMvpRefreshLoadActivity;
import com.tourcool.library.frame.R;
import com.tourcool.ui.mvp.contract.MessageContract;
import com.tourcool.ui.mvp.presenter.MessagePresenter;



/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2019年08月15日23:08
 * @Email: 971613168@qq.com
 */
public class MessageListActivity extends BaseMvpRefreshLoadActivity<MessagePresenter, MessageBean> implements MessageContract.View {
    private SystemMsgAdapter adapter;

    @Override
    protected void loadPresenter() {

    }

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter();
    }

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
    public void loadData(int page) {
        presenter.getMessageList(3, page);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("mvp模式下的下拉刷新");
    }

}
