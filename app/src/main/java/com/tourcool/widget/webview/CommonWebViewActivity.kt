package com.tourcool.widget.webview

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import com.frame.library.core.util.StringUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.tourcool.smartcity.R
import com.tourcool.widget.webview.WebViewConstant.*
import com.tourcool.widget.webview.x5.BaseX5WebViewActivity
import kotlinx.android.synthetic.main.activity_service_webview.*

/**
 *@description : 自定义webview
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年11月26日18:25
 * @Email: 971613168@qq.com
 */
class CommonWebViewActivity : BaseX5WebViewActivity() {
    private var url = ""
    private var isRichText = false
    private var mTitle = ""

    override fun setTitleContent(title: String) {
        if (!TextUtils.isEmpty(title)) {
            mTitleBar?.setTitleMainText(title)
        } else {
            if (!TextUtils.isEmpty(mTitle)) {
                mTitleBar?.setTitleMainText(title)
            }
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_service_webview
    }

    override fun initView(savedInstanceState: Bundle?) {
        url = intent.getStringExtra(EXTRA_WEB_VIEW_URL)
        isRichText = intent.getBooleanExtra(EXTRA_RICH_TEXT_ENABLE, false)
        try {
            mTitle = intent.getStringExtra(EXTRA_WEB_VIEW_TITLE)
        } catch (e: Exception) {

        }


        initProgressBar(mPageLoadingProgressBar)
        initWebView(mWebView)

        mPageLoadingProgressBar.progressDrawable = this.resources
                .getDrawable(R.drawable.color_progressbar)
        if (isRichText) {
            loadHtml(StringUtil.getNotNullValue(WebViewConstant.richText))
        } else {
            loadUrl(url)
        }
        setImmersionBar(false)
    }

    override fun setTitleBar(titleBar: TitleBarView?) {

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun setImmersionBar(darkFont: Boolean) {
    }
}