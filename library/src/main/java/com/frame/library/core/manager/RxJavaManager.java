package com.frame.library.core.manager;

import com.frame.library.core.retrofit.FrameTransformer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @Author: JenkinsZhou on 2018/7/23 11:02
 * @E-Mail: 971613168@qq.com
 * Function: RxJava使用管理类
 * Description:
 * 1、2018-7-23 11:10:35 去掉设置定时器返回值并标记为废弃使用另一个{@link #setTimer(long)}
 */
public class RxJavaManager {

    public interface TimerListener {
        /**
         * 倒计时结束回调
         */
        void timeEnd();
    }

    private static volatile RxJavaManager instance;

    private RxJavaManager() {
    }

    public static RxJavaManager getInstance() {
        if (instance == null) {
            synchronized (RxJavaManager.class) {
                if (instance == null) {
                    instance = new RxJavaManager();
                }
            }
        }
        return instance;
    }

    /**
     * 创建Observable
     *
     * @param value
     * @param delay
     * @param unit
     * @param <T>
     * @return
     */
    public <T> Observable<T> getDelayObservable(T value, long delay, TimeUnit unit) {
        return Observable.just(value)
                .delay(delay, unit)
                .compose(FrameTransformer.<T>switchSchedulers());
    }

    /**
     * 创建 时延单位秒的Observable
     *
     * @param value
     * @param delay
     * @param <T>
     * @return
     */
    public <T> Observable<T> getDelayObservable(T value, long delay) {
        return getDelayObservable(value, delay, TimeUnit.SECONDS);
    }

    /**
     * 设置时延为毫秒的定时器
     *
     * @param delayTime
     * @return
     */
    public Observable<Long> setTimer(long delayTime) {
        return getDelayObservable(delayTime, delayTime, TimeUnit.MILLISECONDS);
    }


    /**
     * milliseconds毫秒后执行next操作(倒计时)
     *
     * @param milliseconds
     * @param
     */
    public void doEventCountDown(long milliseconds, Observer<Long> observer) {
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    /**
     * 每隔milliseconds毫秒 执行一次命令
     *
     * @param milliseconds
     * @param observable
     */
    public void doEventByInterval(long milliseconds, Observer<Long> observable) {
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observable);
    }

}
