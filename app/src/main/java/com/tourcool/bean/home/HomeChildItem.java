package com.tourcool.bean.home;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月26日14:19
 * @Email: 971613168@qq.com
 */
public class HomeChildItem implements Serializable, Cloneable {
    /**
     * 还未添加
     */
    public static final int STATUS_NO_SELECTED = 1;

    /**
     * 已经添加
     */
    public static final int STATUS_SELECTED = 2;

    /**
     * 是否是父类模块
     */
    private boolean parentGroup = true;

    public boolean isParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(boolean parentGroup) {
        this.parentGroup = parentGroup;
    }

    private String clickLink;
    private String clickType;
    private String icon;
    private int id;
    private String subTitle;
    private String title;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * 使用序列化技术实现深拷贝
     *
     * @return
     */
    public HomeChildItem copy()  {
        //将对象写入流中
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream ;
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //从流中取出
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream ;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return (HomeChildItem) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected HomeChildItem clone() throws CloneNotSupportedException {
        return (HomeChildItem) super.clone();
    }
}
