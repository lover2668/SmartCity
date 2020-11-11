package com.tourcool.ui.social.detail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.frame.library.core.util.FrameUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.SocialRecordDetailAdapter
import com.tourcool.bean.social.SocialBaseInfo
import com.tourcool.bean.social.SocialDetail
import com.tourcool.core.config.AppConfig
import com.tourcool.core.constant.ItemConstant.*
import com.tourcool.core.constant.SocialConstant.EXTRA_SOCIAL_TYPE
import com.tourcool.core.module.mvp.BaseMvpRefreshLoadActivity
import com.tourcool.smartcity.R

class SocialListDetailActivity : BaseMvpRefreshLoadActivity<SocialDetailPresenter, SocialDetail>(), SocialDetailContract.View {
    private var adapter: SocialRecordDetailAdapter? = null
    private var tvHeaderName: TextView? = null
    private var tvHeaderIdCard: TextView? = null
    private var socialType: String? = null
    override fun getContentLayout(): Int {
        return R.layout.frame_layout_title_refresh_recycler
    }

    override fun beforeSetTitleBar(titleBar: TitleBarView?) {
        super.beforeSetTitleBar(titleBar)
        socialType = intent.getStringExtra(EXTRA_SOCIAL_TYPE)
    }
    override fun initView(savedInstanceState: Bundle?) {

    }


    override fun loadData(page: Int) {
        presenter.getSocialDetailList(page, socialType)
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        if (titleBar != null) {
            setMarginTop(titleBar)
            titleBar.setBgResource(R.drawable.bg_gradient_title_common)
            titleBar.setLeftTextDrawable(R.drawable.ic_back_white)
            titleBar.setTitleMainTextColor(FrameUtil.getColor(R.color.whiteCommon))
            titleBar.mainTitleTextView.textSize = AppConfig.TITLE_MAIN_TITLE_SIZE.toFloat()
            var type = ""
            when (StringUtil.getNotNullValue(socialType)) {
                ITEM_TYPE_SOCIAL_QUERY_GS -> {
                    type = "工伤"
                }
                ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER -> {
                    type = "养老"
                }
                ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK -> {
                    type = "失业"
                }
                ITEM_TYPE_SOCIAL_QUERY_BIRTH -> {
                    type = "生育"
                }

            }
            titleBar.setTitleMainText(type + "保险缴费记录")
        }

    }

    override fun getAdapter(): BaseQuickAdapter<SocialDetail, BaseViewHolder> {
        adapter = SocialRecordDetailAdapter()
        val headerView = View.inflate(mContext, R.layout.item_social_header, null)
        tvHeaderName = headerView.findViewById(R.id.tvHeaderName)
        tvHeaderIdCard = headerView.findViewById(R.id.tvHeaderIdCard)
        adapter!!.addHeaderView(headerView)
        return adapter!!
    }

    override fun loadPresenter() {
        presenter?.getSocialBaseInfo()
    }

    override fun createPresenter(): SocialDetailPresenter {
        return SocialDetailPresenter()
    }

    override fun showSocialInfo(socialBaseInfo: SocialBaseInfo?) {
        tvHeaderName?.text = StringUtil.getNotNullValueLine(socialBaseInfo?.name)
        tvHeaderIdCard?.text = StringUtil.getNotNullValueLine(socialBaseInfo?.idNo)
    }


}