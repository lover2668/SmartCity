package com.tourcool.bean.driver;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description : JenkinsZhou
 * @company :途酷科技
 * @date 2020年11月27日14:13
 * @Email: 971613168@qq.com
 */
public class DriverAgainstInfo {


    /**
     * city :
     * hphm :
     * hpzl :
     * lists : [{"act":"","archiveno":"","area":"","cjjg":"","code":"","date":"","fen":"","handled":"","handledStr":"","money":"","wzcity":""}]
     * province :
     */

    private String city;
    private String hphm;
    private String hpzl;
    private String province;
    private List<DriverAgainstDetail> lists;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHphm() {
        return hphm;
    }

    public void setHphm(String hphm) {
        this.hphm = hphm;
    }

    public String getHpzl() {
        return hpzl;
    }

    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<DriverAgainstDetail> getLists() {
        return lists;
    }

    public void setLists(List<DriverAgainstDetail> lists) {
        this.lists = lists;
    }


}
