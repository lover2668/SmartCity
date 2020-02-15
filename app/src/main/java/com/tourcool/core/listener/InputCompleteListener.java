package com.tourcool.core.listener;

import android.text.Editable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月15日17:19
 * @Email: 971613168@qq.com
 */
public interface InputCompleteListener {
    void onComplete(Editable editable, String code);
}
