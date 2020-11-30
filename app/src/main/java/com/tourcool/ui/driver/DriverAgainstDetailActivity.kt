package com.tourcool.ui.driver

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.adapter.driverlicense.DriverAgainstAdapter
import com.tourcool.bean.driver.DriverAgainstInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_driving_against_result.*

/**
 *@description : 违章详情
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月27日14:36
 * @Email: 971613168@qq.com
 */
class DriverAgainstDetailActivity : BaseTitleTransparentActivity() {
    private var adapter: DriverAgainstAdapter? = null
    private var carPlantNum: String? = null
    private var carFrameNum: String? = null
    private var engineNum: String? = null
    private var carType: String? = null
    private var tvAgainstTime: TextView? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_driving_against_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        carPlantNum = intent.getStringExtra(EXTRA_PLANT_NUM)
        carFrameNum = intent.getStringExtra(EXTRA_FRAME_NUM)
        engineNum = intent.getStringExtra(EXTRA_ENGINE_NUM)
        carType = intent.getStringExtra(EXTRA_CAR_TYPE)
        initAdapter()
    }
    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("查询结果")
    }

    companion object {
        const val EXTRA_PLANT_NUM = "EXTRA_PLANT_NUM"
        const val EXTRA_FRAME_NUM = "EXTRA_FRAME_NUM"
        const val EXTRA_ENGINE_NUM = "EXTRA_ENGINE_NUM"
        const val EXTRA_CAR_TYPE = "EXTRA_CAR_TYPE"
    }

    private fun initAdapter() {
        againstRecyclerView.layoutManager = LinearLayoutManager(mContext)
        val headerView = View.inflate(mContext, R.layout.item_header_driving_against_result, null)
        headerView.findViewById<TextView>(R.id.tvCarNum).text = carPlantNum
        headerView.findViewById<TextView>(R.id.tvCarFrameNum).text = carFrameNum
        headerView.findViewById<TextView>(R.id.tvEngineNum).text = engineNum
        tvAgainstTime = headerView.findViewById(R.id.tvAgainstTime)
        adapter = DriverAgainstAdapter()
        adapter!!.bindToRecyclerView(againstRecyclerView)
        adapter!!.addHeaderView(headerView)
        val emptyView = View.inflate(mContext,R.layout.common_status_layout_empty,null)
        adapter!!.emptyView = emptyView

    }

    override fun loadData() {
        super.loadData()
        requestDriverAgainstInfo()
    }

    private fun requestDriverAgainstInfo() {
        ApiRepository.getInstance().requestDriverAgainstInfo(carPlantNum, carFrameNum, engineNum, carType).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<DriverAgainstInfo>>() {
            override fun onRequestNext(entity: BaseResult<DriverAgainstInfo>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showDriverAgainstInfo(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }

        })
    }

    private fun showDriverAgainstInfo(detail: DriverAgainstInfo?) {
        if (detail == null) {
            return
        }
        val time = "(" + detail.lists!!.size.toString() + "次)"
        tvAgainstTime?.text = time
        adapter?.setNewData(detail.lists)
    }
}