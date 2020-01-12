package com.tourcool.ui.certify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.alipay.sdk.app.AuthTask
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.util.OrderInfoUtil2_0
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.PayResult
import com.tourcool.bean.ali.AuthResult
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import kotlinx.android.synthetic.main.activity_real_name_certification_new.*

/**
 *@description :实名认证
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月02日10:01
 * @Email: 971613168@qq.com
 */
class SelectCertifyActivity : BaseCommonTitleActivity(), View.OnClickListener {
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
//                skipCertify(EXTRA_CERTIFY_ALI_PAY)
                authV2()
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

    /**
     * 支付宝账户授权业务示例
     */
    private fun authV2() {
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
        TARGET_ID =   "123456782567"
        val rsa2: Boolean = RSA2_PRIVATE.isNotEmpty()
        val authInfoMap: Map<String, String> = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2)
        val info: String = OrderInfoUtil2_0.buildOrderParam(authInfoMap)
        val privateKey: String = if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
        val sign: String = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2)
        val authInfo = "$info&$sign"
        val authRunnable = Runnable {
            // 构造AuthTask 对象
            val authTask = AuthTask(this@SelectCertifyActivity)
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
                    val resultStatus: String = authResult.getResultStatus()
                    // 判断resultStatus 为“9000”且result_code
// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) { // 获取alipay_open_id，调支付时作为参数extern_token 的value
// 传入，则支付账户为该授权账户
                        ToastUtil.showSuccess("授权成功")
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

}