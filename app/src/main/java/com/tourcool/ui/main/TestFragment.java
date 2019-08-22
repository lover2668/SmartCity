package com.tourcool.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.widget.linkage.LinkageRecyclerView;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.frame.library.core.widget.linkage.bean.BaseGroupedItem;
import com.frame.library.core.widget.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.frame.library.core.widget.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tourcool.bean.ElemeGroupedItem;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.library.frame.R;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月22日16:08
 * @Email: 971613168@qq.com
 */
public class TestFragment extends BaseTitleFragment {
    private static final int SPAN_COUNT_FOR_GRID_MODE = 2;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initLinkageData(mContentView.findViewById(R.id.linkageView));
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBackgroundColor(TourCooUtil.getColor(R.color.blue4287FF));
        TextView mainText = titleBar.getMainTitleTextView();
        titleBar.setTitleMainText("智慧宜兴");
        mainText.setText("");
        mainText.setTextColor(TourCooUtil.getColor(R.color.white));
        mainText.setCompoundDrawablesWithIntrinsicBounds(null, null, TourCooUtil.getDrawable(R.mipmap.icon_title_name), null);
        titleBar.setBgResource(R.drawable.bg_gradient_title_common);
    }



    public static TestFragment newInstance() {
        Bundle args = new Bundle();
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initLinkageData(LinkageRecyclerView linkage) {
        Gson gson = new Gson();
        List<ElemeGroupedItem> items = gson.fromJson(getString(R.string.eleme_json),
                new TypeToken<List<ElemeGroupedItem>>() {
                }.getType());

        linkage.init(items, new ElemePrimaryAdapterConfig(), new ElemeSecondaryAdapterConfig());
    }

    private static class ElemePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getLayoutId() {
            return R.layout.default_adapter_linkage_primary;
        }

        @Override
        public int getGroupTitleViewId() {
            return R.id.tv_group;
        }

        @Override
        public int getRootViewId() {
            return R.id.layout_group;
        }

        @Override
        public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
            TextView tvTitle = ((TextView) holder.mGroupTitle);
            tvTitle.setText(title);

            tvTitle.setBackgroundColor(mContext.getResources().getColor(
                    selected ? R.color.colorPurple : R.color.colorWhite));
            tvTitle.setTextColor(ContextCompat.getColor(mContext,
                    selected ? R.color.colorWhite : R.color.colorGray));
            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
            tvTitle.setFocusable(selected);
            tvTitle.setFocusableInTouchMode(selected);
            tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
        }
    }

    private static class ElemeSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getGridLayoutId() {
            return 0;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.adapter_eleme_secondary_linear;
        }

        @Override
        public int getHeaderLayoutId() {
            return R.layout.default_adapter_linkage_secondary_header;
        }

        @Override
        public int getFooterLayoutId() {
            return 0;
        }

        @Override
        public int getHeaderTextViewId() {
            return R.id.secondary_header;
        }

        @Override
        public int getSpanCountOfGridMode() {
            return SPAN_COUNT_FOR_GRID_MODE;
        }

        @Override
        public void onBindViewHolder(LinkageSecondaryViewHolder holder,
                                     BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

            ((TextView) holder.getView(R.id.iv_goods_name)).setText(item.info.getTitle());
            Glide.with(mContext).load(item.info.getImgUrl()).into((ImageView) holder.getView(R.id.iv_goods_img));
            holder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                //TODO
            });

            holder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                //TODO
            });
        }

        @Override
        public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

//            ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
        }

        @Override
        public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

        }
    }

}
