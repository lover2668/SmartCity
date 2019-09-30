package com.tourcool.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.helper.drag.OnDragVHListener;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月29日9:54
 * @Email: 971613168@qq.com
 */
    /**
     * 我的频道
     */
    public class MyViewHolder extends BaseViewHolder implements OnDragVHListener {
        private TextView textView;
        private ImageView imgEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvModuleName);
            imgEdit = (ImageView) itemView.findViewById(R.id.ivModuleIcon);
        }

        /**
         * item 被选中时
         */
        @Override
        public void onItemSelected() {
            textView.setBackgroundResource(R.color.green0DBD00);
        }

        /**
         * item 取消选中时
         */
        @Override
        public void onItemFinish() {
            textView.setBackgroundResource(R.color.gray666666);
        }
    }

