package com.sanshisoft.wenjiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.TabFragmentAdapter;
import com.sanshisoft.wenjiang.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by chenleicpp on 2015/9/9.
 */
public class TabPagerActivity extends BaseActivity {

    public static final String INT_EXTRA = "int_extra";

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.pager)
    ViewPager mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private int pos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tab_pager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getIntent().getIntExtra(INT_EXTRA,-1);
        setupToolbar();
        setupViewPager(pos);
    }

    private void setupToolbar(){
        mToolbar.setTitle("首页导航");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupViewPager(int pos) {
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mViewPager.setCurrentItem(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_navi) {
            startActivity(new Intent(TabPagerActivity.this, CategoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
