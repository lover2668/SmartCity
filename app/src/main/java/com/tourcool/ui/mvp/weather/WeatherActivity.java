package com.tourcool.ui.mvp.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.weather.WeatherAdapter;
import com.tourcool.bean.weather.SimpleWeather;
import com.tourcool.bean.weather.WeatherEntity;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.DateUtil;
import com.tourcool.smartcity.R;
import com.tourcool.ui.base.BaseTitleTransparentActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_LOGIN;
import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_WEATHER;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_DUO_YUN;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_QING;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_XIAO_YU;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_YIN;

/**
 * @author :JenkinsZhou
 * @description :天气
 * @company :途酷科技
 * @date 2019年09月27日13:57
 * @Email: 971613168@qq.com
 */
@Route(path = ACTIVITY_URL_WEATHER)
public class WeatherActivity extends BaseTitleTransparentActivity implements View.OnClickListener {
    private WeatherAdapter weatherAdapter;
    private View headerView;
    private TextView tvCityName;
    private TextView tvDate;
    private TextView tvTemperatureRange;
    private TextView tvWeatherDesc;
    private TextView tvAirQuality;
    private TextView tvWindDesc;
    private TextView tvTemperature;
    private ImageView ivWeather;
    @Override
    public int getContentLayout() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
         headerView = View.inflate(mContext,R.layout.header_weather_layout,null);
        tvCityName = headerView.findViewById(R.id.tvCityName);
        tvDate = headerView.findViewById(R.id.tvDate);
        tvTemperatureRange = headerView.findViewById(R.id.tvTemperatureRange);
        tvWeatherDesc = headerView.findViewById(R.id.tvWeatherDesc);
        tvWindDesc = headerView.findViewById(R.id.tvWindDesc);
        tvTemperature = headerView.findViewById(R.id.tvTemperature);
        ivWeather = headerView.findViewById(R.id.ivWeather);
        tvAirQuality = headerView.findViewById(R.id.tvAirQuality);
        tvTemperature = headerView.findViewById(R.id.tvTemperature);
    }

    @Override
    public void loadData() {
        RecyclerView rvWeather = findViewById(R.id.rvWeather);
        rvWeather.setLayoutManager(new LinearLayoutManager(mContext));
        weatherAdapter = new WeatherAdapter();
        weatherAdapter.bindToRecyclerView(rvWeather);
        weatherAdapter.addHeaderView(headerView);
        requestWeatherInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvlevle2:
                Intent intent = new Intent();
                intent.setClass(mContext,WeatherActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("天气");
    }


    private void requestWeatherInfo() {
        ApiRepository.getInstance().requestWeatherInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult<WeatherEntity>>() {
                    @Override
                    public void onRequestNext(BaseResult<WeatherEntity> entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            handleCallBack(entity.data);
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }
                });
    }



    private void handleCallBack(WeatherEntity weatherEntity){
        if(weatherEntity == null ||weatherEntity.getDailyWeathers() == null ){
            return;
        }
        showTodayWeather(weatherEntity.getWeather());
        weatherAdapter.setNewData(weatherEntity.getDailyWeathers());
    }

    private String transformDate(String date) {
        return  DateUtil.formatDateToMonthAndDaySlash(date);
    }


    private void showTodayWeather(SimpleWeather simpleWeather){
        if (simpleWeather == null){
            return;
        }
        String tempRange = simpleWeather.getTemplow() +"°～"+simpleWeather.getTemphigh()+"°";
        tvDate.setText(transformDate(simpleWeather.getDate()));
        tvCityName.setText(simpleWeather.getCity());
        tvTemperature.setText(simpleWeather.getTemp()+"°");
        tvTemperatureRange.setText(tempRange);
        tvWeatherDesc.setText(simpleWeather.getWeather());
        tvAirQuality.setText("空气质量："+simpleWeather.getQuality());
        tvWindDesc.setText(simpleWeather.getWinddirect()+"："+simpleWeather.getWindspeed()+"级");
        switch (simpleWeather.getWeather()) {
            case WEATHER_QING:
                GlideManager.loadImg(R.mipmap.ic_weather_day_qing,ivWeather);
                break;
            case WEATHER_DUO_YUN:
                GlideManager.loadImg(R.mipmap.ic_weather_duoyun,ivWeather);
                break;
            case WEATHER_YIN:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_yin, ivWeather);
                break;
            case WEATHER_XIAO_YU:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_xiao_yu, ivWeather);
                break;
            default:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_unknown, ivWeather);
                break;
        }
    }
}
