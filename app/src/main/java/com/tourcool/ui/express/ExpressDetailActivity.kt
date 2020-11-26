package com.tourcool.ui.express

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.adapter.express.ExpressStepAdapter
import com.tourcool.bean.express.ExpressBean
import com.tourcool.bean.express.ExpressInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.tourcool.ui.express.ExpressQueryActivity.Companion.EXTRA_EXPRESS_COM_NAME
import com.tourcool.ui.express.ExpressQueryActivity.Companion.EXTRA_EXPRESS_COM_NO
import com.tourcool.ui.express.ExpressQueryActivity.Companion.EXTRA_EXPRESS_NO
import com.tourcool.ui.express.ExpressQueryActivity.Companion.EXTRA_PHONE
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_express_detail.*

/**
 *@description : 物流详情
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日19:43
 * @Email: 971613168@qq.com
 */
class ExpressDetailActivity : BaseTitleTransparentActivity() {
    private var adapter: ExpressStepAdapter? = null
    private var company: String? = null
    private var companyName: String? = null
    private var expressNum: String? = null
    private var mobile: String? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_express_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        companyName = intent.getStringExtra(EXTRA_EXPRESS_COM_NAME)
        company = intent.getStringExtra(EXTRA_EXPRESS_COM_NO)
        company = StringUtil.getNotNullValue(company)
        expressNum = intent.getStringExtra(EXTRA_EXPRESS_NO)
        expressNum = StringUtil.getNotNullValue(expressNum)
        mobile = intent.getStringExtra(EXTRA_PHONE)
        mobile = StringUtil.getNotNullValue(mobile)
        rvExpress.layoutManager = LinearLayoutManager(mContext)
        adapter = ExpressStepAdapter()
        adapter!!.bindToRecyclerView(rvExpress)
        tvExpressName.text = companyName+"快递"
        tvExpressNumber.text = expressNum
        TourCooLogUtil.i("物流公司："+company)
        requestExpressDetail()
    }

    private fun requestExpressDetail() {
        ApiRepository.getInstance().requestExpressDeliveryDetail(company, expressNum, mobile).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<ExpressBean>>() {
            override fun onRequestNext(entity: BaseResult<ExpressBean>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showDataList(entity.data.list)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    private fun showDataList(data: MutableList<ExpressInfo>) {
        adapter?.setNewData(data)

    }
}