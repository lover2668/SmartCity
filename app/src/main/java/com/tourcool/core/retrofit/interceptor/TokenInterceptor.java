package com.tourcool.core.retrofit.interceptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.StackUtil;
import com.frame.library.core.util.ToastUtil;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.TokenInfo;
import com.tourcool.core.MyApplication;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.constant.RouteConstance;
import com.tourcool.core.retrofit.TokenService;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.ui.mvp.account.LoginActivity;

import org.greenrobot.eventbus.EventBus;

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
    private static int ACTIVITY_SINGLE_FLAG = Intent.FLAG_ACTIVITY_SINGLE_TOP;
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
            String skipLoginFlag = originalRequest.header(SKIP_LOGIN_FLAG);
            boolean skipLoginEnable = skipLoginFlag == null || skipLoginFlag.equalsIgnoreCase(YES);
            TourCooLogUtil.d(TAG, "需要token验证!!!");
            //需要token校验
            Request tokenRequest = chain.request().newBuilder()
                    .removeHeader(KEY_TOKEN)
                    .addHeader(KEY_TOKEN, TOKEN + AccountHelper.getInstance().getAccessToken())
                    .build();
            TourCooLogUtil.d(TAG, "tokenRequest携带的token=" + AccountHelper.getInstance().getAccessToken());
            Response tokenResponse = chain.proceed(tokenRequest);
            if (tokenResponse.code() == RequestConfig.CODE_REQUEST_TOKEN_INVALID) {
                //说明token过期
                TourCooLogUtil.e(TAG, "tokenRequest携带的token已经失效,需要重新请求");
                TokenInfo tokenInfo = getNewToken();
                if (tokenInfo == null) {
                    if (skipLoginEnable) {
                        skipLogin();
                    }
                    if(!TextUtils.isEmpty(AccountHelper.getInstance().getAccessToken())){
                        ToastUtil.show("登录已失效,请重新登录");
                    }
                    AccountHelper.getInstance().logout();
                    EventBus.getDefault().post(new UserInfoEvent());
                    return chain.proceed(tokenRequest);
                } else {
                    //token刷新成功
                    TourCooLogUtil.i(TAG, "获取新token成功--->" + tokenInfo.getAccess_token());
                    AccountHelper.getInstance().setAccessToken(tokenInfo.getAccess_token());
                    AccountHelper.getInstance().setRefreshToken(tokenInfo.getRefresh_token());
                    Request newTokenRequest = chain.request().newBuilder()
                            .removeHeader(KEY_TOKEN)
                            .addHeader(KEY_TOKEN, TOKEN + tokenInfo.getAccess_token())
                            .build();
                    TourCooLogUtil.i(TAG, "newTokenRequest携带的token--->" + tokenInfo.getAccess_token());
                    return chain.proceed(newTokenRequest);
                }
            } else {
                TourCooLogUtil.i(TAG, "当前token验证通过 不需要刷新token~：携带的token是tokenResponse中的=" + AccountHelper.getInstance().getAccessToken());
                return tokenResponse;
            }
        }
    }

    private void skipLogin() {
        AccountHelper.getInstance().logout();
        Activity currentAct = StackUtil.getInstance().getCurrent();
        if (currentAct != null) {
            currentAct.finish();
        }
        FrameUtil.startActivity(MyApplication.getContext(), LoginActivity.class);
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
        TourCooLogUtil.i(TAG, "获取新token传入的参数:" + AccountHelper.getInstance().getRefreshToken());
        Call<BaseResult> call = apiService.getNewToken(AccountHelper.getInstance().getRefreshToken());
        try {
            BaseResult baseResult = call.execute().body();
            if (baseResult != null && baseResult.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                return parseJavaBean(baseResult.data, TokenInfo.class);
            } else {
                TourCooLogUtil.e(TAG, "refreshToken都已经失效了 只能重新登录了");
                TourCooLogUtil.e(TAG, baseResult);
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


    /**
     * @param context
     * @param activity 跳转Activity
     */
    public static void startActivity(Context context, Class<? extends Activity> activity) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, activity);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
