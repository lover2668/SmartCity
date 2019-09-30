package com.tourcool.adapter.news;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月30日12:40
 * @Email: 971613168@qq.com
 */
public class NewsAdapter extends BaseQuickAdapter<HomeChildItem, BaseViewHolder> {
    public NewsAdapter(){
        super(R.layout.item_news_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeChildItem item) {
        ImageView ivNewsIcon = helper.getView(R.id.ivNewsIcon);
        GlideManager.loadRoundImg(item.getIcon(),ivNewsIcon,2);
      boolean newsImageEnable = !TextUtils.isEmpty(item.getIcon());
       helper.setGone(R.id.ivNewsIcon,newsImageEnable);

    }
}
