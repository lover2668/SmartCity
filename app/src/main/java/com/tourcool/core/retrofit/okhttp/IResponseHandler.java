package com.tourcool.core.retrofit.okhttp;


/**
 * @author :JenkinsZhou
 * @description :回调接口父类
 * @company :途酷科技
 * @date 2019年04月16日12:51
 * @Email: 971613168@qq.com
 */
public interface IResponseHandler {

    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
