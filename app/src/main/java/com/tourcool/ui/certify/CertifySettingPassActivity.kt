package com.tourcool.ui.certify

import android.os.Bundle
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.core.module.activity.BaseTitleActivity
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年01月03日11:32
 * @Email: 971613168@qq.com
 */
class CertifySettingPassActivity : BaseCommonTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_certify_setting_pass
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText("设置密码")
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

}