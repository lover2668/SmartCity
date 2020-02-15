package com.tourcool.bean.parking;

/**
 * @author :JenkinsZhou
 * @description :停车记录
 * @company :翼迈科技股份有限公司
 * @date 2020年02月15日23:21
 * @Email: 971613168@qq.com
 */
public class ParingRecord {


    /**
     * carNum : 苏B2NR58
     * carPortCode : A16012
     * endTime : 1580720751000
     * feeAmount : 11
     * id : 681798
     * parkingLot : 东尤场停车点
     * parkingLotCode : 100001_A16
     * startTime : 1580710195000
     * parkTime : 10556
     */

    private String carNum;
    private String carPortCode;
    private long endTime;
    private String feeAmount;
    private long id;
    private String parkingLot;
    private String parkingLotCode;
    private long startTime;
    private int parkTime;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarPortCode() {
        return carPortCode;
    }

    public void setCarPortCode(String carPortCode) {
        this.carPortCode = carPortCode;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(String parkingLot) {
        this.parkingLot = parkingLot;
    }

    public String getParkingLotCode() {
        return parkingLotCode;
    }

    public void setParkingLotCode(String parkingLotCode) {
        this.parkingLotCode = parkingLotCode;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getParkTime() {
        return parkTime;
    }

    public void setParkTime(int parkTime) {
        this.parkTime = parkTime;
    }
}
