package com.tourcool.ui.garbage

import android.os.Bundle
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.garbage.Garbage
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.tourcool.ui.garbage.GarbageQueryActivity.Companion.EXTRA_GARBAGE_NAME
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_garbage_detail.*

/**
 *@description : 垃圾查询详情
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日20:28
 * @Email: 971613168@qq.com
 */
class GarbageDetailActivity : BaseTitleTransparentActivity() {
    private var garbageName: String? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_garbage_detail
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("垃圾介绍")
    }

    override fun initView(savedInstanceState: Bundle?) {
        garbageName = intent.getStringExtra(EXTRA_GARBAGE_NAME)
        garbageName = StringUtil.getNotNullValue(garbageName)
        tvClose.setOnClickListener {
            finish()
        }
        requestSearchGarbage()
    }

    private fun requestSearchGarbage() {
        ApiRepository.getInstance().requestSearchGarbage(garbageName).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Garbage>>() {
            override fun onRequestNext(entity: BaseResult<Garbage>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showGarbageInfo(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }

        })
    }

    private fun showGarbageInfo(garbage: Garbage?) {
        if (garbage == null) {
            return
        }
        tvGarbageName.text = StringUtil.getNotNullValue(garbage.name)
        tvGarbageType.text = StringUtil.getNotNullValue(garbage.category)
        tvGarbageGuide.text = StringUtil.getNotNullValue(garbage.category + "投放指导")
        val desc = StringUtil.getNotNullValue(garbage.require) + "\n" + StringUtil.getNotNullValue(garbage.explain) + "\n" + StringUtil.getNotNullValue(garbage.common)
        tvGarbageDesc.text = desc
    }
}