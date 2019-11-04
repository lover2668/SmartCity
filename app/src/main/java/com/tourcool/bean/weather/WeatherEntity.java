package com.tourcool.bean.weather;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年11月04日15:30
 * @Email: 971613168@qq.com
 */
public class WeatherEntity {
    /**
     * weather : {"city":"宜兴市","date":"2019-11-04","week":"星期一","weather":"多云","temp":"18","temphigh":"21","templow":"11","humidity":"76","pressure":"1024","windspeed":"0.2","winddirect":"静风","windpower":null,"quality":"良"}
     * dailyWeathers : [{"date":"2019-11-04","week":"星期一","weather":"阴","sunrise":"06:17","sunset":"17:10","temphigh":"21","templow":"11","winddirect":"东北风","windpower":"微风"},{"date":"2019-11-05","week":"星期二","weather":"多云","sunrise":"06:18","sunset":"17:10","temphigh":"20","templow":"10","winddirect":"东北风","windpower":"微风"},{"date":"2019-11-06","week":"星期三","weather":"多云","sunrise":"06:19","sunset":"17:09","temphigh":"20","templow":"15","winddirect":"东北风","windpower":"微风"},{"date":"2019-11-07","week":"星期四","weather":"阴","sunrise":"06:20","sunset":"17:08","temphigh":"20","templow":"8","winddirect":"东风","windpower":"微风"},{"date":"2019-11-08","week":"星期五","weather":"晴","sunrise":"06:20","sunset":"17:07","temphigh":"19","templow":"8","winddirect":"北风","windpower":"微风"},{"date":"2019-11-09","week":"星期六","weather":"多云","sunrise":"06:21","sunset":"17:07","temphigh":"21","templow":"10","winddirect":"东北风","windpower":"微风"},{"date":"2019-11-10","week":"星期日","weather":"晴","sunrise":"06:22","sunset":"17:06","temphigh":"20","templow":"9","winddirect":"东南风","windpower":"微风"}]
     */
    private SimpleWeather weather;
    private List<SimpleWeather> dailyWeathers;

    public List<SimpleWeather> getDailyWeathers() {
        return dailyWeathers;
    }

    public void setDailyWeathers(List<SimpleWeather> dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
    }

    public SimpleWeather getWeather() {
        return weather;
    }

    public void setWeather(SimpleWeather weather) {
        this.weather = weather;
    }
}
