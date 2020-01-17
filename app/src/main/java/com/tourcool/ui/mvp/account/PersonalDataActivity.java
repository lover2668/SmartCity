package com.tourcool.ui.mvp.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.util.StatusBarUtil;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.retrofit.UploadProgressBody;
import com.frame.library.core.retrofit.UploadRequestListener;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.UserInfo;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.helper.ImagePickerHelper;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.core.widget.InputDialog;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.smartcity.R;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.apache.commons.lang.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :个人资料
 * @company :途酷科技
 * @date 2019年10月09日10:52
 * @Email: 971613168@qq.com
 */
public class PersonalDataActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private ImageView ivAvatar;
    private TextView tvNickName;
    private List<LocalMedia> selectFileList = new ArrayList<>();
    private List<String> imageDiskPathList = new ArrayList<>();
    private String avatarUrl;
    private String avatarPath;
    private KProgressHUD hud;
    private MyHandler mHandler = new MyHandler(this);
    private Message message;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("个人资料");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNickName:
                showInputDialog(getTextValue(tvNickName));
                break;
            case R.id.ivAvatar:
                selectPic();
                break;
            case R.id.tvConfirm:
                doEditUserInfo();
                break;
            default:
                break;
        }
    }

    @Override
    protected void loadPresenter() {

    }

    @Override
    public void loadData() {
        showUserInfo();
        avatarUrl = AccountHelper.getInstance().getUserInfo().getIconUrl();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_personal_data;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ivAvatar = findViewById(R.id.ivAvatar);
        tvNickName = findViewById(R.id.tvNickName);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tvNickName.setOnClickListener(this);
        if (!AccountHelper.getInstance().isLogin()) {
            ToastUtil.show("您还未登录");
            finish();
        }

    }

    private void showInputDialog(String content) {
        InputDialog dialog = new InputDialog(mContext, "输入昵称", content);
        dialog.setConfirmListener(v -> {
            setText(tvNickName, dialog.getContent());
            KeyboardHelper.closeKeyboard(dialog.getInstanstace());
            dialog.dismiss();
        });
        dialog.setCancelListener(v -> {
            KeyboardHelper.closeKeyboard(dialog.getInstanstace());
            dialog.dismiss();
        });
        dialog.show();
    }

    private void selectPic() {

        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(StatusBarUtil.isSupportStatusBarFontChange() ? R.style.picture_default_style : R.style.PicturePickerStyle_White)
                // 最大图片选择数量
                .maxSelectNum(1)
                // 最小选择数量
                .minSelectNum(1)
                // 每行显示个数
                .imageSpanCount(4)
                // 多选 or 单选
                .selectionMode(PictureConfig.SINGLE)
                // 是否可预览图片
                .previewImage(true)
                // 是否可播放音频
                .enablePreviewAudio(false)
                // 是否显示拍照按钮
                .isCamera(true)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                // 是否裁剪
                .enableCrop(true)
                // 是否压缩
                .compress(true)
                //同步true或异步false 压缩 默认同步
                .synOrAsy(true)
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .glideOverride(160, 160)
                // 是否显示uCrop工具栏，默认不显示
                .hideBottomControls(false)
                // 是否显示gif图片
                .isGif(false)
                // 裁剪框是否可拖拽
                .freeStyleCropEnabled(false)
                // 是否传入已选图片
                .selectionMedia(selectFileList)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                //结果回调onActivityResult code
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectFileList = PictureSelector.obtainMultipleResult(data);
                    handlePictureSelectCallback();
                    break;
                default:
                    break;

            }
        }
    }


    private void handlePictureSelectCallback() {
        imageDiskPathList.clear();
        for (LocalMedia localMedia : selectFileList) {
            imageDiskPathList.add(localMedia.getCompressPath());
        }
        //本地头像路径
        avatarPath = parsePath(imageDiskPathList);
        TourCooLogUtil.i(TAG, "头像路径:" + avatarPath);
        GlideManager.loadCircleImg(avatarPath, ivAvatar);
    }

    private String parsePath(List<String> imageList) {
        if (imageList == null || imageList.isEmpty()) {
            return "";
        }
        if (imageList.size() == 1) {
            return TourCooUtil.getNotNullValue(imageList.get(0));
        } else return StringUtils.join(imageList, ",");
    }


    private void showUserInfo() {
        if (AccountHelper.getInstance().isLogin()) {
            tvNickName.setText(AccountHelper.getInstance().getUserInfo().getNickname());
            GlideManager.loadCircleImg(TourCooUtil.getUrl(AccountHelper.getInstance().getUserInfo().getIconUrl()), ivAvatar);
        } else {
            tvNickName.setText(TourCooUtil.getNotNullValueLine(null));
            GlideManager.loadCircleImg("", ivAvatar);
        }
    }

    private void requestEditUserInfo() {
        TourCooLogUtil.i(TAG, "头像url：" + avatarUrl);
        ApiRepository.getInstance().requestEditUserInfo(getTextValue(tvNickName), avatarUrl).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            ToastUtil.showSuccess("修改成功");
                            notifyRefreshUserInfo();
                            finish();
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }
                });
    }


    private void doEditUserInfo() {
        if (TextUtils.isEmpty(getTextValue(tvNickName))) {
            ToastUtil.show("请输入昵称");
            return;
        }
        if (TextUtils.isEmpty(avatarPath)) {
            //本地头像路径为空 则说明用户没有选择头像 直接上传原头像
            requestEditUserInfo();
        } else {
            //不为空 则需要上传头像
            loadImagePath();
            uploadImage(imageDiskPathList);
        }
    }


    private void initProgressDialog() {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAutoDismiss(false)
                .setMaxProgress(100);
    }


    private void showHudProgressDialog() {
        if (hud != null) {
            hud.setProgress(0);
        } else {
            initProgressDialog();
        }
        hud.show();
    }

    private void updateProgress(int progress) {
        TourCooLogUtil.i("进度：" + progress);
        if (hud != null) {
            hud.setProgress(progress);
        }
    }

    private void closeHudProgressDialog() {
        if (hud != null && hud.isShowing()) {
            hud.setProgress(0);
            hud.dismiss();
        }
        hud = null;
    }


    private static class MyHandler extends Handler {
        WeakReference<PersonalDataActivity> personalDataActivity;

        MyHandler(PersonalDataActivity dataActivity) {
            personalDataActivity = new WeakReference<>(dataActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    personalDataActivity.get().updateProgress(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 上传图片
     *
     * @param imageList
     */
    private void uploadImage(List<String> imageList) {
        if (imageList == null || imageList.isEmpty()) {
            ToastUtil.show("您还没选择图片");
            return;
        }
        File file;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //注意，file是后台约定的参数，如果是多图，files，如果是单张图片，file就行
        for (String imagePath : imageList) {
            //这里上传的是多图
            file = new File(imagePath);
            builder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
        RequestBody requestBody = builder.build();

        UploadProgressBody uploadProgressBody = new UploadProgressBody(requestBody, new UploadRequestListener() {
            @Override
            public void onProgress(float progress, long current, long total) {
                message = mHandler.obtainMessage();
                message.what = 1;
                message.arg1 = (int) (progress * 100);
                mHandler.sendMessage(message);
            }

            @Override
            public void onFail(Throwable e) {
                TourCooLogUtil.e("异常：" + e.toString());
                closeHudProgressDialog();
            }
        });
        showHudProgressDialog();
        ApiRepository.getInstance().getApiService().uploadFiles(uploadProgressBody).enqueue(new Callback<BaseResult<List<String>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<String>>> call, Response<BaseResult<List<String>>> response) {
                closeHudProgressDialog();
                BaseResult<List<String>> resp = response.body();
                if (resp != null) {
                    if (resp.status == CODE_REQUEST_SUCCESS && resp.data != null) {
                        List<String> imageUrlList = new ArrayList<>(resp.data);
                        if (imageUrlList.isEmpty()) {
                            ToastUtil.show("未获取到图片链接");
                            return;
                        }
                        //获取用户头像url
                        avatarUrl = imageUrlList.get(0);
                        imageDiskPathList.clear();
                        //将头像路径置为空 不然会死循环
                        avatarPath = "";
                        //再修改头像
                        doEditUserInfo();
                    } else {
                        ToastUtil.showFailed(resp.errorMsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResult<List<String>>> call, Throwable t) {
                closeHudProgressDialog();
                ToastUtil.show("图片上传失败 请稍后再试");
            }
        });
    }


    /**
     * 解决图片重复上传bug
     */
    private void loadImagePath() {
        imageDiskPathList.clear();
        for (LocalMedia localMedia : selectFileList) {
            imageDiskPathList.add(localMedia.getCompressPath());
        }
    }


    private void notifyRefreshUserInfo() {
        EventBus.getDefault().post(new UserInfoEvent());
    }
}
