package com.sanshisoft.wenjiang.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseActivity;
import com.sanshisoft.wenjiang.base.BaseTask;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/7/20.
 * 三级新闻列表页
 */
public class NewsActivity extends BaseActivity {
    public static final String NEWS_ID = "id";
    public static final String NEWS_TYPE = "type";
    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    @Bind(R.id.tv_news_title)
    TextView tvNewsTitle;
    @Bind(R.id.lv_news)
    PullToRefreshListView lvNews;

    private static final int PAGE_SIZE = 10;

    private static int currentNum = 1;
    private int categoryId;

    private ListView mListView;
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

        Bundle b = getIntent().getExtras();
        if (b != null){
            categoryId = b.getInt(NEWS_ID);
        }

        lvNews.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        lvNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        mListView = lvNews.getRefreshableView();
        getDatas(1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }

    private void getDatas(int page){
        showWaitDialog("正在加载...");
        RemoteApi.getNavNewsList(mHandler, categoryId, currentNum, PAGE_SIZE);
    }

    private void getListNewsTask(byte[] body){
        GetListNewsTask task = new GetListNewsTask(this,"正在加载...",body);
        task.execute();
    }

    private AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
           getListNewsTask(responseBody);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideWaitDialog();
            ToastUtils.quickToast(NewsActivity.this, error.getMessage());
        }
    };

    private class GetListNewsTask extends BaseTask<Void,String>{

        private byte[] datas;

        public GetListNewsTask(Activity activity, String message,byte[] datas) {
            super(activity, message);
            this.datas = datas;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String result = new String(datas,"gb2312");
                Log.d("test",result);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void doError() {
            if (error != null && !StringUtils.isEmpty(error)){
                hideWaitDialog();
                ToastUtils.quickToast(NewsActivity.this,error);
            }
        }

        @Override
        public void doStuffWithResult(String s) {

        }
    }
}
