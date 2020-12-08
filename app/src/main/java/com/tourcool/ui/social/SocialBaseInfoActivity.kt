package com.tourcool.ui.social

import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.NetworkUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.social.SocialBaseInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_society_info.*

class SocialBaseInfoActivity : BaseCommonTitleActivity() {

    override fun getContentLayout(): Int {
        return R.layout.activity_society_info
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("用户基本信息")
    }

    override fun loadData() {
        super.loadData()
        requestSocialBaseInfo()
    }

    private fun requestSocialBaseInfo() {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtil.show("请检查网络连接")
            return
        }
        ApiRepository.getInstance().requestSocialBaseInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<SocialBaseInfo?>>() {
            override fun onRequestNext(entity: BaseResult<SocialBaseInfo?>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    if (entity.data == null) {
                        return
                    }
                    showSocialBaseInfo(entity.data!!)
                } else {
                    ToastUtil.showFailed(entity.errorMsg)
                }
            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                ToastUtil.show(e!!.message)
            }
        })
    }

    private fun showSocialBaseInfo(info: SocialBaseInfo) {
        tvAccount.text =StringUtil.getNotNullValueLine(info.sbzh)
        tvCbDw.text = StringUtil.getNotNullValueLine(info.company)
        tvRecentPayMonth.text = StringUtil.getNotNullValueLine(info.jfny)
        showMoney(tvInsuranceMedical,info.ylbxye)
        showMoney(tvInsuranceInjury,info.gsjnje)
        showMoney(tvInsuranceBirth,info.yyjnje)
        showMoney(tvInsuranceLossJob,info.syjnje)
    }

    private fun showMoney(textView: TextView?, money: String?) {
        if (TextUtils.isEmpty(money)) {
            textView?.text = StringUtil.getNotNullValueLine(money)
        } else {
            val result = money + "元"
            textView?.text = result
        }
    }
}