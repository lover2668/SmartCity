package com.tourcool.ui.social.detail;

import com.frame.library.core.UiManager;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.util.ToastUtil;
import com.tourcool.bean.social.SocialBaseInfo;
import com.tourcool.bean.social.SocialSecurityResult;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.module.mvp.BasePresenter;

import java.util.ArrayList;

public class SocialDetailPresenter  extends BasePresenter<SocialDetailContract.SocialDetailModel, SocialDetailContract.View> implements SocialDetailContract.Presenter {
    @Override
    protected SocialDetailContract.SocialDetailModel createModule() {
        return new SocialDetailModel();
    }

    @Override
    public void start() {

    }



    @Override
    public void getSocialDetailList(int pageIndex, String type) {
        if (!isViewAttached()) {
            return;
        }
        getModule().getSocialDetailPageList(new BaseObserver<BaseResult<SocialSecurityResult>>(getView().getIHttpRequestControl()) {
            @Override
            public void onRequestNext(BaseResult<SocialSecurityResult> entity) {
                UiManager.getInstance().getHttpRequestControl().httpRequestSuccess(getView().getIHttpRequestControl(), entity == null || entity.data == null ? new ArrayList<>() : entity.data.getData(), null);
                TourCooLogUtil.i("服务器返回的数据",entity);
            }
        },pageIndex,type);
    }

    @Override
    public void getSocialBaseInfo() {
        getModule().getSocialBaseInfo(new BaseLoadingObserver<BaseResult<SocialBaseInfo>>() {
            @Override
            public void onRequestNext(BaseResult<SocialBaseInfo> entity) {
                if(entity == null){
                    ToastUtil.showFailed("数据有误 请稍后再试");
                    return;
                }
                if(entity.status == RequestConfig.CODE_REQUEST_SUCCESS){
                    getView().showSocialInfo(entity.data);
                }else {
                    ToastUtil.show(entity.errorMsg);
                }
            }
        });
    }
}
