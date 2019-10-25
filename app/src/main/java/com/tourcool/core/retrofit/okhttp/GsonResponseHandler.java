package com.tourcool.core.retrofit.okhttp;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author :JenkinsZhou
 * @description :Gson回调
 * @company :途酷科技
 * @date 2019年04月16日12:51
 * @Email: 971613168@qq.com
 */
public abstract class GsonResponseHandler<T> implements IResponseHandler {

    Type mType;

    public GsonResponseHandler() {
        //反射获取带泛型的class
        Type myclass = getClass().getGenericSuperclass();
        if (myclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) myclass;      //获取所有泛型
        mType = $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);  //将泛型转为type
    }

    public final Type getType() {
        return mType;
    }

    public abstract void onSuccess(int statusCode, T response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
