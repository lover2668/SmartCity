package com.tourcool.bean.home;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月27日10:56
 * @Email: 971613168@qq.com
 */
public class Weather {


    /**
     * air_level : 良
     * city : 宜兴
     * date : 2019-08-23
     * tem : 28℃
     * wea : 阵雨转多云
     * wea_img : lei
     */

    private String air_level;
    private String city;
    private String date;
    private String tem;
    private String wea;
    private String wea_img;

    public String getAir_level() {
        return air_level;
    }

    public void setAir_level(String air_level) {
        this.air_level = air_level;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getWea() {
        return wea;
    }

    public void setWea(String wea) {
        this.wea = wea;
    }

    public String getWea_img() {
        return wea_img;
    }

    public void setWea_img(String wea_img) {
        this.wea_img = wea_img;
    }

    public Weather() {
    }
}
