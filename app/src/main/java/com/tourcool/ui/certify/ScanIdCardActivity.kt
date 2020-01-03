package com.tourcool.ui.certify

import android.os.Bundle
import com.frame.library.core.module.activity.FrameTitleActivity
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.core.util.TourCooUtil
import com.tourcool.smartcity.R

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月03日18:02
 * @Email: 971613168@qq.com
 */
class ScanIdCardActivity : FrameTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_certify_scan_id_card
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        initTitleBar(titleBar!!)
    }

    override fun initView(savedInstanceState: Bundle?) {

    }


    private fun initTitleBar(titleBar: TitleBarView) {
        titleBar.setBackgroundColor(TourCooUtil.getColor(R.color.transparent))
        setMarginTop(titleBar)
        val mainText = titleBar.mainTitleTextView
        mainText.text = ""
        titleBar.setTitleMainText("身份证扫描")
        titleBar.setRightText("完成")
        mainText.setTextColor(TourCooUtil.getColor(R.color.white))
    }
}