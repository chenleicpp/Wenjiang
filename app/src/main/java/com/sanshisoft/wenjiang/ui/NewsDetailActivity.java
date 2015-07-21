package com.sanshisoft.wenjiang.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.base.BaseActivity;

import java.lang.reflect.Field;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/7/21.
 * 四级新闻详情页
 */
public class NewsDetailActivity extends BaseActivity {
    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    @Bind(R.id.tv_news_title)
    TextView tvNewsTitle;
    @Bind(R.id.webview_news_detail)
    WebView webviewNewsDetail;

    private ProgressDialog dialog;

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
                startActivity(new Intent(NewsDetailActivity.this, CategoryActivity.class));
            }
        });
        setWebView();
        webviewNewsDetail.loadUrl("http://testweb.timeerp.com/mobile/getNewsDetail.asp?new_id=201");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newsdetail;
    }

    private void setWebView() {
        WebSettings webSettings = webviewNewsDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("gb2312");
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        if (getAndroidSdkVersionCode() >= 11) {
            webSettings.setDisplayZoomControls(false);
        } else {
            setZoomControlGone(webviewNewsDetail);
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webviewNewsDetail.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (dialog != null) {
                    dialog.setMessage("努力加载中 " + newProgress + "%...");
                }
            }

        });
        webviewNewsDetail.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                if (dialog == null) {
                    dialog = new ProgressDialog(NewsDetailActivity.this);
                }
                dialog.setMessage("努力加载中 0%...");
                // 设置是否可按退回键取消
                dialog.setCancelable(true);
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.stopLoading();
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                ToastUtils.quickToast(NewsDetailActivity.this,"网页加载出错，请重试！");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

    }

    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    private int getAndroidSdkVersionCode(){
        return android.os.Build.VERSION.SDK_INT;
    }
}
