package com.tourcool.ui.social.detail;

import com.frame.library.core.retrofit.BaseObserver;
import com.tourcool.bean.social.SocialBaseInfo;
import com.tourcool.bean.social.SocialSecurityResult;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.retrofit.repository.ApiRepository;

public class SocialDetailModel implements SocialDetailContract.SocialDetailModel {
    @Override
    public void getSocialDetailPageList(BaseObserver<BaseResult<SocialSecurityResult>> observer, int pageIndex, String type) {
        ApiRepository.getInstance().requestSocialPageDataByType(pageIndex,type). subscribe(observer);
    }

    @Override
    public void getSocialBaseInfo(BaseObserver<BaseResult<SocialBaseInfo>> observer) {
        ApiRepository.getInstance().requestSocialBaseInfo().subscribe(observer);
    }
}
