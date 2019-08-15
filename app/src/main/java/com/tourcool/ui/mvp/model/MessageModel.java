package com.tourcool.ui.mvp.model;

import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.tourcool.core.base.BaseEntity;
import com.tourcool.core.entity.BasePageBean;
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
    public void getMessagePageList(BaseLoadingObserver<BaseEntity<BasePageBean<MessageBean>>> observer, int userId, int pageIndex) {
        ApiRepository.getInstance().requestMsgList(userId, pageIndex).
                subscribe(observer);
    }
}
