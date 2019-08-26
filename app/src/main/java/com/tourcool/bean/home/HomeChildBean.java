package com.tourcool.bean.home;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月26日14:20
 * @Email: 971613168@qq.com
 */
public class HomeChildBean {
    private String clickLink;
    private String clickType;
    private String icon;
    private String title;
    private List<HomeChildItem> childList;

    public String getClickLink() {
        return clickLink;
    }

    public void setClickLink(String clickLink) {
        this.clickLink = clickLink;
    }

    public String getClickType() {
        return clickType;
    }

    public void setClickType(String clickType) {
        this.clickType = clickType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HomeChildItem> getChildList() {
        return childList;
    }

    public void setChildList(List<HomeChildItem> childList) {
        this.childList = childList;
    }
}
