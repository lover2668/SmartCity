package com.tourcool.ui.constellation

import android.os.Bundle
import com.frame.library.core.basis.BaseFragment
import com.tourcool.smartcity.R

/**
 *@description : 本周财运
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日17:35
 * @Email: 971613168@qq.com
 */
class FortuneWeekFragment : BaseFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_fortune_week
    }

    override fun initView(savedInstanceState: Bundle?) {
    }


    companion object {
        fun newInstance(): FortuneWeekFragment? {
            val args = Bundle()
            val fragment = FortuneWeekFragment()
            fragment.arguments = args
            return fragment
        }
    }
}