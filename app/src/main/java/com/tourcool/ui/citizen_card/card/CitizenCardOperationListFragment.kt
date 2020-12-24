package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.frame.library.core.basis.BaseFragment
import com.tourcool.core.widget.CommonRadiusDialog
import com.tourcool.smartcity.R


/**
 *@description : 市名卡操作
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日17:17
 * @Email: 971613168@qq.com
 */
class CitizenCardOperationListFragment : BaseFragment(), View.OnClickListener {

    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_operation_function_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        mContentView.findViewById<LinearLayout>(R.id.llCardRecharge).setOnClickListener(this)
        mContentView.findViewById<LinearLayout>(R.id.llCardUnbind).setOnClickListener(this)
    }

    companion object {


        fun newInstance(): CitizenCardOperationListFragment {
            return CitizenCardOperationListFragment()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llCardRecharge -> {
                val fragment = parentFragment as CitizenCardOperationContainerFragment
                fragment.showFragmentRecharge()
            }
            R.id.llCardUnbind -> {
                val dialog = CommonRadiusDialog(mContext)
                dialog.setNegativeButton("好的") {

                }.setMsg("请您先去线下市民卡网点办\n" +
                        "理实体卡，再进行绑定操作").setTitle("无实体卡可绑定").setCancelable(false).setCanceledOnTouchOutside(false).show()
            }
            else -> {
            }
        }
    }


}