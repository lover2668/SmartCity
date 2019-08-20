package com.tourcool.bean;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月20日17:33
 * @Email: 971613168@qq.com
 */
public class TwoLevelChildBean implements Serializable {
    private String childItemIcon;
    private String childItemTitle;
    private String childItemDesc;

    public String getChildItemIcon() {
        return childItemIcon;
    }

    public void setChildItemIcon(String childItemIcon) {
        this.childItemIcon = childItemIcon;
    }

    public String getChildItemTitle() {
        return childItemTitle;
    }

    public void setChildItemTitle(String childItemTitle) {
        this.childItemTitle = childItemTitle;
    }

    public String getChildItemDesc() {
        return childItemDesc;
    }

    public void setChildItemDesc(String childItemDesc) {
        this.childItemDesc = childItemDesc;
    }




}
