package com.tourcool.ui.constellation

import android.os.Bundle
import com.frame.library.core.basis.BaseFragment
import com.tourcool.smartcity.R

/**
 *@description : 财运（天）
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日16:43
 * @Email: 971613168@qq.com
 */
class FortuneDayFragment : BaseFragment() {
    private var mType: Int = 0
    override fun getContentLayout(): Int {
        return R.layout.fragment_fortune_day
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    companion object {
        fun newInstance(type: Int): FortuneDayFragment? {
            val args = Bundle()
            val fragment = FortuneDayFragment()
            fragment.arguments = args
            fragment.mType = type
            return fragment
        }
    }

}