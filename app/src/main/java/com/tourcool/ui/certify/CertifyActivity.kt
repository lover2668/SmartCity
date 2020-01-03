package com.tourcool.ui.certify

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.msd.ocr.idcard.LibraryInitOCR
import com.tourcool.core.module.activity.BaseTitleActivity
import com.tourcool.smartcity.R
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_ALI_FACE
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_ALI_PAY
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_BANK_CARD
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_PHONE
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_PHONE
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.KEY_CERTIFY_TYPE
import com.tourcool.util.VibrateUtil
import kotlinx.android.synthetic.main.activity_certify_identity.*
import org.json.JSONObject

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月02日11:32
 * @Email: 971613168@qq.com
 */
class CertifyActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "CertifyBandCardActivity"
    private var isVibrate = false
    private var certifyType = ""
    override fun getContentLayout(): Int {
        return R.layout.activity_certify_identity
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        certifyType = intent.getStringExtra(KEY_CERTIFY_TYPE)
        showTitleByCertifyType()
    }

    override fun initView(savedInstanceState: Bundle?) {
        llSkipScanIdentify.setOnClickListener(this)
        tvNextStep.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llSkipScanIdentify -> {
                skipIdentify()
            }
            R.id.tvNextStep -> {
                doNext()
            }
            else -> {
            }
        }
    }


    private fun skipIdentify() {
        LibraryInitOCR.initOCR(this)
        val bundle = Bundle()
        bundle.putBoolean("saveImage", true)
        bundle.putInt("requestCode", SelectCertifyActivity.SCAN_ID_CARD_REQUEST)
        bundle.putInt("type", 0)
        //0身份证, 1驾驶证
        LibraryInitOCR.startScan(this@CertifyActivity, bundle)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        TourCooLogUtil.i(mTag, "requestCode=" + requestCode + "resultCode = " + resultCode)
        when (resultCode) {
            -1 -> {
                val result = data!!.getStringExtra("OCRResult")
                try {
                    val jo = JSONObject(result)
                    val sb = StringBuffer()
                    sb.append(String.format("正面 = %s\n", jo.opt("type")))
                    sb.append(String.format("姓名 = %s\n", jo.opt("name")))
                    sb.append(String.format("性别 = %s\n", jo.opt("sex")))
                    sb.append(String.format("民族 = %s\n", jo.opt("folk")))
                    sb.append(String.format("日期 = %s\n", jo.opt("birt")))
                    sb.append(String.format("号码 = %s\n", jo.opt("num")))
                    sb.append(String.format("住址 = %s\n", jo.opt("addr")))
                    sb.append(String.format("签发机关 = %s\n", jo.opt("issue")))
                    sb.append(String.format("有效期限 = %s\n", jo.opt("valid")))
                    sb.append(String.format("整体照片 = %s\n", jo.opt("imgPath")))
                    sb.append(String.format("头像路径 = %s\n", jo.opt("headPath")))
                    TourCooLogUtil.d(mTag, jo)
                    TourCooLogUtil.i(mTag, sb.toString())
                    showScanCallbackSuccess(jo)
                    vibrate()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else -> {
            }
        }
    }


    private fun showScanCallbackSuccess(result: JSONObject) {
        try {
            setTextValue(etName, result.opt("name") as String?)
            setTextValue(etIdCardNumber, result.opt("num") as String?)
        } catch (e: Exception) {
            e.printStackTrace()
            TourCooLogUtil.e(mTag, "showScanCallbackSuccess()异常--->" + e.message)
        }
    }

    private fun vibrate() {
        // 开启震动
        isVibrate = true
        VibrateUtil.vibrate(mContext, 200L)
    }


    private fun doNext() {
        if (!cBoxAgree.isChecked) {
            ToastUtil.show("请先同意协议")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etName))) {
            ToastUtil.show("请输入姓名")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etIdCardNumber))) {
            ToastUtil.show("请输入身份证号")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etPhoneNumber))) {
            ToastUtil.show("请输入手机号")
            return
        }
        if (!StringUtil.isPhoneNumber(getTextValue(etPhoneNumber))) {
            ToastUtil.show("请输入正确的手机号")
            return
        }
        skipByType()
    }


    private fun showTitleByCertifyType() {
        when (certifyType) {
            EXTRA_CERTIFY_ALI_PAY -> {
                mTitleBar!!.setTitleMainText("支付宝实名认证")
            }
            EXTRA_CERTIFY_PHONE -> {
                mTitleBar!!.setTitleMainText("手机实名认证")
            }
            EXTRA_CERTIFY_BANK_CARD -> {
                mTitleBar!!.setTitleMainText("银联卡认证")
            }
            EXTRA_CERTIFY_ALI_FACE -> {
                mTitleBar!!.setTitleMainText("人脸识别认证")
            }
            else -> {
            }
        }
    }


    private fun skipByType() {
        val intent = Intent()
        intent.putExtra(KEY_CERTIFY_TYPE, certifyType)
        intent.putExtra(EXTRA_PHONE, getTextValue(etPhoneNumber))
        intent.setClass(mContext, ScanIdCardActivity::class.java)

        startActivity(intent)

    }
}