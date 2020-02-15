package com.tourcool.bean.parking;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月14日20:03
 * @Email: 971613168@qq.com
 */
public class CarInfo implements Parcelable {

    /**
     * carId : 0
     * carNum :
     * carType :
     */

    private String carId;
    private String carNum;
    private String carType;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.carId);
        dest.writeString(this.carNum);
        dest.writeString(this.carType);
    }

    public CarInfo() {
    }

    protected CarInfo(Parcel in) {
        this.carId = in.readString();
        this.carNum = in.readString();
        this.carType = in.readString();
    }

    public static final Parcelable.Creator<CarInfo> CREATOR = new Parcelable.Creator<CarInfo>() {
        @Override
        public CarInfo createFromParcel(Parcel source) {
            return new CarInfo(source);
        }

        @Override
        public CarInfo[] newArray(int size) {
            return new CarInfo[size];
        }
    };
}
