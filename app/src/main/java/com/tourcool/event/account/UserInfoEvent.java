package com.tourcool.event.account;

import com.tourcool.bean.account.UserInfo;

/**
 * @author :JenkinsZhou
 * @description :用户信息相关事件
 * @company :途酷科技
 * @date 2019年11月01日13:59
 * @Email: 971613168@qq.com
 */
public class UserInfoEvent {
    public UserInfo userInfo;

    public UserInfoEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfoEvent() {
    }
}
