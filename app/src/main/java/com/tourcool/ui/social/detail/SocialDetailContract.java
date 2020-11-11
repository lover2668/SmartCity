package com.tourcool.ui.social.detail;

import com.frame.library.core.control.IHttpRequestControl;
import com.frame.library.core.retrofit.BaseObserver;
import com.tourcool.bean.social.SocialBaseInfo;
import com.tourcool.bean.social.SocialSecurityResult;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.module.mvp.IBaseModel;
import com.tourcool.core.module.mvp.IBaseView;

public class SocialDetailContract {
    interface SocialDetailModel extends IBaseModel {
        void getSocialDetailPageList(BaseObserver<BaseResult<SocialSecurityResult>> observer, int pageIndex,String type);

        void getSocialBaseInfo(BaseObserver<BaseResult<SocialBaseInfo>> observer);
    }

    interface View extends IBaseView {
        IHttpRequestControl getIHttpRequestControl();

        void showSocialInfo(SocialBaseInfo socialBaseInfo);
    }

    interface Presenter {
        void getSocialDetailList(int pageIndex,String type);

        void getSocialBaseInfo();
    }
}
