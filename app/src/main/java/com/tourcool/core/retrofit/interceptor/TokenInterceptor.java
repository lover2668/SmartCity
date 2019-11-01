package com.tourcool.core.retrofit.interceptor;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.frame.library.core.log.TourCooLogUtil;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.TokenInfo;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.constant.RouteConstance;
import com.tourcool.core.retrofit.TokenService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tourcool.core.config.RequestConfig.KEY_TOKEN;
import static com.tourcool.core.config.RequestConfig.TOKEN;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月29日9:54
 * @Email: 971613168@qq.com
 */
public class TokenInterceptor implements Interceptor {
    public static final String TAG = "TokenInterceptor";
    private static final String TOKEN_FLAG = "NeedToken";
    private static final String SKIP_LOGIN_FLAG = "skipLogin";
    private static final String YES = "true";
    private static final String NO = "false";
    public static final String HEADER_NEED_TOKEN = TOKEN_FLAG + ": " + YES;
    public static final String HEADER_NO_NEED_TOKEN = TOKEN_FLAG + ": " + NO;
    public static final String HEADER_NOT_SKIP_LOGIN = SKIP_LOGIN_FLAG + ": " + NO;
    public static final String HEADER_SKIP_LOGIN = SKIP_LOGIN_FLAG + ": " + YES;
    private static final String URL_REFRESH_TOKEN = RequestConfig.BASE_URL_API;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String tokenFlag = originalRequest.header(TOKEN_FLAG);
        if (tokenFlag == null || tokenFlag.equalsIgnoreCase(NO)) {
            TourCooLogUtil.d(TAG, "不需要token验证 不做任何处理");
            return chain.proceed(originalRequest);
        } else {
            String skipLoginFlag =originalRequest.header(SKIP_LOGIN_FLAG);
            boolean skipLoginEnable =skipLoginFlag == null || skipLoginFlag.equalsIgnoreCase(YES);
            TourCooLogUtil.d(TAG, "需要token验证!!!");
            //需要token校验
            Request newRequest = chain.request().newBuilder()
                    .removeHeader(KEY_TOKEN)
                    .addHeader(KEY_TOKEN, TOKEN + AccountHelper.getInstance().getAccessToken())
                    .build();
            Response newResponse = chain.proceed(newRequest);
            if (newResponse.code() == RequestConfig.CODE_REQUEST_TOKEN_INVALID) {
                //说明token过期
                TokenInfo tokenInfo = getNewToken();
                if (tokenInfo == null) {
                    if(skipLoginEnable){
                        skipLogin();
                    }
                    return  chain.proceed(newRequest);
                } else {
                    //token刷新成功
                    AccountHelper.getInstance().setAccessToken(tokenInfo.getAccess_token());
                    AccountHelper.getInstance().setRefreshToken(tokenInfo.getRefresh_token());
                    TourCooLogUtil.d(TAG, "刷新了token:" + tokenInfo.getAccess_token());
                    Request lastRequest = chain.request().newBuilder()
                            .removeHeader(KEY_TOKEN)
                            .addHeader(KEY_TOKEN, TOKEN + tokenInfo.getAccess_token())
                            .build();
                    return chain.proceed(lastRequest);
                }
            } else {
                TourCooLogUtil.i(TAG, "当前token验证通过 不需要刷新token~");
                return newResponse;
            }
    }}

    private void skipLogin() {
        ARouter.getInstance().build(RouteConstance.ACTIVITY_URL_LOGIN).navigation();
    }


    private TokenInfo getNewToken() {
        Retrofit retrofit = new Retrofit.Builder()
                //基础url,其他部分在GetRequestInterface里
                .baseUrl(URL_REFRESH_TOKEN)
                //Gson数据转换器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //创建网络请求接口实例
        TokenService apiService = retrofit.create(TokenService.class);
        Call<BaseResult> call = apiService.getNewToken(AccountHelper.getInstance().getRefreshToken());
        try {
            BaseResult baseResult = call.execute().body();
            if (baseResult != null && baseResult.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                return parseJavaBean(baseResult.data, TokenInfo.class);
            }
        } catch (Exception ex) {
            TourCooLogUtil.e(TAG, "getNewToken()报错-->" + ex.getMessage());
        }
        return null;
    }


    private <T> T parseJavaBean(Object data, Class<T> tClass) {
        try {
            return JSON.parseObject(JSON.toJSONString(data), tClass);
        } catch (Exception e) {
            TourCooLogUtil.e("parseJavaBean()报错--->" + e.toString());
            return null;
        }
    }


}
