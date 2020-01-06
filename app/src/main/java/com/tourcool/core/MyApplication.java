package com.tourcool.core;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tourcool.bean.greendao.GreenDaoHelper;
import com.tourcool.core.retrofit.interceptor.TokenInterceptor;
import com.frame.library.core.util.FrameUtil;
import com.tourcool.core.config.AppConfig;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.constant.ApiConstant;
import com.tourcool.core.constant.SPConstant;
import com.tourcool.core.impl.ActivityControlImpl;
import com.tourcool.core.impl.AppConfigImpl;
import com.tourcool.core.impl.HttpRequestControlImpl;
import com.tourcool.core.impl.SwipeBackControlImpl;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.module.main.MainTabActivity;
import com.tourcool.core.util.NotificationUtil;
import com.frame.library.core.UiManager;
import com.frame.library.core.crash.CrashManager;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.log.widget.LogFileEngineFactory;
import com.frame.library.core.log.widget.config.LogLevel;
import com.frame.library.core.retrofit.FrameRetrofit;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.util.FormatUtil;
import com.frame.library.core.util.StackUtil;
import com.frame.library.core.util.SpUtil;
import com.frame.library.core.util.SizeUtil;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.didichuxing.doraemonkit.kit.webdoor.WebDoorManager;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.tourcool.smartcity.BuildConfig;
import com.tourcool.smartcity.R;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.frame.library.core.log.LogConfig.PATH_LOG_SAVE;
import static com.frame.library.core.log.LogConfig.TAG_LOG_PRE_SUFFIX;


/**
 * @Author: JenkinsZhou on 2018/7/31 10:43
 * @E-Mail: 971613168@qq.com
 * Function:基础配置Application
 * Description:
 */
public class MyApplication  extends LitePalApplication {

    private static MyApplication mContext;
    private static String TAG = "MyApplication";
    private static int imageHeight = 0;
    private long start;
    private Handler handler;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        handler = new Handler();
        loadDelay();
        //初始化Logger日志打印
        LoggerManager.init(TAG, BuildConfig.LOG_ENABALE,
                PrettyFormatStrategy.newBuilder()
                        .methodOffset(0)
                        .showThreadInfo(true)
                        .methodCount(3));
        start = System.currentTimeMillis();
        //初始化日志
        initLog();
        //异常处理初始化
        CrashManager.init(mContext);
        GreenDaoHelper.getInstance().initDatabass(mContext);
        LoggerManager.i(TAG, "start:" + start + ";Application:" + FrameUtil.getApplication());
        //最简单UI配置模式-必须进行初始化-最新版本无需初始化FastLib内部自动初始化
//         UiManager.init(this);
        //以下为更丰富自定义方法
        //全局UI配置参数-按需求设置
        AppConfigImpl impl = new AppConfigImpl(mContext);
        ActivityControlImpl activityControl = new ActivityControlImpl();
        UiManager.getInstance()
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //全局设置RecyclerView
                .setFastRecyclerViewControl(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                //设置全局TitleBarView相关配置
                .setTitleBarViewControl(impl)
                //设置Activity滑动返回控制-默认开启滑动返回功能不需要设置透明主题
                .setSwipeBackControl(new SwipeBackControlImpl())
                //设置Activity/Fragment相关配置(横竖屏+背景+虚拟导航栏+状态栏+生命周期)
                .setActivityFragmentControl(activityControl)
                //设置BasisActivity 子类按键监听
                .setActivityKeyEventControl(activityControl)
                //配置BasisActivity 子类事件派发相关
                .setActivityDispatchEventControl(activityControl)
                //设置http请求结果全局控制
                .setHttpRequestControl(new HttpRequestControlImpl())
                //配置{@link BaseObserver#onError(Throwable)}全局处理
                .setFastObserverControl(impl)
                //设置主页返回键控制-默认效果为2000 毫秒时延退出程序
                .setQuitAppControl(impl)
                //设置ToastUtil全局控制
                .setToastControl(impl);

        //初始化Retrofit配置
        FrameRetrofit.getInstance()
                //配置全局网络请求BaseUrl
                .setBaseUrl(RequestConfig.BASE_URL_API)
                //信任所有证书--也可设置setCertificates(单/双向验证)
                .setCertificates()
                //设置统一请求头
//                .addHeader(header)
//                .addHeader(key,value)
                //设置请求全局log-可设置tag及Level类型
                .setLogEnable(true)
//                .setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BODY)
                //设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)
                //默认30 s
                .setTimeout(5);

        //注意设置baseUrl要以/ 结尾 service 里的方法不要以/打头不然拦截到的url会有问题
        //以下为配置多BaseUrl--默认方式一优先级高 可通过FastRetrofit.getInstance().setHeaderPriorityEnable(true);设置方式二优先级
        //方式一 通过Service 里的method-(如:) 设置 推荐 使用该方式不需设置如方式二的额外Header
        FrameRetrofit.getInstance()
//                .addInterceptor(mHeaderInterceptor)
                .addInterceptor(new TokenInterceptor())
                .putBaseUrl(ApiConstant.API_UPDATE_APP, BuildConfig.BASE__UPDATE_URL);

        //方式二 通过 Service 里添加特定header设置
        //step1
        FrameRetrofit.getInstance()
                //设置Header模式优先-默认Method方式优先
                .setHeaderPriorityEnable(true)
                .putHeaderBaseUrl(ApiConstant.API_UPDATE_APP_KEY, BuildConfig.BASE__UPDATE_URL);
        //step2
        // 需要step1中baseUrl的方法需要在对应service里增加
        // @Headers({FrameRetrofit.BASE_URL_NAME_HEADER + ApiConstant.API_UPDATE_APP_KEY})
        //增加一个Header配置注意FastRetrofit.BASE_URL_NAME_HEADER是必须为step1调用putHeaderBaseUrl方法设置的key
        // 参考com.aries.library.fast.demo.retrofit.service.ApiService#updateApp

        //初始化友盟统计
//        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(mContext, "5b349499b27b0a085f000052", "FastLib"));
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext());
        //初始化通知栏控制
        NotificationUtil.getInstance().init(getApplicationContext());

        //设置用户下载App的初始渠道
        String appChannel = (String) SpUtil.get(getApplicationContext(), SPConstant.SP_KEY_APP_CHANNEL, "");
        LoggerManager.i(TAG, "appChannel0:" + appChannel + ";week:" + FormatUtil.formatWeek(System.currentTimeMillis()) + ";:" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        if (TextUtils.isEmpty(appChannel)) {
            appChannel = CrashReport.getAppChannel();
            Log.i(TAG, "appChannel1:" + appChannel);
            SpUtil.put(getApplicationContext(), SPConstant.SP_KEY_APP_CHANNEL, appChannel);
        } else {
            CrashReport.setAppChannel(getApplicationContext(), appChannel);
        }
        LoggerManager.i(TAG, "appChannel2:" + appChannel);
        TourCooLogUtil.i(TAG, "application总耗时:" + (System.currentTimeMillis() - start));
        if (AppConfig.DEBUG_MODE) {
            //初始化哆啦A梦
            DoraemonKit.install(this);
            // H5任意门功能需要，非必须
            DoraemonKit.setWebDoorCallback(new WebDoorManager.WebDoorCallback() {
                @Override
                public void overrideUrlLoading(Context context, String s) {
                    // 使用自己的H5容器打开这个链接
                    LoggerManager.i("overrideUrlLoading", "url:" + s);
                    WebViewActivity.start(StackUtil.getInstance().getCurrent(), s);
                }
            });
        }

        setShortcut();

    }

    private void setShortcut() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = mContext.getSystemService(ShortcutManager.class);
            List<ShortcutInfo> list = new ArrayList<>();
            ShortcutInfo shortGit;
            ShortcutInfo shortBlog;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jianshu.com/u/a229eee96115"));
            intent.setClassName(getPackageName(), MainTabActivity.class.getName());
            intent.putExtra("url", "https://www.jianshu.com/u/a229eee96115");
            Intent intentGit = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JenkinsZhou"));
            intentGit.setClassName(getPackageName(), MainTabActivity.class.getName());
            intentGit.putExtra("url", "https://github.com/JenkinsZhou");
            shortGit = new ShortcutInfo.Builder(this, "github")
                    .setShortLabel("GitHub")
                    .setLongLabel("GitHub-JenkinsZhou")
                    .setIcon(Icon.createWithResource(mContext, R.drawable.ic_github))
                    .setIntent(intentGit)
                    .build();
            shortBlog = new ShortcutInfo.Builder(this, "jianshu")
                    .setShortLabel("简书")
                    .setLongLabel("简书-JenkinsZhou")
                    .setIcon(Icon.createWithResource(mContext, R.drawable.ic_book))
                    .setIntent(intent)
                    .build();
            list.add(shortGit);
            list.add(shortBlog);
            shortcutManager.setDynamicShortcuts(list);
        }
    }

    /**
     * 获取banner及个人中心设置ImageView宽高
     *
     * @return
     */
    public static int getImageHeight() {
        imageHeight = (int) (SizeUtil.getScreenWidth() * 0.55);
        return imageHeight;
    }

    public static boolean isSupportElevation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 是否控制底部导航栏
     *
     * @return
     */
    public static boolean isControlNavigation() {
        LoggerManager.i(TAG, "mode:" + Build.MODEL);
        return false;
    }

    public static Context getContext() {
        return mContext;
    }


    /**
     * 初始化日志配置
     */
    private void initLog() {
        TourCooLogUtil.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix(TAG_LOG_PRE_SUFFIX)
                .configShowBorders(false).
                configLevel(LogLevel.TYPE_VERBOSE);
        // 支持输入日志到文件
        String filePath = Environment.getExternalStorageDirectory() + PATH_LOG_SAVE;
        TourCooLogUtil.getLogFileConfig().configLogFileEnable(BuildConfig.DEBUG)
                .configLogFilePath(filePath)
                .configLogFileLevel(LogLevel.TYPE_VERBOSE)
                .configLogFileEngine(new LogFileEngineFactory(this));
    }


    public static Application getInstance() {
        return mContext;
    }

    /**
     * 延时加载
     */
    private void loadDelay() {
        handler.postDelayed(() -> {
            initArouter();
            TourCooLogUtil.i(TAG, "initArouter()已执行");
        }, 500);

    }

    /**
     * 初始化路由
     */
    private void initArouter() {
        if (AppConfig.DEBUG_MODE) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(mContext);
    }


  /*  *//**
     * header拦截器*//*

    private Interceptor mHeaderInterceptor = chain -> {
        Request.Builder request = chain.request().newBuilder();
        //避免某些服务器配置攻击,请求返回403 forbidden 问题
        request.addHeader("User-Agent", "Mozilla/5.0 (Android)");
        request.addHeader(KEY_TOKEN, TOKEN + AccountHelper.getInstance().getAccessToken());
        TourCooLogUtil.i(TAG, "携带的token:" + AccountHelper.getInstance().getAccessToken());
        return chain.proceed(request.build());
    };*/


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
