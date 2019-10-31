package com.tourcool.bean.account;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月31日14:24
 * @Email: 971613168@qq.com
 */
public class UserInfo extends LitePalSupport implements Parcelable {


    /**
     * authenticationLevel : 0
     * iconUrl :
     * nickname :
     * phoneNumber :
     */

    private int authenticationLevel;
    private String iconUrl;
    private String nickname;
    private String phoneNumber;

    public int getAuthenticationLevel() {
        return authenticationLevel;
    }

    public void setAuthenticationLevel(int authenticationLevel) {
        this.authenticationLevel = authenticationLevel;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.authenticationLevel);
        dest.writeString(this.iconUrl);
        dest.writeString(this.nickname);
        dest.writeString(this.phoneNumber);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.authenticationLevel = in.readInt();
        this.iconUrl = in.readString();
        this.nickname = in.readString();
        this.phoneNumber = in.readString();
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
