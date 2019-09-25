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
  /*  public static final String BASE_URL = "http://192.168.1.222:8090/";

    public static final String BASE_URL_NO_LINE = "http://192.168.1.222:8090";*/

    /**
     * 测试环境 穿透
     */
   /* public static final String BASE_URL = "https://tourcoo.xiaomiqiu.com/";

    public static final String BASE_URL_NO_LINE = "https://tourcoo.xiaomiqiu.com";*/


    /**
     * 正式环境
     */
//    public static final String BASE_URL = "http://47.104.168.189:8080/iov/";

    public static final String BASE_URL = "http://47.94.142.231:8080/";
//    public static final String BASE_URL = "http://192.168.1.201:8080/";
    public static final String BASE_URL_NO_LINE = "http://47.104.168.189:8080/iov";

    /**
     * 接口URL + ""
     */
    public static final String BASE_URL_API = BASE_URL+"gunsApi/" ;

    public static final int CODE_REQUEST_SUCCESS = 200;

    public static final int CODE_REQUEST_TOKEN_INVALID = 2;
    public static final int CODE_REQUEST_SUCCESS_NOT_REGISTER = -100;
    public static final String MSG_TOKEN_INVALID = "登录失效";
    public static final String BANNER_URL = BASE_URL_API + "custom-service/referral";
}
