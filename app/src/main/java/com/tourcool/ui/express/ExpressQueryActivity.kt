package com.tourcool.ui.express

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.SizeUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.express.ExpressCompany
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.widget.searchview.BSearchEdit
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_express_query.*

/**
 *@description : 快递查询
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日19:02
 * @Email: 971613168@qq.com
 */
class ExpressQueryActivity : BaseCommonTitleActivity(), View.OnClickListener {
    private var bSearchEdit: BSearchEdit? = null
    private var currentSelectPosition = -1
    private val expressList = ArrayList<ExpressCompany>()
    override fun getContentLayout(): Int {
        return R.layout.activity_express_query
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvQuery.setOnClickListener {
            skipDetail()
        }
        tvExpressCom.post {
            initSearchView(viewLineVertical.width.toFloat())
        }
        tvExpressCom.inputType = InputType.TYPE_NULL
        tvExpressCom.setOnClickListener(this)
        llExpressContent.setOnClickListener(this)
        ivExpressSelect.setOnClickListener(this)

    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("快递查询")
    }

    private fun requestExpressCompany() {
        ApiRepository.getInstance().requestExpressCompany().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<ExpressCompany>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<ExpressCompany>>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showExpressCompany(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    private fun showExpressCompany(list: MutableList<ExpressCompany>?) {
        val companyList = ArrayList<String>()
        if (list == null) {
            ToastUtil.show("未获取到物流公司")
            return
        }
        list.forEach {
            companyList.add(it.com)
        }
        expressList.clear()
        expressList.addAll(list)
        bSearchEdit!!.setSearchList(companyList)
        bSearchEdit!!.showPopup()
    }

    companion object {
        const val EXTRA_EXPRESS_COM_NO = "EXTRA_EXPRESS_COM_NO"
        const val EXTRA_EXPRESS_COM_NAME = "EXTRA_EXPRESS_COM_NAME"
        const val EXTRA_EXPRESS_NO = "EXTRA_EXPRESS_NO"
        const val EXTRA_PHONE = "EXTRA_PHONE"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llExpressContent, R.id.ivExpressSelect, R.id.tvExpressCom -> {

                requestExpressCompany()
            }
            else -> {
            }
        }
    }

    private fun initSearchView(widthPx: Float) {
        //第三个必须要设置窗体的宽度，单位dp
        bSearchEdit = BSearchEdit(this, viewLineVertical, SizeUtil.px2dp(widthPx))
        bSearchEdit!!.setTimely(false)
        bSearchEdit!!.build()
        bSearchEdit!!.setTextClickListener { position, text ->
            currentSelectPosition = position
            tvExpressCom.setText(text!!)
        }
    }

    private fun skipDetail() {
        if (TextUtils.isEmpty(etExpressNum.text.toString())) {
            ToastUtil.show("请输入快递单号")
            return
        }
        if (TextUtils.isEmpty(tvExpressCom.text.toString())) {
            ToastUtil.show("请选择快递公司")
            return
        }
        if (TextUtils.isEmpty(etPhone.text.toString())) {
            ToastUtil.show("请输入收货人手机号")
            return
        }
        if (!StringUtil.isPhoneNumber(etPhone.text.toString())) {
            ToastUtil.show("请输入正确的手机号")
            return
        }
        if (currentSelectPosition < 0 || currentSelectPosition >= expressList.size) {
            ToastUtil.show("未获取到快递公司")
            return
        }
        val intent = Intent()
        intent.putExtra(EXTRA_EXPRESS_COM_NO, expressList[currentSelectPosition].no)
        intent.putExtra(EXTRA_EXPRESS_COM_NAME, expressList[currentSelectPosition].com)
        intent.putExtra(EXTRA_EXPRESS_NO, etExpressNum.text.toString())
        intent.putExtra(EXTRA_PHONE, etPhone.text.toString())
        intent.setClass(mContext, ExpressDetailActivity::class.java)
        startActivity(intent)
    }
}