package com.tourcool.core.retrofit.repository;

import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.retrofit.FrameRetrofit;
import com.frame.library.core.retrofit.FrameTransformer;
import com.frame.library.core.retrofit.RetryWhen;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.StringUtil;
import com.tourcool.bean.certify.FaceCertify;
import com.tourcool.bean.kitchen.KitchenGroup;
import com.tourcool.bean.parking.CarInfo;
import com.tourcool.bean.parking.ParingRecord;
import com.tourcool.bean.search.SeachEntity;
import com.tourcool.bean.social.SocialBaseInfo;
import com.tourcool.bean.social.SocialSecurityResult;
import com.tourcool.bean.weather.WeatherEntity;
import com.tourcool.core.MyApplication;
import com.tourcool.core.base.BaseMovieEntity;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.entity.Authenticate;
import com.tourcool.core.entity.BasePageResult;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.entity.UpdateEntity;
import com.tourcool.core.retrofit.service.ApiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_BIRTH;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:25
 * @E-Mail: 971613168@qq.com
 * @Function: Retrofit api调用示例
 * @Description:
 */
public class ApiRepository extends AbstractRepository {
    public static final String TAG = "提交到后台的参数";
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

    public ApiService getApiService() {
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
        params.put("apikey", "0b2bdeda43b5688921839c8ecb20399b");
        params.put("start", start);
        params.put("count", count);
        return FrameTransformer.switchSchedulers(getApiService().getMovie(url, params).retryWhen(new RetryWhen()));
    }

    /**
     * 检查版本--是否传递本地App 版本相关信息根据具体接口而定(demo这里是可以不需要传的,所有判断逻辑放在app端--不推荐)
     *
     * @return
     */
    public Observable<UpdateEntity> updateApp() {
        Map<String, Object> params = new HashMap<>(2);
        params.put("versionCode", FrameUtil.getVersionCode(MyApplication.getContext()));
        params.put("versionName", FrameUtil.getVersionName(MyApplication.getContext()));
        return FrameTransformer.switchSchedulers(getApiService().updateApp(params).retryWhen(new RetryWhen()));
    }


    /**
     * 消息列表
     *
     * @param ownerId
     * @return
     */
    public Observable<BaseResult<BasePageResult<MessageBean>>> requestMsgList(int ownerId, int pageIndex) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("ownerId", ownerId);
        params.put("pageIndex", pageIndex + "");
        params.put("pageSize", 10 + "");
        return FrameTransformer.switchSchedulers(getApiService().requestMsgList(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<Object>> requestHomeInfo(int screenId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("screenId", screenId);
        return FrameTransformer.switchSchedulers(getApiService().requestHomeInfo(params).retryWhen(new RetryWhen()));
    }


    /**
     * 获取验证码
     *
     * @param mobile
     * @return
     */
    public Observable<BaseResult> getVcode(String mobile) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("phoneNumber", mobile);
        return FrameTransformer.switchSchedulers(getApiService().getVCode(params).retryWhen(new RetryWhen()));
    }

    /**
     * 短信登录
     *
     * @param mobile
     * @param smsCode
     * @return
     */
    public Observable<BaseResult> loginBySms(String mobile, String smsCode) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("phoneNumber", mobile);
        params.put("smsCode", smsCode);
        return FrameTransformer.switchSchedulers(getApiService().loginBySms(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult> register(String mobile, String smsCode, String pass, String confirmPass) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("phoneNumber", mobile);
        params.put("smsCode", smsCode);
        params.put("password", pass);
        params.put("confirmPassword", confirmPass);
        return FrameTransformer.switchSchedulers(getApiService().register(params).retryWhen(new RetryWhen()));
    }

    /**
     * 密码登录
     *
     * @param phone
     * @param pass
     * @return
     */
    public Observable<BaseResult> loginByPassword(String phone, String pass) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("phoneNumber", phone);
        params.put("password", pass);
        return FrameTransformer.switchSchedulers(getApiService().loginByPassword(params).retryWhen(new RetryWhen()));
    }

    /**
     * 验证码登录
     *
     * @param phone
     * @param smsCode
     * @return
     */
    public Observable<BaseResult> loginByVcode(String phone, String smsCode) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("phoneNumber", phone);
        params.put("smsCode", smsCode);
        return FrameTransformer.switchSchedulers(getApiService().loginByVcode(params).retryWhen(new RetryWhen()));
    }

    /**
     * 获取服务页tab数据
     *
     * @return
     */
    public Observable<BaseResult> requestServiceList() {
        return FrameTransformer.switchSchedulers(getApiService().requestServiceList().retryWhen(new RetryWhen()));
    }

    /**
     * 重置密码
     *
     * @param phoneNumber
     * @param vCode
     * @param pass
     * @param passConfirm
     * @return
     */
    public Observable<BaseResult> requestResetPass(String phoneNumber, String vCode, String pass, String passConfirm) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("confirmPassword", passConfirm);
        params.put("password", pass);
        params.put("phoneNumber", phoneNumber);
        params.put("smsCode", vCode);
        return FrameTransformer.switchSchedulers(getApiService().resetPassword(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult> requestLogout() {
        return FrameTransformer.switchSchedulers(getApiService().requestLogout().retryWhen(new RetryWhen()));
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    public Observable<BaseResult> requestUserInfo() {
        return FrameTransformer.switchSchedulers(getApiService().requestUserInfo().retryWhen(new RetryWhen()));
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    public Observable<BaseResult> requestChangePass(String oldPassword, String newPassword, String confirmPassword) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("oldPassword", oldPassword);
        params.put("password", newPassword);
        params.put("confirmPassword", confirmPassword);
        TourCooLogUtil.i(TAG, params);
        return FrameTransformer.switchSchedulers(getApiService().requestChangePass(params).retryWhen(new RetryWhen()));
    }


    /**
     * 修改用户信息
     *
     * @param nickName
     * @param avatarUrl
     * @return
     */
    public Observable<BaseResult> requestEditUserInfo(String nickName, String avatarUrl) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("nickname", nickName);
        params.put("iconUrl", avatarUrl);
        return FrameTransformer.switchSchedulers(getApiService().requestEditUserInfo(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<WeatherEntity>> requestWeatherInfo() {
        return FrameTransformer.switchSchedulers(getApiService().requestWeatherInfo().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<SeachEntity>> requestSearch(String search) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("search", search);
        return FrameTransformer.switchSchedulers(getApiService().requestSearch(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> requestBindPhone(String phoneNumber, String smsCode) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("phoneNumber", phoneNumber);
        params.put("smsCode", smsCode);
        return FrameTransformer.switchSchedulers(getApiService().requestBindPhone(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult> requestSetPass(String password, String confirmPassword) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("password", password);
        params.put("confirmPassword", confirmPassword);
        return FrameTransformer.switchSchedulers(getApiService().requestSetPass(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<Authenticate>>> requestAuthentication() {
        return FrameTransformer.switchSchedulers(getApiService().requestAuthentication().retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult> requestAliAuthentication() {
        return FrameTransformer.switchSchedulers(getApiService().requestAliAuthentication().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> requestAuthenticationIdCard(String idCard, String name) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("idCard", idCard);
        params.put("name", name);
        TourCooLogUtil.i("提交到后台的参数", params);
        return FrameTransformer.switchSchedulers(getApiService().requestAuthenticationIdCard(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<FaceCertify>> requestAuthenticationFace(String idCard, String name) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("idCard", idCard);
        params.put("name", name);
        return FrameTransformer.switchSchedulers(getApiService().requestAuthenticationFace(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<KitchenGroup>>> requestKitchenList() {
        return FrameTransformer.switchSchedulers(getApiService().requestKitchenList().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<String>> requestKitchenVideoLiveUrl(String serialNumber, String channelNumber) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("indexCode", serialNumber + "#" + channelNumber);
        return FrameTransformer.switchSchedulers(getApiService().requestKitchenVideoLiveUrl(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<String>> requestAddCar(String carNum, int carType) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("carNum", carNum);
        params.put("carType", carType);
        TourCooLogUtil.i("提交到后台的参数", params);
        return FrameTransformer.switchSchedulers(getApiService().requestAddCar(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<CarInfo>>> requestCarList() {
        return FrameTransformer.switchSchedulers(getApiService().requestCarList().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<String>> requestUnBindCar(String carId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("carId", carId);
        TourCooLogUtil.i("提交到后台的参数", params);
        return FrameTransformer.switchSchedulers(getApiService().requestUnBindCar(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<ParingRecord>>> requestQueryParkingRecord(String carId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("carNum", carId);
        TourCooLogUtil.i("提交到后台的参数", params);
        return FrameTransformer.switchSchedulers(getApiService().requestQueryParkingRecord(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<String>>> requestLastPayPlantNum() {
        return FrameTransformer.switchSchedulers(getApiService().requestLastPayPlantNum().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<String>> requestFindParkingUrl() {
        return FrameTransformer.switchSchedulers(getApiService().requestFindParkingUrl().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<SocialBaseInfo>> requestSocialBaseInfo() {
        return FrameTransformer.switchSchedulers(getApiService().requestSocialBaseInfo().retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<SocialSecurityResult>> requestSocialPageDataByType(int pageIndex, String socialType) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("page", pageIndex + "");
        params.put("pageSize", 10 + "");
        switch (StringUtil.getNotNullValue(socialType)) {
            //养老查询
            case ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER:
                return FrameTransformer.switchSchedulers(getApiService().requestSocialPageDataTakeCareOlder(params).retryWhen(new RetryWhen()));
            //失业查询
            case ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK:
                return FrameTransformer.switchSchedulers(getApiService().requestSocialPageDataLoseWork(params).retryWhen(new RetryWhen()));
            //生育查询
            case ITEM_TYPE_SOCIAL_QUERY_BIRTH:
                return FrameTransformer.switchSchedulers(getApiService().requestSocialPageDataBirth(params).retryWhen(new RetryWhen()));
            default:
                return FrameTransformer.switchSchedulers(getApiService().requestSocialPageDataGs(params).retryWhen(new RetryWhen()));
        }

    }
}
