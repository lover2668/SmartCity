package com.tourcool.ui.parking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.NetworkUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.parking.MyCarAdapter
import com.tourcool.bean.parking.CarInfo

import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.widget.IosAlertDialog
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseBlackTitleActivity
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.kitchen.VideoListActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_parking_arrears_pay.*
import kotlinx.android.synthetic.main.activity_parking_my_car.*
import kotlinx.android.synthetic.main.activity_parking_my_car.recyclerViewCommon
import kotlinx.android.synthetic.main.activity_parking_my_car.smartRefreshCommon
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager

/**
 *@description :车辆列表
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月14日19:18
 * @Email: 971613168@qq.com
 */
class CarListActivity : BaseBlackTitleActivity(),View.OnClickListener {
    private var mStatusManager: StatusLayoutManager? = null
    private var adapter: MyCarAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_parking_my_car
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText("我的车辆")
    }

    override fun initView(savedInstanceState: Bundle?) {
        recyclerViewCommon.layoutManager = LinearLayoutManager(mContext)
        adapter = MyCarAdapter()
        smartRefreshCommon.setEnableRefresh(false)
        tvAddCar.setOnClickListener(this)
        adapter!!.bindToRecyclerView(recyclerViewCommon)
        setStatusManager()
        setItemClick()
        requestCarList()
    }

    private fun setItemClick() {
        adapter!!.setOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            when (view!!.id) {
                R.id.ivUnbind -> {
                    showAlertUnbind((adapter!!.data[position] as CarInfo).carId)
                }
                else -> {
                }

            }
        })
    }

    private fun setStatusManager() {
        val builder = StatusLayoutManager.Builder(rlContainer)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setEmptyLayout(R.layout.layout_car_empty)
                .setErrorLayout(R.layout.view_error_layout)
                .setErrorClickViewID(R.id.llErrorRequest)
                .setEmptyClickViewID(R.id.tvAddCar)
                .setDefaultEmptyText(com.tourcool.library.frame.demo.R.string.fast_multi_empty)
                .setDefaultLoadingText(com.tourcool.library.frame.demo.R.string.fast_multi_loading)
                .setOnStatusChildClickListener(object : OnStatusChildClickListener {
                    override fun onEmptyChildClick(view: View) {
                       skipBindCar()
                    }

                    override fun onErrorChildClick(view: View) {
                        /*     if (mIFastRefreshLoadView.getErrorClickListener() != null) {
                                 mIFastRefreshLoadView.getErrorClickListener().onClick(view)
                                 return
                             }*/
                        mStatusManager!!.showLoadingLayout()
                        requestCarList()
                    }

                    override fun onCustomerChildClick(view: View) {
                        /*  if (mIFastRefreshLoadView.getCustomerClickListener() != null) {
                              mIFastRefreshLoadView.getCustomerClickListener().onClick(view)
                              return
                          }*/
                        mStatusManager!!.showLoadingLayout()
                        requestCarList()
                    }
                })
        /*    if (mManager != null && mManager.getMultiStatusView() != null) {
                mManager.getMultiStatusView().setMultiStatusView(builder, mIFastRefreshLoadView)
            }*/
//        mIFastRefreshLoadView.setMultiStatusView(builder)
        mStatusManager = builder.build()
        mStatusManager!!.showLoadingLayout()
    }


  /*  private fun requestBindCar() {
        if (!NetworkUtil.isConnected(mContext)) {
            mStatusManager!!.showLoadingLayout()
            baseHandler.postDelayed({
                mStatusManager!!.showCustomLayout(R.layout.view_no_netwrok_layout, R.id.llNoNetwok)
            }, 300)
            return
        }
        ApiRepository.getInstance().requestAddCar("苏BTX109", "1").compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onRequestNext(entity: BaseResult<*>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    mStatusManager!!.showSuccessLayout()
                    ToastUtil.showSuccess(entity.data.toString())
                    *//*   if (entity.data.isEmpty()) {
                           mStatusManager!!.showEmptyLayout()
                       } else {
                           mStatusManager!!.showSuccessLayout()
                       }*//*
                } else {
                    ToastUtil.show(entity.errorMsg)
                    smartRefreshCommon.finishRefresh()
                    mStatusManager!!.showErrorLayout()
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                smartRefreshCommon.finishRefresh()
                mStatusManager!!.showErrorLayout()
                ToastUtil.show(e!!.message)
            }
        })
    }*/


    private fun requestCarList() {
        if (!NetworkUtil.isConnected(mContext)) {
            mStatusManager!!.showLoadingLayout()
            baseHandler.postDelayed({
                mStatusManager!!.showCustomLayout(R.layout.parking_view_no_net_work_layout,R.id.tvRefreshNet)
            }, 300)
            return
        }
        ApiRepository.getInstance().requestCarList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<CarInfo>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<CarInfo>>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    if (entity.data.isEmpty()) {
                        mStatusManager!!.showEmptyLayout()
                    } else {
                        adapter!!.setNewData(entity.data)
                        mStatusManager!!.showSuccessLayout()
                    }
                    setViewVisible(tvAddCar,entity.data.isNotEmpty())
                } else {
                    ToastUtil.show(entity.errorMsg)
                    smartRefreshCommon.finishRefresh()
                    mStatusManager!!.showErrorLayout()
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                smartRefreshCommon.finishRefresh()
                mStatusManager!!.showErrorLayout()
                ToastUtil.show(e!!.message)
            }
        })
    }

    private fun showAlertUnbind(carId: String) {
        IosAlertDialog(mContext)
                .init()
                .setDialogBackground(FrameUtil.getDrawable(R.drawable.bg_radius_20_white))
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setTitle("解绑车辆")
                .setNegativeTextColor(FrameUtil.getColor(R.color.gray888888))
                .setPositiveButton("确定") {
                    //                    skipDetailSettingIntent()
                    requestUnBindCar(carId)
                }
                .setNegativeButton("取消") {
                    //                    exitApp()
                }.show()
    }


    private fun requestUnBindCar(carId: String) {
        if (!NetworkUtil.isConnected(mContext)) {
            mStatusManager!!.showLoadingLayout()
            baseHandler.postDelayed({
                mStatusManager!!.showCustomLayout(R.layout.view_no_netwrok_layout, R.id.llNoNetwok)
            }, 300)
            return
        }
        ApiRepository.getInstance().requestUnBindCar(carId).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<String>>() {
            override fun onRequestNext(entity: BaseResult<String>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.show("解绑成功")
                    requestCarList()
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                smartRefreshCommon.finishRefresh()
                mStatusManager!!.showErrorLayout()
                ToastUtil.show(e!!.message)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvAddCar -> {
                skipBindCar()
            }
            else -> {
            }
        }
    }
    private fun skipBindCar() {
        val intent = Intent()
        intent.setClass(mContext, AddCarActivity::class.java)
        startActivityForResult(intent,6001)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            requestCarList()
        }
    }
}