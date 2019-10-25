package com.tourcool.core.retrofit.okhttp;

import android.os.Handler;

import com.frame.library.core.log.TourCooLogUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月16日13:08
 * @Email: 971613168@qq.com
 */
public class RequestCallBack implements Callback {
    public static final String TAG = "RequestCallBack";
    private Handler mHandler;
    private IResponseHandler mResponseHandler;

    public RequestCallBack(Handler handler, IResponseHandler responseHandler) {
        mHandler = handler;
        mResponseHandler = responseHandler;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        TourCooLogUtil.e("onFailure", e);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseHandler.onFailure(0, e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if (response.isSuccessful()) {
            final String response_body = response.body().string();
            //json回调
            if (mResponseHandler instanceof JsonResponseHandler) {
                try {
                    final JSONObject jsonBody = new JSONObject(response_body);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((JsonResponseHandler) mResponseHandler).onSuccess(response.code(), jsonBody);
                        }
                    });
                } catch (JSONException e) {
                    TourCooLogUtil.e("onResponse fail parse jsonobject, body=" + response_body);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResponseHandler.onFailure(response.code(), "fail parse jsonobject, body=" + response_body);
                        }
                    });
                }
            } else if (mResponseHandler instanceof GsonResponseHandler) {
                //gson回调
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            ((GsonResponseHandler) mResponseHandler).onSuccess(response.code(),
                                    gson.fromJson(response_body, ((GsonResponseHandler) mResponseHandler).getType()));
                        } catch (Exception e) {
                            TourCooLogUtil.e("onResponse fail parse gson, body=" + response_body, e);
                            mResponseHandler.onFailure(response.code(), "fail parse gson, body=" + response_body);
                        }

                    }
                });
            }
        } else {
            TourCooLogUtil.e(TAG,"onResponse fail status="+response.code() );
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(0, "fail status=" + response.code());
                }
            });
        }
    }
}
