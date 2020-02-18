package com.tourcool.ui.parking

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.NetworkUtil
import com.frame.library.core.util.StringUtil
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.parking.CarInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.listener.InputCompleteListener
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.widget.keyboard.KingKeyboard
import com.tourcool.core.widget.vclayout.Utils
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseBlackTitleActivity
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_parking_car_bind.*
import kotlinx.android.synthetic.main.activity_parking_pay_fast.*
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.*
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.etPlantLetter
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.etPlantName
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.etPlantNumber1
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.etPlantNumber2
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.etPlantNumber3
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.etPlantNumber4
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.etPlantNumber5
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.etPlantNumber6
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.keyboardParent
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.llCarListContainer
import kotlinx.android.synthetic.main.activity_parking_query_fee_pay.tvQueryFee

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月15日21:42
 * @Email: 971613168@qq.com
 */
class QueryParkingFeeActivity : BaseBlackTitleActivity(),View.OnClickListener {
    private var mEditTexts: MutableList<EditText>? = null

    private lateinit var kingKeyboard: KingKeyboard
    private var mOnCompleteListener: InputCompleteListener? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_parking_query_fee_pay
    }
    companion object{
        const val EXTRA_PLANT_NUM = "EXTRA_PLANT_NUM"
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText("欠费缴纳")
    }
    override fun initView(savedInstanceState: Bundle?) {
//        llCarListContainer
        //初始化KingKeyboard
        tvQueryFee.setOnClickListener(this)
        mEditTexts = ArrayList()
        kingKeyboard = KingKeyboard(this, keyboardParent)
        //然后将EditText注册到KingKeyboard即可
        kingKeyboard.register(etPlantName, KingKeyboard.KeyboardType.LICENSE_PLATE)
        kingKeyboard.register(etPlantLetter, KingKeyboard.KeyboardType.LICENSE_PLATE_MODE_CHANGE)
        kingKeyboard.register(etPlantNumber1, KingKeyboard.KeyboardType.LICENSE_PLATE_MODE_CHANGE)
        kingKeyboard.register(etPlantNumber2, KingKeyboard.KeyboardType.LICENSE_PLATE_MODE_CHANGE)
        kingKeyboard.register(etPlantNumber3, KingKeyboard.KeyboardType.LICENSE_PLATE_MODE_CHANGE)
        kingKeyboard.register(etPlantNumber4, KingKeyboard.KeyboardType.LICENSE_PLATE_MODE_CHANGE)
        kingKeyboard.register(etPlantNumber5, KingKeyboard.KeyboardType.LICENSE_PLATE_MODE_CHANGE)
        kingKeyboard.setKeyboardCustom(R.xml.keyboard_custom)
        setupEditText(etPlantName)
        setupEditText(etPlantLetter)
        setupEditText(etPlantNumber1)
        setupEditText(etPlantNumber2)
        setupEditText(etPlantNumber3)
        setupEditText(etPlantNumber4)
        setupEditText(etPlantNumber5)
        kingKeyboard.setVibrationEffectEnabled(true)
        //设置"用户名"提示文字的大小
        val s = SpannableString("新能源")
        val textSize = AbsoluteSizeSpan(9, true)
        s.setSpan(textSize, 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        etPlantNumber6.hint = s
        etPlantNumber1.requestFocus()
    }

    override fun loadData() {
        super.loadData()
        requestCarList()

    }

    private fun requestCarList() {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtil.show("请检查网络连接")
            return
        }
        ApiRepository.getInstance().requestCarList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<CarInfo>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<CarInfo>>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    loadFastQueryByCarList(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                ToastUtil.show(e!!.message)
            }
        })
    }


    private fun loadFastQueryByCarList(carList: MutableList<CarInfo>?) {
        if (carList == null) {
            setViewGone(llFastQuery, false)
            return
        }
        setViewGone(llFastQuery, carList.isNotEmpty())
        if(carList.isEmpty()){
            llCarListContainer.removeAllViews()
            return
        }
        for (carInfo in carList) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.layout_textview_car_info,null)
            val textView = view.findViewById<TextView>(R.id.tvCarInfo)
            textView.text = carInfo.carNum
            llCarListContainer.addView(view)
            textView.setOnClickListener(View.OnClickListener {
                fillPlantNum( textView.text.toString())
                        kingKeyboard.hideKeyboard()
            })
        }
    }



    private fun requestLastPayPlantNum() {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtil.show("请检查网络连接")
            return
        }
        ApiRepository.getInstance().requestLastPayPlantNum().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<String>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<String>>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    /* if (entity.data.isEmpty()) {
                         mStatusManager!!.showEmptyLayout()
                     } else {
                         adapter!!.setNewData(entity.data)
                         mStatusManager!!.showSuccessLayout()
                     }*/
                    loadFastQueryByCarPlantNum(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                ToastUtil.show(e!!.message)
            }
        })
    }

    private fun loadFastQueryByCarPlantNum(numberList: MutableList<String>?) {
        if (numberList == null) {
            setViewGone(llFastQuery, false)
            return
        }
        setViewGone(llFastQuery, numberList.isNotEmpty())
        llCarListContainer.removeAllViews()
        if(numberList.isEmpty()){
            return
        }
        for (info in numberList) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.layout_textview_car_info,null)
            val textView = view.findViewById<TextView>(R.id.tvCarInfo)
            textView.text =info
            llCarListContainer.addView(view)
            textView.setOnClickListener(View.OnClickListener {
                fillPlantNum( textView.text.toString())
                kingKeyboard.hideKeyboard()
            })
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvQueryFee -> {
                skipPayParkingRecord()
            }
            else -> {
            }
        }
    }

    private fun skipPayParkingRecord() {
        if(!checkPlantNum() ){
            ToastUtil.show("请输入完整的车牌号")
            return
        }
        var lastNum = etPlantNumber6.text.toString()
        lastNum = lastNum.toUpperCase()
        val num = getPlantNum(mEditTexts!!) +lastNum
        if(!StringUtil.isCarnumberNo(num) ){
            ToastUtil.show("请输入正确的车牌号")
            return
        }
        val intent = Intent()
        intent.putExtra(EXTRA_PLANT_NUM,getPlantNum(mEditTexts!!) +etPlantNumber6.text.toString().toUpperCase())
        intent.setClass(mContext, PayFeeRecordActivity::class.java)
        startActivity(intent)
    }

    private fun requestQueryParkingRecord(carNum: String ?){
        ApiRepository.getInstance().requestQueryParkingRecord(carNum).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onRequestNext(entity: BaseResult<*>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    /* if (entity.data.isEmpty()) {
                         mStatusManager!!.showEmptyLayout()
                     } else {
                         adapter!!.setNewData(entity.data)
                         mStatusManager!!.showSuccessLayout()
                     }*/

                } else {
                    ToastUtil.show(entity.errorMsg)
                }

            }

            override fun onRequestError(e: Throwable?) {
                super.onRequestError(e)
                ToastUtil.show(e!!.message)
            }
        })
    }


    private fun doQueryParkingFee(){

    }


    private fun fillPlantNum(plantNum : String ?){
        if(plantNum.isNullOrEmpty()){
            return
        }
      val arrays =   plantNum.toCharArray()
        if(mEditTexts!!.size != 7){
            return
        }
        when (arrays.size) {
            7 -> {
                for( index in arrays.indices){
                    val currentEditText =  mEditTexts!![index]
                    currentEditText.setText(arrays[index].toString())
                    currentEditText.setSelection(currentEditText.text.toString().length)
                }
                etPlantNumber6.setText("")
            }
            8 -> {
                for( index in 0 until  arrays.size -1 ){
                    mEditTexts!![index].setText(arrays[index].toString())
                }
                etPlantNumber6!!.setText(arrays[arrays.size -1].toString())
                etPlantNumber6.setSelection(etPlantNumber6.text.toString().length)
            }
            else -> {
                ToastUtil.show("车牌号有误")
            }
        }
    }

    private fun focusLast(et: EditText?) {
        val index = mEditTexts!!.indexOf(et)
        if (index != 0) {
            val lastEt = mEditTexts!![index - 1]
            lastEt.requestFocus()
            lastEt.setSelection(lastEt.text.toString().length)
        }
    }


    fun focusNext(et: EditText?) {
        val index = mEditTexts!!.indexOf(et)
        if (index < mEditTexts!!.size - 1) {
            val nextEt = mEditTexts!![index + 1]
            nextEt.requestFocus()
            nextEt.setSelection(nextEt.text.toString().length)
        } else {
            if (mOnCompleteListener != null) {
                val editable = Editable.Factory.getInstance().newEditable("")
                for (editText in mEditTexts!!) {
                    editable.append(editText.text)
                }
                mOnCompleteListener!!.onComplete(editable, editable.toString())
            }
            et!!.setSelection(et.text.toString().length)
        }
    }

    /**
     * 在Activity或Fragment的生命周期中调用对应的方法
     */
    override fun onResume() {
        super.onResume()
        kingKeyboard.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        kingKeyboard.onDestroy()
    }

    private inner class InnerTextWatcher(var innerEditText: EditText) : TextWatcher {
        var maxLength: Int = Utils.getMaxLength(innerEditText)
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            val count = s.length
            if (maxLength == 0) {
                return
            }
            if (count >= maxLength) {
                focusNext(innerEditText)
            } else if (count == 0) {
                focusLast(innerEditText);
            }
        }

    }



    private fun setupEditText(editText: EditText) {
        mEditTexts!!.add(editText)
        editText.addTextChangedListener(InnerTextWatcher(editText))

    }


    override fun onBackPressed() {
        if (kingKeyboard.isShow()) {
            kingKeyboard.hideKeyboard()
            return
        }
        super.onBackPressed()
    }

    private fun checkPlantNum() : Boolean{
        for(editText in mEditTexts!!){
            if(editText.text.toString().isNotEmpty()){
                continue
            }else{
                return false
            }
        }
        return true
    }


    private fun getPlantNum(editTextList: MutableList<EditText>) : String{
        var stringBuffer = StringBuffer()
        for(editText in editTextList){
            stringBuffer.append(editText.text.toString())
        }
        return( stringBuffer.toString()).toUpperCase()
    }

}