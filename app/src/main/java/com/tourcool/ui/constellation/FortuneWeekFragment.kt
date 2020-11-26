package com.tourcool.ui.constellation

import android.os.Bundle
import com.alibaba.fastjson.JSONObject
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.bean.constellation.WeekConstellation
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_fortune_week.*

/**
 *@description : 本周财运
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日17:35
 * @Email: 971613168@qq.com
 */
class FortuneWeekFragment : BaseFragment() {
    private var starName = ""

    private var mTypeStr = "week"
    override fun getContentLayout(): Int {
        return R.layout.fragment_fortune_week
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun loadData() {
        super.loadData()
        requestFortuneWeek()
    }


    companion object {
        fun newInstance(starName: String): FortuneWeekFragment? {
            val args = Bundle()
            val fragment = FortuneWeekFragment()
            fragment.arguments = args
            fragment.starName = starName
            return fragment
        }
    }

    private fun showWeekFortune(data: WeekConstellation?) {
        if (data == null) {
            ToastUtil.show("服务器数据异常")
            return
        }
        tvDateWeek.text = StringUtil.getNotNullValueLine(data.date)
        tvWeekTh.text = StringUtil.getNotNullValueLine("第" + data.weekth.toString() + "周")
        tvWeekHealthy.text = StringUtil.getNotNullValueLine(data.health)
        tvWeekJob.text = StringUtil.getNotNullValueLine(data.job)
        tvWeekLove.text = StringUtil.getNotNullValueLine(data.love)
        tvWeekMoney.text = StringUtil.getNotNullValueLine(data.money)

        tvWeekWork.text = StringUtil.getNotNullValueLine(data.work)
    }

    private fun requestFortuneWeek() {
        ApiRepository.getInstance().requestConstellation(starName, mTypeStr).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>>() {
            override fun onRequestNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showWeekFortune(parseFortuneWeek(entity.data))
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    private fun parseFortuneWeek(data: Any?): WeekConstellation? {
        return try {
            val json = JSONObject.toJSONString(data)
            JSONObject.parseObject(json, WeekConstellation::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}