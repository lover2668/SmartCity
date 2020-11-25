package com.tourcool.ui.garbage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.bean.garbage.GarbageHotKey
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseTitleTransparentActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_garbage_query_enter.*


/**
 *@description : 垃圾分类
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月25日20:24
 * @Email: 971613168@qq.com
 */
class GarbageQueryActivity : BaseTitleTransparentActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_garbage_query_enter
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("垃圾分类")
    }

    override fun initView(savedInstanceState: Bundle?) {
        etGarbageName.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSkip()
                return@OnEditorActionListener true
            }
            false
        })
        ivSearch.setOnClickListener {
            doSkip()
        }
        listenInput(etGarbageName, ivClearInput)
        requestGarbageHotWord()
    }


    private fun doSkip() {
        if (TextUtils.isEmpty(etGarbageName.text.toString())) {
            ToastUtil.show("请先输入垃圾名称")
            return
        }
        val intent = Intent()
        intent.setClass(mContext, GarbageDetailActivity::class.java)
        intent.putExtra(EXTRA_GARBAGE_NAME, etGarbageName.text.toString())
        startActivity(intent)
        etGarbageName.setText("")
    }

    companion object {
        const val EXTRA_GARBAGE_NAME = "EXTRA_GARBAGE_NAME"
    }

    private fun listenInput(editText: EditText, imageView: ImageView) {
        setViewVisible(imageView, !TextUtils.isEmpty(editText.text.toString()))
        imageView.setOnClickListener { v: View? -> editText.setText("") }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                setViewVisible(imageView, s.length != 0)
            }
        })
    }

    private fun requestGarbageHotWord() {
        ApiRepository.getInstance().requestGarbageHotWord().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<GarbageHotKey>>>() {
            override fun onRequestNext(entity: BaseResult<MutableList<GarbageHotKey>>?) {
                if (entity == null) {
                    return
                }
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    showGarbageHotKey(entity.data)
                } else {
                    ToastUtil.show(entity.errorMsg)
                }
            }
        })
    }

    //    list: MutableList<GarbageHotKey>?
    private fun showGarbageHotKey(list: MutableList<GarbageHotKey>?) {
        ToastUtil.show("热词数量：" + list!!.size)
    }

}