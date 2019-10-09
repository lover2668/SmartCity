package com.tourcool.ui.mvp.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.util.StatusBarUtil;
import com.frame.library.core.manager.GlideManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tourcool.core.helper.ImagePickerHelper;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.core.widget.InputDialog;
import com.tourcool.smartcity.R;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private ImagePickerHelper mImagePickerHelper;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNickName:
                showInputDialog(getTextValue(tvNickName));
                break;
            case R.id.ivAvatar:
                selectPic();
                break;
            default:
                break;
        }
    }

    @Override
    protected void loadPresenter() {

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
        ivAvatar.setOnClickListener(this);
        tvNickName.setOnClickListener(this);
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
                .selectionMedia(selectList)
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
                    selectList = PictureSelector.obtainMultipleResult(data);
                    handlePictureSelectCallback();
                    break;
                default:
                    break;

            }
        }
    }


    private void handlePictureSelectCallback() {
        imageList.clear();
        for (LocalMedia localMedia : selectList) {
            imageList.add(localMedia.getCompressPath());
        }
        GlideManager.loadCircleImg(parsePath(imageList), ivAvatar, TourCooUtil.getDrawable(R.mipmap.img_placeholder_car));
    }

    private String parsePath(List<String> imageList) {
        if (imageList == null || imageList.isEmpty()) {
            return "";
        }
        if (imageList.size() == 1) {
            return TourCooUtil.getNotNullValue(imageList.get(0));
        } else return StringUtils.join(imageList, ",");
    }
}
