package com.tourcool.ui.constellation

import android.os.Bundle
import com.frame.library.core.basis.BaseFragment
import com.tourcool.smartcity.R

/**
 *@description : 本月运势
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日18:09
 * @Email: 971613168@qq.com
 */
class FortuneMonthFragment : BaseFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_fortune_month
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    companion object {
        fun newInstance(): FortuneMonthFragment? {
            val args = Bundle()
            val fragment = FortuneMonthFragment()
            fragment.arguments = args
            return fragment
        }
    }
}