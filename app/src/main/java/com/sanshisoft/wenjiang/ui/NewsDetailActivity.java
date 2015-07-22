package com.sanshisoft.wenjiang.ui;

import android.app.Activity;
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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseActivity;
import com.sanshisoft.wenjiang.base.BaseTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;
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

    private String categoryName;
    private int newsId;
    private int categoryId;

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            categoryName = bundle.getString(NewsActivity.NEWS_CATEGORY);
            newsId = bundle.getInt(NewsActivity.NEWS_ID);
            categoryId = bundle.getInt(NewsActivity.CATEGORY_ID);
        }
        tvNewsTitle.setText(categoryName);
        RemoteApi.getNavNewsDetail(mHandler,categoryId,newsId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newsdetail;
    }

    private AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            getNewsDetailTask(responseBody);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideWaitDialog();
            ToastUtils.quickToast(NewsDetailActivity.this, error.getMessage());
        }
    };

    private void getNewsDetailTask(byte[] datas){
        GetDetailTask task = new GetDetailTask(this,"正在加载...",datas);
        task.execute();
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

    private class GetDetailTask extends BaseTask<Void,String>{

        private byte[] datas;

        public GetDetailTask(Activity activity, String message,byte[] datas) {
            super(activity, message);
            this.datas = datas;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String result = new String(datas,"gb2312");
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(String str) {
            if (str != null && !StringUtils.isEmpty(str)){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(str);
                    int result_code = obj.getInt("result_code");
                    if (result_code == 1){
                        String url = obj.getString("new_url");
                        webviewNewsDetail.loadUrl(url);
                    }else{
                        ToastUtils.quickToast(NewsDetailActivity.this,"获取新闻详情失败，请重试！");
                        NewsDetailActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void doError() {
            if (error != null && !StringUtils.isEmpty(error)){
                ToastUtils.quickToast(NewsDetailActivity.this,error);
            }
        }
    }
}
