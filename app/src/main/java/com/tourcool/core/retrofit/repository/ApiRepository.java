package com.tourcool.core.retrofit.repository;

import com.tourcool.core.entity.BasePageBean;
import com.tourcool.core.entity.BaseResult;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.entity.UpdateEntity;
import com.frame.library.core.retrofit.FrameRetrofit;
import com.frame.library.core.util.TourCooUtil;
import com.tourcool.core.MyApplication;
import com.tourcool.core.base.BaseMovieEntity;
import com.tourcool.core.retrofit.service.ApiService;
import com.frame.library.core.retrofit.FrameRetryWhen;
import com.frame.library.core.retrofit.FrameTransformer;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:25
 * @E-Mail: 971613168@qq.com
 * @Function: Retrofit api调用示例
 * @Description:
 */
public class ApiRepository extends BaseRepository {

    private static volatile ApiRepository instance;
    private ApiService mApiService;

    private ApiRepository() {
        mApiService = getApiService();
    }

    public static ApiRepository getInstance() {
        if (instance == null) {
            synchronized (ApiRepository.class) {
                if (instance == null) {
                    instance = new ApiRepository();
                }
            }
        }
        return instance;
    }

    private ApiService getApiService() {
        mApiService = FrameRetrofit.getInstance().createService(ApiService.class);
        return mApiService;
    }

    /**
     * 获取电影列表
     *
     * @param url   拼接URL
     * @param start 起始 下标
     * @param count 请求总数量
     * @return
     */
    public Observable<BaseMovieEntity> getMovie(String url, int start, int count) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("apikey","0b2bdeda43b5688921839c8ecb20399b");
        params.put("start", start);
        params.put("count", count);
        return FrameTransformer.switchSchedulers(getApiService().getMovie(url, params).retryWhen(new FrameRetryWhen()));
    }

    /**
     * 检查版本--是否传递本地App 版本相关信息根据具体接口而定(demo这里是可以不需要传的,所有判断逻辑放在app端--不推荐)
     *
     * @return
     */
    public Observable<UpdateEntity> updateApp() {
        Map<String, Object> params = new HashMap<>(2);
        params.put("versionCode", TourCooUtil.getVersionCode(MyApplication.getContext()));
        params.put("versionName", TourCooUtil.getVersionName(MyApplication.getContext()));
        return FrameTransformer.switchSchedulers(getApiService().updateApp(params).retryWhen(new FrameRetryWhen()));
    }

    /**
     * 消息列表
     *
     * @param ownerId
     * @return
     */
    public Observable<BaseResult<BasePageBean<MessageBean>>> requestMsgList1(int ownerId, int pageIndex) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("ownerId", ownerId);
        params.put("pageIndex", pageIndex + "");
        params.put("pageSize", 10 + "");
        return FrameTransformer.switchSchedulers(getApiService().requestMsgList1(params).retryWhen(new FrameRetryWhen()));
    }

}
