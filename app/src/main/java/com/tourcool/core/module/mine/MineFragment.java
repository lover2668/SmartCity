package com.tourcool.core.module.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.allen.library.SuperTextView;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.helper.CheckVersionHelper;
import com.tourcool.core.helper.ImagePickerHelper;
import com.tourcool.core.helper.TitleBarViewHelper;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.util.SpanTool;
import com.tourcool.core.widget.OverScrollView;
import com.tourcool.core.widget.ProgressDialog;
import com.frame.library.core.widget.LoadingDialogWrapper;
import com.tourcool.core.MyApplication;
import com.frame.library.core.control.IFrameRefreshView;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.retrofit.FrameRetrofit;
import com.frame.library.core.retrofit.FrameTransformer;
import com.frame.library.core.retrofit.FrameUploadRequestBody;
import com.frame.library.core.retrofit.FrameUploadRequestListener;
import com.frame.library.core.util.FormatUtil;
import com.frame.library.core.util.SizeUtil;
import com.aries.ui.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.smartcity.R;
import com.tourcool.ui.mvp.MessageListActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @Author: JenkinsZhou on 2018/7/13 17:09
 * @E-Mail: 971613168@qq.com
 * @Function: 我的
 * @Description:
 */
public class MineFragment extends BaseTitleFragment implements IFrameRefreshView {

    @BindView(R.id.sv_containerMine)
    OverScrollView mSvContainer;
    @BindView(R.id.tv_coverMine) TextView mTvCover;
    @BindView(R.id.stv_infoMine) SuperTextView mStvInfo;
    @BindView(R.id.stv_updateMine) SuperTextView mStvUpdate;
    private ImageView mIvHead;
    private boolean mIsLight;
    private SmartRefreshLayout mRefreshLayout;

    private ImagePickerHelper mImagePickerHelper;
    private TitleBarViewHelper mTitleBarViewHelper;
    private static final int REQUEST_CODE_CHOOSE = 23;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Activity 可以通过给根布局添加SmartRefreshLayout实现
     * Fragment 根布局getParent 为null无法获取父容器故需传递一个被SmartRefreshLayout包裹的View
     * 如Fragment根布局为SmartRefreshLayout 此处传递null即可
     *
     * @return
     */
    @Override
    public View getContentView() {
        return mSvContainer;
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        int statusHeight = StatusBarUtil.getStatusBarHeight() + getResources().getDimensionPixelSize(R.dimen.dp_title_height);
        LoggerManager.i("statusHeight:" + statusHeight + ";dp:" + SizeUtil.px2dp(statusHeight));
        refreshLayout.setHeaderInsetStart(SizeUtil.px2dp(statusHeight));
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainTextColor(Color.WHITE)
                .setDividerVisible(false)
                .setTitleMainText(R.string.mine);
        titleBar.getBackground().setAlpha(0);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mImagePickerHelper = new ImagePickerHelper(mContext);
        mIvHead = mStvInfo.getLeftIconIV();
        GlideManager.loadCircleImg("https://avatars0.githubusercontent.com/u/19605922?s=460&v=4", mIvHead);
        LoggerManager.d("imageHeight:" + mIvHead.getLayoutParams().height + ";screenWidth:" + SizeUtil.getScreenWidth());
        SpanTool.getBuilder(mStvInfo.getLeftString())
                .append("https://github.com/JenkinsZhou")
                .setUnderline()
                .setForegroundColor(Color.BLUE)
                .setBoldItalic()
                .into(mStvInfo.getLeftTextView());
        SpanTool.getBuilder(mStvInfo.getLeftBottomString())
                .append("http://www.jianshu.com/u/a229eee96115")
                .setUnderline()
                .setForegroundColor(Color.BLUE)
                .setBoldItalic()
                .into(mStvInfo.getLeftBottomTextView());

        mStvInfo.setLeftTvClickListener(() -> WebViewActivity.start(mContext, "https://github.com/JenkinsZhou"));
        mStvInfo.setLeftBottomTvClickListener(() -> WebViewActivity.start(mContext, "http://www.jianshu.com/u/a229eee96115"));
        mStvInfo.getLeftBottomTextView().setGravity(Gravity.LEFT);
        ViewCompat.setElevation(mStvInfo, getResources().
                getDimension(R.dimen.dp_elevation));
        ViewCompat.setTranslationZ(mStvInfo, 3f);
        if (!MyApplication.isSupportElevation()) {
            mStvInfo.setShapeStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dp_line_size))
                    .setShapeStrokeColor(ContextCompat.getColor(mContext, R.color.colorLineGray))
                    .useShape();
        }

        mIvHead.setOnClickListener(v -> mImagePickerHelper.selectPicture(1000, (requestCode, list) -> {
            if (list == null || list.size() == 0 || requestCode != 1000) {
                return;
            }
            GlideManager.loadCircleImg(list.get(0), mIvHead);
        }));

        mStvUpdate.setRightString("当前版本:V" + FrameUtil.getVersionName(mContext));
        //根据屏幕宽度重新调整背景图
        int heightCover = SizeUtil.getScreenWidth() * 1 / 2;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTvCover.getLayoutParams();
        if (params != null) {
            params.height = heightCover;
        }
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) mStvInfo.getLayoutParams();
        if (margin != null) {
            margin.topMargin = heightCover - SizeUtil.dp2px(20);
        }
        mTitleBarViewHelper = new TitleBarViewHelper(mContext)
                .setTitleBarView(mTitleBar)
                .setOverScrollView(mSvContainer)
                .setShowTextEnable(true)
                .setMaxHeight(heightCover)
                .setOnScrollListener(new TitleBarViewHelper.OnScrollListener() {
                    @Override
                    public void onScrollChange(int alpha, boolean isLightMode) {
                        mIsLight = isLightMode;
                    }
                });
        LoggerManager.i("initView_getParent:" + mContentView.getParent() + ";rootView:" + mContentView.getRootView());
    }

    /**
     * 演示文件上传--需设置自己的上传路径
     *
     * @param listFile
     */
    private void uploadFile(List<String> listFile) {
        if (listFile == null) {
            return;
        }
        final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle("上传文件");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("上传中...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressNumberFormat("");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                //额外参数
                .addFormDataPart("basicId", "basicId");
        //添加上传实体
        for (int i = 0; i < listFile.size(); i++) {
            File file = new File(listFile.get(i));
            int finalI = i;
            builder.addFormDataPart("uploadfiles", file.getName(), getUploadRequestBody(file, new FrameUploadRequestListener() {
                @Override
                public void onProgress(float progress, long current, long total) {
                    if (!mProgressDialog.isShowing()) {
                        return;
                    }
                    mProgressDialog.setMessage("上传中(" + (finalI + 1) + "/" + listFile.size() + ")");
                    mProgressDialog.setProgressNumberFormat(FormatUtil.formatDataSize(current) + "/" + FormatUtil.formatDataSize(total));
                    mProgressDialog.setMax((int) total);
                    mProgressDialog.setProgress((int) current);
                    LoggerManager.i("uploadFile", ":i=" + finalI + ";progress:" + progress + ";current:" + current + ";total:" + total);
                }

                @Override
                public void onFail(Throwable e) {
                    LoggerManager.i("uploadFile", "error:" + e.getMessage());
                }
            }));
        }
        RequestBody requestBody = builder.build();
        //上传地址需自行设置
        FrameRetrofit.getInstance().uploadFile("http://XXXX/v1/ftp/upload-files", requestBody)
                .compose(FrameTransformer.switchSchedulers())
                .subscribe(new BaseLoadingObserver<ResponseBody>(new LoadingDialogWrapper(mContext, mProgressDialog)) {
                    @Override
                    public void onRequestNext(ResponseBody entity) {
                        String message = "上传返回:";
                        try {
                            message += entity.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new AlertDialog.Builder(mContext)
                                .setTitle("上传文件返回结果")
                                .setMessage(message)
                                .setPositiveButton(R.string.ensure, null)
                                .create()
                                .show();
                    }

                });
    }


    private RequestBody getUploadRequestBody(File file, FrameUploadRequestListener listener) {
        if (listener == null) {
            return RequestBody.create(MultipartBody.FORM, file);
        }
        return new FrameUploadRequestBody(RequestBody.create(MultipartBody.FORM, file), listener);
    }

    @OnClick({R.id.stv_setting, R.id.stv_libraryMine, R.id.stv_thirdLibMine
            , R.id.stv_shareMine, R.id.stv_updateMine, R.id.stv_uploadMine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.stv_setting:
//                FrameUtil.startActivity(mContext, SettingActivity.class);
                FrameUtil.startActivity(mContext, MessageListActivity.class);

                break;
            case R.id.stv_libraryMine:
                WebViewActivity.start(mContext, "https://github.com/JenkinsZhou/FastLib/blob/master/README.md");
                break;
            case R.id.stv_thirdLibMine:
                FrameUtil.startActivity(mContext, ThirdLibraryActivity.class);
                break;
            case R.id.stv_shareMine:
                FrameUtil.startShareText(mContext, getString(R.string.share_content));
                break;
            case R.id.stv_updateMine:
                //演示大文件下载--王者荣耀
//                UpdateEntity updateEntity = new UpdateEntity();
//                updateEntity.url = "http://gdown.baidu.com/data/wisegame/008c0de8d4355b41/wangzherongyao_35011414.apk";
//                CheckVersionHelper.with((BaseActivity) mContext)
//                        .downloadApk(updateEntity, "king_glory.apk", true);
                new CheckVersionHelper(mContext)
                        .checkVersion(true);
//                new PgyUpdateManager.Builder()
//                        .setForced(false)                //设置是否强制提示更新
//                        // v3.0.4+ 以上同时可以在官网设置强制更新最高低版本；网站设置和代码设置一种情况成立则提示强制更新
//                        .setUserCanRetry(false)         //失败后是否提示重新下载
//                        .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
//                        .register();
                break;
            case R.id.stv_uploadMine:
                mImagePickerHelper.selectFile(1001, 5, (requestCode, list) -> {
                    if (list == null || list.size() == 0 || requestCode != 1001) {
                        return;
                    }
                    uploadFile(list);
                });
                break;
        }
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        if (isVisibleToUser) {
            if (mRefreshLayout != null) {
                mRefreshLayout.autoRefresh();
            }
            if (mIsLight) {
                StatusBarUtil.setStatusBarLightMode(mContext);
            } else {
                StatusBarUtil.setStatusBarDarkMode(mContext);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImagePickerHelper != null) {
            mImagePickerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTitleBarViewHelper != null) {
            mTitleBarViewHelper.onDestroy();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
    }



}
