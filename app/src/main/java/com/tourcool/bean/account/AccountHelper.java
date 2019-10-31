package com.tourcool.bean.account;


import com.blankj.utilcode.util.SPUtils;

import org.litepal.LitePal;


/**
 * @author :zhoujian
 * @description :账户信息帮助类
 * @company :途酷科技
 * @date 2019年 04月 3日 16时52分
 * @Email: 971613168@qq.com
 */
public class AccountHelper {

    public static final String PREF_ACCESS_TOKEN = "access_token";
    public static final String PREF_REFRESH_TOKEN = "refresh_token";

    private static class SingletonInstance {
        private static final AccountHelper INSTANCE = new AccountHelper();
    }

    public static AccountHelper getInstance() {
        return SingletonInstance.INSTANCE;
    }


    /**
     * 访问需要的token
     */
    private String accessToken = "";

    /**
     * 刷新token需要的token
     */
    private String refreshToken = "";

    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        if (userInfo != null) {
            return userInfo;
        } else {
            UserInfo userInfo = LitePal.findFirst(UserInfo.class);
            setUserInfo(userInfo);
            return userInfo;
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        LitePal.deleteAll(UserInfo.class);
        if (userInfo != null) {
            userInfo.save();
        }
    }

    public void saveUserInfoToDisk(UserInfo userInfo) {
        LitePal.deleteAll(UserInfo.class);
        if (userInfo == null) {
            setUserInfo(null);
            return;
        }
        userInfo.save();
    }

    public String getAccessToken() {
        return SPUtils.getInstance().getString(PREF_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        SPUtils.getInstance().put(PREF_ACCESS_TOKEN, accessToken);
    }

    public String getRefreshToken() {
        return SPUtils.getInstance().getString(PREF_REFRESH_TOKEN, "");
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        SPUtils.getInstance().put(PREF_REFRESH_TOKEN, refreshToken);
    }


    public boolean isLogin() {
        return userInfo != null;
    }

}