package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.frame.library.core.basis.BaseFragment
import com.tourcool.smartcity.R


/**
 *@description : JenkinsZhou
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月24日10:09
 * @Email: 971613168@qq.com
 */
class CitizenCardOperationContainerFragment : BaseFragment() {
    private var functionFragment: CitizenCardOperationListFragment? = null

    private var rechargeFragment: CitizenCardOperationRechargeFragment? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_container
    }

    override fun initView(savedInstanceState: Bundle?) {
        init()
    }

    companion object {
        fun newInstance(): CitizenCardOperationContainerFragment? {
            return CitizenCardOperationContainerFragment()
        }
    }

    private fun init() {
        functionFragment = CitizenCardOperationListFragment.newInstance()
        rechargeFragment = CitizenCardOperationRechargeFragment.newInstance()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, functionFragment!!)
        transaction.add(R.id.fragmentContainer, rechargeFragment!!)
        transaction.hide(rechargeFragment!!)
        transaction.show(functionFragment!!)
        transaction.commit()
    }


     fun showFragmentFunctionList() {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        if (functionFragment != null && rechargeFragment != null) {
            transaction.show(functionFragment!!)
            transaction.hide(rechargeFragment!!)
            transaction.commit()
        }
    }

     fun showFragmentRecharge() {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        if (functionFragment != null && rechargeFragment != null) {
            transaction.show(rechargeFragment!!)
            transaction.hide(functionFragment!!)
            transaction.commit()
        }
    }
}