package com.tourcool.ui.constellation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.frame.library.core.manager.GlideManager
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.SizeUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.constellation.TabPagerAdapter
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import kotlinx.android.synthetic.main.activity_constellation_tab.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView


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
    private var mStarName = ""
    private var mStarDate = ""
    private var imageId = -1
    private var mChannelPagerAdapter: TabPagerAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_constellation_tab
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("星座运势")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mStarName = intent.getStringExtra(ConstellationListActivity.EXTRA_STAR_NAME)
        mStarName = StringUtil.getNotNullValue(mStarName)
        mStarDate = intent.getStringExtra(ConstellationListActivity.EXTRA_STAR_DATE)
        mStarDate = StringUtil.getNotNullValue(mStarDate)
        imageId =   intent.getIntExtra(ConstellationListActivity.EXTRA_STAR_IMAGE,-1)
        GlideManager.loadCircleImg(imageId,ivStar)
        initFragment()
        initMagicIndicator()
        tvStarName.text = mStarName
        tvMonth.text = mStarDate
        mChannelPagerAdapter = TabPagerAdapter(mFragments, supportFragmentManager)
        mViewPager.adapter = mChannelPagerAdapter
        mViewPager.offscreenPageLimit = mTabNameList.size
        commonNavigator.notifyDataSetChanged()
    }

    private fun initMagicIndicator() {
        /*    commonNavigator = CommonNavigator(mContext)
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
                    simplePagerTitleView.textSize = 12f
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
            ViewPagerHelper.bind(magicIndicator, mViewPager)*/
        commonNavigator = CommonNavigator(mContext)
        commonNavigator.isSkimOver = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTabNameList.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = mTabNameList.get(index)
                clipPagerTitleView.textColor = FrameUtil.getColor(R.color.grayA2A2A2)
                clipPagerTitleView.clipColor = Color.WHITE
                clipPagerTitleView.textSize = SizeUtil.sp2px(12f).toFloat()

                clipPagerTitleView.setOnClickListener {
                    mViewPager.currentItem = index
                }
                return clipPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                val navigatorHeight: Float = context.getResources().getDimension(R.dimen.common_navigator_height)
                val borderWidth: Float = SizeUtil.dp2px(1f).toFloat()
                val lineHeight = navigatorHeight - 2 * borderWidth
                indicator.lineHeight = lineHeight
                indicator.roundRadius = (lineHeight / 2)
                indicator.yOffset = borderWidth
                indicator.setColors(FrameUtil.getColor(R.color.colorPrimary))
                return indicator
            }
        }
        commonNavigator.isAdjustMode = true
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun initFragment() {
        mFragments.clear()
        //今天
        mFragments.add(FortuneDayFragment.newInstance(0, mStarName)!!)
        mTabNameList.add("今 日")
        //明天
        mFragments.add(FortuneDayFragment.newInstance(1, mStarName)!!)
        mTabNameList.add("明 日")
        //本周
        mFragments.add(FortuneWeekFragment.newInstance(mStarName)!!)
        mTabNameList.add("本 周")
        //本月
        mFragments.add(FortuneMonthFragment.newInstance(mStarName)!!)
        mTabNameList.add("本 月")
        //今年
        mFragments.add(FortuneYearFragment.newInstance(mStarName)!!)
        mTabNameList.add("今 年")

    }


}