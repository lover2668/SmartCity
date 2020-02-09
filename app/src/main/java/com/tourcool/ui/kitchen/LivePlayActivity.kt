package com.tourcool.ui.kitchen

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.frame.library.core.retrofit.BaseLoadingObserver
import com.frame.library.core.util.ToastUtil
import com.frame.library.core.widget.titlebar.TitleBarView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.tourcool.bean.kitchen.KitchenLiveInfo
import com.tourcool.core.base.BaseResult
import com.tourcool.core.config.RequestConfig
import com.tourcool.core.retrofit.repository.ApiRepository
import com.tourcool.smartcity.R
import com.tourcool.ui.base.BaseCommonTitleActivity
import com.tourcool.ui.kitchen.VideoListActivity.Companion.EXTRA_LIVE_LIST
import com.tourcool.ui.kitchen.VideoListActivity.Companion.EXTRA_POSITION
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_bright_kitchen_video_live.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年02月06日10:12
 * @Email: 971613168@qq.com
 */
class LivePlayActivity : BaseCommonTitleActivity() {
    private var position = -1
    private var isPlay = false
    private var isPause = false
    private var orientationUtils: OrientationUtils? = null
    private var liveInfo : KitchenLiveInfo ?= null
    private var liveVideoList : MutableList<KitchenLiveInfo> ?= null
    override fun getContentLayout(): Int {
        return R.layout.activity_bright_kitchen_video_live
    }

    override fun beforeInitView(savedInstanceState: Bundle?) {
        super.beforeInitView(savedInstanceState)
        liveVideoList = intent!!.extras!!.getSerializable(EXTRA_LIVE_LIST) as ArrayList<KitchenLiveInfo>
        position = intent!!.extras!!.getInt(EXTRA_POSITION)
        if(position>= 0){
            liveInfo = liveVideoList!![position]
        }
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar!!.setTitleMainText(liveInfo!!.cameraName)
        titleBar.getLinearLayout(Gravity.END)
    }

    override fun loadData() {
        super.loadData()
        requestVideoList()
        showVideoLiveInfo()
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    private fun requestVideoList() {
        if (liveVideoList == null || liveVideoList!!.isEmpty() || position < 0) {
            ToastUtil.show("视频直播地址有误")
            return
        }
        liveInfo =liveVideoList!![position]
        ApiRepository.getInstance().requestKitchenVideoLiveUrl(liveInfo!!.deviceSerial, "" + liveInfo!!.chanNum).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<String>>() {
            override fun onRequestNext(entity: BaseResult<String>) {
                if (entity.status == RequestConfig.CODE_REQUEST_SUCCESS) {
                    loadPlay(entity.data)
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


    private fun loadPlay(url: String) {
//        val testUrl = "http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4"
        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        imageView.setImageResource(R.mipmap.xxx1)

        //增加title
        detailPlayer.titleTextView.visibility = View.GONE
        detailPlayer.backButton.visibility = View.GONE
        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(this, detailPlayer)
        //初始化不打开外部的旋转
        //初始化不打开外部的旋转
        orientationUtils!!.isEnable = false
        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption.setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(url)
                .setCacheWithPlay(false)
//                .setVideoTitle("测试视频")
                .setVideoAllCallBack(object : GSYSampleCallBack() {
                    override fun onPrepared(url: String, vararg objects: Any) {
                        super.onPrepared(url, *objects)
                        //开始播放了才能旋转和全屏
                        orientationUtils!!.isEnable = true
                        isPlay = true
                    }

                    override fun onQuitFullscreen(url: String, vararg objects: Any) {
                        super.onQuitFullscreen(url, *objects)
                        if (orientationUtils != null) {
                            orientationUtils!!.backToProtVideo()
                        }
                    }
                }).setLockClickListener { view, lock ->
                    if (orientationUtils != null) { //配合下方的onConfigurationChanged
                        orientationUtils!!.isEnable = !lock
                    }
                }.build(detailPlayer)

        detailPlayer.fullscreenButton.setOnClickListener(View.OnClickListener {
            //直接横屏
            orientationUtils!!.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            detailPlayer.startWindowFullscreen(this@LivePlayActivity, true, true)
        })
        detailPlayer.startPlayLogic()
    }

    override fun onPause() {
        detailPlayer.currentPlayer.onVideoPause()
        super.onPause()
        isPause = true
    }


    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            detailPlayer.currentPlayer.release()
        }
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }

    override fun onResume() {
        super.onResume()
        detailPlayer.currentPlayer.onVideoResume(false)
        super.onResume()
        isPause = false
    }


    override fun onBackPressed() {
        //先返回正常状态
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
        }
    }


    private fun showVideoLiveInfo(){
        tvVideoLiveName.text = liveInfo!!.cameraName
        tvCameraNumber.text = "序列号："+ liveInfo!!.deviceSerial
    }
}