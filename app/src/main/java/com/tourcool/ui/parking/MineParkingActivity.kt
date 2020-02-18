package com.tourcool.ui.parking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.frame.library.core.manager.GlideManager
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.NetworkUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.bean.account.AccountHelper
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.module.WebViewActivity
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.util.TourCooUtil
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseBlackTitleActivity
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.certify.CertifyMessageActivity
import com.tourcool.ui.certify.SelectCertifyActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_certify_identity.*
import kotlinx.android.synthetic.main.activity_parking_mine.*
import kotlinx.android.synthetic.main.activity_parking_mine.tvPhoneNumber

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月14日16:08
 * @Email: 971613168@qq.com
 */
class MineParkingActivity : BaseBlackTitleActivity() ,View.OnClickListener{
    override fun getContentLayout(): Int {
        return R.layout.activity_parking_mine
    }

    override fun initView(savedInstanceState: Bundle?) {
        if(!AccountHelper.getInstance().isLogin){
            ToastUtil.show("您还未登录")
            finish()
            return
        }
        llMyCar.setOnClickListener(this)
        llPayParkingFee.setOnClickListener(this)
        llFindParking.setOnClickListener(this)
        loadMineInfo()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llMyCar -> {
                skipCarList()
            }
            R.id.llPayParkingFee->{
                skipPayParkingFee()
            }
            R.id.llFindParking->{
                requestFindParkingUrl()
            }
            else -> {
            }
        }
    }

    private fun loadMineInfo(){
        tvNickName.text = AccountHelper.getInstance().userInfo.nickname
        tvPhoneNumber.text = AccountHelper.getInstance().userInfo.phoneNumber
        GlideManager.loadCircleImg(TourCooUtil.getUrl(AccountHelper.getInstance().userInfo.iconUrl),ivAvatar)
    }


    private fun skipPayParkingFee() {
        val intent = Intent()
        intent.setClass(mContext, QueryParkingFeeActivity::class.java)
        startActivity(intent)
    }

    private fun skipCarList() {
        val intent = Intent()
        intent.setClass(mContext, CarListActivity::class.java)
        startActivityForResult(intent,6002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            setResult(resultCode)
    }



    private fun requestFindParkingUrl() {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtil.show("请检查网络连接")
            return
        }
        ApiRepository.getInstance().requestFindParkingUrl().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<String>>() {
            override fun onRequestNext(entity: BaseResult<String>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                  WebViewActivity.start(mContext,entity.data)
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
}