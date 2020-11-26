package com.tourcool.ui.calender

import android.os.Bundle
import android.text.TextUtils
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.canlender.YellowCalendarDetail
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_yellow_canlender.*
import java.util.*


/**
 *@description : 黄历
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月26日16:12
 * @Email: 971613168@qq.com
 */
class YellowCalenderDetailActivity : BaseTitleTransparentActivity() {
    private var pvTime: TimePickerView? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_yellow_canlender
    }

    override fun initView(savedInstanceState: Bundle?) {
        initDatePicker()
        llSelectDate.setOnClickListener {
            pvTime?.show()
        }
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("查黄历")
    }

    override fun loadData() {
        super.loadData()
        val date = Date(System.currentTimeMillis())
        tvSelectDate.text = StringUtil.dateFormat(date)
        requestYellowCalender(tvSelectDate.text.toString())
    }

    private fun requestYellowCalender(date: String?) {
        if (TextUtils.isEmpty(date)) {
            ToastUtil.show("请先选择日期")
            return
        }
        ApiRepository.getInstance().requestYellowCalendar(date).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<YellowCalendarDetail>>() {
            override fun onRequestNext(entity: BaseResult<YellowCalendarDetail>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showYellowCalender(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    private fun showYellowCalender(detail: YellowCalendarDetail?) {
        if (detail == null) {
            ToastUtil.show("服务器数据异常")
            return
        }
        tvLongLi.text = StringUtil.getNotNullValue(detail.yinli)
        tvYangLi.text = StringUtil.getNotNullValue(detail.yangli)
        tvWuxing.text = StringUtil.getNotNullValue(detail.wuxing)
        tvYi.text = StringUtil.getNotNullValue(detail.yi)
        tvBaiji.text = StringUtil.getNotNullValue(detail.baiji)
        tvXiongShen.text = StringUtil.getNotNullValue(detail.xiongshen)
        tvChongSha.text = StringUtil.getNotNullValue(detail.chongsha)
        tvJi.text = StringUtil.getNotNullValue(detail.ji)
        tvJiShen.text = StringUtil.getNotNullValue(detail.jishen)
    }

    private fun initDatePicker() {
        val selectedDate: Calendar = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        val endDate: Calendar = Calendar.getInstance()

        //正确设置方式 原因：注意事项有说明
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(1949, 0, 1)
        endDate.set(2100, 11, 31)
        pvTime = TimePickerBuilder(this) { date, v ->
            //选中事件回调
            tvSelectDate.text = StringUtil.dateFormat(date)
            requestYellowCalender(tvSelectDate.text.toString())
        }
                .setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
                .setCancelText("取消") //取消按钮文字
                .setSubmitText("确认") //确认按钮文字
                .setTitleSize(20) //标题文字大小
                .setTitleText("选择日期") //标题文字
                .setOutSideCancelable(true) //点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true) //是否循环滚动
                .setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate) //起始终止年月日设定
                //默认设置为年月日时分秒
                .setLabel("年", "月", "日", null, null, null)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false) //是否显示为对话框样式
                .build()
    }
}