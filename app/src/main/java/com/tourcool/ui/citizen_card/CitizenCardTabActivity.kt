package com.tourcool.ui.citizen_card

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.SizeUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.constellation.TabPagerAdapter
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.tourcool.ui.citizen_card.card.CitizenCardDealRecordFragment
import com.tourcool.ui.citizen_card.card.CitizenCardInfoFragment
import com.tourcool.ui.citizen_card.card.CitizenCardOperationContainerFragment
import kotlinx.android.synthetic.main.activity_citizen_card_info_tab.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView

/**
 *@description : 市名卡tab页
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年12月23日10:24
 * @Email: 971613168@qq.com
 */
class CitizenCardTabActivity : BaseTitleTransparentActivity() {
    private lateinit var commonNavigator: CommonNavigator
    private var mTabNameList = ArrayList<String>()
    private val mFragments = ArrayList<Fragment>()
    private var mChannelPagerAdapter: TabPagerAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_citizen_card_info_tab
    }

    override fun initView(savedInstanceState: Bundle?) {
        initFragment()
        initMagicIndicator()
        mChannelPagerAdapter = TabPagerAdapter(mFragments, supportFragmentManager)
        mViewPager.adapter = mChannelPagerAdapter
        mViewPager.offscreenPageLimit = mTabNameList.size+1
        commonNavigator.notifyDataSetChanged()
        tvIdCard.setOnClickListener {
            FrameUtil.startActivity(mContext,CitizenCardQrCodeActivity::class.java)
        }
    }

    private fun initFragment() {
        mFragments.clear()
        mFragments.add(CitizenCardDealRecordFragment.newInstance()!!)
        mTabNameList.add("市民卡信息")
        mFragments.add(CitizenCardInfoFragment.newInstance()!!)
        mTabNameList.add("市民卡交易")
        mFragments.add(CitizenCardOperationContainerFragment.newInstance()!!)
        mTabNameList.add("市民卡操作")
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("市民卡")
    }

    private fun initMagicIndicator() {
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
}