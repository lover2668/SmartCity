package com.tourcool.ui.mvp.model;

import com.frame.library.core.retrofit.BaseObserver;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.entity.BasePageResult;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.ui.mvp.contract.MessageContract;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月15日18:23
 * @Email: 971613168@qq.com
 */
public class MessageModel implements MessageContract.MessageModel {


    @Override
    public void getMessagePageList(BaseObserver<BaseResult<BasePageResult<MessageBean>>> observer, int userId, int pageIndex) {
        ApiRepository.getInstance().requestMsgList(userId, pageIndex).
                subscribe(observer);
    }
}
