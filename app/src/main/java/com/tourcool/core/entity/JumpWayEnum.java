package com.tourcool.core.entity;


/**
 * 跳转方式
 *
 * @author peter
 * create: 2019-09-30 09:39
 **/
public enum JumpWayEnum  {

    NONE(0, "不跳转"),
    OUTER_LINK(1, "外链"),
    INNER_LINK(2, "内部链接"),
    PRELOAD(3, "预加载链接，通常是加载新闻资讯页面时使用");


    private int code;


    private String descr;

    JumpWayEnum(int code, String descr) {
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
