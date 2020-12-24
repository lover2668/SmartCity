package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import com.frame.library.core.basis.BaseFragment
import com.tourcool.smartcity.R

/**
 *@description : 市名卡信息Fragment
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日16:42
 * @Email: 971613168@qq.com
 */
class CitizenCardInfoFragment : BaseFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_info
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    companion object {


        fun newInstance(): CitizenCardInfoFragment? {
            return CitizenCardInfoFragment()
        }
    }

}