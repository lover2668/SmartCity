package com.tourcool.ui.mvp.presenter;



import com.frame.library.core.UiManager;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.tourcool.core.base.BaseEntity;
import com.tourcool.core.entity.BasePageBean;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.ui.mvp.contract.MessageContract;
import com.tourcool.ui.mvp.model.MessageModel;

import java.util.ArrayList;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月13日17:34
 * @Email: 971613168@qq.com
 */
public class MessagePresenter extends BasePresenter<MessageContract.MessageModel, MessageContract.View> implements MessageContract.Presenter {


    @Override
    protected MessageContract.MessageModel createModule() {
        return new MessageModel();
    }

    @Override
    public void start() {

    }


    @Override
    public void getMessageList(int userId, int pageIndex) {
        getModule().getMessagePageList(new BaseLoadingObserver<BaseEntity<BasePageBean<MessageBean>>>(getView().getIHttpRequestControl1()) {
            @Override
            public void _onNext(BaseEntity<BasePageBean<MessageBean>> entity) {
                UiManager.getInstance().getHttpRequestControl().httpRequestSuccess(getView().getIHttpRequestControl1(), entity == null || entity.data == null ? new ArrayList<>() : entity.data.getElements(), null);
                TourCooLogUtil.i("服务器返回的数据",entity);
            }

          /*  @Override
            public void onRequestNext(BaseEntity<BasePageBean<MessageBean>> entity) {


            }*/
        }, userId, pageIndex);
    }
}
