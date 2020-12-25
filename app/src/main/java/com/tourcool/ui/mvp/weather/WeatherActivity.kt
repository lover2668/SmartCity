package com.tourcool.ui.mvp.weather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.frame.library.core.manager.GlideManager
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.tourcool.adapter.weather.WeatherAdapter
import com.tourcool.bean.weather.SimpleWeather
import com.tourcool.bean.weather.WeatherEntity
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.constant.RouteConstance
import com.tourcool.core.constant.WeatherConstant
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.util.DateUtil
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * @author :JenkinsZhou
 * @description :天气
 * @company :途酷科技
 * @date 2019年09月27日13:57
 * @Email: 971613168@qq.com
 */
@Route(path = RouteConstance.ACTIVITY_URL_WEATHER)
class WeatherActivity : BaseTitleTransparentActivity(), View.OnClickListener, OnRefreshListener {
    private var weatherAdapter: WeatherAdapter? = null
    private var headerView: View? = null
    private var tvCityName: TextView? = null
    private var tvDate: TextView? = null
    private var tvTemperatureRange: TextView? = null
    private var tvWeatherDesc: TextView? = null
    private var tvAirQuality: TextView? = null
    private var tvWindDesc: TextView? = null
    private var tvTemperature: TextView? = null
    private var ivWeather: ImageView? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_weather
    }

    override fun initView(savedInstanceState: Bundle?) {
        smartRefreshCommon!!.setRefreshHeader(ClassicsHeader(mContext))
        smartRefreshCommon.setOnRefreshListener(this)
        headerView = View.inflate(mContext, R.layout.header_weather_layout, null)
        tvCityName = headerView!!.findViewById(R.id.tvCityName)
        tvDate = headerView!!.findViewById(R.id.tvDate)
        tvTemperatureRange = headerView!!.findViewById(R.id.tvTemperatureRange)
        tvWeatherDesc = headerView!!.findViewById(R.id.tvWeatherDesc)
        tvWindDesc = headerView!!.findViewById(R.id.tvWindDesc)
        tvTemperature = headerView!!.findViewById(R.id.tvTemperature)
        ivWeather = headerView!!.findViewById(R.id.ivWeather)
        tvAirQuality = headerView!!.findViewById(R.id.tvAirQuality)
        tvTemperature = headerView!!.findViewById(R.id.tvTemperature)
    }

    override fun loadData() {
        val rvWeather = findViewById<RecyclerView>(R.id.rvWeather)
        rvWeather.layoutManager = LinearLayoutManager(mContext)
        weatherAdapter = WeatherAdapter()
        weatherAdapter!!.bindToRecyclerView(rvWeather)
        weatherAdapter!!.addHeaderView(headerView)
        requestWeatherInfo()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvlevle2 -> {
                val intent = Intent()
                intent.setClass(mContext, WeatherActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    override fun setTitleBar(titleBar: TitleBarView) {
        super.setTitleBar(titleBar)
        titleBar.setTitleMainText("天气")
    }

    private fun requestWeatherInfo() {
        ApiRepository.getInstance().requestWeatherInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<WeatherEntity?>?>() {
            override fun onRequestNext(entity: BaseResult<WeatherEntity?>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    handleCallBack(entity.data)
                } else {
                    ToastUtil.showFailed(entity.errorMsg)
                }
            }
        })
    }

    private fun handleCallBack(weatherEntity: WeatherEntity?) {
        smartRefreshCommon!!.finishRefresh()
        if (weatherEntity == null || weatherEntity.dailyWeathers == null) {
            return
        }
        showTodayWeather(weatherEntity.weather)
        weatherAdapter!!.setNewData(weatherEntity.dailyWeathers)
    }

    private fun transformDate(date: String): String {
        return DateUtil.formatDateToMonthAndDaySlash(date)
    }

    private fun showTodayWeather(simpleWeather: SimpleWeather?) {
        if (simpleWeather == null) {
            return
        }
        val tempRange = simpleWeather.templow + "°～" + simpleWeather.temphigh + "°"
        tvDate!!.text = transformDate(simpleWeather.date)
        tvCityName!!.text = simpleWeather.city
        tvTemperature!!.text = simpleWeather.temp + "°"
        tvTemperatureRange!!.text = tempRange
        tvWeatherDesc!!.text = simpleWeather.weather
        tvAirQuality!!.text = "空气质量：" + simpleWeather.quality
        tvWindDesc!!.text = simpleWeather.winddirect + "：" + simpleWeather.windspeed + "级"


        when (simpleWeather.weather) {
            WeatherConstant.WEATHER_QING -> GlideManager.loadImg(R.mipmap.ic_weather_day_qing, ivWeather)
            WeatherConstant.WEATHER_DUO_YUN -> GlideManager.loadImg(R.mipmap.ic_weather_duoyun, ivWeather)
            WeatherConstant.WEATHER_YIN -> GlideManager.loadImgCenterInside(R.mipmap.ic_weather_yin, ivWeather)
            WeatherConstant.WEATHER_XIAO_YU, WeatherConstant.WEATHER_YU -> GlideManager.loadImgCenterInside(R.mipmap.ic_weather_xiao_yu, ivWeather)
            WeatherConstant.WEATHER_RAIN_AND_SNOW -> GlideManager.loadImgCenterInside(R.mipmap.ic_weather_rain_and_snow, ivWeather)
            WeatherConstant.WEATHER_SMALL_SNOW -> GlideManager.loadImgCenterInside(R.mipmap.ic_weather_small_snow, ivWeather)
            WeatherConstant.WEATHER_RAIN_MIDDLE -> GlideManager.loadImgCenterInside(R.mipmap.ic_weather_middle_snow, ivWeather)
            WeatherConstant.WEATHER_HEAVY_RAIN, WeatherConstant.WEATHER_TOP_RAIN -> GlideManager.loadImgCenterInside(R.mipmap.ic_weather_top_rain, ivWeather)
            else -> {
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_unknown, ivWeather)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        requestWeatherInfo()
    }
}