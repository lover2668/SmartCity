package com.tourcool.ui.certify

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.msd.ocr.idcard.LibraryInitOCR
import com.tourcool.core.module.activity.BaseTitleActivity
import com.tourcool.smartcity.R
import kotlinx.android.synthetic.main.activity_certify_identity.*
import org.json.JSONException
import org.json.JSONObject

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月02日11:32
 * @Email: 971613168@qq.com
 */
class CertifyBandCardActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "CertifyBandCardActivity"
    override fun getContentLayout(): Int {
        return R.layout.activity_certify_identity
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("银联卡认证")
    }

    override fun initView(savedInstanceState: Bundle?) {
        llSkipScanIdentify.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llSkipScanIdentify -> {
                skipIdentify()
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
        LibraryInitOCR.startScan(this@CertifyBandCardActivity, bundle)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        TourCooLogUtil.i(mTag, "requestCode="+requestCode+"resultCode = "+resultCode)
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
                    TourCooLogUtil.i(mTag, sb.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            else -> {
            }
        }
    }
}