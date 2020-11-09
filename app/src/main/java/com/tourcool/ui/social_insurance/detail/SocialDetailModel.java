package com.tourcool.ui.social_insurance.detail;

import com.frame.library.core.retrofit.BaseObserver;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.entity.BasePageResult;
import com.tourcool.core.entity.social.SocialDetail;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.ui.mvp.contract.MessageContract;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年11月06日17:22
 * @Email: 971613168@qq.com
 */
public class SocialDetailModel implements SocialDetailContract.SocialDetailModel  {
    @Override
    public void getSocialDetailPageList(BaseObserver<BaseResult<BaseResult<SocialSecurityResult>>> observer, int pageIndex) {
        ApiRepository.getInstance().requestSocialPageDataGs(pageIndex). subscribe(observer);
       /* ApiRepository.getInstance().requestMsgList(userId, pageIndex).
                subscribe(observer);*/
    }
}
