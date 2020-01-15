package com.tourcool.adapter.weather;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.home.Weather;
import com.tourcool.bean.weather.SimpleWeather;
import com.tourcool.core.util.DateUtil;
import com.tourcool.smartcity.R;

import java.util.List;

import static com.tourcool.core.constant.WeatherConstant.WEATHER_DUO_YUN;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_QING;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_RAIN_AND_SNOW;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_SMALL_SNOW;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_XIAO_YU;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_YIN;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年11月04日11:33
 * @Email: 971613168@qq.com
 */
public class WeatherAdapter extends BaseQuickAdapter<SimpleWeather, BaseViewHolder> {
    public WeatherAdapter() {
        super(R.layout.item_weather_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SimpleWeather item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tvDate, transformDate(item.getDate()));
        ImageView ivWeather = helper.getView(R.id.ivWeather);
        String tempRange = item.getTemplow() + "°～" + item.getTemphigh() + "°";
        helper.setText(R.id.tvTemperatureRange, tempRange);
        switch (item.getWeather()) {
            case WEATHER_QING:
                GlideManager.loadImg(R.mipmap.ic_weather_day_qing, ivWeather);
                break;
            case WEATHER_DUO_YUN:
                GlideManager.loadImg(R.mipmap.ic_weather_duoyun, ivWeather);
                break;
            case WEATHER_YIN:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_yin, ivWeather);
                break;
            case WEATHER_XIAO_YU:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_xiao_yu, ivWeather);
                break;
            case WEATHER_RAIN_AND_SNOW:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_rain_and_snow, ivWeather);
                break;
            case WEATHER_SMALL_SNOW:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_small_snow, ivWeather);
                break;
            default:
                TourCooLogUtil.i(TAG, "item.getWeather()=" + item.getWeather());
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_unknown, ivWeather);
                break;
        }
    }


    private String transformDate(String date) {
        return DateUtil.formatDateToMonthAndDaySlash(date);
    }
}
