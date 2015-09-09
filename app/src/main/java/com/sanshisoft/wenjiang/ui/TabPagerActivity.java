package com.sanshisoft.wenjiang.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.TabFragmentAdapter;
import com.sanshisoft.wenjiang.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by chenleicpp on 2015/9/9.
 */
public class TabPagerActivity extends BaseActivity {

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tab_pager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager() {

        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }
}
