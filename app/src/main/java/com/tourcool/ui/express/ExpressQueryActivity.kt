package com.tourcool.ui.express

import android.os.Bundle
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.express.ExpressCompany
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_express_query.*

/**
 *@description : 快递查询
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日19:02
 * @Email: 971613168@qq.com
 */
class ExpressQueryActivity : BaseCommonTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_express_query
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvQuery.setOnClickListener {
            FrameUtil.startActivity(mContext, ExpressDetailActivity::class.java)
        }
        tvExpressCompany.setOnClickListener {
            requestExpressCompany()
        }
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("快递查询")
    }

    private fun requestExpressCompany() {
        ApiRepository.getInstance().requestExpressCompany().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<ExpressCompany>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<ExpressCompany>>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showExpressCompany(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    private fun showExpressCompany(list: MutableList<ExpressCompany>?) {
        ToastUtil.show("热词数量：" + list!!.size)
    }

    companion object{
        const val EXTRA_EXPRESS_COM = "EXTRA_EXPRESS_COM"
        const val EXTRA_EXPRESS_NO = "EXTRA_EXPRESS_NO"
    }
}