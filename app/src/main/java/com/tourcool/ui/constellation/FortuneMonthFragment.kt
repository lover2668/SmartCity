package com.tourcool.ui.constellation

import android.os.Bundle
import com.alibaba.fastjson.JSONObject
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.bean.constellation.MonthConstellation
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_fortune_month.*

/**
 *@description : 本月运势
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日18:09
 * @Email: 971613168@qq.com
 */
class FortuneMonthFragment : BaseFragment() {
    private var mTypeStr = "month"
    private var starName = ""
    override fun getContentLayout(): Int {
        return R.layout.fragment_fortune_month
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    companion object {
        fun newInstance(starName: String): FortuneMonthFragment? {
            val args = Bundle()
            val fragment = FortuneMonthFragment()
            fragment.arguments = args
            fragment.starName = starName
            return fragment
        }
    }

    override fun loadData() {
        super.loadData()
        requestFortuneMonth()
    }

    private fun requestFortuneMonth() {
        ApiRepository.getInstance().requestConstellation(starName, mTypeStr).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>>() {
            override fun onRequestNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showFortuneMonth(parseFortuneMonth(entity.data))
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    private fun parseFortuneMonth(data: Any?): MonthConstellation? {
        return try {
            val json = JSONObject.toJSONString(data)
            JSONObject.parseObject(json, MonthConstellation::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showFortuneMonth(data: MonthConstellation?) {
        if (data == null) {
            ToastUtil.show("服务器数据异常")
            return
        }
        tvMonthDate.text = data.date
        tvMonthAll.text = StringUtil.getNotNullValue(data.all)
        tvMonthHealth.text = StringUtil.getNotNullValue(data.health)
        tvMonthLove.text = StringUtil.getNotNullValue(data.love)
        tvMonthMoney.text = StringUtil.getNotNullValue(data.money)
        tvMonthWork.text = StringUtil.getNotNullValue(data.work)


    }
}