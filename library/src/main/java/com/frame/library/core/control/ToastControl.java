package com.frame.library.core.control;

import android.widget.Toast;

import com.aries.ui.view.radius.RadiusTextView;
import com.frame.library.core.util.ToastUtil;

/**
 * @Author: JenkinsZhou on 2019/1/18 17:49
 * @E-Mail: 971613168@qq.com
 * @Function: {@link ToastUtil} 控制
 * @Description:
 */
public interface ToastControl {

    /**
     * 处理其它异常情况
     *
     * @return
     */
    Toast getToast();

    /**
     * 设置Toast
     *
     * @param toast    ToastUtil 中的Toast
     * @param textView ToastUtil 中的Toast设置的View
     */
    void setToast(Toast toast, RadiusTextView textView);
}
