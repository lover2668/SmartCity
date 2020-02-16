package com.tourcool.ui.parking

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.NetworkUtil
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
import kotlinx.android.synthetic.main.activity_parking_pay_fast.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月16日13:37
 * @Email: 971613168@qq.com
 */
class FastParkingActivity : BaseBlackTitleActivity(),View.OnClickListener {
    private var mEditTexts: MutableList<EditText>? = null

    private lateinit var kingKeyboard: KingKeyboard
    private var mOnCompleteListener: InputCompleteListener? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_parking_pay_fast
    }
    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText("快捷缴费")
    }
    override fun initView(savedInstanceState: Bundle?) {
        ivEntranceMineParking.setOnClickListener(this)
        ivEntrancePayParking.setOnClickListener(this)
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
        etPlantNumber1.requestFocus()
    }

    override fun loadData() {
        super.loadData()
        requestCarList()
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
                    mEditTexts!![index].setText(arrays[index].toString())
                }
                etPlantNumber6.setText("")
            }
            8 -> {
                for( index in 0 until  arrays.size -1 ){
                    mEditTexts!![index].setText(arrays[index].toString())
                }
                etPlantNumber6!!.setText(arrays[arrays.size -1].toString())
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
        }
    }


    fun focusNext(et: EditText?) {
        val index = mEditTexts!!.indexOf(et)
        if (index < mEditTexts!!.size - 1) {
            val nextEt = mEditTexts!![index + 1]
            nextEt.requestFocus()
        } else {
            if (mOnCompleteListener != null) {
                val editable = Editable.Factory.getInstance().newEditable("")
                for (editText in mEditTexts!!) {
                    editable.append(editText.text)
                }
                mOnCompleteListener!!.onComplete(editable, editable.toString())
            }
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivEntranceMineParking -> {
                skipMineParking()
            }
            R.id.ivEntrancePayParking -> {
                skipPayParking()
            }
            else -> {
            }
        }
    }


    private fun skipMineParking() {
        val intent = Intent()
        intent.setClass(mContext, MineParkingActivity::class.java)
        startActivity(intent)
    }


    private fun skipPayParking() {
        val intent = Intent()
        intent.setClass(mContext, QueryParkingFeeActivity::class.java)
        startActivity(intent)
    }

    private fun requestCarList() {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtil.show("请检查网络连接")
            return
        }
        ApiRepository.getInstance().requestCarList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<CarInfo>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<CarInfo>>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    /* if (entity.data.isEmpty()) {
                         mStatusManager!!.showEmptyLayout()
                     } else {
                         adapter!!.setNewData(entity.data)
                         mStatusManager!!.showSuccessLayout()
                     }*/
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
            return
        }
        setViewGone(llCarListContainer, carList.isNotEmpty())
        if(carList.isEmpty()){
            setViewVisible(tvFastSelectPlantNum, false)
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


}