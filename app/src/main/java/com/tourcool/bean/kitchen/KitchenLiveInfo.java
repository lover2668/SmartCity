package com.tourcool.bean.kitchen;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :明厨直播实体
 * @company :翼迈科技股份有限公司
 * @date 2020年02月05日17:43
 * @Email: 971613168@qq.com
 */
public class KitchenLiveInfo implements Serializable, Parcelable {

    private String cameraName;
    private int chanNum;
    private String deviceSerial;
    private int isEncrypt;
    private int onlineStatus;
    private int status;
    private int userCameraState;
    private String groupId;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    private int layoutType;

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public int getChanNum() {
        return chanNum;
    }

    public void setChanNum(int chanNum) {
        this.chanNum = chanNum;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getIsEncrypt() {
        return isEncrypt;
    }

    public void setIsEncrypt(int isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserCameraState() {
        return userCameraState;
    }

    public void setUserCameraState(int userCameraState) {
        this.userCameraState = userCameraState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cameraName);
        dest.writeInt(this.chanNum);
        dest.writeString(this.deviceSerial);
        dest.writeInt(this.isEncrypt);
        dest.writeInt(this.onlineStatus);
        dest.writeInt(this.status);
        dest.writeInt(this.userCameraState);
    }

    public KitchenLiveInfo() {
    }

    protected KitchenLiveInfo(Parcel in) {
        this.cameraName = in.readString();
        this.chanNum = in.readInt();
        this.deviceSerial = in.readString();
        this.isEncrypt = in.readInt();
        this.onlineStatus = in.readInt();
        this.status = in.readInt();
        this.userCameraState = in.readInt();
    }

    public static final Creator<KitchenLiveInfo> CREATOR = new Creator<KitchenLiveInfo>() {
        @Override
        public KitchenLiveInfo createFromParcel(Parcel source) {
            return new KitchenLiveInfo(source);
        }

        @Override
        public KitchenLiveInfo[] newArray(int size) {
            return new KitchenLiveInfo[size];
        }
    };
}
