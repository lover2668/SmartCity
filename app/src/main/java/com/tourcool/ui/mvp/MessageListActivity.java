package com.tourcool.ui.mvp;

import android.os.Bundle;
import android.view.View;

import com.aries.library.fast.demo.R;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.UiManager;
import com.frame.library.core.control.IHttpRequestControl;
import com.frame.library.core.retrofit.BaseObserver;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tourcool.core.adapter.SystemMsgAdapter;
import com.tourcool.core.entity.BasePageBean;
import com.tourcool.core.entity.BaseResult;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.module.mvp.BaseMvpRefreshLoadActivity;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.ui.mvp.contract.MessageContract;
import com.tourcool.ui.mvp.presenter.MessagePresenter;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

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
//        presenter.getMessageList(3, page);
        test(3,page);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("mvp模式下的下拉刷新");
    }

    @Override
    public void setDataList(BasePageBean<MessageBean> pageBean) {

    }

    @Override
    public IHttpRequestControl getIHttpRequestControl1() {
         IHttpRequestControl requestControl = new IHttpRequestControl() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                return mRefreshLayout;
            }

            @Override
            public BaseQuickAdapter getRecyclerAdapter() {
                return adapter;
            }

            @Override
            public StatusLayoutManager getStatusLayoutManager() {
                return mStatusManager;
            }

            @Override
            public int getCurrentPage() {
                return mDefaultPage;
            }

            @Override
            public int getPageSize() {
                return mDefaultPageSize;
            }

            @Override
            public Class<?> getRequestClass() {
                return mClass;
            }
        };
        return requestControl;
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


    private void test(int userId,int page){
        ApiRepository.getInstance().requestMsgList1(userId,page).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseResult<BasePageBean<MessageBean>>>(getIHttpRequestControl()) {
                    @Override
                    public void _onNext(BaseResult<BasePageBean<MessageBean>> entity) {
                        UiManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), entity.data == null ? new ArrayList<>() : entity.data.getElements(), null);
                    }
                });
    }

}
