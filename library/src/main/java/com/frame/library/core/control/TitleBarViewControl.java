package com.frame.library.core.control;


import com.frame.library.core.widget.titlebar.TitleBarView;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:51
 * @E-Mail: 971613168@qq.com
 * Function: 全局TitleBarView属性控制
 * Description:
 */
public interface TitleBarViewControl {

    /**
     * 全局设置TitleBarView 属性回调
     *
     * @param titleBar
     * @param cls 包含TitleBarView的类
     * @return
     */
    boolean createTitleBarViewControl(TitleBarView titleBar, Class<?> cls);
}
