package com.tourcool.core.retrofit;

import com.tourcool.core.base.BaseResult;
import com.tourcool.core.retrofit.interceptor.TokenInterceptor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月28日11:17
 * @Email: 971613168@qq.com
 */
public interface TokenService {

    @Headers({TokenInterceptor.HEADER_NO_NEED_TOKEN,})
    @GET("public/web/refresh-token")
    Call<BaseResult> getNewToken(@Query("refreshToken") String token);
}
