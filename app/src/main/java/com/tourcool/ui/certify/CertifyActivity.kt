package com.tourcool.ui.certify

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.alibaba.fastjson.JSON
import com.alipay.mobile.android.verify.sdk.ServiceFactory
import com.alipay.sdk.app.AuthTask
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.OrderInfoUtil2_0
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.PayResult
import com.tourcool.bean.account.AccountHelper
import com.tourcool.bean.ali.AuthResult
import com.tourcool.bean.certify.FaceCertify
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_ALI_FACE
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_ALI_PAY
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_BANK_CARD
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_ID_CARD
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_CERTIFY_PHONE
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.EXTRA_PHONE
import com.tourcool.ui.certify.SelectCertifyActivity.Companion.KEY_CERTIFY_TYPE
import com.tourcool.ui.kitchen.ProtocolActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_certify_identity.*
import org.json.JSONObject

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月02日11:32
 * @Email: 971613168@qq.com
 */
class CertifyActivity : BaseCommonTitleActivity(), View.OnClickListener {
    companion object {
        const val REQUEST_CODE_SCAN_ID = 1002
    }

    private var idCard = ""
    private var name = ""
    private var phone = ""
    private val SDK_PAY_FLAG = 1
    private val SDK_AUTH_FLAG = 2
    private val RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCVZmJoLPFmTQr6b0NmhQ51fs8VqVn2vWGiRlZ7AW4c+adlaY6j8whIXvoqnwkIYdTUJjZqXJzARx5dvXpXFon+fULgP/ZvEiu3mDleJ+63820Roi/2Nf32k9FKTBS96AUD64U+rnDOESsUkBDZS0XUe0N96CE42pgp2LP2XEQ2cQjwKG4eu5qvXlqwe5Tqh+b7N4g3gZES0b3/EHhLIYCzueLoOqO1uFUJFMaLPWKcY4gHIgvlXrf3/IgB2QRWxCMh8Hnj62LXjTKY4F5k6pT0NtZb8C8GPjo2wQhjBNrjLNOpZFMp+Q+aus1Sg9/hZT+GOIDyEAupBMoRUQD7QVfvAgMBAAECggEAG9mnNJZUNebcygyduunI5TxLbFVSkP2CytZj3rBIj5w2iWAhGA0BGUSjS/iznV1naFjrQe6bxfg7/+uHd96awNcm9VjXHqN7hNEauKOnC6GUTno2iKZN/n5VwIzoPPKYpL9t6l5oZvGqXz3v9iHjFZYY4cq5DrkpLnvYKG/Qw3kLPM2rT1Zr40JrK4/9NXsZJbN/omNntfBOMjsRuBidpuOq4bPHKRsGxYZE0834ushciHROVMlDG1OEt/xfKNK1ZnR1b1HhNUqItI7m+bOAyeYdulUIJZ8ZXpXi10PXfvp9QuJWhGh7yq6A/VhOu4H7k58gHpfnvjgWCzcwwZhD2QKBgQDgqEtJK9z9PNird33nwi5GvhxNJdVSQKzzlx+usI5gS4+o+3K7uDpjB7occt2fFHao7VaiZDNx5vzIQUPB2gKL94yUuvTCF+KRcUkHP3Xo4f7a99tocLIlHb6GE8w19L0pwf6MtgoQUSm93ux6re87J0Nt1nogz0oFtM0hwFz7WwKBgQCqPj9rCoMEwl9/HJgoNoeg7zSOvgr/yGZKkqk6sPfL/fC7b7YOUXkrmO//xrgN8fk/ubN3Ok9L69rOM0O+eHE/IfgxGnowiMV44Nt5FG5FUsKfyjJv5y4vlzZ868c+d9WkzdzPDKuVK7iry6gSyoxaStONkjqcqsDb+H6tBxz9/QKBgQDdV7LlYtwWfS6UseRQxVbGyGQl9pKYubQtQ8YpXTbJ1WizexptrXOJQoGxqdKE+6p1gXGMCeFN5eFEhFj3044SzVGq8BBacyH9XnovM+0+B/wLTYzU98PtIcMnYrYeqwT88W+Uv35m2TChK9QeguRjiam7vEcTsGgj+yDokhiKZQKBgFqzN05sF0md1Qr/zQD/rFrNlo7GKU4FEpqAcLDYP+zqqiryZWhTd98GaDc5RC4J0OVmpnrLEhw82CIrpdgAizU7f+OJW7gn5i10fvmPLQC6Cv7e1uhPnoe0ZE9BvrFFXwmitBWLho6+8HB23GDGkOg6HWO7mIaHYqDPT44X0BZxAoGATLa71moAWigwpRUf3tih/Rvk2E2Nt/J4zdL/A90BDk8d4+yxPACaN97/5SEHpKi9AeKvwtsf/ZYOXwMrd/tXn/xi9RpvdaF1ZH4l8tWFPdDT5woyiGrp24xM22EDlta4UPujyADBrIKDDuoX6zsNZi7Uk7PuA2fxvf31a8xiFyM="
    private val RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIf0Y3UEYW4LxzwBh/C2MUFy8BtMYrbPXu64LsoP7nF02XsD5QRd4hH4UPoZ54mtEIVLawUOzdq1vEvRuo0cCwS0qxXzey07mzHd4Hrdo9sQhwogjRTm5EMkMDyhNyAeesrh5rXkefTgN9HrUzXm4xn8/kguJh4b7D+Soy8oAaN9AgMBAAECgYBBhjXRFhVnFmIlt48vIoBGzQCQB5akCPn2D+rtFBSVtf2DXnMoUZ89jfJpN8byAekudRTVUTdBq8leuWwxykw8eopzODp6YOOn9viEbIbmAmCUNqZojpw9l5e2q8LG+J9R5GN+7I+anw1T4grE7xV7Y2SEzlemM2OHNSfA+atweQJBALqcfWhMfZDF9YG+QnTV3SYT8Sm8Ehx7iHN91uxnVWTgPNDCeql+qvfPo/64L8p7XwNETZNO8LCNrgrBS5fG2f8CQQC6geXC4aLypEaI1g5JwJkpgChixKITQ+pU+MLy5jyFpjSfxpfqxYs+bqwCLOdD2zRc6hqcr0wQReR4aRfUA+qDAkBPHB5SA/NLV5Cfow+7RhrNlLCtF6y55GjRRSzC5X+fFyzQ91FW33sNOTp1tpGUDj8WPHOCmnmQ7080AgPQ4RzzAkEAocKSDYF3u61BQ+MsB0mVTcpKvMdArybjcglDxDpuWZh64ezzy/rDt0VzyHs7pb3UX5XnuOfNN/v2mXzE3uz6kwJBAIzvFtmjp1Q4Vq4qBHHobPOyEpx+4Dn3ZzLOkjEC9LoN6MnK9qgyLykXQixbRmr9RvQ5RBVq4C6MvHBmVu5eHtc="
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    val APPID = "2021001106643450"

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    val PID = "2088702233946790"

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    var TARGET_ID = System.currentTimeMillis().toString() + ""

    private val mTag = "CertifyActivity"
    private var isVibrate = false
    private var certifyType = ""
    override fun getContentLayout(): Int {
        return R.layout.activity_certify_identity
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        certifyType = intent.getStringExtra(KEY_CERTIFY_TYPE)
        showInfoByCertifyType()
    }

    override fun initView(savedInstanceState: Bundle?) {
        llSkipScanIdentify.setOnClickListener(this)
        tvNextStep.setOnClickListener(this)
        tvServiceAgreement.setOnClickListener(this)
        if (!AccountHelper.getInstance().isLogin) {
            ToastUtil.show("请先登录")
            finish()
            return
        }
        setTextValue(tvPhoneNumber, StringUtil.getNotNullValue(AccountHelper.getInstance().userInfo.phoneNumber))
        tvDesc.text = "1.点击\"下一步\"后，请按页面提示进行操作。\n2.您所录入的相关信息将只用于核实身份真实性，不会提供给第三方。"
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llSkipScanIdentify -> {
                skipIdentify()
            }
            R.id.tvNextStep -> {
                doNext()
            }
            R.id.tvServiceAgreement -> {
                skipServiceAgreement()
            }
            else -> {
            }
        }
    }


    private fun skipIdentify() {
//        LibraryInitOCR.initOCR(this)
        /*  val bundle = Bundle()
          bundle.putBoolean("saveImage", true)
          bundle.putInt("requestCode", SelectCertifyActivity.SCAN_ID_CARD_REQUEST)
          bundle.putBoolean("showSelect", false)
          bundle.putInt("type", 0)
          //0身份证, 1驾驶证
          LibraryInitOCR.startScan(this@CertifyActivity, bundle)*/
        val intent = Intent()
        intent.putExtra(KEY_CERTIFY_TYPE, certifyType)
        intent.setClass(mContext, ScanIdCardActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SCAN_ID)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        TourCooLogUtil.i(mTag, "requestCode=" + requestCode + "resultCode = " + resultCode)
        when (resultCode) {
            -1 -> {
                /* val result = data!!.getStringExtra("OCRResult")
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
                 }*/
                val string = data?.getStringExtra("result_scan_callback")
                showScanCallbackSuccess(string)
            }

            else -> {
            }
        }
    }


    private fun showScanCallbackSuccess(resultJson: String?) {
        try {
            val result = JSONObject(resultJson)
            setTextValue(etName, result.opt("name") as String?)
            setTextValue(etIdCardNumber, result.opt("num") as String?)
        } catch (e: Exception) {
            e.printStackTrace()
            TourCooLogUtil.e(mTag, "showScanCallbackSuccess()异常--->" + e.message)
        }
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
        if (!StringUtil.isIdCard(getTextValue(etIdCardNumber))) {
            ToastUtil.show("请输入正确的身份证号")
            return
        }
        if (TextUtils.isEmpty(getTextValue(tvPhoneNumber))) {
            ToastUtil.show("请输入手机号")
            return
        }
        if (!StringUtil.isPhoneNumber(getTextValue(tvPhoneNumber))) {
            ToastUtil.show("请输入正确的手机号")
            return
        }
        skipByType()
    }


    private fun showInfoByCertifyType() {
        when (certifyType) {
            EXTRA_CERTIFY_ALI_PAY -> {
                mTitleBar!!.setTitleMainText("支付宝实名认证")
                tvProgressOne.text = "身份认证"
                setTextValue(tvNextStep, "认证")
            }
            EXTRA_CERTIFY_PHONE -> {
                mTitleBar!!.setTitleMainText("手机实名认证")
                tvProgressOne.text = "身份认证"
                setTextValue(tvNextStep, "认证")

            }
            EXTRA_CERTIFY_BANK_CARD -> {
                mTitleBar!!.setTitleMainText("银联卡认证")
                tvProgressOne.text = "身份认证"
                setTextValue(tvNextStep, "认证")
            }
            EXTRA_CERTIFY_ALI_FACE -> {
                mTitleBar!!.setTitleMainText("人脸识别认证")
                tvProgressOne.text = "身份认证"
                setTextValue(tvNextStep, "下一步")
            }
            EXTRA_CERTIFY_ID_CARD -> {
                mTitleBar!!.setTitleMainText("身份证认证")
                tvProgressOne.text = "身份认证"
                setTextValue(tvNextStep, "认证")
            }
            else -> {
            }
        }
    }


    private fun skipByType() {
        /*  val intent = Intent()
          intent.putExtra(KEY_CERTIFY_TYPE, certifyType)
          intent.putExtra(EXTRA_PHONE, getTextValue(etPhoneNumber))
          intent.setClass(mContext, CertifyMessageActivity::class.java)
          startActivity(intent)*/
        when (certifyType) {
            EXTRA_CERTIFY_ALI_PAY -> {
                //阿里授权登录认证
                authAliLogin()
            }
            EXTRA_CERTIFY_ID_CARD -> {
                //身份证认证
                authIdCard()
            }
            EXTRA_CERTIFY_ALI_FACE -> {
                //人脸识别认证
                authFace()
            }
            else -> {
            }
        }
    }


    @SuppressLint("HandlerLeak")
    private val mHandler1: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String?, String?>)
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo: String = payResult.getResult() // 同步返回需要验证的信息
                    val resultStatus: String = payResult.getResultStatus()
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) { // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.showSuccess("支付成功")
//                      showAlert(this@PayDemoActivity, getString(R.string.pay_success) + payResult)
                    } else { // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.showFailed("支付失败")
                    }
                }
                SDK_AUTH_FLAG -> {
                    val authResult = AuthResult(msg.obj as Map<String?, String?>, true)
                    val resultStatus: String = authResult.resultStatus
                    // 判断resultStatus 为“9000”且result_code
// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) { // 获取alipay_open_id，调支付时作为参数extern_token 的value
// 传入，则支付账户为该授权账户
                        requestAliAuthentication()
                        TourCooLogUtil.i("授权返回结果", authResult)
                    } else { // 其他状态值则为授权失败
                        ToastUtil.showFailed("授权失败")
                    }
                }
                else -> {
                }
            }
        }
    }

    /**
     * 支付宝认证
     */
    private fun requestAliAuthentication() {
        ApiRepository.getInstance().requestAliAuthentication().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onRequestNext(entity: BaseResult<*>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
//                    skipMessageCertify()
                    ToastUtil.show("认证成功")
                    //支付宝认证回调
                    setResultCallback(2)
                    finish()
                } else {
                    ToastUtil.show(entity.errorMsg)
                }

            }
        })
    }


    /**
     * 身份证认证
     */
    private fun requestAuthenticationIdCard(idCard: String, name: String) {
        ApiRepository.getInstance().requestAuthenticationIdCard(idCard, name).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onRequestNext(entity: BaseResult<*>) {
                TourCooLogUtil.i(mTag, entity)
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
//                    skipMessageCertify()
                    ToastUtil.show("认证成功")
//                   身份证认证回调
                    setResultCallback(3)
                    finish()
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    /**
     * 支付宝账户授权业务示例
     */
    private fun authAliLogin() {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(TARGET_ID)) {
            ToastUtil.show("信息不完整")
            return
        }
        /*
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
		 *
		 * authInfo 的获取必须来自服务端；
		 */
        val rsa2: Boolean = RSA2_PRIVATE.isNotEmpty()
        val authInfoMap: Map<String, String> = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2)
        val info: String = OrderInfoUtil2_0.buildOrderParam(authInfoMap)
        val privateKey: String = if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
        val sign: String = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2)
        val authInfo = "$info&$sign"
        val authRunnable = Runnable {
            // 构造AuthTask 对象
            val authTask = AuthTask(this@CertifyActivity)
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(authInfo, true)
            val msg = Message()
            msg.what = SDK_AUTH_FLAG
            msg.obj = result
            mHandler1.sendMessage(msg)
        }
        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()
    }


    private fun skipMessageCertify() {
        val intent = Intent()
        intent.putExtra(KEY_CERTIFY_TYPE, certifyType)
        intent.putExtra(EXTRA_PHONE, getTextValue(tvPhoneNumber))
        intent.setClass(mContext, CertifyMessageActivity::class.java)
        startActivity(intent)
    }


    private fun authIdCard() {
        idCard = getTextValue(etIdCardNumber)
        name = getTextValue(etName)
        phone = getTextValue(tvPhoneNumber)
        if (idCard.isNullOrEmpty()) {
            ToastUtil.show("请输入身份号")
            return
        }
        if (name.isNullOrEmpty()) {
            ToastUtil.show("请输入用户姓名")
            return
        }
        if (phone.isNullOrEmpty()) {
            ToastUtil.show("请输入手机号码")
            return
        }
        requestAuthenticationIdCard(idCard, name)
    }


    private fun authFace() {
        idCard = getTextValue(etIdCardNumber)
        name = getTextValue(etName)
        phone = getTextValue(tvPhoneNumber)
        if (idCard.isNullOrEmpty()) {
            ToastUtil.show("请输入身份号")
            return
        }
        if (name.isNullOrEmpty()) {
            ToastUtil.show("请输入用户姓名")
            return
        }
        if (phone.isNullOrEmpty()) {
            ToastUtil.show("请输入手机号码")
            return
        }
        ApiRepository.getInstance().requestAuthenticationFace(idCard, name).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<FaceCertify>>() {
            override fun onRequestNext(entity: BaseResult<FaceCertify>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    faceCertifyCallback(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                   /* if (StringUtil.getNotNullValue(entity.errorMsg).contains("人脸识别认证服务调用失败")) {
                        ToastUtil.show("请输入正确的身份证号")
                    } else {
                        ToastUtil.show(entity.errorMsg)
                    }*/
                }
            }
        })
    }


    private fun faceCertifyCallback(faceCertify: FaceCertify) {
        // 封装认证数据
        // 封装认证数据
        val requestInfo = com.alibaba.fastjson.JSONObject()
        requestInfo["url"] = faceCertify.authenticationUrl
        requestInfo["certifyId"] = faceCertify.certifyId
        // 发起认证
        ServiceFactory.build().startService(this@CertifyActivity, requestInfo) { response ->
            // 回调处理
            Log.i("人脸认证回调", JSON.toJSONString(response))
            when (response["resultStatus"]) {
                "6001" -> {
//                    ToastUtil.show("人脸与信息不匹配")
                }
                "9000" -> {
                    //人脸认证回调
                    setResultCallback(4)
                    ToastUtil.show("人脸识别认证成功")
                    finish()
                }
                else -> {
//                    ToastUtil.show("人脸识别失败")
                }
            }

        }
    }

    private fun skipServiceAgreement() {
//        WebViewActivity.start(mContext, "http://www.baidu.com")
        val intent = Intent()
//        intent.putExtra(KEY_CERTIFY_TYPE, certifyType)
        intent.setClass(mContext, ProtocolActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setResultCallback(type: Int) {
        val data = Intent()
        data.putExtra("certifyType", type)
        setResult(Activity.RESULT_OK, data)
    }
}


