package com.tourcool.ui.driver

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.core.widget.keyboard.KingKeyboard
import com.tourcool.core.widget.keyboard.KingKeyboard.KeyboardType.LICENSE_PLATE_PROVINCE
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import kotlinx.android.synthetic.main.activity_driving_against_query.*
import java.util.*

/**
 *@description : 违章查询
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月26日20:12
 * @Email: 971613168@qq.com
 */
class DriverIllegalQueryActivity : BaseCommonTitleActivity() {
    private var pvCarTypeOptions: OptionsPickerView<CarType>? = null
    private lateinit var kingKeyboard: KingKeyboard
    private val carTypeList = ArrayList<CarType>()
    private var selectCarType: CarType? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_driving_against_query
    }

    override fun initView(savedInstanceState: Bundle?) {
        initKeyboard()
        initCarTypeSelect()
        tvQuery.setOnClickListener {
            doQuery()
        }
        tvCarType.setOnClickListener {
            pvCarTypeOptions?.show()
        }
        llContentView.setOnClickListener {
            hideKeyBoard()
        }
    }

    private fun doQuery() {
        if (TextUtils.isEmpty(etDriverLicense.text.toString())) {
            ToastUtil.show("请输入车牌号码")
            return
        }
        if (TextUtils.isEmpty(etCarFrameNum.text.toString())) {
            ToastUtil.show("请输入车架号")
            return
        }
        if (TextUtils.isEmpty(etEngineNum.text.toString())) {
            ToastUtil.show("请输入发动机号")
            return
        }
        if (TextUtils.isEmpty(tvCarType.text.toString())) {
            ToastUtil.show("请选择车辆类型")
            return
        }
        if (selectCarType == null) {
            ToastUtil.show("未获取到车辆类型")
            return
        }
        kingKeyboard.hideKeyboard()
        skipAgainstDetail()
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("违章查询")
    }


    private fun skipAgainstDetail() {
        val intent = Intent()
        intent.putExtra(DriverAgainstDetailActivity.EXTRA_PLANT_NUM, etDriverLicense.text.toString())
        intent.putExtra(DriverAgainstDetailActivity.EXTRA_FRAME_NUM, etCarFrameNum.text.toString())
        intent.putExtra(DriverAgainstDetailActivity.EXTRA_ENGINE_NUM, etEngineNum.text.toString())
        intent.putExtra(DriverAgainstDetailActivity.EXTRA_CAR_TYPE, selectCarType!!.type)
        intent.setClass(mContext, DriverAgainstDetailActivity::class.java)
        startActivity(intent)
    }

    private fun initCarTypeSelect() {
        // 不联动的多级选项
        carTypeList.add(CarType("02", "小型汽车"))
        carTypeList.add(CarType("01", "大型汽车"))
        carTypeList.add(CarType("52", "新能源小型车"))
        carTypeList.add(CarType("51", "新能源大型车"))
        pvCarTypeOptions = OptionsPickerBuilder(this) { options1, options2, options3, v ->
            selectCarType = carTypeList[options1]
            showCarType(selectCarType)
        }.setContentTextSize(18).setCyclic(true, false, false).setItemVisibleCount(5).build()

        pvCarTypeOptions!!.setNPicker(carTypeList, null, null)
        pvCarTypeOptions!!.setSelectOptions(0)
    }

    private fun showCarType(carType: CarType?) {
        tvCarType.text = carType?.typeDesc
    }

    private fun initKeyboard() {
        kingKeyboard = KingKeyboard(this, keyboardParent)
        //然后将EditText注册到KingKeyboard即可
        kingKeyboard.register(etDriverLicense, KingKeyboard.KeyboardType.LICENSE_PLATE)
    }

    override fun onResume() {
        super.onResume()
        kingKeyboard.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        kingKeyboard.onDestroy()
    }

    override fun onBackPressed() {
        if (kingKeyboard.isShow()) {
            kingKeyboard.hideKeyboard()
            return
        }
        super.onBackPressed()
    }

    private fun hideKeyBoard() {
        if (kingKeyboard.isShow()) {
            kingKeyboard.hideKeyboard()
        }
    }

    private fun listenPlantInput() {
        etDriverLicense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length <= 1) {
                    kingKeyboard.switchKeyboard(LICENSE_PLATE_PROVINCE)
                } else {
                    kingKeyboard.switchKeyboard()
                }
            }

        })
    }
}