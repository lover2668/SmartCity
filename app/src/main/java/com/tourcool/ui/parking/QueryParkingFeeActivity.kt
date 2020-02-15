package com.tourcool.ui.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.NetworkUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.bean.parking.CarInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月15日21:42
 * @Email: 971613168@qq.com
 */
class QueryParkingFeeActivity : BaseCommonTitleActivity(),View.OnClickListener {
    override fun getContentLayout(): Int {
        return R.layout.activity_parking_query_fee_pay
    }

    override fun initView(savedInstanceState: Bundle?) {
//        llCarListContainer

    }

    override fun loadData() {
        super.loadData()
        requestCarList()
    }

    private fun requestCarList() {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtil.show("请检查网络连接")
            return
        }
        ApiRepository.getInstance().requestCarList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<CarInfo>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<CarInfo>>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    /* if (entity.data.isEmpty()) {
                         mStatusManager!!.showEmptyLayout()
                     } else {
                         adapter!!.setNewData(entity.data)
                         mStatusManager!!.showSuccessLayout()
                     }*/
                 loadFastQueryByCarList(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                ToastUtil.show(e!!.message)
            }
        })
    }


    private fun loadFastQueryByCarList(carList: MutableList<CarInfo>?) {
        if (carList == null) {
            setViewGone(llFastQuery, false)
            return
        }
        setViewGone(llFastQuery, carList.isNotEmpty())
        if(carList.isEmpty()){
            return
        }
        for (carInfo in carList) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.layout_textview_car_info,null)
            val textView = view.findViewById<TextView>(R.id.tvCarInfo)
            textView.text = carInfo.carNum
            llCarListContainer.addView(view)
            textView.setOnClickListener(View.OnClickListener {
                ToastUtil.show(textView.text)
                val carNum =    textView.text as String
                requestQueryParkingRecord( carNum)
            })
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvQueryFee -> {

            }
            else -> {
            }
        }
    }



    private fun requestQueryParkingRecord(carNum: String ?){
        ApiRepository.getInstance().requestQueryParkingRecord(carNum).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onRequestNext(entity: BaseResult<*>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    /* if (entity.data.isEmpty()) {
                         mStatusManager!!.showEmptyLayout()
                     } else {
                         adapter!!.setNewData(entity.data)
                         mStatusManager!!.showSuccessLayout()
                     }*/

                } else {
                    ToastUtil.show(entity.errorMsg)
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                ToastUtil.show(e!!.message)
            }
        })
    }


    private fun doQueryParkingFee(){

    }
}