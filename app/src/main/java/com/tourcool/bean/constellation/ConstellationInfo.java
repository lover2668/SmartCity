package com.tourcool.bean.constellation;

/**
 * @author :JenkinsZhou
 * @description : 星座
 * @company :途酷科技
 * @date 2020年11月25日15:33
 * @Email: 971613168@qq.com
 */
public class ConstellationInfo {

    private String name;

    private String month;

    private int imageIcon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ConstellationInfo(String name, String month, int imageIcon) {
        this.name = name;
        this.month = month;
        this.imageIcon = imageIcon;
    }

    public ConstellationInfo() {
    }
}
