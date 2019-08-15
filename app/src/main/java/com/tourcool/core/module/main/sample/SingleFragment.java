package com.tourcool.core.module.main.sample;

import android.os.Bundle;

import com.tourcool.core.module.activity.MovieBaseFragment;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:20
 * @E-Mail: 971613168@qq.com
 * @Function: 单Fragment Activity懒加载示例
 * @Description:
 */
public class SingleFragment extends MovieBaseFragment {

    public static SingleFragment newInstance(String url) {
        Bundle args = new Bundle();
        SingleFragment fragment = new SingleFragment();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }
}
