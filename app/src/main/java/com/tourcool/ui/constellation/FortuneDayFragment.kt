package com.tourcool.ui.constellation

import android.os.Bundle
import com.alibaba.fastjson.JSONObject
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.tourcool.bean.constellation.DayConstellation
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_fortune_day.*

/**
 *@description : 财运（天）
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日16:43
 * @Email: 971613168@qq.com
 */
class FortuneDayFragment : BaseFragment() {
    private var mType: Int = 0
    private var starName = ""
    private var mTypeStr = ""
    override fun getContentLayout(): Int {
        return R.layout.fragment_fortune_day
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun loadData() {
        super.loadData()
        requestFortuneDay()
    }

    companion object {
        fun newInstance(type: Int, starName: String): FortuneDayFragment? {
            val args = Bundle()
            val fragment = FortuneDayFragment()
            fragment.arguments = args
            fragment.mType = type
            if (type == 0) {
                fragment.mTypeStr = "today"
            } else {
                fragment.mTypeStr = "tomorrow"
            }
            fragment.starName = starName
            return fragment
        }
    }

    private fun requestFortuneDay() {
        ApiRepository.getInstance().requestConstellation(starName, mTypeStr).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>>() {
            override fun onRequestNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showDayFortune(parseFortuneDay(entity.data))
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    private fun parseFortuneDay(data: Any?): DayConstellation? {
        return try {
            val json = JSONObject.toJSONString(data)
            JSONObject.parseObject(json, DayConstellation::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showDayFortune(data: DayConstellation?) {
        if (data == null) {
            ToastUtil.show("服务器数据异常")
            return
        }
        tvDateDay.text = data.datetime
        tvLuckyNum.text = data.number.toString()
        tvLuckyColor.text = data.color
        tvMatchStarName.text = data.qFriend
        tvTodayDesc.text = data.summary
        try {
            allPercentPb.progress = data.all.toInt()
            healthPb.progress = data.all.toInt()
            lovePb.progress = data.love.toInt()
            workPb.progress = data.work.toInt()
            moneyPb.progress = data.money.toInt()
            tvProgressAll.text = data.all
            tvProgressHealthy.text = data.health
            tvProgressLove.text = data.love
            tvProgressWork.text = data.work
            tvProgressMoney.text = data.money

        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        if (mType == 0) {
            tvTitle.text = "今日概述"
        } else {
            tvTitle.text = "明日概述"
        }
    }
}