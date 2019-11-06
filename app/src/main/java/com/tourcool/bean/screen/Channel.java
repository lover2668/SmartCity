package com.tourcool.bean.screen;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月16日11:05
 * @Email: 971613168@qq.com
 */
public class Channel implements Parcelable, Serializable {

    private int id;
    private String icon;
    private String circleIcon;

    public String getCircleIcon() {
        return circleIcon;
    }

    public void setCircleIcon(String circleIcon) {
        this.circleIcon = circleIcon;
    }

    private String title;
    private String description;
    private int jumpWay;
    private String link;
    /**
     *  跳转链接需要的权限
     */
    private int authLevel;
    private int channelType;
    private long createTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getJumpWay() {
        return jumpWay;
    }

    public void setJumpWay(int jumpWay) {
        this.jumpWay = jumpWay;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(int authLevel) {
        this.authLevel = authLevel;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.icon);
        dest.writeString(this.circleIcon);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.jumpWay);
        dest.writeString(this.link);
        dest.writeInt(this.authLevel);
        dest.writeInt(this.channelType);
        dest.writeLong(this.createTime);
    }

    public Channel() {
    }

    protected Channel(Parcel in) {
        this.id = in.readInt();
        this.icon = in.readString();
        this.circleIcon = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.jumpWay = in.readInt();
        this.link = in.readString();
        this.authLevel = in.readInt();
        this.channelType = in.readInt();
        this.createTime = in.readLong();
    }

    public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel source) {
            return new Channel(source);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}
