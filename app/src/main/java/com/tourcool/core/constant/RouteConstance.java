package com.tourcool.core.constant;

/**
 * @author :JenkinsZhou
 * @description :路由管理
 * @company :途酷科技
 * @date 2019年10月15日14:40
 * @Email: 971613168@qq.com
 */
public class RouteConstance {

    public static final String TAG = "RouteConstance";
    private static final String MODULE_ACCOUNT = "/account/";

    /**
     * 系统设置
     */
    public static final String ACTIVITY_URL_SETTING =MODULE_ACCOUNT+"SystemSettingActivity";

    /**
     * 登录
     */
    public static final String ACTIVITY_URL_LOGIN = MODULE_ACCOUNT+"LoginActivity";

    /**
     * 垃圾分类
     */
    public static final String ACTIVITY_URL_GABAGE_CLASSIFY = "/garbage/GabageClassifyTabActivity";

    /**
     * 忘记密码
     */
    public static final String ACTIVITY_URL_FORGET_PASS = MODULE_ACCOUNT+"ForgetPasswordActivity";

    /**
     * 天气
     */
    public static final String ACTIVITY_URL_WEATHER = "/mvp/weather/"+"WeatherActivity";

    /**
     * 天气
     */
    public static final String ACTIVITY_URL_SEARCH = "/mvp/search/"+"SearchActivity";
}
