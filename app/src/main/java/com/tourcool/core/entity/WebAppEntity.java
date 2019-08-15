package com.tourcool.core.entity;

import android.graphics.Color;

/**
 * @Author: JenkinsZhou on 2019/4/24 14:28
 * @E-Mail: 971613168@qq.com
 * @Function:
 * @Description:
 */
public class WebAppEntity {


    public int icon;
    public CharSequence title;
    public String url;
    public int color = Color.WHITE;

    public WebAppEntity() {
    }

    public WebAppEntity(int icon, CharSequence title, String url, int color) {
        this.icon = icon;
        this.title = title;
        this.url = url;
        this.color = color;
    }

    public WebAppEntity(int icon, CharSequence title, String url) {
        this(icon, title, url, Color.WHITE);
    }
}
