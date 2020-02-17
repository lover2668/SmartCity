package com.tourcool.core.retrofit.service;


import com.tourcool.bean.certify.FaceCertify;
import com.tourcool.bean.kitchen.KitchenGroup;
import com.tourcool.bean.parking.CarInfo;
import com.tourcool.bean.parking.ParingRecord;
import com.tourcool.bean.search.SeachEntity;
import com.tourcool.bean.weather.WeatherEntity;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.constant.ApiConstant;
import com.tourcool.core.entity.Authenticate;
import com.tourcool.core.entity.BasePageBean;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.entity.UpdateEntity;
import com.frame.library.core.retrofit.FrameRetrofit;
import com.tourcool.core.base.BaseMovieEntity;
import com.tourcool.core.retrofit.interceptor.TokenInterceptor;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import static com.tourcool.core.retrofit.interceptor.TokenInterceptor.HEADER_NOT_SKIP_LOGIN;

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
    Observable<BaseResult<BasePageBean<MessageBean>>> requestMsgList(@Body Map<String, Object> map);


    /**
     * 获取屏幕信息
     *
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @GET("public/app/screen")
    Observable<BaseResult<Object>> requestHomeInfo(@QueryMap Map<String, Object> map);


    /**
     * 获取验证码
     *
     * @param map
     * @return
     */
    @GET("public/web/sms")
    Observable<BaseResult> getVCode(@QueryMap Map<String, Object> map);

    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @POST("public/app/login-sms")
    Observable<BaseResult> loginBySms(@QueryMap Map<String, Object> map);

    /**
     * 用户注册
     *
     * @param map
     * @return
     */
    @POST("public/app/register")
    Observable<BaseResult> register(@Body Map<String, Object> map);


    /**
     * 密码登录
     *
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @POST("public/app/login")
    Observable<BaseResult> loginByPassword(@Body Map<String, Object> map);


    /**
     * 验证码登录
     *
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @POST("public/app/login-sms")
    Observable<BaseResult> loginByVcode(@Body Map<String, Object> map);

    /**
     * 请求服务列表
     *
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @GET("public/app/service")
    Observable<BaseResult> requestServiceList();

    /**
     * 重置密码
     *
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @PUT("public/app/reset-password")
    Observable<BaseResult> resetPassword(@Body Map<String, Object> map);

    /**
     * 退出登录
     *
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @GET("app/user/logout")
    Observable<BaseResult> requestLogout();



    @Headers({TokenInterceptor.HEADER_NEED_TOKEN,HEADER_NOT_SKIP_LOGIN})
    @GET("app/user")
    Observable<BaseResult> requestUserInfo();

    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @PUT("app/password/change")
    Observable<BaseResult> requestChangePass(@Body Map<String, Object> map);


    /**
     * 修改用户信息
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @PUT("app/user")
    Observable<BaseResult> requestEditUserInfo(@Body Map<String, Object> map);



    /**
     * 多个文件上传
     *
     * @param files
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @POST("app/file/upload/img")
    Call<BaseResult<List<String>>> uploadFiles(@Body RequestBody files);


    /**
     * 获取未来一周天气
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @GET("public/app/weather")
    Observable<BaseResult<WeatherEntity>> requestWeatherInfo();

    /**
     * 搜索
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @GET("public/app/search")
    Observable<BaseResult<SeachEntity>> requestSearch(@QueryMap Map<String, Object> map);


    /**
     * 绑定手机号
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @PUT("app/user/phone")
    Observable<BaseResult> requestBindPhone(@Body Map<String, Object> map);


    /**
     * 首次短信登录设置密码
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @PUT("app/password")
    Observable<BaseResult> requestSetPass(@Body Map<String, Object> map);


    /**
     * 用户认证状态列表
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @GET("app/user/authentication-list")
    Observable<BaseResult<List<Authenticate>>> requestAuthentication();

    /**
     * 支付宝认证
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @POST("app/user/authentication/alipay")
    Observable<BaseResult> requestAliAuthentication();


    /**
     * 身份证认证
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @POST("app/user/authentication/id-card")
    Observable<BaseResult> requestAuthenticationIdCard(@Body Map<String, Object> map);

    /**
     * 人脸识别认证
     * @param map
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @POST("app/user/authentication/face")
    Observable<BaseResult<FaceCertify>> requestAuthenticationFace(@Body Map<String, Object> map);

    /**
     * 明厨亮灶视频列表
     * @return
     */
    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @GET("public/app/get_transparent_kitchen_list")
    Observable<BaseResult<List<KitchenGroup>>> requestKitchenList();


    @Headers(TokenInterceptor.HEADER_NO_NEED_TOKEN)
    @GET("public/app/get_transparent_kitchen_addr")
    Observable<BaseResult<String>> requestKitchenVideoLiveUrl(@QueryMap Map<String, Object> map);


    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @POST("car/bind")
    Observable<BaseResult<String>> requestAddCar(@Body Map<String, Object> map);

    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @GET("car/list")
    Observable<BaseResult<List<CarInfo>>> requestCarList();


    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @POST("car/unbind")
    Observable<BaseResult<String>> requestUnBindCar(@QueryMap Map<String, Object> map);

    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @GET("order/query-parking-arrears")
    Observable<BaseResult<List<ParingRecord>>> requestQueryParkingRecord(@QueryMap Map<String, Object> map);

    @Headers(TokenInterceptor.HEADER_NEED_TOKEN)
    @GET("car/get-carNum-records")
    Observable<BaseResult<List<String>>> requestLastPayPlantNum();

}


