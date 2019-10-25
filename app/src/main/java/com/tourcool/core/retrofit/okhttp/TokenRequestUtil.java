package com.tourcool.core.retrofit.okhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.frame.library.core.log.TourCooLogUtil;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.core.util.TourCooUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author :JenkinsZhou
 * @description :获取token的工具类
 * @company :途酷科技
 * @date 2019年04月16日12:51
 * @Email: 971613168@qq.com
 */
public class TokenRequestUtil {
    public static final String TAG = "TokenRequestUtil";
    private OkHttpClient client;
    private static TokenRequestUtil instance;
    private static final String TOKEN_KEY = "token";

    public TokenRequestUtil() {
        client = new OkHttpClient.Builder().addInterceptor(new HeaderInterceptor()).build();
    }

    /**
     * 获取句柄
     *
     * @return
     */
    public static TokenRequestUtil getInstance() {
        if (instance == null) {
            instance = new TokenRequestUtil();
        }

        return instance;
    }


    /**
     * post 请求
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void post(Context context, final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        //post builder 参数
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request;

        //发起request
        if (context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .tag(context)
                    .build();
        }


        client.newCall(request).enqueue(new RequestCallBack(new Handler(), responseHandler));
    }

    /**
     * get 请求
     *
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void get(final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        get(null, url, params, responseHandler);
    }

    /**
     * get 请求
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void get(Context context, final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        //拼接url
        String get_url = url;
        if (params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }

        Request request;

        //发起request
        if (context == null) {
            request = new Request.Builder()
                    .url(get_url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(get_url)
                    .tag(context)
                    .build();
            TourCooLogUtil.i(TAG, "实际请求的url:" + get_url);
        }

        client.newCall(request).enqueue(new RequestCallBack(new Handler(Looper.getMainLooper()), responseHandler));
    }


    /**
     * 取消当前context的所有请求
     *
     * @param context
     */
    public void cancel(Context context) {
        if (client != null) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(context)) {
                    call.cancel();
                }
            }
            for (Call call : client.dispatcher().runningCalls()) {
                if (call.request().tag().equals(context)) {
                    call.cancel();
                }
            }
        }
    }

    /**
     * 头部拦截器
     */
    private class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    //避免某些服务器配置攻击,请求返回403 forbidden 问题
                    .addHeader("User-Agent", "Mozilla/5.0 (Android)")
                    .addHeader(TOKEN_KEY, TourCooUtil.getNotNullValue(AccountHelper.getInstance().getAccessToken()))
                    .build();
            Response response = chain.proceed(request);
            TourCooLogUtil.d(TAG, "HeaderInterceptor的token：" + TourCooUtil.getNotNullValue(AccountHelper.getInstance().getAccessToken()));
            return response;
        }
    }
}
