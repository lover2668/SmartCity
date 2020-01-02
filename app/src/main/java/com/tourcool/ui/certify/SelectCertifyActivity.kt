package com.tourcool.ui.certify

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.msd.ocr.idcard.LibraryInitOCR
import com.tourcool.core.module.activity.BaseTitleActivity
import com.tourcool.smartcity.R
import com.tourcool.ui.orc.OrcSelectActivity
import kotlinx.android.synthetic.main.activity_real_name_certification_new.*

/**
 *@description :实名认证
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月02日10:01
 * @Email: 971613168@qq.com
 */
class SelectCertifyActivity : BaseTitleActivity(), View.OnClickListener {
    companion object {
        const val SCAN_ID_CARD_REQUEST = 1
        const val SCAN_DRIVERCARD_REQUEST = 2
        const val SCAN_BANKCARD_REQUEST = 3
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_real_name_certification_new
    }

    override fun setTitleBar(titleBar: TitleBarView?) {

    }

    override fun initView(savedInstanceState: Bundle?) {
        llCertifyAli.setOnClickListener(this)
        llCertifyPhone.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llCertifyAli -> {
                skipIdentify()
            }
            R.id.llCertifyPhone -> {
                skipTest()
            }
            else -> {
            }
        }
    }


    private fun skipIdentify() {
       skipTest()
    }

    private fun skipTest() {
        var rechargeIntent = Intent()
        rechargeIntent.setClass(mContext, CertifyBandCardActivity::class.java)
        startActivity(rechargeIntent)
    }


}