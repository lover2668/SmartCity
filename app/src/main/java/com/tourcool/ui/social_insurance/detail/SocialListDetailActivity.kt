package com.tourcool.ui.social_insurance.detail

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.social.SocialRecordDetailAdapter
import com.tourcool.core.entity.social.SocialDetail
import com.tourcool.core.module.mvp.BaseMvpRefreshLoadActivity
import com.tourcool.smartcity.R

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月06日17:36
 * @Email: 971613168@qq.com
 */
class SocialListDetailActivity : BaseMvpRefreshLoadActivity<SocialDetailPresenter, SocialDetail>(), SocialDetailContract.View {

    private var adapter: SocialRecordDetailAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.frame_layout_title_refresh_recycler
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun loadData(page: Int) {
        presenter.getSocialDetailList(page)
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("XX保险缴费记录")
    }

    override fun getAdapter(): BaseQuickAdapter<SocialDetail, BaseViewHolder> {
        adapter = SocialRecordDetailAdapter()
        return adapter!!
    }

    override fun loadPresenter() {

    }

    override fun createPresenter(): SocialDetailPresenter {
        return SocialDetailPresenter()
    }
}