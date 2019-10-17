package com.tourcool.core.entity;


/**
 * 栏目类型
 * <p>
 * +架构型栏目（栏目下包含栏目）
 * +功能型
 * +轮播图型栏目
 * +文字型栏目
 *
 * @author peter
 * create: 2019-10-08 09:38
 **/
public enum ColumnTypeEnum  {

    FRAMEWORK(0, "架构型栏目（栏目下包含栏目）", new Integer[]{2}),
    FUNCTIONAL(1, "功能型", new Integer[]{2}),
    CAROUSEL(2, "轮播图型栏目", new Integer[]{0}),
    TEXT(3, "文字型栏目", new Integer[]{1}),
    WEATHER(4, "天气", new Integer[]{3});

    private int code;

    private String descr;

    private Integer[] containChannelCodes;

    ColumnTypeEnum(int code, String descr, Integer[] containChannelCodes) {
        this.code = code;
        this.descr = descr;
        this.containChannelCodes = containChannelCodes;
    }

    public boolean isFramework() {
        return ColumnTypeEnum.FRAMEWORK == this;
    }


    public int getCode() {
        return code;
    }

    public String getDescr() {
        return descr;
    }



}
