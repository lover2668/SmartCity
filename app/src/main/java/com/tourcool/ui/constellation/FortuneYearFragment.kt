package com.tourcool.ui.constellation

import android.os.Bundle
import com.frame.library.core.basis.BaseFragment
import com.tourcool.smartcity.R

/**
 *@description : 年度运势
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日18:12
 * @Email: 971613168@qq.com
 */
class FortuneYearFragment : BaseFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_fortune_year
    }

    override fun initView(savedInstanceState: Bundle?) {
    }


    companion object {
        fun newInstance(): FortuneYearFragment? {
            val args = Bundle()
            val fragment = FortuneYearFragment()
            fragment.arguments = args
            return fragment
        }
    }
}