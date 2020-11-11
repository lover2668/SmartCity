package com.tourcool.core.config;


/**
 * @author :JenkinsZhou
 * @description :服务器接口配置类
 * @company :途酷科技
 * @date 2019年06月28日15:59
 * @Email: 971613168@qq.com
 */
public class RequestConfig {




    /**
     * 测试环境
     */

//    public static final String BASE_URL_NO_LINE = "http://192.168.1.217:8000";

//    public static final String BASE_URL_NO_LINE = "http://192.168.1.201:8080";
    /**
     * 正式环境
     */
//    public static final String BASE_URL_NO_LINE = "http://36.155.115.191:8000";

    public static final String BASE_URL_NO_LINE = "http://36.156.140.151:8000";


    public static final String BASE_URL = BASE_URL_NO_LINE+"/";




    /**
     * 接口URL + ""
     */
    public static final String BASE_URL_API = BASE_URL+"api/" ;

    public static final int CODE_REQUEST_SUCCESS = 200;

    public static final int CODE_REQUEST_TOKEN_INVALID = 401;
    public static final int CODE_REQUEST_SUCCESS_NOT_REGISTER = -100;
    public static final String MSG_TOKEN_INVALID = "登录失效";
    public static final String BANNER_URL = BASE_URL_API + "custom-service/referral";
    public static final String KEY_TOKEN = "Authorization";
    public static final String TOKEN = "Bearer ";
    public static final String EXCEPTION_NO_NETWORK = "ConnectException";
    public static final String MSG_SEND_SUCCESS = "发送成功";
    public static final String STRING_REQUEST_TOKEN_INVALID = "401";
}
