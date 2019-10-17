package com.tourcool.core.entity;



/**
 * 频道的类型
 * +图片（轮播图）
 * +文字（热点资讯）
 * +功能（频道）
 *
 * @author peter
 * create: 2019-09-30 10:33
 **/
public enum ChannelTypeEnum {

    PICTURE(0, "图片类型"),
    TEXT(1, "文本类型"),
    FUNCTION(2, "功能型"),
    WEATHER(3, "天气型");

    private int code;
    private String descr;

    ChannelTypeEnum(int code, String descr) {
        this.code = code;
        this.descr = descr;
    }


    public int getCode() {
        return code;
    }

    public String getDescr() {
        return descr;
    }



}
