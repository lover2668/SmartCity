package com.tourcool.bean.weather;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月24日14:51
 * @Email: 971613168@qq.com
 */
public class SimpleWeather {


   /* {
        "city": "宜兴市",
            "date": "2019-10-24",
            "week": "星期四",
            "weather": "晴",
            "temp": "24",
            "temphigh": "25",
            "templow": "14",
            "humidity": "49",
            "pressure": "1017",
            "windspeed": "2.4",
            "winddirect": "西风",
            "windpower": null,
            "quality": "良"
    }
}*/

    private String city;
    private String date;
    private String week;
    private String weather;
    private String temp;
    private String temphigh;
    private String templow;
    private String humidity;
    private String pressure;
    private String windspeed;
    private String winddirect;
    private Object windpower;
    private String quality;

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemphigh() {
        return temphigh;
    }

    public void setTemphigh(String temphigh) {
        this.temphigh = temphigh;
    }

    public String getTemplow() {
        return templow;
    }

    public void setTemplow(String templow) {
        this.templow = templow;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public String getWinddirect() {
        return winddirect;
    }

    public void setWinddirect(String winddirect) {
        this.winddirect = winddirect;
    }

    public Object getWindpower() {
        return windpower;
    }

    public void setWindpower(Object windpower) {
        this.windpower = windpower;
    }
}
