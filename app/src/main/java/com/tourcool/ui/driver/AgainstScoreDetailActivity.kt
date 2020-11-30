package com.tourcool.ui.driver

import android.os.Bundle
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.tourcool.ui.driver.AgainstScoreQueryActivity.Companion.EXTRA_DRIVER_FILE_NUMBER
import com.tourcool.ui.driver.AgainstScoreQueryActivity.Companion.EXTRA_DRIVER_LICENSE_NUMBER
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_driving_against_score_total.*

/**
 *@description : 驾驶员计分查询详情
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月30日9:39
 * @Email: 971613168@qq.com
 */
class AgainstScoreDetailActivity : BaseTitleTransparentActivity() {
    private var driverLicense = ""
    private var dossierNum = ""
    override fun getContentLayout(): Int {
        return R.layout.activity_driving_against_score_total
    }
    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("驾驶员累计计分")
    }
    override fun loadData() {
        super.loadData()
        tvDriverLicense.text = driverLicense
        tvDossierNum.text = dossierNum
        requestDriverLicenseScore()
    }

    override fun initView(savedInstanceState: Bundle?) {
        driverLicense = intent.getStringExtra(EXTRA_DRIVER_LICENSE_NUMBER)
        dossierNum = intent.getStringExtra(EXTRA_DRIVER_FILE_NUMBER)

    }

    private fun requestDriverLicenseScore() {
        ApiRepository.getInstance().requestDriverLicenseScore(driverLicense, dossierNum).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<String>>() {
            override fun onRequestNext(entity: BaseResult<String>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showDriverScore(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }

        })
    }

    private fun showDriverScore(score: String) {
        tvScoreDeduction.text = score + "分"
        try {
            tvScoreTotal.text = score
            val remain = 12 - score.toInt()
            tvScoreRemain.text = remain.toString() + "分"
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }


    }
}