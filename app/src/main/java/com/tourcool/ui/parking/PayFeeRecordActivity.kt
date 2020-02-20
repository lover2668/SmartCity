package com.tourcool.ui.parking

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.NetworkUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.tourcool.adapter.parking.ParkingRecordAdapter
import com.tourcool.bean.parking.ParingRecord
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseBlackTitleActivity
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.parking.QueryParkingFeeActivity.Companion.EXTRA_PLANT_NUM
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_parking_arrears_pay.*
import kotlinx.android.synthetic.main.activity_parking_arrears_pay.recyclerViewCommon
import kotlinx.android.synthetic.main.activity_parking_arrears_pay.smartRefreshCommon
import kotlinx.android.synthetic.main.activity_parking_my_car.*
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月16日11:18
 * @Email: 971613168@qq.com
 */
class PayFeeRecordActivity : BaseBlackTitleActivity() , OnRefreshListener ,View.OnClickListener{
    private var plantNum = ""
    private var mStatusManager: StatusLayoutManager? = null
    private var adapter : ParkingRecordAdapter ?= null
    override fun getContentLayout(): Int {
        return R.layout.activity_parking_arrears_pay
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText("欠费缴纳")
    }
    override fun initView(savedInstanceState: Bundle?) {

        plantNum = intent!!.getStringExtra(EXTRA_PLANT_NUM)
        if(plantNum.isEmpty()){
            ToastUtil.show("未获取到车牌号")
            finish()
        }
        tvPayParkingFee.setOnClickListener(this)
        smartRefreshCommon.setRefreshHeader(ClassicsHeader(mContext))
        smartRefreshCommon.setOnRefreshListener(this)
        recyclerViewCommon.layoutManager = LinearLayoutManager(mContext)
        setStatusManager()
        adapter = ParkingRecordAdapter()
        adapter!!.bindToRecyclerView(recyclerViewCommon)
    }

    override fun loadData() {
        super.loadData()
        requestQueryParkingRecord(plantNum)
        setItemClick()
    }


    private fun requestQueryParkingRecord(carNum: String ?){
        if (!NetworkUtil.isConnected(mContext)) {
            mStatusManager!!.showLoadingLayout()
            baseHandler.postDelayed({
                mStatusManager!!.showCustomLayout(R.layout.parking_view_no_net_work_layout,R.id.tvRefreshNet)
                smartRefreshCommon.finishRefresh(false)
            },300)
            return
        }
        ApiRepository.getInstance().requestQueryParkingRecord(carNum).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<List<ParingRecord>>>() {
            override fun onRequestNext(entity: BaseResult<List<ParingRecord>>) {
                smartRefreshCommon.finishRefresh()
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                     if (entity.data.isEmpty()) {
                         mStatusManager!!.showEmptyLayout()
                     } else {
                         adapter!!.setNewData(entity.data)
                         mStatusManager!!.showSuccessLayout()
                     }
                } else {
                    ToastUtil.show(entity.errorMsg)
                    mStatusManager!!.showErrorLayout()
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                ToastUtil.show(e!!.message)
                mStatusManager!!.showErrorLayout()
                smartRefreshCommon.finishRefresh()
            }
        })
    }

    private fun setItemClick(){
        adapter!!.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val currentRecord  =     adapter.data[position] as ParingRecord
            currentRecord.isSelect = !currentRecord.isSelect
            adapter!!.notifyDataSetChanged()
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        requestQueryParkingRecord(plantNum)
    }

    private fun setStatusManager() {
        val builder = StatusLayoutManager.Builder(llContainer)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setEmptyLayout(R.layout.parking_view_no_data_layout)
                .setErrorLayout(R.layout.view_error_layout)
                .setErrorClickViewID(R.id.llErrorRequest)
                .setDefaultEmptyText(com.tourcool.library.frame.demo.R.string.fast_multi_empty)
                .setDefaultLoadingText(com.tourcool.library.frame.demo.R.string.fast_multi_loading)
                .setOnStatusChildClickListener(object : OnStatusChildClickListener {
                    override fun onEmptyChildClick(view: View) {
                    }

                    override fun onErrorChildClick(view: View) {
                        /*     if (mIFastRefreshLoadView.getErrorClickListener() != null) {
                                 mIFastRefreshLoadView.getErrorClickListener().onClick(view)
                                 return
                             }*/
                        mStatusManager!!.showLoadingLayout()
                        requestQueryParkingRecord(plantNum)
                    }

                    override fun onCustomerChildClick(view: View) {
                        /*  if (mIFastRefreshLoadView.getCustomerClickListener() != null) {
                              mIFastRefreshLoadView.getCustomerClickListener().onClick(view)
                              return
                          }*/
                        mStatusManager!!.showLoadingLayout()
                        requestQueryParkingRecord(plantNum)
                    }
                })
        /*    if (mManager != null && mManager.getMultiStatusView() != null) {
                mManager.getMultiStatusView().setMultiStatusView(builder, mIFastRefreshLoadView)
            }*/
//        mIFastRefreshLoadView.setMultiStatusView(builder)
        mStatusManager = builder.build()
        mStatusManager!!.showLoadingLayout()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvPayParkingFee -> {
                var hasSelect = false
                for(index in 0 until   adapter!!.data.size  ){
                    if(!(adapter!!.data[index] as ParingRecord).isSelect ){
                        continue
                    }else{
                        hasSelect = true
                        break
                    }
                }
                if(hasSelect){
                    ToastUtil.show("支付功能暂未开放")
                }else{
                    ToastUtil.show("请先选择缴纳项目")
                }

            }
            else -> {
            }
        }
    }

}