package com.frame.library.core.retrofit;

/**
 * @author :zhoujian
 * @description : 上传进度监听
 * @company :翼迈科技
 * @date 2019年03月04日上午 11:34
 * @Email: 971613168@qq.com
 */
public interface UploadRequestListener {

    /**
     * 上传进度回调
     *
     * @param progress 进度
     * @param current  已上传字节数
     * @param total    总上传字节数
     */
    void onProgress(float progress, long current, long total);

    /**
     * 上传失败回调
     *
     * @param e 错误
     */
    void onFail(Throwable e);
}
