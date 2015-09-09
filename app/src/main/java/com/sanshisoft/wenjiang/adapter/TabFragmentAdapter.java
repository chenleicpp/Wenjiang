package com.sanshisoft.wenjiang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sanshisoft.wenjiang.AppContext;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.ui.fragment.NewsExFragment;
import com.sanshisoft.wenjiang.ui.fragment.NewsFragment;
import com.sanshisoft.wenjiang.ui.fragment.NewsImageFragment;

/**
 * Created by chenleicpp on 2015/9/9.
 */
public class TabFragmentAdapter extends FragmentStatePagerAdapter {

    String[] tabs;

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = AppContext.getInstance().getResources().getStringArray(R.array.tab_category);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new NewsExFragment();
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return new NewsFragment();
            case 7:
            case 8:
                return new NewsImageFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
