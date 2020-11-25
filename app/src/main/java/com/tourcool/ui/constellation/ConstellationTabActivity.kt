package com.tourcool.ui.constellation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.constellation.ConstellationPagerAdapter
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import kotlinx.android.synthetic.main.activity_constellation_tab.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *@description : 星座tab页
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日16:27
 * @Email: 971613168@qq.com
 */
class ConstellationTabActivity : BaseTitleTransparentActivity() {
    private lateinit var commonNavigator: CommonNavigator
    private var mTabNameList = ArrayList<String>()
    private val mFragments = ArrayList<Fragment>()
    private var mChannelPagerAdapter: ConstellationPagerAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_constellation_tab
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("星座运势")
    }
    override fun initView(savedInstanceState: Bundle?) {
        initFragment()
        initMagicIndicator()
        mChannelPagerAdapter = ConstellationPagerAdapter(mFragments, supportFragmentManager)
        mViewPager.adapter = mChannelPagerAdapter
        mViewPager.offscreenPageLimit = mTabNameList.size
        commonNavigator.notifyDataSetChanged()
    }

    private fun initMagicIndicator() {
        commonNavigator = CommonNavigator(mContext)
        commonNavigator.isSkimOver = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mTabNameList.size
            }


            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.normalColor = resources.getColor(R.color.blue)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.colorAccent)
                simplePagerTitleView.text = mTabNameList[index]
                simplePagerTitleView.textSize =12f
                simplePagerTitleView.setOnClickListener {
                    mViewPager.currentItem = index
                }

                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                linePagerIndicator.setColors(resources.getColor(R.color.colorAccent))
                return linePagerIndicator
            }
        }
        commonNavigator.isAdjustMode = true
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun initFragment() {
        mFragments.clear()
        //今天
        mFragments.add(FortuneDayFragment.newInstance(1)!!)
        mTabNameList.add("今 日")
        //明天
        mFragments.add(FortuneDayFragment.newInstance(2)!!)
        mTabNameList.add("明 日")
        //本周
        mFragments.add(FortuneWeekFragment.newInstance()!!)
        mTabNameList.add("本 周")
        //本月
        mFragments.add(FortuneMonthFragment.newInstance()!!)
        mTabNameList.add("本 月")
        //今年
        mFragments.add(FortuneYearFragment.newInstance()!!)
        mTabNameList.add("今 年")

    }


}