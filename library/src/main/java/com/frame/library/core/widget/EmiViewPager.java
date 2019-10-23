package com.frame.library.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.tourcool.library.frame.demo.R;


/**
 * @author :zhoujian
 * @description :支持禁止左右滑动的viewpager
 * @company :翼迈科技
 * @date 2018年 09月 17日 09时20分
 * @Email: 971613168@qq.com
 */
public class EmiViewPager extends ViewPager {


    /**
     * 左右滑动是否可用（默认可用）
     */
    private boolean slidingEnable;

    public EmiViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EmiViewPager);
        slidingEnable = typedArray.getBoolean(R.styleable.EmiViewPager_slidingEnable, true);
        typedArray.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!slidingEnable) {
            return false;
        } else {
            return super.onTouchEvent(motionEvent);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!slidingEnable) {
            return false;
        } else {
            return super.onInterceptTouchEvent(motionEvent);
        }
    }
}
