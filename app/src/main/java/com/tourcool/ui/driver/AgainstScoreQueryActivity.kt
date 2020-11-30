package com.tourcool.ui.driver

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import kotlinx.android.synthetic.main.activity_driving_against_score_result.*

/**
 *@description : 驾驶员计分查询
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月30日9:36
 * @Email: 971613168@qq.com
 */
class AgainstScoreQueryActivity : BaseCommonTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_driving_against_score_result
    }


    override fun initView(savedInstanceState: Bundle?) {
        tvQuery.setOnClickListener {
            doSkip()
        }
    }
    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("驾驶员累计计分查询")
    }

    companion object {
        const val EXTRA_DRIVER_FILE_NUMBER = "EXTRA_DRIVER_FILE_NUMBER"
        const val EXTRA_DRIVER_LICENSE_NUMBER = "EXTRA_DRIVER_LICENSE_NUMBER"
    }


    private fun skipDetail() {
        val intent = Intent()
        intent.putExtra(EXTRA_DRIVER_LICENSE_NUMBER, etDriverLicense.text.toString())
        intent.putExtra(EXTRA_DRIVER_FILE_NUMBER, etDossierNum.text.toString())
        intent.setClass(mContext, AgainstScoreDetailActivity::class.java)
        startActivity(intent)
    }


    private fun doSkip() {
        if (TextUtils.isEmpty(etDriverLicense.text.toString())) {
            ToastUtil.show("请输入驾驶证号")
            return
        }
        if (TextUtils.isEmpty(etDossierNum.text.toString())) {
            ToastUtil.show("请输入档案编号")
            return
        }
        skipDetail()
    }
}