package com.tourcool.core.base;

import android.os.Build;
import android.view.View;

import com.tourcool.core.touch.ItemTouchHelperViewHolder;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @Author: JenkinsZhou on 2018/8/9 17:29
 * @E-Mail: 971613168@qq.com
 * Function: 实现拖拽ViewHolder
 * Description:
 */
public class BaseItemTouchViewHolder extends BaseViewHolder implements ItemTouchHelperViewHolder {

    public BaseItemTouchViewHolder(View view) {
        super(view);
    }

    @Override
    public void onItemSelectedChanged() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.setTranslationZ(30);
        }
    }

    @Override
    public void onItemClear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.setTranslationZ(0);
        }
    }

}
