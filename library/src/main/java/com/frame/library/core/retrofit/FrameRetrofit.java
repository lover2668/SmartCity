package com.frame.library.core.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.util.SsLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: JenkinsZhou on 2018/7/24 13:10
 * @E-Mail: 971613168@qq.com
 * Function: Retrofit封装
 * Description:
 * 1、2017-11-16 12:48:31 JenkinsZhou 修改读、写、链接超时时间均为readTimeout的BUG会造成超时设置无效BUG
 * 2、修改初始化FastMultiUrl类的位置到createService以免超时控制不到问题
 * 3、2018-6-30 21:32:36 调整设置全局请求头方式并新增设置单个请求头方法
 * 4、2018-6-30 21:32:47 修复设置全局请求头不生效BUG
 * 5、2018-7-2 16:49:52 将控制多请求头功能由{@link FrameMultiUrl}移植至此方便统一管理
 * 6、2018-7-3 12:22:55 新增Service 缓存机制-可控制是否使用缓存{@link #createService(Class, boolean)}
 * 7、2018-7-3 13:20:58 新增多BaseUrl设置方式header模式{@link #putHeaderBaseUrl(String, String) {@link #putHeaderBaseUrl(Map)}}
 * 及method模式{@link #putBaseUrl(String, String) {@link #putHeaderBaseUrl(Map)}}
 * 8、2018-7-3 16:06:56 新增下载文件功能{@link #downloadFile(String)}
 * 9、2018-7-11 08:59:00 修改{@link #removeHeader(String)}key判断错误问题
 * 10、2018-7-24 13:10:49 新增默认header User-Agent -避免某些服务器配置攻击,请求返回403 forbidden 问题
 * 11、2018-7-30 16:13:40 修改日志打印规则
 * 12、2018-7-31 09:54:30 删除原有单独retrofit模式使用全局retrofit 通过设置Log禁用最后还原模式解决日志拦截器造成获取文件流卡住问题
 * 13、2018-8-23 12:31:34 新增设置是否打印json格式日志方法{@link #setLogJsonEnable(boolean)}
 */
public class FrameRetrofit {


    private static volatile FrameRetrofit sManager;
    private static volatile Retrofit sRetrofit;
    private static volatile Retrofit.Builder sRetrofitBuilder;
    private static OkHttpClient.Builder sClientBuilder;
    private static OkHttpClient sClient;
    /**
     * 重定向访问Url--通过设置header模式
     */
    public static final String BASE_URL_NAME_HEADER = FrameMultiUrl.BASE_URL_NAME_HEADER;
    /**
     * 默认读、写、连接超时
     */
    private long mDelayTime = 20;
    /**
     * 是否打印json格式 通过Logger.json
     */
    private boolean mLogJsonEnable = true;
    /**
     * Service 缓存-避免重复创建同一个Service
     */
    private Map<String, Object> mServiceMap = new HashMap<>();
    /**
     * 证书配置
     */
    private SsLUtil.SSLParams mSslParams = new SsLUtil.SSLParams();
    /**
     * 日志拦截器
     */
    private HttpLoggingInterceptor mLoggingInterceptor;
    /**
     * 统一header
     */
    private Map<String, Object> mHeaderMap = new HashMap<>();
    /**
     * header拦截器
     */
    private Interceptor mHeaderInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder request = chain.request().newBuilder();
            //避免某些服务器配置攻击,请求返回403 forbidden 问题
            addHeader("User-Agent", "Mozilla/5.0 (Android)");
            addHeader("token", "2627ff98-fb2a-42ca-95b2-0e8ce8681d63");
            if (mHeaderMap.size() > 0) {
                for (Map.Entry<String, Object> entry : mHeaderMap.entrySet()) {
                    request.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            return chain.proceed(request.build());
        }
    };

    private FrameRetrofit() {
        sClientBuilder = new OkHttpClient.Builder();
        sClientBuilder.addInterceptor(mHeaderInterceptor);
        sRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        setTimeout(mDelayTime);
        FrameMultiUrl.getInstance().with(sClientBuilder);
    }

    public static FrameRetrofit getInstance() {
        if (sManager == null) {
            synchronized (FrameRetrofit.class) {
                if (sManager == null) {
                    sManager = new FrameRetrofit();
                }
            }
        }
        return sManager;
    }

    /**
     * 对外暴露 OkHttpClient,方便自定义
     *
     * @return
     */
    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        return sClientBuilder;
    }

    public static OkHttpClient getOkHttpClient() {
        if (sClient == null) {
            sClient = getOkHttpClientBuilder().build();
        }
        return sClient;
    }

    /**
     * 对外暴露 Retrofit,方便自定义
     *
     * @return
     */
    public static Retrofit.Builder getRetrofitBuilder() {
        sRetrofitBuilder.client(getOkHttpClient());
        return sRetrofitBuilder;
    }

    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = getRetrofitBuilder().build();
        }
        return sRetrofit;
    }

    /**
     * 获取请求service 默认缓存处理
     *
     * @param apiService 目标Service
     * @param <T>
     * @return
     */
    public <T> T createService(Class<T> apiService) {
        return createService(apiService, true);
    }

    /**
     * 创建Service
     *
     * @param apiService
     * @param useCacheEnable --是否使用缓存Service
     * @param <T>
     * @return
     */
    public <T> T createService(Class<T> apiService, boolean useCacheEnable) {
        if (useCacheEnable && apiService != null) {
            if (mServiceMap.containsKey(apiService.getName())) {
                LoggerManager.i("className:" + apiService.getName() + ";service取自缓存");
                return (T) mServiceMap.get(apiService.getName());
            }
            T tClass = getRetrofit().create(apiService);
            mServiceMap.put(apiService.getName(), tClass);
            return tClass;
        }
        return getRetrofit().create(apiService);
    }

    /**
     * @param fileUrl 下载全路径 配合{@link BaseDownloadObserver}实现文件下载进度监听
     * @return
     */
    public Observable<ResponseBody> downloadFile(String fileUrl) {
        return downloadFile(fileUrl, null);
    }

    /**
     * 下载文件
     *
     * @param fileUrl 下载全路径 配合{@link BaseDownloadObserver}实现文件下载进度监听
     * @return
     */
    public Observable<ResponseBody> downloadFile(String fileUrl, Map<String, Object> header) {
        //下载前获取当前日志是否开启
        final boolean logEnable = FrameRetrofit.getInstance().isLogEnable();
        //下载前关闭日志功能--日志拦截器会不停拦截文件流造成无法进入onNext回调本地进行保存文件操作给人感觉就是卡住
        FrameRetrofit.getInstance().setLogEnable(false);
        return FrameRetrofit.getRetrofit()
                .create(FrameRetrofitService.class)
                .downloadFile(fileUrl, header == null ? new HashMap<String, Object>(0) : header)
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) {
                        //onNext回调前还原log状态
                        FrameRetrofit.getInstance().setLogEnable(logEnable);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 上传文件/参数 配合{@link FrameUploadRequestBody}及{@link FrameUploadRequestListener}实现上传进度监听
     *
     * @param uploadUrl
     * @param body
     * @return
     */
    public Observable<ResponseBody> uploadFile(String uploadUrl, @Nullable RequestBody body) {
        return uploadFile(uploadUrl, body, null);
    }

    /**
     * 上传文件/参数 配合{@link FrameUploadRequestBody}及{@link FrameUploadRequestListener}实现上传进度监听
     *
     * @param uploadUrl 请求全路径
     * @param body      请求body 可将文件及其他参数放进body
     * @param header    可设置额外请求头信息
     * @return
     */
    public Observable<ResponseBody> uploadFile(String uploadUrl, @Nullable final RequestBody body, Map<String, Object> header) {
        return getRetrofit()
                .create(FrameRetrofitService.class)
                .uploadFile(uploadUrl, body, header == null ? new HashMap<>() : header)
                .compose(FrameTransformer.<ResponseBody>switchSchedulers());
    }

    /**
     * 设置请求头{@link #mHeaderInterceptor}
     *
     * @param key
     * @param value
     * @return
     */
    public FrameRetrofit addHeader(String key, Object value) {
        if (!TextUtils.isEmpty(key) && value != null) {
            mHeaderMap.put(key, value);
        }
        return this;
    }

    /**
     * 添加统一的请求头
     *
     * @param map
     * @return
     */
    public FrameRetrofit addHeader(final Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            mHeaderMap.putAll(map);
        }
        return this;
    }

    /**
     * 清空全局请求头
     *
     * @param key
     * @return
     */
    public FrameRetrofit removeHeader(String key) {
        if (!TextUtils.isEmpty(key) && mHeaderMap.containsKey(key)) {
            mHeaderMap.remove(key);
        }
        return this;
    }

    /**
     * 清空所有全局请求头
     *
     * @return
     */
    public FrameRetrofit removeHeader() {
        mHeaderMap.clear();
        return this;
    }

    /**
     * 设置全局BaseUrl
     *
     * @param baseUrl
     * @return
     */
    public FrameRetrofit setBaseUrl(String baseUrl) {
        sRetrofitBuilder.baseUrl(baseUrl);
        FrameMultiUrl.getInstance().setGlobalBaseUrl(baseUrl);
        return this;
    }

    public FrameRetrofit setRetryOnConnectionFailure(boolean enable) {
        sClientBuilder.retryOnConnectionFailure(enable);
        return this;
    }

    public FrameRetrofit setTimeout(long second) {
        mDelayTime = second;
        return setTimeout(second, TimeUnit.SECONDS);
    }

    /**
     * 设置所有超时
     *
     * @param second
     * @param unit
     * @return
     */
    public FrameRetrofit setTimeout(long second, TimeUnit unit) {
        setReadTimeout(second, unit);
        setWriteTimeout(second, unit);
        setConnectTimeout(second, unit);
        return this;
    }


    public FrameRetrofit setReadTimeout(long second) {
        return setReadTimeout(second, TimeUnit.SECONDS);
    }

    /**
     * 设置读取超时时间
     *
     * @param second
     * @param unit   单位
     * @return
     */
    public FrameRetrofit setReadTimeout(long second, TimeUnit unit) {
        sClientBuilder.readTimeout(second, unit);
        return this;
    }

    public FrameRetrofit setWriteTimeout(long second) {
        return setWriteTimeout(second, TimeUnit.SECONDS);
    }

    /**
     * 设置写入超时时间
     *
     * @param second
     * @param unit
     * @return
     */
    public FrameRetrofit setWriteTimeout(long second, TimeUnit unit) {
        sClientBuilder.writeTimeout(second, unit);
        return this;
    }

    public FrameRetrofit setConnectTimeout(long second) {
        return setConnectTimeout(second, TimeUnit.SECONDS);
    }

    /**
     * 设置连接超时时间
     *
     * @param second
     * @param unit
     * @return
     */
    public FrameRetrofit setConnectTimeout(long second, TimeUnit unit) {
        sClientBuilder.connectTimeout(second, unit);
        return this;
    }

    /**
     * 是否通过Logger.json打印json格式的返回日志
     *
     * @param enable
     * @return
     */
    public FrameRetrofit setLogJsonEnable(boolean enable) {
        this.mLogJsonEnable = enable;
        return this;
    }

    /**
     * 获取当前是否设置日志打印
     *
     * @return
     */
    public boolean isLogEnable() {
        return mLoggingInterceptor != null && mLoggingInterceptor.getLevel() != HttpLoggingInterceptor.Level.NONE;
    }

    /**
     * 设置日志打印
     *
     * @param enable
     * @return
     */
    public FrameRetrofit setLogEnable(boolean enable) {
        return setLogEnable(enable, this.getClass().getSimpleName(), HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 设置是否打印日志
     *
     * @param enable
     * @param tag
     * @return
     */
    public FrameRetrofit setLogEnable(boolean enable, String tag, HttpLoggingInterceptor.Level level) {
        if (TextUtils.isEmpty(tag)) {
            tag = getClass().getSimpleName();
        }
        if (enable) {
            if (mLoggingInterceptor == null) {
                final String finalTag = tag;
                mLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        if (TextUtils.isEmpty(message)) {
                            return;
                        }
                        //json格式使用Logger.json打印
                        boolean isJson = message.startsWith("[") || message.startsWith("{");
                        isJson = isJson && mLogJsonEnable;
                        if (isJson) {
                            LoggerManager.json(finalTag, message);
                            return;
                        }
                        Log.d(finalTag, message);
                    }
                });
            }
            mLoggingInterceptor.setLevel(level);
            sClientBuilder.addInterceptor(mLoggingInterceptor);
        } else {
            if (mLoggingInterceptor != null) {
                mLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
        }
        return this;
    }

    /**
     * 获取证书配置
     *
     * @return
     */
    public SsLUtil.SSLParams getCertificates() {
        return mSslParams;
    }


    /**
     * 设置服务器证书 {@link OkHttpClient.Builder#sslSocketFactory(SSLSocketFactory, X509TrustManager)}
     *
     * @param sslSocketFactory
     * @param trustManager
     * @return
     */
    public FrameRetrofit setCertificates(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        mSslParams.sslSocketFactory = sslSocketFactory;
        mSslParams.trustManager = trustManager;
        sClientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        return this;
    }

    /**
     * 默认信任所有证书-不安全
     *
     * @return
     */
    public FrameRetrofit setCertificates() {
        SsLUtil.SSLParams sslParams = SsLUtil.getSslSocketFactory();
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
    }

    /**
     * @param trustManager 使用自己设置的X509TrustManager
     * @return
     */
    public FrameRetrofit setCertificates(X509TrustManager trustManager) {
        SsLUtil.SSLParams sslParams = SsLUtil.getSslSocketFactory(trustManager);
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
    }

    /**
     * 使用预埋证书,校验服务端证书(自签名证书)-单向认证
     *
     * @param certificates 用含有服务端公钥的证书校验服务端证书
     * @return
     */
    public FrameRetrofit setCertificates(InputStream... certificates) {
        SsLUtil.SSLParams sslParams = SsLUtil.getSslSocketFactory(certificates);
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
    }

    /**
     * 使用bks证书和密码管理客户端证书(双向认证),使用预埋证书,校验服务端证书(自签名证书)
     * 客户端使用bks证书校验服务端证书;
     *
     * @param bksFile
     * @param password
     * @param certificates 用含有服务端公钥的证书校验服务端证书
     * @return
     */
    public FrameRetrofit setCertificates(InputStream bksFile, String password, InputStream... certificates) {
        SsLUtil.SSLParams sslParams = SsLUtil.getSslSocketFactory(bksFile, password, certificates);
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
    }

    /**
     * 客户端使用bks证书校验服务端证书
     *
     * @param bksFile
     * @param password
     * @param trustManager 如果需要自己校验,那么可以自己实现相关校验;如果不需要自己校验,那么传null即可
     * @return
     */
    public FrameRetrofit setCertificates(InputStream bksFile, String password, X509TrustManager trustManager) {
        SsLUtil.SSLParams sslParams = SsLUtil.getSslSocketFactory(bksFile, password, trustManager);
        return setCertificates(sslParams.sslSocketFactory, sslParams.trustManager);
    }

    /**
     * 设置多BaseUrl-解析器
     *
     * @param parser
     * @return
     */
    public FrameRetrofit setUrlParser(FrameUrlParser parser) {
        FrameMultiUrl.getInstance().setUrlParser(parser);
        return this;
    }

    /**
     * 控制管理器是否拦截,在每个域名地址都已经确定,不需要再动态更改时可设置为 false
     *
     * @param enable
     * @return
     */
    public FrameRetrofit setUrlInterceptEnable(boolean enable) {
        FrameMultiUrl.getInstance().setIntercept(enable);
        return this;
    }

    /**
     * 是否Service Header设置多BaseUrl优先--默认method优先
     *
     * @param enable
     * @return
     */
    public FrameRetrofit setHeaderPriorityEnable(boolean enable) {
        FrameMultiUrl.getInstance().setHeaderPriorityEnable(enable);
        return this;
    }

    /**
     * 存放多BaseUrl 映射关系service 设置header模式-需要才设置
     *
     * @param map
     * @return
     */
    public FrameRetrofit putHeaderBaseUrl(Map<String, String> map) {
        FrameMultiUrl.getInstance().putHeaderBaseUrl(map);
        return this;
    }

    /**
     * 存放多BaseUrl 映射关系设置header模式-需要才设置
     *
     * @param urlKey
     * @param urlValue
     * @return
     */
    public FrameRetrofit putHeaderBaseUrl(String urlKey, String urlValue) {
        FrameMultiUrl.getInstance().putHeaderBaseUrl(urlKey, urlValue);
        return this;
    }

    /**
     * 移除BaseUrl映射关系设置header模式{@link #putHeaderBaseUrl(String, String)} key对应
     *
     * @param urlKey
     * @return
     */
    public FrameRetrofit removeHeaderBaseUrl(String urlKey) {
        FrameMultiUrl.getInstance().removeHeaderBaseUrl(urlKey);
        return this;
    }

    /**
     * 移除所有BaseUrl 映射关系设置header模式:即仅使用{@link #setBaseUrl(String)}中设置
     *
     * @return
     */
    public FrameRetrofit removeHeaderBaseUrl() {
        FrameMultiUrl.getInstance().clearAllHeaderBaseUrl();
        return this;
    }

    /**
     * 存放多BaseUrl 映射关系method模式-需要才设置
     *
     * @param map
     * @return
     */
    public FrameRetrofit putBaseUrl(Map<String, String> map) {
        FrameMultiUrl.getInstance().putBaseUrl(map);
        return this;
    }

    /**
     * 存放多BaseUrl 映射关系method模式-需要才设置
     *
     * @param method   retrofit service 除baseUrl外的部分即@POST或@GET里的内容
     * @param urlValue
     * @return
     */
    public FrameRetrofit putBaseUrl(String method, String urlValue) {
        FrameMultiUrl.getInstance().putBaseUrl(method, urlValue);
        return this;
    }

    /**
     * 移除BaseUrl映射关系映射关系method模式{@link #putBaseUrl(String, String)} key对应
     *
     * @param method retrofit service 除baseUrl外的部分即@POST或@GET里的内容
     * @return
     */
    public FrameRetrofit removeBaseUrl(String method) {
        FrameMultiUrl.getInstance().removeBaseUrl(method);
        return this;
    }

    /**
     * 移除所有BaseUrl 映射关系method模式:即仅使用{@link #setBaseUrl(String)}中设置
     *
     * @return
     */
    public FrameRetrofit removeBaseUrl() {
        FrameMultiUrl.getInstance().clearAllBaseUrl();
        return this;
    }
}
