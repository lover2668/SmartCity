package com.tourcool.ui.mvp.garbage;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.frame.library.core.basis.BaseFragment;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月23日14:22
 * @Email: 971613168@qq.com
 */
public class GabageClassifyFragment extends BaseFragment {
    @Override
    public int getContentLayout() {
        return R.layout.fragment_tab_garbage_classify;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        RecyclerView rvParent = mContentView.findViewById(R.id.rvParent);
        RecyclerView rvChild = mContentView.findViewById(R.id.rvChild);
    }


    public static GabageClassifyFragment newInstance() {
        Bundle args = new Bundle();
        GabageClassifyFragment fragment = new GabageClassifyFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
