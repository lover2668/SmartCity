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
        constellationList.add(ConstellationInfo("金牛座", "4.20-5.20", R.drawable.ic_star_jinniu))
        constellationList.add(ConstellationInfo("双子座", "5.21-6.21", R.mipmap.ic_star_bai_yang))

        constellationList.add(ConstellationInfo("巨蟹座", "6.22-7.22", R.mipmap.ic_star_juxie))
        constellationList.add(ConstellationInfo("狮子座", "7.23-8.22", R.mipmap.ic_star_shizi))
        constellationList.add(ConstellationInfo("处女座", "8.23-9.22", R.mipmap.ic_star_chunv))

        constellationList.add(ConstellationInfo("天秤座", "9.23-10.23", R.mipmap.ic_star_tiancheng))
        constellationList.add(ConstellationInfo("天蝎座", "10.24-11.22", R.mipmap.ic_star_tianxie))
        constellationList.add(ConstellationInfo("射手座", "11.23-12.21", R.mipmap.ic_star_sheshou))

        constellationList.add(ConstellationInfo("摩羯座", "12.22-1.19", R.mipmap.ic_star_mojie))
        constellationList.add(ConstellationInfo("水瓶座", "1.20-2.18", R.mipmap.ic_star_shuiping))
        constellationList.add(ConstellationInfo("双鱼座", "2.19-3.20", R.mipmap.ic_star_shuangyu))


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