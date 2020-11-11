@file:Suppress("UNCHECKED_CAST")

package com.tourcool.ui.kitchen

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.tourcool.adapter.kitchen.KitchenDeviceAdapter
import com.tourcool.bean.kitchen.KitchenGroup
import com.tourcool.bean.kitchen.KitchenLiveInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_bright_kitchen_device_list.*
import kotlinx.android.synthetic.main.view_no_netwrok_layout.*
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月05日17:56
 * @Email: 971613168@qq.com
 */

class DeviceListActivity : BaseCommonTitleActivity(), OnRefreshListener,View.OnClickListener {
//    private var mStatusManager: StatusLayoutManager? = null
    private var tempList : MutableList<KitchenGroup> ?= null
    companion object {
        const val EXTRA_VIDEO_GROUP = "EXTRA_VIDEO_GROUP"
        const val EXTRA_GROUP_NAME = "EXTRA_GROUP_NAME"
        const val STRING_TITLE_ALL = "全部"
    }
    private var adapter: KitchenDeviceAdapter? = null
    private var groupName: String? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_bright_kitchen_device_list
    }

    override fun beforeInitView(savedInstanceState: Bundle?) {
        super.beforeInitView(savedInstanceState)
        groupName = intent.getStringExtra(EXTRA_GROUP_NAME)
       if(groupName == null){
           groupName = STRING_TITLE_ALL
       }
    }



    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText(groupName)
    }


    override fun initView(savedInstanceState: Bundle?) {
        llNoNet.setOnClickListener(this)
        llErrorLayout.setOnClickListener(this)
        llEmptyLayout.setOnClickListener(this)
        rvCommon.layoutManager = LinearLayoutManager(mContext)
        mSmartRefresh.setOnRefreshListener(this)
        val header = ClassicsHeader(mContext)
        mSmartRefresh.setEnableHeaderTranslationContent(true)
                .setEnableOverScrollDrag(true)
        mSmartRefresh.setRefreshHeader(header)
        adapter = KitchenDeviceAdapter()
        adapter!!.bindToRecyclerView(rvCommon)
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            for (index in 0 until adapter!!.data.size) {
                val kitchenVideo = adapter.data[index] as KitchenGroup
                kitchenVideo.isSelect = index == position
                if (index == position) {
                    groupName = (adapter.data[index] as KitchenGroup).groupName
                    showSelectTitleAndSet(groupName!!)
                }
            }
            adapter.notifyDataSetChanged()
            skipVideo(adapter.data[position] as KitchenGroup,groupName!!)
        }
        initTopClick()
//        setStatusManager()
        requestVideoList()
    }


    private fun requestVideoList() {
        if (!NetworkUtil.isConnected(mContext)) {
//            mStatusManager!!.showLoadingLayout()
            baseHandler.postDelayed({
                mSmartRefresh.finishRefresh(false)
                showNoNetLayout()
//                mStatusManager!!.showCustomLayout(R.layout.common_status_layout_no_network,R.id.llNoNetwok)
//                mStatusManager!!.showSuccessLayout()
            },300)
            return
        }
        ApiRepository.getInstance().requestKitchenList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<KitchenGroup>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<KitchenGroup>>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    loadDeviceList(entity.data)
                    mSmartRefresh.finishRefresh(300)
                    if (entity.data.isEmpty()) {
                     showEmptyLayout()
                    } else {
                    showSuccessLayout()
                    }
                } else {
                    ToastUtil.show(entity.errorMsg)
               showErrorLayout()
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
               showErrorLayout()
                ToastUtil.show(e!!.message)
                mSmartRefresh.finishRefresh(false)
//                mSmartRefresh.removeAllViews()
            }
        })
    }


    private fun loadDeviceList(list: MutableList<KitchenGroup>?) {
        if (list != null) {
            assignmentVideoInfo(list)
            tempList = ArrayList()
            val selectPosition = findSelectItemIndex(list)
            if (groupName != null) {
                showSelectTitleAndSet(groupName!!)
            }
            if (selectPosition >= 0) {
                for (index in 0 until list.size) {
                    list[index].isSelect = selectPosition == index
                }
                rvCommon!!.scrollToPosition(selectPosition)
            }
            //缓存全部数据
            tempList!!.addAll(list)
            adapter!!.setNewData(list)
        }
//        mStatusManager!!.showLoadingLayout()

    }


    private fun assignmentVideoInfo(list: MutableList<KitchenGroup>?){
        if(list == null){
            return
        }
        for (group in list){
            if(group.childrenList != null){
                for (kitchenLiveInfo in group.childrenList) {
                    if(kitchenLiveInfo != null){
                        kitchenLiveInfo.groupName = group.groupName
                        kitchenLiveInfo.groupId = group.groupId
                    }
                }
            }
        }
    }

    private fun skipVideo(data: KitchenGroup,groupName: String) {
        val intent = Intent()
        intent.setClass(mContext, VideoListActivity::class.java)
        val bundle = Bundle()
        data.groupName = groupName
        bundle.putSerializable(EXTRA_VIDEO_GROUP, data)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
           requestVideoList()
    }


    private fun findSelectItemIndex(list: MutableList<KitchenGroup>): Int {
        for (index in 0 until list.size) {
            if (list[index].groupName == groupName) {
                return index
            }
        }
        return -1
    }


    private fun showSelectTitleAndSet(group: String) {
        if(group == STRING_TITLE_ALL){
            tvGroupName.text = ""
        }else{
            tvGroupName.text = group
        }
        groupName = group
    }


    private fun setStatusManager() {
        /*val builder = StatusLayoutManager.Builder(llContainerView)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setEmptyLayout(R.layout.common_status_layout_empty)
                .setErrorLayout(R.layout.view_error_layout)
                .setErrorClickViewID(R.id.llErrorRequest)
                .setOnStatusChildClickListener(object : OnStatusChildClickListener {
                    override fun onEmptyChildClick(view: View) {

                    }

                    override fun onErrorChildClick(view: View) {
                        mStatusManager!!.showLoadingLayout()
                        requestVideoList()
                    }

                    override fun onCustomerChildClick(view: View) {
                        mStatusManager!!.showLoadingLayout()
                        requestVideoList()
                    }
                })

        mStatusManager = builder.build()*/
    }

    private fun initTopClick(){
        llGroupAll.setOnClickListener {
            val data = transformGroupListToGroup(tempList!!)
            if(data != null){
                skipVideo(data,STRING_TITLE_ALL)
            }
        }
    }

    private fun transformGroupListToGroup(list: MutableList<KitchenGroup>) : KitchenGroup ?{
        val  kitchenGroup = KitchenGroup()
        val tempList = ArrayList<KitchenLiveInfo>()
        if (list.isNullOrEmpty()) {
            return null
        }
        for (currentKitchenGroup in list) {
            if(currentKitchenGroup.childrenList != null){
                tempList.addAll(currentKitchenGroup.childrenList)
            }
        }
        assignmentVideoInfo(list)
        kitchenGroup.childrenList = tempList
        return kitchenGroup
    }


    private fun showErrorLayout(){
        llNoNet.visibility = View.GONE
        mSmartRefresh.visibility = View.INVISIBLE
        llEmptyLayout.visibility = View.GONE
        llErrorLayout.visibility = View.VISIBLE
    }

    private fun showEmptyLayout(){
        llNoNet.visibility = View.GONE
        mSmartRefresh.visibility = View.INVISIBLE
        llEmptyLayout.visibility = View.GONE
        llErrorLayout.visibility = View.VISIBLE
    }
    private fun showNoNetLayout(){
        llNoNet.visibility = View.VISIBLE
        mSmartRefresh.visibility = View.INVISIBLE
        llEmptyLayout.visibility = View.GONE
        llErrorLayout.visibility = View.GONE
        ToastUtil.show("网络未连接")
    }
    private fun showSuccessLayout(){
        llNoNet.visibility = View.GONE
        mSmartRefresh.visibility = View.VISIBLE
        llEmptyLayout.visibility = View.GONE
        llErrorLayout.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llNoNet -> {
                requestVideoList()
            }
            R.id.llEmptyLayout -> {
                requestVideoList()
            }
            R.id.llErrorLayout -> {
                requestVideoList()
            }
            else -> {
            }
        }
    }
}