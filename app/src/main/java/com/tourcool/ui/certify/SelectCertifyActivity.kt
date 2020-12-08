package com.tourcool.ui.certify

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.alipay.sdk.app.OpenAuthTask
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.certify.CertifyTypeAdapter
import com.tourcool.bean.PayResult
import com.tourcool.bean.ali.AuthResult
import com.tourcool.bean.certify.CertifyType
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.entity.Authenticate
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.event.account.UserInfoEvent
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_real_name_certification_new.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 *@description :实名认证
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月02日10:01
 * @Email: 971613168@qq.com
 */
class SelectCertifyActivity : BaseCommonTitleActivity(), View.OnClickListener {
    private val mTag = "SelectCertifyActivity"
    private val SDK_PAY_FLAG = 1
    private val SDK_AUTH_FLAG = 2
    private var callbackType = -1
    private val RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCVZmJoLPFmTQr6b0NmhQ51fs8VqVn2vWGiRlZ7AW4c+adlaY6j8whIXvoqnwkIYdTUJjZqXJzARx5dvXpXFon+fULgP/ZvEiu3mDleJ+63820Roi/2Nf32k9FKTBS96AUD64U+rnDOESsUkBDZS0XUe0N96CE42pgp2LP2XEQ2cQjwKG4eu5qvXlqwe5Tqh+b7N4g3gZES0b3/EHhLIYCzueLoOqO1uFUJFMaLPWKcY4gHIgvlXrf3/IgB2QRWxCMh8Hnj62LXjTKY4F5k6pT0NtZb8C8GPjo2wQhjBNrjLNOpZFMp+Q+aus1Sg9/hZT+GOIDyEAupBMoRUQD7QVfvAgMBAAECggEAG9mnNJZUNebcygyduunI5TxLbFVSkP2CytZj3rBIj5w2iWAhGA0BGUSjS/iznV1naFjrQe6bxfg7/+uHd96awNcm9VjXHqN7hNEauKOnC6GUTno2iKZN/n5VwIzoPPKYpL9t6l5oZvGqXz3v9iHjFZYY4cq5DrkpLnvYKG/Qw3kLPM2rT1Zr40JrK4/9NXsZJbN/omNntfBOMjsRuBidpuOq4bPHKRsGxYZE0834ushciHROVMlDG1OEt/xfKNK1ZnR1b1HhNUqItI7m+bOAyeYdulUIJZ8ZXpXi10PXfvp9QuJWhGh7yq6A/VhOu4H7k58gHpfnvjgWCzcwwZhD2QKBgQDgqEtJK9z9PNird33nwi5GvhxNJdVSQKzzlx+usI5gS4+o+3K7uDpjB7occt2fFHao7VaiZDNx5vzIQUPB2gKL94yUuvTCF+KRcUkHP3Xo4f7a99tocLIlHb6GE8w19L0pwf6MtgoQUSm93ux6re87J0Nt1nogz0oFtM0hwFz7WwKBgQCqPj9rCoMEwl9/HJgoNoeg7zSOvgr/yGZKkqk6sPfL/fC7b7YOUXkrmO//xrgN8fk/ubN3Ok9L69rOM0O+eHE/IfgxGnowiMV44Nt5FG5FUsKfyjJv5y4vlzZ868c+d9WkzdzPDKuVK7iry6gSyoxaStONkjqcqsDb+H6tBxz9/QKBgQDdV7LlYtwWfS6UseRQxVbGyGQl9pKYubQtQ8YpXTbJ1WizexptrXOJQoGxqdKE+6p1gXGMCeFN5eFEhFj3044SzVGq8BBacyH9XnovM+0+B/wLTYzU98PtIcMnYrYeqwT88W+Uv35m2TChK9QeguRjiam7vEcTsGgj+yDokhiKZQKBgFqzN05sF0md1Qr/zQD/rFrNlo7GKU4FEpqAcLDYP+zqqiryZWhTd98GaDc5RC4J0OVmpnrLEhw82CIrpdgAizU7f+OJW7gn5i10fvmPLQC6Cv7e1uhPnoe0ZE9BvrFFXwmitBWLho6+8HB23GDGkOg6HWO7mIaHYqDPT44X0BZxAoGATLa71moAWigwpRUf3tih/Rvk2E2Nt/J4zdL/A90BDk8d4+yxPACaN97/5SEHpKi9AeKvwtsf/ZYOXwMrd/tXn/xi9RpvdaF1ZH4l8tWFPdDT5woyiGrp24xM22EDlta4UPujyADBrIKDDuoX6zsNZi7Uk7PuA2fxvf31a8xiFyM="
    private val RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIf0Y3UEYW4LxzwBh/C2MUFy8BtMYrbPXu64LsoP7nF02XsD5QRd4hH4UPoZ54mtEIVLawUOzdq1vEvRuo0cCwS0qxXzey07mzHd4Hrdo9sQhwogjRTm5EMkMDyhNyAeesrh5rXkefTgN9HrUzXm4xn8/kguJh4b7D+Soy8oAaN9AgMBAAECgYBBhjXRFhVnFmIlt48vIoBGzQCQB5akCPn2D+rtFBSVtf2DXnMoUZ89jfJpN8byAekudRTVUTdBq8leuWwxykw8eopzODp6YOOn9viEbIbmAmCUNqZojpw9l5e2q8LG+J9R5GN+7I+anw1T4grE7xV7Y2SEzlemM2OHNSfA+atweQJBALqcfWhMfZDF9YG+QnTV3SYT8Sm8Ehx7iHN91uxnVWTgPNDCeql+qvfPo/64L8p7XwNETZNO8LCNrgrBS5fG2f8CQQC6geXC4aLypEaI1g5JwJkpgChixKITQ+pU+MLy5jyFpjSfxpfqxYs+bqwCLOdD2zRc6hqcr0wQReR4aRfUA+qDAkBPHB5SA/NLV5Cfow+7RhrNlLCtF6y55GjRRSzC5X+fFyzQ91FW33sNOTp1tpGUDj8WPHOCmnmQ7080AgPQ4RzzAkEAocKSDYF3u61BQ+MsB0mVTcpKvMdArybjcglDxDpuWZh64ezzy/rDt0VzyHs7pb3UX5XnuOfNN/v2mXzE3uz6kwJBAIzvFtmjp1Q4Vq4qBHHobPOyEpx+4Dn3ZzLOkjEC9LoN6MnK9qgyLykXQixbRmr9RvQ5RBVq4C6MvHBmVu5eHtc="

    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    val APPID = "2018100361597260"

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
        const val EXTRA_CERTIFY_ID_CARD = "EXTRA_CERTIFY_ID_CARD"
        const val EXTRA_CERTIFY_ALI_FACE = "EXTRA_CERTIFY_ALI_FACE"
        const val EXTRA_PHONE = "EXTRA_PHONE"
    }

    private var adapter: CertifyTypeAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_real_name_certification_new
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText("实名认证")
        /*
          titleBar.setRightText("跳过认证").setRightTextColor(FrameUtil.getColor(R.color.white)).setOnRightTextClickListener(object : View.OnClickListener {
              override fun onClick(v: View?) {
                  ToastUtil.show("跳过认证")
              }

          })*/
    }

    override fun initView(savedInstanceState: Bundle?) {
        llCertifyAli.setOnClickListener(this)
        llCertifyPhone.setOnClickListener(this)
        llCertifyBankcard.setOnClickListener(this)
        llCertifyAliFace.setOnClickListener(this)
        rvCommon.layoutManager = GridLayoutManager(mContext, 2)
        adapter = CertifyTypeAdapter()
        adapter!!.bindToRecyclerView(rvCommon)
        requestAuthenticationList()
    }

    override fun onClick(v: View?) {

    }


    private fun skipCertify(type: String) {
        val intent = Intent()
        intent.putExtra(KEY_CERTIFY_TYPE, type)
        intent.setClass(mContext, CertifyActivity::class.java)
        startActivityForResult(intent, 2002)
    }


    private fun handleAuthenticationCallback(entity: BaseResult<List<Authenticate>>) {
        setViewVisible(mTitleBar.getTextView(Gravity.END), !hasAllAuthentication(entity.data))
        loadCertifyType(entity.data, callbackType)
    }

    private fun hasAllAuthentication(list: List<Authenticate>): Boolean {
        for (authenticate in list) {
            if (authenticate.isAuthenticated) {
                continue
            }
            return false
        }
        return true
    }


    private fun requestAuthenticationList() {
        ApiRepository.getInstance().requestAuthentication().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<List<Authenticate>>>() {
            override fun onRequestNext(entity: BaseResult<List<Authenticate>>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    TourCooLogUtil.i(mTag, entity.data)
                    handleAuthenticationCallback(entity)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }

            }
        })
    }

    private fun loadCertifyType(list: List<Authenticate>, callbackType: Int) {
        val typeList: MutableList<CertifyType> = ArrayList()
//        val certifyTypeAli = CertifyType("支付宝账号认证", R.mipmap.ic_certify_ali)
        val certifyTypeIdCard = CertifyType("身份证实名认证", R.mipmap.ic_certify_id_card)
        val certifyTypeFace = CertifyType("支付宝人脸识别", R.mipmap.ic_certify_face)
        for (authenticate in list) {
            when (authenticate.type) {
                2 -> {
                  /*  //支付宝认证
                    certifyTypeAli.isCertified = authenticate.isAuthenticated
                    if (callbackType == 2) {
                        certifyTypeAli.isCertified = true
                    }*/
                }
                3 -> {
                    //身份证认证
                    certifyTypeIdCard.isCertified = authenticate.isAuthenticated
                    if (callbackType == 3) {
                        certifyTypeIdCard.isCertified = true
                    }
                }
                4 -> {
                    //人脸认证
                    certifyTypeFace.isCertified = authenticate.isAuthenticated
                    if (callbackType == 4) {
                        certifyTypeFace.isCertified = true
                    }
                }
            }
        }
//        typeList.add(certifyTypeAli)
        typeList.add(certifyTypeIdCard)
        typeList.add(certifyTypeFace)
        adapter!!.setNewData(typeList)
        adapter!!.setOnItemClickListener { adapter, view, position ->
            val type = adapter.data[position] as CertifyType
            when (type.certifyName) {
                "支付宝账号认证" -> {
                    //支付宝认证
//                    skipCertify(EXTRA_CERTIFY_ALI_PAY)
                    openAuthScheme()
                }
                "身份证实名认证" -> {
                    //身份证认证
                    skipCertify(EXTRA_CERTIFY_ID_CARD)
                }
                "支付宝人脸识别" -> {
                    //支付宝人脸识别认证
                    skipCertify(EXTRA_CERTIFY_ALI_FACE)
                }
                else -> {
                }
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


    private fun requestAliAuthentication() {
        ApiRepository.getInstance().requestAliAuthentication().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onRequestNext(entity: BaseResult<*>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
//                    skipMessageCertify()
                    ToastUtil.show("认证成功")
                    requestAuthenticationList()
                } else {
                    ToastUtil.show(entity.errorMsg)
                }

            }
        })
    }


    /**
     * 通用跳转授权业务 Demo
     */
    private fun openAuthScheme() {
        // 传递给支付宝应用的业务参数
        val bizParams: MutableMap<String, String> = HashMap()
        val url = "https://authweb.alipay.com/auth?auth_type=PURE_OAUTH_SDK&app_id=$APPID&scope=auth_user&state=init"
        bizParams["url"] = url
        // 支付宝回跳到你的应用时使用的 Intent Scheme。请设置为不和其它应用冲突的值。
// 请不要像 Demo 这样设置为 __alipaysdkdemo__!
// 如果不设置，将无法使用 H5 中间页的方法(OpenAuthTask.execute() 的最后一个参数)回跳至
// 你的应用。
//
// 注意！参见 AndroidManifest 中 <AlipayResultActivity> 的 android:scheme，此两处
// 必须设置为相同的值。
        val scheme = "smart_city_yi_xing_ali_paysdk"
        // 唤起授权业务
        val task = OpenAuthTask(mContext)
        task.execute(
                scheme,  // Intent Scheme
                OpenAuthTask.BizType.AccountAuth,
                // 业务类型
                bizParams,  // 业务参数
                openAuthCallback,  // 业务结果回调。注意：此回调必须被你的应用保持强引用
                true) // 是否需要在用户未安装支付宝 App 时，使用 H5 中间页中转。建议设置为 true。
    }


    /**
     * 通用跳转授权业务的回调方法。
     * 此方法在支付宝 SDK 中为弱引用，故你的 App 需要以成员变量等方式保持对 Callback 的强引用，
     * 以免对象被回收。
     * 以局部变量保持对 Callback 的引用是不可行的。
     */
    private var openAuthCallback = OpenAuthTask.Callback { resultCode, memo, bundle ->
        if (resultCode == OpenAuthTask.OK) {
            val result = String.format("业务成功，结果码: %s\n结果信息: %s\n结果数据: %s", resultCode, memo, bundleToString(bundle))
//            requestAliAuthentication()
            TourCooLogUtil.i(mTag, "result=$result")
            if (result.contains("auth_code")) {
                requestAliAuthentication()
            } else {
                ToastUtil.show("认证失败")
            }
//            ToastUtil.show("业务成功")
            //                showAlert(PayDemoActivity.this, String.format("业务成功，结果码: %s\n结果信息: %s\n结果数据: %s", resultCode, memo, bundleToString(bundle)));
        } else {
            ToastUtil.show("认证失败")
            //                showAlert(PayDemoActivity.this, String.format("业务失败，结果码: %s\n结果信息: %s\n结果数据: %s", resultCode, memo, bundleToString(bundle)));
        }
    }

    private fun bundleToString(bundle: Bundle?): String? {
        if (bundle == null) {
            return "null"
        }
        val sb = StringBuilder()
        for (key in bundle.keySet()) {
            sb.append(key).append("=>").append(bundle[key]).append("\n")
        }
        return sb.toString()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                callbackType = data.getIntExtra("certifyType", -1)
            }
            //刷新用户数据
            EventBus.getDefault().postSticky(UserInfoEvent())
            //刷新认证结果
            requestAuthenticationList()
        }
    }


}