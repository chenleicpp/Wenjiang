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
import android.widget.ZoomButtonsController;

import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.base.BaseActivity;

import java.lang.reflect.Field;

import butterknife.Bind;

/**
 * Created by chenleicpp on 2015/7/24.
 * 底栏footer
 */
public class FooterActivity extends BaseActivity {

    public static final String TYPE = "type";

    public static final int TYPE_SINA = 1;
    public static final int TYPE_TENGXUN = 2;
    public static final int TYPE_WEIXIN = 3;
    public static final int TYPE_CONTACT = 4;
    private static final String SINA_URL = "http://weibo.com/wjncfzj";
    private static final String TENGXUN_URL = "http://t.qq.com/wjqncfzj";
    private static final String CONTACT_URL = "http://www.wjagri.cn/show_app_lxwm.asp?showid=2";
    private static final String WEIXIN_URL = "http://www.wjagri.cn/show_app.asp?showid=3";
    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    @Bind(R.id.webview_footer)
    WebView webviewFooter;
    @Bind(R.id.iv_footer)
    ImageView ivFooter;

    private int type;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWebView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(TYPE);
        }
        if (type == TYPE_WEIXIN){
//            webviewFooter.setVisibility(View.GONE);
//            ivFooter.setVisibility(View.VISIBLE);
            webviewFooter.setVisibility(View.VISIBLE);
            ivFooter.setVisibility(View.GONE);
            webviewFooter.loadUrl(WEIXIN_URL);
        }else if (type == TYPE_SINA){
            webviewFooter.setVisibility(View.VISIBLE);
            ivFooter.setVisibility(View.GONE);
            webviewFooter.loadUrl(SINA_URL);
        }else if (type == TYPE_TENGXUN){
            webviewFooter.setVisibility(View.VISIBLE);
            ivFooter.setVisibility(View.GONE);
            webviewFooter.loadUrl(TENGXUN_URL);
        }else if (type == TYPE_CONTACT){
            webviewFooter.setVisibility(View.VISIBLE);
            ivFooter.setVisibility(View.GONE);
            webviewFooter.loadUrl(CONTACT_URL);
        }

        ibTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibTitlebarCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FooterActivity.this, CategoryActivity.class));
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_footer;
    }

    private void setWebView() {
        WebSettings webSettings = webviewFooter.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("gb2312");
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        if (getAndroidSdkVersionCode() >= 11) {
            webSettings.setDisplayZoomControls(false);
        } else {
            setZoomControlGone(webviewFooter);
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webviewFooter.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (dialog != null) {
                    dialog.setMessage("努力加载中 " + newProgress + "%...");
                }
            }

        });
        webviewFooter.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                if (dialog == null) {
                    dialog = new ProgressDialog(FooterActivity.this);
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
                ToastUtils.quickToast(FooterActivity.this, "网页加载出错，请重试！");
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
