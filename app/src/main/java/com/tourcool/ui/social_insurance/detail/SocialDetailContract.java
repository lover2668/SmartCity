package com.tourcool.ui.social_insurance.detail;

import com.frame.library.core.control.IHttpRequestControl;
import com.frame.library.core.retrofit.BaseObserver;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.entity.BasePageResult;
import com.tourcool.core.entity.social.SocialDetail;
import com.tourcool.core.module.mvp.IBaseModel;
import com.tourcool.core.module.mvp.IBaseView;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年11月06日17:13
 * @Email: 971613168@qq.com
 */
public interface SocialDetailContract {

    interface SocialDetailModel extends IBaseModel {
        void getSocialDetailPageList(BaseObserver<BaseResult<BaseResult<SocialSecurityResult>>> observer, int pageIndex);
    }

    interface View extends IBaseView {
        IHttpRequestControl getIHttpRequestControl();

    }

    interface Presenter {
        void getSocialDetailList( int pageIndex);
    }
}
