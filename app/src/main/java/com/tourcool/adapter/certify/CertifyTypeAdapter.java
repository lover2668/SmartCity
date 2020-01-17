package com.tourcool.adapter.certify;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.certify.CertifyType;
import com.tourcool.smartcity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年01月16日10:44
 * @Email: 971613168@qq.com
 */
public class CertifyTypeAdapter extends BaseQuickAdapter<CertifyType, BaseViewHolder> {

    public CertifyTypeAdapter() {
        super(R.layout.item_certify_type);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CertifyType item) {
        helper.setText(R.id.tvCertifyName, item.getCertifyName());
        ImageView imageView = helper.getView(R.id.ivCertifyType);
        imageView.setImageResource(item.getCertifyIconId());
        helper.setGone(R.id.ivHasCertify, item.isCertified());
    }
}
