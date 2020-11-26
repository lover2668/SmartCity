package com.tourcool.ui.constellation

import android.os.Bundle
import com.alibaba.fastjson.JSONObject
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.bean.constellation.YearConstellation
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_fortune_year.*

/**
 *@description : 年度运势
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日18:12
 * @Email: 971613168@qq.com
 */
class FortuneYearFragment : BaseFragment() {
    private var mTypeStr = "year"
    private var starName = ""

    override fun getContentLayout(): Int {
        return R.layout.fragment_fortune_year
    }

    override fun initView(savedInstanceState: Bundle?) {
    }


    companion object {
        fun newInstance(starName: String): FortuneYearFragment? {
            val args = Bundle()
            val fragment = FortuneYearFragment()
            fragment.arguments = args
            fragment.starName = starName
            return fragment
        }
    }

    override fun loadData() {
        super.loadData()
        requestFortuneYear()
    }

    private fun requestFortuneYear() {
        ApiRepository.getInstance().requestConstellation(starName, mTypeStr).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>>() {
            override fun onRequestNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showFortuneYear(parseFortuneYear(entity.data))
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    private fun parseFortuneYear(data: Any?): YearConstellation? {
        return try {
            val json = JSONObject.toJSONString(data)
            JSONObject.parseObject(json, YearConstellation::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showFortuneYear(data: YearConstellation?) {
        if (data == null) {
            ToastUtil.show("服务器数据异常")
            return
        }
        tvYearTh.text = StringUtil.getNotNullValue(data.date)
        val stringBufferPass = StringBuffer("")
        stringBufferPass.append(data.mima.info)
        stringBufferPass.append("\n")
        data.mima.text.forEach {
            stringBufferPass.append(it)
        }
        tvYearPass.text = StringUtil.getNotNullValue(stringBufferPass.toString())
        val stringBufferWork = StringBuffer("")
        data.career.forEach {
            stringBufferWork.append(it)
        }
        tvYearWork.text = StringUtil.getNotNullValue(stringBufferWork.toString())


        val stringBufferLove = StringBuffer("")
        data.love.forEach {
            stringBufferLove.append(it)
        }
        tvYearLove.text = StringUtil.getNotNullValue(stringBufferLove.toString())
    }

}