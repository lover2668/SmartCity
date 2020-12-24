package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import com.frame.library.core.basis.BaseFragment
import com.tourcool.smartcity.R

/**
 *@description : 交易记录
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日16:29
 * @Email: 971613168@qq.com
 */
class CitizenCardDealRecordFragment : BaseFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_deal_record
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    companion object {


        fun newInstance(): CitizenCardDealRecordFragment? {
            return CitizenCardDealRecordFragment()
        }
    }
}