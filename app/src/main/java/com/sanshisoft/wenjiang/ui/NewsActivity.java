package com.sanshisoft.wenjiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/7/20.
 * 三级新闻列表页
 */
public class NewsActivity extends BaseActivity {
    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    @Bind(R.id.tv_news_title)
    TextView tvNewsTitle;
    @Bind(R.id.lv_news)
    PullToRefreshListView lvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ibTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibTitlebarCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsActivity.this, CategoryActivity.class));
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }
}
