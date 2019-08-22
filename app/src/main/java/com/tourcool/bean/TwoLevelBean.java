package com.tourcool.bean;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :二级分类实体类
 * @company :途酷科技
 * @date 2019年08月20日17:01
 * @Email: 971613168@qq.com
 */
public class TwoLevelBean implements Cloneable {
    private String groupName;
    private List<TwoLevelChildBean> childBeans;
    private String groupIcon;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<TwoLevelChildBean> getChildBeans() {
        return childBeans;
    }

    public void setChildBeans(List<TwoLevelChildBean> childBeans) {
        this.childBeans = childBeans;
    }


    @Override
    public Object clone()
    {
        Object object = null;
        try {
            object = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return object;
    }
}
