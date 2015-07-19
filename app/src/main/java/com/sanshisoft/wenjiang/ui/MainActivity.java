package com.sanshisoft.wenjiang.ui;

import android.content.Intent;
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
}
