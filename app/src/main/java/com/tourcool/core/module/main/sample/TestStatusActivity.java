package com.tourcool.core.module.main.sample;

import android.app.Activity;
import android.os.Bundle;


import androidx.annotation.Nullable;

import com.tourcool.library.frame.R;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:23
 * @E-Mail: 971613168@qq.com
 * @Function: 模拟非FastLib三方库且未实现状态栏沉浸效果
 * @Description:
 */
public class TestStatusActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_status);
    }
}
