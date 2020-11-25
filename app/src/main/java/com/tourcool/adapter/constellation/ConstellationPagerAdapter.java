package com.tourcool.adapter.constellation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @description: 运势的adapter
 */

public class ConstellationPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    public ConstellationPagerAdapter(List<Fragment> fragmentList, FragmentManager fm) {
        super(fm);
        mFragments = fragmentList != null ? fragmentList : new ArrayList<>();
    }

    public void setData(List<Fragment> mFragments){
        this.mFragments = mFragments;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}