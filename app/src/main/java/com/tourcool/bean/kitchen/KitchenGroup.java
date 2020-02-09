package com.tourcool.bean.kitchen;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月05日17:30
 * @Email: 971613168@qq.com
 */
public class KitchenGroup implements Parcelable, Serializable {

    /**
     * childrenList : [{"cameraName":"","chanNum":0,"deviceSerial":"","isEncrypt":0,"onlineStatus":0,"status":0,"userCameraState":0}]
     * groupId :
     * groupName :
     */

    private String groupId;
    private String groupName;
    private List<KitchenLiveInfo> childrenList;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<KitchenLiveInfo> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<KitchenLiveInfo> childrenList) {
        this.childrenList = childrenList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupId);
        dest.writeString(this.groupName);
        dest.writeList(this.childrenList);
        dest.writeByte(this.select ? (byte) 1 : (byte) 0);
    }

    public KitchenGroup() {
    }

    protected KitchenGroup(Parcel in) {
        this.groupId = in.readString();
        this.groupName = in.readString();
        this.childrenList = new ArrayList<KitchenLiveInfo>();
        in.readList(this.childrenList, KitchenLiveInfo.class.getClassLoader());
        this.select = in.readByte() != 0;
    }

    public static final Parcelable.Creator<KitchenGroup> CREATOR = new Parcelable.Creator<KitchenGroup>() {
        @Override
        public KitchenGroup createFromParcel(Parcel source) {
            return new KitchenGroup(source);
        }

        @Override
        public KitchenGroup[] newArray(int size) {
            return new KitchenGroup[size];
        }
    };
}
