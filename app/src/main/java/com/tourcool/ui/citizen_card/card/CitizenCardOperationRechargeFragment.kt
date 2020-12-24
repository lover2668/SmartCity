package com.tourcool.ui.citizen_card.card

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.frame.library.core.basis.BaseFragment
import com.frame.library.core.util.ToastUtil
import com.tourcool.adapter.citizen_card.CitizenCardRechargeAdapter
import com.tourcool.bean.recharge.RechargeEntity
import com.tourcool.smartcity.R
import java.util.*

/**
 *@description : JenkinsZhou
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月24日10:18
 * @Email: 971613168@qq.com
 */
class CitizenCardOperationRechargeFragment : BaseFragment(), View.OnClickListener {
    private val mRechargeEntityList: MutableList<RechargeEntity> = ArrayList()
    private var adapter: CitizenCardRechargeAdapter? = null
    private var mRechargeRecyclerView: RecyclerView? = null
    private var cbAli: CheckBox? = null
    private var cbWeChat: CheckBox? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_citizen_card_operation_recharge
    }

    override fun initView(savedInstanceState: Bundle?) {
        mRechargeRecyclerView = mContentView.findViewById(R.id.mRechargeRecyclerView)
        mContentView.findViewById<RelativeLayout>(R.id.rlBack).setOnClickListener(this)
        mContentView.findViewById<LinearLayout>(R.id.llPayTypeAli).setOnClickListener(this)
        mContentView.findViewById<LinearLayout>(R.id.llPayTypeWeChat).setOnClickListener(this)
        mContentView.findViewById<TextView>(R.id.tvConfirm).setOnClickListener(this)
        cbAli = mContentView.findViewById(R.id.cbAli)
        cbWeChat = mContentView.findViewById(R.id.cbWeChat)
        mRechargeRecyclerView!!.layoutManager = GridLayoutManager(mContext, 3)
    }

    override fun loadData() {
        super.loadData()
        initData()
    }

    companion object {
        fun newInstance(): CitizenCardOperationRechargeFragment {
            return CitizenCardOperationRechargeFragment()
        }
    }

    /**
     * 状态检测 用于内存不足的时候保证fragment不会重叠
     *
     * @param savedInstanceState*/

    /*private fun stateCheck(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
          val  fm = childFragmentManager
            val fts: FragmentTransaction = fm.beginTransaction()
            val af = AnimationFragment()
            mContent = af
            fts.add(R.id.content_frame, af)
            fts.commit()
        } else {
            val af: AnimationFragment? = fragmentManager
                    .findFragmentByTag(tags.get(0)) as AnimationFragment?
            val pf: PlainFragment? = fragmentManager
                    .findFragmentByTag(tags.get(1)) as PlainFragment?
            val rf: RecordFragment? = fragmentManager
                    .findFragmentByTag(tags.get(2)) as RecordFragment?
            val inf: InformationFragment? = fragmentManager
                    .findFragmentByTag(tags.get(3)) as InformationFragment?
            val tf: TestingFragment? = fragmentManager
                    .findFragmentByTag(tags.get(4)) as TestingFragment?
            fragmentManager!!.beginTransaction().show(af).hide(pf).hide(rf)
                    .hide(inf).hide(tf).commit()
        }
    }*/

    private fun initData() {
        cbAli?.isChecked = true
        cbWeChat?.isChecked = false
        mRechargeEntityList.add(RechargeEntity(10.0, true))
        mRechargeEntityList.add(RechargeEntity(30.0))
        mRechargeEntityList.add(RechargeEntity(50.0))
        mRechargeEntityList.add(RechargeEntity(80.0))
        mRechargeEntityList.add(RechargeEntity(100.0))
        mRechargeEntityList.add(RechargeEntity(200.0))
        adapter = CitizenCardRechargeAdapter(mRechargeEntityList)
        adapter!!.bindToRecyclerView(mRechargeRecyclerView)
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            setSelect(position)
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * 设置选中属性
     *
     * @param position
     */
    private fun setSelect(position: Int) {
        if (position >= mRechargeEntityList.size) {
            return
        }
        var rechargeEntity: RechargeEntity
        for (i in mRechargeEntityList.indices) {
            rechargeEntity = mRechargeEntityList[i]
            rechargeEntity.selected = i == position
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlBack -> {
                val fragment = parentFragment as CitizenCardOperationContainerFragment
                fragment.showFragmentFunctionList()
            }
            R.id.llPayTypeAli -> {
                cbAli?.isChecked = true
                cbWeChat?.isChecked = false
            }
            R.id.llPayTypeWeChat -> {
                cbAli?.isChecked = false
                cbWeChat?.isChecked = true
            }
            R.id.tvConfirm -> {
                ToastUtil.show("确定支付")
            }


            else -> {
            }
        }
    }
}