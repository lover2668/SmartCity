package com.tourcool.ui.certify

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.frame.library.core.manager.RxJavaManager
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.constant.TimeConstant
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.util.TourCooUtil
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_PHONE
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.KEY_CERTIFY_TYPE
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_certify_sms_validation.*

/**
 *@description :短信验证
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月03日11:01
 * @Email: 971613168@qq.com
 */
class CertifyMessageActivity : BaseCommonTitleActivity(), View.OnClickListener {
    private val disposableList: MutableList<Disposable> = ArrayList()
    private var timeCount = TimeConstant.COUNT
    private var phone = ""
    override fun getContentLayout(): Int {
        return R.layout.activity_certify_sms_validation
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText("短信验证")
    }

    override fun initView(savedInstanceState: Bundle?) {
        intent.getStringExtra(KEY_CERTIFY_TYPE)
        phone = intent.getStringExtra(EXTRA_PHONE)
        setTextValue(tvBindPhone, phone)
        tvNextStep.setOnClickListener(this)
        tvSendSms.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvNextStep -> {
                skip()
            }
            R.id.tvSendSms -> {
                sendVCodeAndCountDownTime(getTextValue(tvBindPhone))
            }
            else -> {
            }
        }
    }

    /**
     * 倒计时
     */
    private fun countDownTime() {
        reset()
        setClickEnable(tvSendSms, false)
        mHandler.postDelayed({
            tvSendSms.background = FrameUtil.getDrawable(R.drawable.bg_radius_25_gray_hollow)
            tvSendSms.setTextColor(FrameUtil.getColor(R.color.grayA2A2A2))
        }, TimeConstant.ONE_SECOND.toLong())
        RxJavaManager.getInstance().doEventByInterval(TimeConstant.ONE_SECOND.toLong(), object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                disposableList.add(d)
            }

            override fun onNext(aLong: Long) {
                --timeCount
                setTextValue(tvSendSms, "还有" + timeCount + "秒")
                if (aLong >= TimeConstant.COUNT - 1) {
                    onComplete()
                }
            }

            override fun onError(e: Throwable) {
                cancelTime()
            }

            override fun onComplete() {
                reset()
                cancelTime()
            }
        })
    }


    private fun reset() {
        setClickEnable(tvSendSms, true)
        timeCount = TimeConstant.COUNT
        setText(tvSendSms, "发送验证码")
        tvSendSms.setTextColor(FrameUtil.getColor(R.color.blue55A9FF))
    }

    private fun cancelTime() {
            var disposable: Disposable
            for (i in disposableList.indices) {
                disposable = disposableList[i]
                if (!disposable.isDisposed) {
                    disposable.dispose()
                    disposableList.remove(disposable)
                }
            }
    }


    /**
     * 验证码发送接口并倒计时
     *
     * @param phone
     */
    private fun sendVCodeAndCountDownTime(phone: String) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号")
            return
        }
        if (!TourCooUtil.isMobileNumber(phone)) {
            ToastUtil.show("请输入正确的手机号")
            return
        }
        ApiRepository.getInstance().getVcode(phone).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onRequestNext(entity: BaseResult<*>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess(RequestConfig.MSG_SEND_SUCCESS)
                    //验证码发送成功开始，倒计时
                    countDownTime()
                } else {
                    ToastUtil.showFailed(entity.errorMsg)
                }
            }
        })
    }

    private fun skip() {
        val intent = Intent()
//        intent.putExtra(KEY_CERTIFY_TYPE, type)
        intent.setClass(mContext, CertifySettingPassActivity::class.java)
        startActivity(intent)
    }

    override fun isStatusBarDarkMode(): Boolean {
        return true
    }
}