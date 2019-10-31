package com.tourcool.core.retrofit.okhttp;

import org.json.JSONObject;

import java.io.IOException;

/**
 * @author :JenkinsZhou
 * @description :json类型的回调接口
 * @company :途酷科技
 * @date 2019年04月16日12:51
 * @Email: 971613168@qq.com
 */
public abstract class JsonResponseHandler implements IResponseHandler {

    public abstract void onSuccess(int statusCode, JSONObject response) throws IOException;

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
