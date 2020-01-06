package com.tourcool.ui.certify

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.module.activity.FrameTitleActivity
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.msd.ocr.idcard.LibraryInitOCR
import com.tourcool.core.util.TourCooUtil
import com.tourcool.smartcity.R
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.SCAN_ID_CARD_REQUEST
import com.tourcool.util.VibrateUtil
import kotlinx.android.synthetic.main.activity_certify_scan_id_card.*
import org.json.JSONObject

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月03日18:02
 * @Email: 971613168@qq.com
 */
class ScanIdCardActivity : FrameTitleActivity(), View.OnClickListener {
    private var isVibrate = false
    /**
     * 是否扫描正面
     */
    private var isZheng = true

    private val mTag = "ScanIdCardActivity"
    /**
     * 上一次正面的扫描结果
     */
    private var lastResultZheng: JSONObject? = null

    /**
     * 上一次反面的扫描结果
     */
    private var lastResultFan: JSONObject? = null

    override fun getContentLayout(): Int {
        return R.layout.activity_certify_scan_id_card
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        initTitleBar(titleBar!!)
    }

    override fun initView(savedInstanceState: Bundle?) {
        ivScanIdZheng.setOnClickListener(this)
        ivScanIdFan.setOnClickListener(this)
    }


    private fun initTitleBar(titleBar: TitleBarView) {
        titleBar.setBackgroundColor(TourCooUtil.getColor(R.color.transparent))
        setMarginTop(titleBar)
        val mainText = titleBar.mainTitleTextView
        mainText.text = ""
        titleBar.setTitleMainText("身份证扫描")
        titleBar.setRightText("完成").setRightTextColor(TourCooUtil.getColor(R.color.white))
        mainText.setTextColor(TourCooUtil.getColor(R.color.white))
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
                    handleScanCallbackSuccess(jo)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else -> {
            }
        }
    }


    private fun handleScanCallbackSuccess(result: JSONObject) {
        try {
            //正面回调
            val scanResultName = result.opt("name") as String
            //反面回调
            val scanResultIssue = result.opt("issue") as String
            if (isZheng) {
                if (TextUtils.isEmpty(scanResultName)) {
                    //说明用户点击的是正面 但扫描的是反面 直接拦截回调
                    ToastUtil.show("请扫描身份证正面")
                    return
                } else {
                    vibrate()
                    setTextValue(tvName, result.opt("name") as String?)
                    setTextValue(tvGender, result.opt("sex") as String?)
                    setTextValue(tvEthnic, result.opt("folk") as String?)
                    setTextValue(tvBirthday, result.opt("birt") as String?)
                    setTextValue(tvAddress, result.opt("addr") as String?)
                    setTextValue(tvIdNumber, result.opt("num") as String?)
                }
            } else {
                //用户点击的反面扫描
                if (TextUtils.isEmpty(scanResultIssue)) {
                    //说明用户点击的是反面 但扫描的是正面 直接拦截回调
                    ToastUtil.show("请扫描身份证反面")
                    return
                } else {
                    vibrate()
                    setTextValue(tvAuthority, result.opt("issue") as String?)
                    setTextValue(tvValidDate, result.opt("valid") as String?)
                }
            }

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


    private fun skipScanId() {
        LibraryInitOCR.initOCR(this)
        val bundle = Bundle()
        bundle.putBoolean("saveImage", true)
        bundle.putInt("requestCode", SCAN_ID_CARD_REQUEST)
        bundle.putBoolean("showSelect", false)
        bundle.putInt("type", 0)
        //0身份证, 1驾驶证
        LibraryInitOCR.startScan(this@ScanIdCardActivity, bundle)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivScanIdZheng -> {
                //扫描正面
                isZheng = true
                skipScanId()
            }
            R.id.ivScanIdFan -> {
                //扫描反面
                isZheng = false
                skipScanId()
            }
            else -> {
            }
        }
    }

}