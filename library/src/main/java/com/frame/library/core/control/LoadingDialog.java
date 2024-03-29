package com.frame.library.core.control;

import android.app.Activity;
import androidx.annotation.Nullable;

import com.frame.library.core.widget.LoadingDialogWrapper;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:39
 * @E-Mail: 971613168@qq.com
 * Function: 用于全局配置网络请求登录Loading提示框
 * Description:
 */
public interface LoadingDialog {

    /**
     * 设置快速Loading Dialog
     *
     * @param activity
     * @return
     */
    @Nullable
    LoadingDialogWrapper createLoadingDialog(@Nullable Activity activity);
}
