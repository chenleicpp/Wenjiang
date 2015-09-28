package com.sanshisoft.wenjiang.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.AppConfig;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseActivity;
import com.sanshisoft.wenjiang.bean.SliderBean;
import com.sanshisoft.wenjiang.bean.SliderList;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener{

    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    @Bind(R.id.ll_middle_wjny_area)
    LinearLayout llMiddleWjnyArea;
    @Bind(R.id.ll_middle_gsgg_area)
    LinearLayout llMiddleGsggArea;
    @Bind(R.id.ll_middle_nyzx_area)
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

    @Bind(R.id.home_slider)
    SliderLayout mSlider;

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

        getSliderData();
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
//                Uri uri = Uri.parse("tel:028-82727971");
//                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
//                startActivity(intent);
//                startToFooterActivity(FooterActivity.TYPE_CONTACT);
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
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

//    @OnClick({R.id.ll_middle_wjny_area, R.id.ll_middle_gsgg_area, R.id.ll_middle_nyzx_area, R.id.tv_middle_njfw_area,
//            R.id.ll_middle_djgz_area, R.id.ll_middle_ztzl_area, R.id.ll_middle_zwfw_area,R.id.tv_middle_wjtc_area,R.id.tv_middle_xxny_area})
//    public void homeAreaClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_middle_wjny_area:
//                //温江农业
//                startToNewsActivity(9, 1, "温江农业");
//                break;
//            case R.id.ll_middle_gsgg_area:
//                startToNewsActivity(7, 1, "公示公告");
//                //公示公告
//                break;
//            case R.id.ll_middle_nyzx_area:
//                //农业资讯
//                startToNewsActivity(8, 1, "农业资讯");
//                break;
//            case R.id.tv_middle_njfw_area:
//                //农业服务
//                startToNewsActivity(16, 3, "农技服务");
//                break;
//            case R.id.ll_middle_djgz_area:
//                //党建工作
//                startToNewsExActivity(4, 4, "党建工作");
//                break;
//            case R.id.ll_middle_ztzl_area:
//                //专题专栏
//                startToNewsExActivity(3, 3, "专题专栏");
//                break;
//            case R.id.ll_middle_zwfw_area:
//                //政务服务
//                startToNewsExActivity(2, 2, "政务服务");
//                break;
//            case R.id.tv_middle_wjtc_area:
//                //温江特产
//                startToImageNewsActivity(27,"温江特产");
//                break;
//            case R.id.tv_middle_xxny_area:
//                //休闲农业
//                startToImageNewsActivity(26,"休闲农业");
//                break;
//        }
//    }

    @OnClick({R.id.ll_middle_wjny_area, R.id.ll_middle_gsgg_area, R.id.ll_middle_nyzx_area, R.id.tv_middle_njfw_area,
            R.id.ll_middle_djgz_area, R.id.ll_middle_ztzl_area, R.id.ll_middle_zwfw_area,R.id.tv_middle_wjtc_area,R.id.tv_middle_xxny_area})
    public void homeAreaClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_middle_wjny_area:
                //温江农业
                jump2TabPager(0);
                break;
            case R.id.ll_middle_gsgg_area:
                jump2TabPager(1);
                //公示公告
                break;
            case R.id.ll_middle_nyzx_area:
                //农业资讯
                jump2TabPager(2);
                break;
            case R.id.tv_middle_njfw_area:
                //农业服务
                jump2TabPager(3);
                break;
            case R.id.ll_middle_djgz_area:
                //党建工作
                jump2TabPager(4);
                break;
            case R.id.ll_middle_ztzl_area:
                //专题专栏
                jump2TabPager(5);
                break;
            case R.id.ll_middle_zwfw_area:
                //政务服务
                jump2TabPager(6);
                break;
            case R.id.tv_middle_wjtc_area:
                //温江特产
                jump2TabPager(7);
                break;
            case R.id.tv_middle_xxny_area:
                //休闲农业
                jump2TabPager(8);
                break;
        }
    }

    private void jump2TabPager(int position){
        Intent intent = new Intent();
        intent.setClass(this,TabPagerActivity.class);
        intent.putExtra(TabPagerActivity.INT_EXTRA, position);
        startActivity(intent);
    }

    /**
     *
     * @param category_id
     * @param news_type 父类别id，暂时无用
     * @param news_category
     */
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

    /**
     *
     * @param category_id
     * @param news_type 父类别id，暂时无用
     * @param news_category
     */
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

    private void getSliderData(){
        RemoteApi.getHomeImagesList(mHander2);
    }

    private AsyncHttpResponseHandler mHander2 = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                String result = new String(responseBody,"gb2312");
                Gson gson = new Gson();
                Log.d("test", result);
                if (result != null && !StringUtils.isEmpty(result)){
                    SliderList sl = gson.fromJson(result,SliderList.class);
                    if (sl != null){
                        List<SliderBean> datas = sl.getData();
                        for (int i=0;i<datas.size();i++){
                            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                            textSliderView
                                    .description(datas.get(i).getTitle())
                                    .image(AppConfig.BASE_URL + datas.get(i).getThumb())
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(MainActivity.this);

                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra_id", datas.get(i).getId() + "");
                            textSliderView.getBundle()
                                    .putString("extra_category", datas.get(i).getCategoryId()+"");

                            mSlider.addSlider(textSliderView);
                        }
                        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mSlider.setCustomAnimation(new DescriptionAnimation());
                        mSlider.setDuration(4000);
                    }
                }else {
                    ToastUtils.quickToast(MainActivity.this, "服务器错误，请重试！");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ToastUtils.quickToast(MainActivity.this, "服务器错误，请重试！");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtils.quickToast(MainActivity.this, error.getMessage());
        }
    };

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int id = Integer.parseInt(slider.getBundle().get("extra_id").toString());
        int category_id = Integer.parseInt(slider.getBundle().get("extra_category").toString());
        Intent intent = new Intent();
        intent.setClass(this, NewsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NewsDetailActivity.NEWS_CATEGORY, "新闻详情");
        bundle.putInt(NewsDetailActivity.NEWS_ID, id);
        bundle.putInt(NewsDetailActivity.CATEGORY_ID,category_id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
