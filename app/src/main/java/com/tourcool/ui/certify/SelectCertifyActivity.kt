package com.tourcool.ui.certify

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.core.module.activity.BaseTitleActivity
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import kotlinx.android.synthetic.main.activity_certify_identity.*
import kotlinx.android.synthetic.main.activity_real_name_certification_new.*

/**
 *@description :实名认证
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月02日10:01
 * @Email: 971613168@qq.com
 */
class SelectCertifyActivity : BaseCommonTitleActivity(), View.OnClickListener {
    companion object {
        const val SCAN_ID_CARD_REQUEST = 1
        const val SCAN_DRIVERCARD_REQUEST = 2
        const val SCAN_BANKCARD_REQUEST = 3
        const val KEY_CERTIFY_TYPE = "KEY_CERTIFY_TYPE"
        const val EXTRA_CERTIFY_ALI_PAY = "EXTRA_CERTIFY_ALI_PAY"
        const val EXTRA_CERTIFY_PHONE = "EXTRA_CERTIFY_PHONE"
        const val EXTRA_CERTIFY_BANK_CARD = "EXTRA_CERTIFY_BANK_CARD"
        const val EXTRA_CERTIFY_ALI_FACE = "EXTRA_CERTIFY_ALI_FACE"
        const val EXTRA_PHONE = "EXTRA_PHONE"
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_real_name_certification_new
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText("实名认证")
    }

    override fun initView(savedInstanceState: Bundle?) {
        llCertifyAli.setOnClickListener(this)
        llCertifyPhone.setOnClickListener(this)
        llCertifyBankcard.setOnClickListener(this)
        llCertifyAliFace.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llCertifyAli -> {
                //支付宝认证
                skipCertify(EXTRA_CERTIFY_ALI_PAY)
            }
            R.id.llCertifyPhone -> {
                //手机号认证
                skipCertify(EXTRA_CERTIFY_PHONE)
            }
            R.id.llCertifyBankcard -> {
                //银行卡认证
                skipCertify(EXTRA_CERTIFY_BANK_CARD)
            }
            R.id.llCertifyAliFace -> {
                //支付宝人脸认证
                skipCertify(EXTRA_CERTIFY_ALI_FACE)
            }
            else -> {

            }
        }
    }


    private fun skipCertify(type: String) {
        val intent = Intent()
        intent.putExtra(KEY_CERTIFY_TYPE, type)
        intent.setClass(mContext, CertifyActivity::class.java)
        startActivity(intent)
    }


}