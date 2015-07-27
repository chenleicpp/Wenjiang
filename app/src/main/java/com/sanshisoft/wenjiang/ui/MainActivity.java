package com.sanshisoft.wenjiang.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    @Bind(R.id.ll_middle_wjny_area)
    LinearLayout llMiddleWjnyArea;
    @Bind(R.id.ll_middle_gsgg_area)
    LinearLayout llMiddleGsggArea;
    @Bind(R.id.ll_middle_xwlb_area)
    LinearLayout llMiddleXwlbArea;
    @Bind(R.id.ll_middle_zwfw_area)
    LinearLayout llMiddleZwfwArea;
    @Bind(R.id.ll_middle_djgz_area)
    LinearLayout llMiddleDjgzArea;
    @Bind(R.id.ll_middle_ztzl_area)
    LinearLayout llMiddleZtzlArea;
    @Bind(R.id.tv_middle_njfw_area)
    TextView tvMiddleNjfwArea;
    @Bind(R.id.tv_middle_wjtc_area)
    TextView tvMiddleWjtcArea;
    @Bind(R.id.tv_middle_xxny_area)
    TextView tvMiddleXxnyArea;
    @Bind(R.id.ll_footer_sina_area)
    LinearLayout llFooterSinaArea;
    @Bind(R.id.ll_footer_tengxun_area)
    LinearLayout llFooterTengxunArea;
    @Bind(R.id.ll_footer_weixin_area)
    LinearLayout llFooterWeixinArea;
    @Bind(R.id.ll_footer_tel_area)
    LinearLayout llFooterTelArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ibTitlebarBack.setEnabled(false);
        ibTitlebarCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ll_footer_sina_area, R.id.ll_footer_tengxun_area, R.id.ll_footer_weixin_area, R.id.ll_footer_tel_area})
    public void footerClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_footer_sina_area:
                startToFooterActivity(FooterActivity.TYPE_SINA);
                break;
            case R.id.ll_footer_tengxun_area:
                startToFooterActivity(FooterActivity.TYPE_TENGXUN);
                break;
            case R.id.ll_footer_weixin_area:
                startToFooterActivity(FooterActivity.TYPE_WEIXIN);
                break;
            case R.id.ll_footer_tel_area:
                Uri uri = Uri.parse("tel:028-82727971");
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
                break;
        }
    }

    private void startToFooterActivity(int type) {
        Intent intent = new Intent();
        intent.setClass(this, FooterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(FooterActivity.TYPE, type);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick({R.id.ll_middle_wjny_area, R.id.ll_middle_gsgg_area, R.id.ll_middle_xwlb_area, R.id.tv_middle_njfw_area,
            R.id.ll_middle_djgz_area, R.id.ll_middle_ztzl_area, R.id.ll_middle_zwfw_area,R.id.tv_middle_wjtc_area,R.id.tv_middle_xxny_area})
    public void homeAreaClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_middle_wjny_area:
                //温江农业
                startToNewsActivity(9, 1, "温江农业");
                break;
            case R.id.ll_middle_gsgg_area:
                startToNewsActivity(7, 1, "公示公告");
                //公示公告
                break;
            case R.id.ll_middle_xwlb_area:
                //新闻联播
                startToNewsActivity(8, 1, "新闻联播");
                break;
            case R.id.tv_middle_njfw_area:
                //农业服务
                startToNewsActivity(16, 3, "农技服务");
                break;
            case R.id.ll_middle_djgz_area:
                //党建工作
                startToNewsExActivity(4, 4, "党建工作");
                break;
            case R.id.ll_middle_ztzl_area:
                //专题专栏
                startToNewsExActivity(3, 3, "专题专栏");
                break;
            case R.id.ll_middle_zwfw_area:
                //政务服务
                startToNewsExActivity(2, 2, "政务服务");
                break;
            case R.id.tv_middle_wjtc_area:
                //温江特产
                startToImageNewsActivity(27,"温江特产");
                break;
            case R.id.tv_middle_xxny_area:
                //休闲农业
                startToImageNewsActivity(5,"休闲农业");
                break;
        }
    }

    private void startToNewsActivity(int category_id, int news_type, String news_category) {
        Intent intent = new Intent();
        intent.setClass(this, NewsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(NewsActivity.CATEGORY_ID, category_id);
        bundle.putInt(NewsActivity.NEWS_TYPE, news_type);
        bundle.putString(NewsActivity.NEWS_CATEGORY, news_category);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startToNewsExActivity(int category_id, int news_type, String news_category) {
        Intent intent = new Intent();
        intent.setClass(this, NewsExActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(NewsActivity.CATEGORY_ID, category_id);
        bundle.putInt(NewsActivity.NEWS_TYPE, news_type);
        bundle.putString(NewsActivity.NEWS_CATEGORY, news_category);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startToImageNewsActivity(int category_id, String category_name) {
        Intent intent = new Intent();
        intent.setClass(this, ImageNewsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ImageNewsActivity.CATEGORY_ID, category_id);
        bundle.putString(ImageNewsActivity.CATEGORY_NAME, category_name);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
