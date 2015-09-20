package com.sanshisoft.wenjiang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sanshisoft.wenjiang.AppContext;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.common.ProjectType;
import com.sanshisoft.wenjiang.ui.fragment.NewsExFragment;
import com.sanshisoft.wenjiang.ui.fragment.NewsFragment;
import com.sanshisoft.wenjiang.ui.fragment.NewsImageFragment;
import com.sanshisoft.wenjiang.ui.fragment.WJNYFragment;

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
                return WJNYFragment.newInstance(ProjectType.TYPE_WJNY,9);
            case 1:
                return NewsFragment.newInstance(ProjectType.TYPE_GSGG,7);
            case 2:
                return NewsFragment.newInstance(ProjectType.TYPE_NYZX,8);
            case 3:
                return NewsFragment.newInstance(ProjectType.TYPE_NYFW,16);
            case 4:
                return NewsExFragment.newInstance(ProjectType.TYPE_DJGZ,4);
            case 5:
                return NewsExFragment.newInstance(ProjectType.TYPE_ZTZL,3);
            case 6:
                return NewsExFragment.newInstance(ProjectType.TYPE_ZWFW,2);
            case 7:
                return NewsImageFragment.newInstance(ProjectType.TYPE_WJTC,27);
            case 8:
                return NewsImageFragment.newInstance(ProjectType.TYPE_XXNY,26);
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
