package com.tourcool.bean;

/**
 * @author :JenkinsZhou
 * @description :分类服务实体类
 * @company :途酷科技
 * @date 2019年08月21日16:13
 * @Email: 971613168@qq.com
 */
public class CategoryServiceBean {
    private String categoryName;
    private boolean selected = false;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
