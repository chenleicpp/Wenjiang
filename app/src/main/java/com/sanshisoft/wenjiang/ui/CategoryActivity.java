package com.sanshisoft.wenjiang.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseActivity;
import com.sanshisoft.wenjiang.view.ExGridView;

import org.apache.http.Header;

import butterknife.Bind;

/**
 * Created by chenleicpp on 2015/7/19.
 * 二级网站导航主界面
 */
public class CategoryActivity extends BaseActivity {
    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    //农业资讯
    @Bind(R.id.gv_nyzx)
    ExGridView gvNyzx;
    //政务服务
    @Bind(R.id.gv_zwfw)
    ExGridView gvZwfw;
    //党建工作
    @Bind(R.id.gv_djgz)
    ExGridView gvDjgz;
    //专题专栏
    @Bind(R.id.gv_ztzl)
    ExGridView gvZtzl;
    //休闲农业
    @Bind(R.id.gv_xxny)
    ExGridView gvXxny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ibTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibTitlebarCategory.setEnabled(false);

        RemoteApi.getNavigation(mHandler);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_category;
    }

    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
}
