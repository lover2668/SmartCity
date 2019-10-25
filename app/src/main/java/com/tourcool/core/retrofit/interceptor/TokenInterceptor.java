package com.tourcool.core.retrofit.interceptor;


import com.frame.library.core.log.TourCooLogUtil;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.retrofit.okhttp.IResponseHandler;
import com.tourcool.core.retrofit.okhttp.JsonResponseHandler;
import com.tourcool.core.retrofit.okhttp.RequestCallBack;
import com.tourcool.core.retrofit.okhttp.TokenRequestUtil;
import com.tourcool.core.util.TourCooUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月25日11:41
 * @Email: 971613168@qq.com
 */
public class TokenInterceptor implements Interceptor {
    public static final String TAG = "TokenInterceptor";
    private static final int CODE_REQUEST_TOKEN_INVALID = 401;
    private static final String refreshTokenUrl = RequestConfig.BASE_URL_API + "web/refresh-token";

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 原始请求
        Request request = chain.request()
                .newBuilder()
                //避免某些服务器配置攻击,请求返回403 forbidden 问题
                .addHeader("User-Agent", "Mozilla/5.0 (Android)")
                .build();
        Response response = chain.proceed(request);
        TourCooLogUtil.i(TAG, "请求的token=" + TourCooUtil.getNotNullValue(AccountHelper.getInstance().getRefreshToken()));
        if (response.code() == CODE_REQUEST_TOKEN_INVALID) {
            //token失效 需要重新获取token
            TourCooLogUtil.e(TAG, "检测到token失效 需要重新请求token");
            HashMap<String, String> map = new HashMap<>();
            map.put("refreshToken", AccountHelper.getInstance().getRefreshToken());
            TokenRequestUtil.getInstance().get(refreshTokenUrl, map, new JsonResponseHandler() {
                @Override
                public void onSuccess(int statusCode, JSONObject response) {
                    TourCooLogUtil.i(TAG, "刷新token成功:" + response + "状态码:" + statusCode);

                }

                @Override
                public void onFailure(int statusCode, String error_msg) {
                    TourCooLogUtil.e(TAG, "刷新token失败:" + error_msg + "状态码:" + statusCode + "，需要跳转至登录页面");

                }
            });
            return null;
        }
        return response;
    }


}
