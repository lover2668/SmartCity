package com.tourcool.ui.parking

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.NetworkUtil
import com.frame.library.core.util.ToastUtil
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.listener.InputCompleteListener
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.core.widget.keyboard.KingKeyboard
import com.tourcool.core.widget.vclayout.Utils
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_parking_car_bind.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月14日21:59
 * @Email: 971613168@qq.com
 */
class AddCarActivity : BaseCommonTitleActivity(), View.OnClickListener {
    private var mEditTexts: MutableList<EditText>? = null
//    private var mStatusManager: StatusLayoutManager? = null
    private lateinit var kingKeyboard: KingKeyboard
    private var mOnCompleteListener: InputCompleteListener? = null

    companion object {
        const val CAR_TYPE_SMALL = 1
        const val CAR_TYPE_LARGE = 2
    }
    override fun getContentLayout(): Int {
        return R.layout.activity_parking_car_bind
    }

    override fun initView(savedInstanceState: Bundle?) {
        //初始化KingKeyboard
        mEditTexts = ArrayList()
        kingKeyboard = KingKeyboard(this, keyboardParent)
        tvConfirm.setOnClickListener(this)
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

    override fun onClick(v: View?) {

        when (v!!.id) {
            /*   R.id.tvTest -> {
//                    mPopupKeyboard!!.show(mContext)
                   kingKeyboard.hideKeyboard()
               }*/
            R.id.tvConfirm->{
                doAddCar()
            }
            else -> {
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


    private fun doAddCar(){
        if(!checkPlantNum()){
            ToastUtil.show("请输入正确的车牌号")
            return
        }
        kingKeyboard.hideKeyboard()
        requestBindCar(  getPlantNum(mEditTexts!!) + etPlantNumber6.text, getCarType())
    }



    private fun checkPlantNum() : Boolean{
       for(editText in mEditTexts!!){
           if(editText.text.isNotEmpty()){
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
            stringBuffer.append(editText.text)
        }
        return( stringBuffer.toString()).toUpperCase()
    }

    private fun getCarType() : Int{
        return if(radioCarSmall.isChecked){
            CAR_TYPE_SMALL
        }else{
            CAR_TYPE_LARGE
        }
    }

    private fun requestBindCar(carNum : String, carType : Int) {
        if(!NetworkUtil.isConnected(mContext)){
            ToastUtil.show("网络未连接")
            return
        }
        ApiRepository.getInstance().requestAddCar(carNum, carType).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onRequestNext(entity: BaseResult<*>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("绑定成功")
                    setResult(Activity.RESULT_OK)
                    finish()
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

   /* private fun setStatusManager() {
        val builder = StatusLayoutManager.Builder(llContainer)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setEmptyLayout(R.layout.layout_car_empty)
                .setErrorLayout(R.layout.view_error_layout)
                .setErrorClickViewID(R.id.llErrorRequest)
                .setEmptyClickViewID(R.id.tvAddCar)
                .setDefaultEmptyText(com.tourcool.library.frame.demo.R.string.fast_multi_empty)
                .setDefaultLoadingText(com.tourcool.library.frame.demo.R.string.fast_multi_loading)
                .setOnStatusChildClickListener(object : OnStatusChildClickListener {
                    override fun onEmptyChildClick(view: View) {

                    }

                    override fun onErrorChildClick(view: View) {
                        *//*     if (mIFastRefreshLoadView.getErrorClickListener() != null) {
                                 mIFastRefreshLoadView.getErrorClickListener().onClick(view)
                                 return
                             }*//*
                        mStatusManager!!.showLoadingLayout()

                    }

                    override fun onCustomerChildClick(view: View) {
                        *//*  if (mIFastRefreshLoadView.getCustomerClickListener() != null) {
                              mIFastRefreshLoadView.getCustomerClickListener().onClick(view)
                              return
                          }*//*
                        mStatusManager!!.showLoadingLayout()

                    }
                })
        *//*    if (mManager != null && mManager.getMultiStatusView() != null) {
                mManager.getMultiStatusView().setMultiStatusView(builder, mIFastRefreshLoadView)
            }*//*
//        mIFastRefreshLoadView.setMultiStatusView(builder)
        mStatusManager = builder.build()
        mStatusManager!!.showLoadingLayout()
    }*/
}