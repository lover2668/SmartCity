package com.tourcool.core.retrofit.service;


import com.tourcool.core.base.BaseResult;
import com.tourcool.core.constant.ApiConstant;
import com.tourcool.core.entity.BasePageBean;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.entity.UpdateEntity;
import com.frame.library.core.retrofit.FrameRetrofit;
import com.tourcool.core.base.BaseMovieEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @Author: JenkinsZhou on 2018/7/30 14:01
 * @E-Mail: 971613168@qq.com
 * Function: 接口定义
 * Description:
 */
public interface ApiService {

    /**
     * 获取电影数据
     *
     * @param url
     * @param map
     * @return
     */
    @GET("{url}")
    Observable<BaseMovieEntity> getMovie(@Path("url") String url, @QueryMap Map<String, Object> map);

    /**
     * 检查应用更新--同时设置了Method及Header模式重定向请求Url,默认Method优先;
     * 可通过{@link FrameRetrofit#setHeaderPriorityEnable(boolean)}设置Header模式优先
     *
     * @param map
     * @return
     */
    @GET(ApiConstant.API_UPDATE_APP)
    @Headers({FrameRetrofit.BASE_URL_NAME_HEADER + ApiConstant.API_UPDATE_APP_KEY})
    Observable<UpdateEntity> updateApp(@QueryMap Map<String, Object> map);


    /**
     * 系统消息列表
     *
     * @param map
     * @return
     */
    @POST("message/owner/msg")
    Observable<com.tourcool.core.entity.BaseResult> requestMsgList1(@Body Map<String, Object> map);


    /**
     * 系统消息列表
     *
     * @param map
     * @return
     */
    @POST("message/owner/msg")
    Observable<BaseResult<BasePageBean<MessageBean>>> requestMsgList(@Body Map<String, Object> map);


    /**
     * 获取屏幕信息
     *
     * @param map
     * @return
     */
    @GET("app/screen")
    Observable<BaseResult<Object>> requestHomeInfo(@QueryMap Map<String, Object> map);


    /**
     * 获取验证码
     *
     * @param map
     * @return
     */
    @GET("web/sms")
    Observable<BaseResult> getVCode(@QueryMap Map<String, Object> map);

    @POST("app/login-sms")
    Observable<BaseResult> loginBySms(@QueryMap Map<String, Object> map);

    /**
     * 用户注册
     * @param map
     * @return
     */
    @POST("app/register")
    Observable<BaseResult> register(@Body Map<String, Object> map);


    /**
     * 密码登录
     *
     * @param map
     * @return
     */
    @POST("app/login")
    Observable<BaseResult> loginByPassword(@Body Map<String, Object> map);


    /**
     * 验证码登录
     *
     * @param map
     * @return
     */
    @POST("signLogin/loginByVCode")
    Observable<BaseResult> loginByVCode(@QueryMap Map<String, Object> map);

    /**
     * 请求服务列表
     * @return
     */
    @GET("app/service")
    Observable<BaseResult> requestServiceList();

}


