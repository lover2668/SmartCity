package com.tourcool.ui.constellation

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.constellation.ConstellationAdapter
import com.tourcool.bean.constellation.ConstellationInfo
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import kotlinx.android.synthetic.main.activity_constellation_list.*

/**
 *@description : 星座列表
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日15:54
 * @Email: 971613168@qq.com
 */
class ConstellationListActivity : BaseTitleTransparentActivity() {
    private var adapter: ConstellationAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_constellation_list
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("星座运势")
    }

    override fun initView(savedInstanceState: Bundle?) {
        constellationRefreshLayout.setEnableRefresh(false)
        constellationRefreshLayout.setEnableLoadMore(false)
        constellationRefreshLayout.setEnableHeaderTranslationContent(true)
                .setEnableOverScrollDrag(true)
        loadConstellationList()
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val info = adapter!!.data[position] as ConstellationInfo
            skip(info.name,info.month)
        }
    }


    private fun loadConstellationList() {
        adapter = ConstellationAdapter()
        constellationRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        adapter!!.bindToRecyclerView(constellationRecyclerView)
        val constellationList = ArrayList<ConstellationInfo>()
        constellationList.add(ConstellationInfo("白羊座", "3.21-4.19", R.mipmap.ic_star_bai_yang))
        constellationList.add(ConstellationInfo("金牛座", "4.20-5.20", R.mipmap.ic_star_bai_yang))
        constellationList.add(ConstellationInfo("双子座", "5.21-6.21", R.mipmap.ic_star_bai_yang))
        adapter!!.setNewData(constellationList)
    }

    companion object {
        const val EXTRA_STAR_NAME = "EXTRA_STAR_NAME"
        const val EXTRA_STAR_DATE = "EXTRA_STAR_DATE"

    }

    private fun skip(starName: String,starDate:String) {
        val intent = Intent()
        intent.setClass(mContext, ConstellationTabActivity::class.java)
        intent.putExtra(EXTRA_STAR_NAME, starName)
        intent.putExtra(EXTRA_STAR_DATE, starDate)
        startActivity(intent)
    }
}