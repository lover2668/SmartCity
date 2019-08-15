package com.tourcool.core.entity;

import android.app.Activity;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:18
 * @E-Mail: 971613168@qq.com
 * @Function: 描述性条目实体
 * @Description:
 */
public class WidgetEntity {

    public String title;
    public String content;
    public String url;
    public Class<? extends Activity> activity;

    public WidgetEntity() {
    }

    public WidgetEntity(String title, String content, Class<? extends Activity> activity) {
        this.title = title;
        this.content = content;
        this.activity = activity;
    }

    public WidgetEntity(String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.url = url;
    }
}
