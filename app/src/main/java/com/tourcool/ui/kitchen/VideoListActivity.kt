package com.tourcool.ui.kitchen

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.frame.library.core.log.TourCooLogUtil
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.NetworkUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.tourcool.adapter.kitchen.KitchenVideoAdapter
import com.tourcool.bean.kitchen.KitchenLiveInfo
import com.tourcool.bean.kitchen.KitchenGroup
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.kitchen.DeviceListActivity.Companion.EXTRA_GROUP_NAME
import com.tourcool.ui.kitchen.DeviceListActivity.Companion.STRING_TITLE_ALL
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_bright_kitchen_video_list.*
import kotlinx.android.synthetic.main.activity_bright_kitchen_video_list.mSmartRefresh
import kotlinx.android.synthetic.main.activity_bright_kitchen_video_list.rvCommon
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月05日16:35
 * @Email: 971613168@qq.com
 */
class VideoListActivity : BaseCommonTitleActivity(), OnRefreshListener {
    private var mStatusManager: StatusLayoutManager? = null
    private var videoList: ArrayList<KitchenLiveInfo>? = null
    private val tempList: ArrayList<KitchenLiveInfo> = ArrayList()
    private var adapter: KitchenVideoAdapter? = null
    private var data: KitchenGroup? = null
    private var groupName: String? = null
    private var isLayoutTypeBig = true
    override fun getContentLayout(): Int {
        return R.layout.activity_bright_kitchen_video_list
    }

    companion object {
        const val EXTRA_POSITION = "EXTRA_POSITION"
        const val EXTRA_LIVE_LIST = "EXTRA_LIVE_LIST"
        const val STATUS_ONLINE = 1
        const val LAYOUT_TYPE_BIG = 1
        const val LAYOUT_TYPE_SMALL = 0
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        loadIntent()
        setAdapterAndInitItemClick()
        setTitleBar(mTitleBar)
        TourCooLogUtil.i(TAG, "执行了onNewIntent")
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (groupName == null) {
            groupName = "全部"
        }
        rvCommon.layoutManager = LinearLayoutManager(mContext)
        adapter = KitchenVideoAdapter()
        mSmartRefresh.setRefreshHeader(ClassicsHeader(mContext))
        mSmartRefresh.setOnRefreshListener(this)
        adapter!!.bindToRecyclerView(rvCommon)
        setStatusManager()
        setAdapterAndInitItemClick()
    }

    override fun loadData() {
        super.loadData()
        if (data == null && tempList.isEmpty()) {
            requestVideoList()
        } else {
            //从缓存获取
            loadVideoListByCache(tempList)
        }
    }

    override fun beforeInitView(savedInstanceState: Bundle?) {
        super.beforeInitView(savedInstanceState)
        loadIntent()
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        if (titleBar == null) {
            return
        }
        titleBar.setTitleMainText(groupName)
        initTitleBarClick(titleBar)
    }

    private fun requestVideoList() {
        if (!NetworkUtil.isConnected(mContext)) {
            mStatusManager!!.showLoadingLayout()
            baseHandler.postDelayed({
                mStatusManager!!.showCustomLayout(R.layout.common_status_layout_no_network,R.id.llNoNetwok)
            },300)
            return
        }
        ApiRepository.getInstance().requestKitchenList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<KitchenGroup>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<KitchenGroup>>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    loadVideoList(entity.data)
                    loadItemClickListener()
                    if (entity.data.isEmpty()) {
                        mStatusManager!!.showEmptyLayout()
                    } else {
                        mStatusManager!!.showSuccessLayout()
                    }
                } else {
                    ToastUtil.show(entity.errorMsg)
                    mSmartRefresh.finishRefresh()
                    mStatusManager!!.showErrorLayout()
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                mSmartRefresh.finishRefresh()
                mStatusManager!!.showErrorLayout()
                ToastUtil.show(e!!.message)
            }
        })
    }

    private fun loadVideoList(list: MutableList<KitchenGroup>) {
        videoList = ArrayList()
        mSmartRefresh.finishRefresh(true)
        //先赋值groupId和name
        assignmentVideoInfo(list)
        for (kitchenVideo in list) {
            videoList!!.addAll(kitchenVideo.childrenList)
        }
        tempList.clear()
        tempList.addAll(videoList!!)
        setLayoutStatus(videoList)
        if(groupName.isNullOrEmpty() ||  groupName == STRING_TITLE_ALL){
            //就不过滤数据了 直接显示全部
            LogUtils.i("VideoListActivity","不过滤了")
        }else{
            val id = findCurrentVideoGroupId(videoList, groupName)
            LogUtils.i("VideoListActivity","---------->"+id)
            videoList = getFilterVideoList(videoList!!,id)
        }
        adapter!!.setNewData(videoList)
        scrollToFirstItem()
        changeLayout(isLayoutTypeBig)
    }

    private fun loadVideoListByCache(list: MutableList<KitchenLiveInfo>) {
        videoList = ArrayList()
        videoList!!.addAll(list)
        setLayoutStatus(videoList)
        adapter!!.setNewData(videoList)
        scrollToFirstItem()
    }

    private fun skipVideoLive(position: Int) {
        val videoInfo = if (data != null) {
            data!!.childrenList!![position]
        } else {
            videoList!![position]
        }
        if (videoInfo!!.onlineStatus != STATUS_ONLINE) {
            ToastUtil.show("当前设备不在线")
            return
        }
        val intent = Intent()
        intent.setClass(mContext, LivePlayActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_LIVE_LIST, videoList!!)
        bundle.putInt(EXTRA_POSITION, position)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    private fun changeLayout(isTypeTwo: Boolean) {
        if (videoList == null) {
            return
        }
        for (live in videoList!!) {
            if (isTypeTwo) {
                mTitleBar.setRightTextDrawable(R.mipmap.ic_video_list_type1)
                live.layoutType = 1
            } else {
                mTitleBar.setRightTextDrawable(R.mipmap.ic_video_list_type2)
                live.layoutType = 0
            }
        }
        adapter!!.notifyDataSetChanged()
    }


    private fun skipDeviceList() {
        val intent = Intent()
        if (data != null) {
            intent.putExtra(EXTRA_GROUP_NAME, data!!.groupName)
        }
        intent.setClass(mContext, DeviceListActivity::class.java)
        startActivity(intent)
    }

    private fun loadItemClickListener() {
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> skipVideoLive(position) }
    }

    private fun loadIntent() {
        if (intent != null && intent.extras != null) {
            data = intent!!.extras!!.getSerializable(DeviceListActivity.EXTRA_VIDEO_GROUP) as KitchenGroup
            if (data != null && data!!.childrenList != null) {
                setLayoutStatus(data!!.childrenList)
                groupName = if (data!!.groupName == null) {
                    "全部"
                } else {
                    data!!.groupName
                }
            }
        }
    }

    private fun setAdapterAndInitItemClick() {
        if (data != null) {
            videoList = data!!.childrenList as ArrayList<KitchenLiveInfo>?
            setLayoutStatus(videoList)
            adapter!!.setNewData(videoList)
            scrollToFirstItem()
            adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> skipVideoLive(position) }
        }
    }

    private fun initTitleBarClick(titleBar: TitleBarView?) {
        val linearLayout = titleBar!!.getLinearLayout(Gravity.END)
        titleBar.setOnRightTextClickListener(View.OnClickListener {
            isLayoutTypeBig = !isLayoutTypeBig
            changeLayout(isLayoutTypeBig)
        })
        if (linearLayout.childCount == 2) {
            return
        }
        val rootView = LayoutInflater.from(this).inflate(R.layout.view_image, linearLayout, false)
        val imageView = rootView.findViewById(R.id.ivContent) as ImageView
        imageView.setImageResource(R.mipmap.ic_menu_main)
        linearLayout.addView(rootView)
        rootView.setOnClickListener {
            skipDeviceList()
        }
    }


    private fun setLayoutStatus(list: MutableList<KitchenLiveInfo>?) {
        if (list == null) {
            return
        }
        for (kitchenLiveInfo in list) {
            if (isLayoutTypeBig) {
                kitchenLiveInfo.layoutType = LAYOUT_TYPE_BIG
            } else {
                kitchenLiveInfo.layoutType = LAYOUT_TYPE_SMALL
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        requestVideoList()
    }

    private fun findCurrentVideoGroupId(list: MutableList<KitchenLiveInfo>?, groupName: String?): String {
        if (groupName == null || groupName == STRING_TITLE_ALL) {
            return ""
        }
        TourCooLogUtil.i("VideoListActivity", "找到的id--->" + groupName)
        if (list == null || groupName == STRING_TITLE_ALL) {
            return ""
        } else {
            for (group in list) {
                if (group.groupName == (groupName)) {
                    return group.groupId
                }
            }
        }
        return ""
    }


   private fun getFilterVideoList(list: MutableList<KitchenLiveInfo> ?,groupId : String): ArrayList<KitchenLiveInfo> {
       val filterList  = ArrayList<KitchenLiveInfo>()
       if(list == null){
           return filterList
       }
       for (kitchenLiveInfo in list) {
           if(groupId.isNullOrEmpty()){
               filterList.addAll(tempList)
               return filterList
           }
           if(groupId ==kitchenLiveInfo.groupId ){
               filterList.add(kitchenLiveInfo)
           }
       }
       return filterList
   }


    private fun assignmentVideoInfo(list: MutableList<KitchenGroup>?) {
        if (list == null) {
            return
        }
        for (group in list) {
            if (group.childrenList != null) {
                for (kitchenLiveInfo in group.childrenList) {
                    if (kitchenLiveInfo != null) {
                        kitchenLiveInfo.groupName = group.groupName
                        kitchenLiveInfo.groupId = group.groupId
                    }
                }
            }
        }
    }


    private fun setStatusManager() {
        val contentView: View? = rlContainer ?: return
        val builder = StatusLayoutManager.Builder(contentView!!)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setEmptyLayout(R.layout.common_status_layout_empty)
                .setErrorLayout(R.layout.view_error_layout)
                .setErrorClickViewID(R.id.llErrorRequest)
                .setDefaultEmptyText(com.tourcool.library.frame.demo.R.string.fast_multi_empty)
                .setDefaultLoadingText(com.tourcool.library.frame.demo.R.string.fast_multi_loading)
                .setOnStatusChildClickListener(object : OnStatusChildClickListener {
                    override fun onEmptyChildClick(view: View) {
                        /*   if (mIFastRefreshLoadView.getEmptyClickListener() != null) {
                               mIFastRefreshLoadView.getEmptyClickListener().onClick(view)
                               return
                           }
                           mStatusManager.showLoadingLayout()
                           mIFastRefreshLoadView.onRefresh(mRefreshLayout)*/
                    }

                    override fun onErrorChildClick(view: View) {
                        /*     if (mIFastRefreshLoadView.getErrorClickListener() != null) {
                                 mIFastRefreshLoadView.getErrorClickListener().onClick(view)
                                 return
                             }*/
                        mStatusManager!!.showLoadingLayout()
                        requestVideoList()
                    }

                    override fun onCustomerChildClick(view: View) {
                        /*  if (mIFastRefreshLoadView.getCustomerClickListener() != null) {
                              mIFastRefreshLoadView.getCustomerClickListener().onClick(view)
                              return
                          }*/
                        mStatusManager!!.showLoadingLayout()
                        requestVideoList()
                    }
                })
        /*    if (mManager != null && mManager.getMultiStatusView() != null) {
                mManager.getMultiStatusView().setMultiStatusView(builder, mIFastRefreshLoadView)
            }*/
//        mIFastRefreshLoadView.setMultiStatusView(builder)
        mStatusManager = builder.build()
        mStatusManager!!.showLoadingLayout()
    }

    private fun scrollToFirstItem(){
        if(adapter!!.data.isNotEmpty()){
            rvCommon.scrollToPosition(0)
        }
    }


}