package com.tourcool.adapter.express;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tourcool.smartcity.R;
import com.xuexiang.xui.adapter.listview.BaseListAdapter;
import com.xuexiang.xui.utils.ResUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :JenkinsZhou
 * @description : 物流下拉选择框适配器
 * @company :途酷科技
 * @date 2020年11月26日9:24
 * @Email: 971613168@qq.com
 */
public class ExpressDropDownAdapter extends BaseListAdapter <String, ExpressDropDownAdapter.ViewHolder>{

    public ExpressDropDownAdapter(Context context) {
        super(context);
    }

    public ExpressDropDownAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_drop_down_list_item;
    }

    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.mText.setText(item);
        if (mSelectPosition != -1) {
            if (mSelectPosition == position) {
                holder.mText.setSelected(true);
                holder.mText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResUtils.getVectorDrawable(holder.mText.getContext(), R.drawable.ic_checked_right), null);
            } else {
                holder.mText.setSelected(false);
                holder.mText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.text)
        TextView mText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
