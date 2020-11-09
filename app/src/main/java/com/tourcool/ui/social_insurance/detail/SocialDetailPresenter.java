package com.tourcool.ui.social_insurance.detail;

import com.frame.library.core.UiManager;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.entity.BasePageResult;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.entity.social.SocialDetail;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.ui.mvp.contract.MessageContract;

import java.util.ArrayList;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年11月06日17:19
 * @Email: 971613168@qq.com
 */
public class SocialDetailPresenter extends BasePresenter<SocialDetailContract.SocialDetailModel, SocialDetailContract.View> implements SocialDetailContract.Presenter {
    @Override
    protected SocialDetailContract.SocialDetailModel createModule() {
        return new SocialDetailModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void getSocialDetailList( int pageIndex) {
        if (!isViewAttached()) {
            return;
        }
        getModule().getSocialDetailPageList(new BaseLoadingObserver<BaseResult<BasePageResult<SocialDetail>>>(getView().getIHttpRequestControl()) {
            @Override
            public void onRequestNext(BaseResult<BasePageResult<SocialDetail>> entity) {
                UiManager.getInstance().getHttpRequestControl().httpRequestSuccess(getView().getIHttpRequestControl(), entity == null || entity.data == null ? new ArrayList<>() : entity.data.getElements(), null);
                TourCooLogUtil.i("服务器返回的数据",entity);
            }
        }, pageIndex);
    }
}
