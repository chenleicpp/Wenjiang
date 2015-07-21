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

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseActivity;
import com.sanshisoft.wenjiang.base.BaseTask;
import com.sanshisoft.wenjiang.bean.NewsBean;
import com.sanshisoft.wenjiang.bean.NewsList;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
    private int categoryType;

    private ListView mListView;
    private List<NewsBean> mDatas;
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
            categoryType = b.getInt(NEWS_TYPE);
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
        switch (categoryType) {
            case CategoryActivity.CATEGORY_TYPE_NYZX:
                tvNewsTitle.setText("农业资讯");
                break;
            case CategoryActivity.CATEGORY_TYPE_ZWFW:
                tvNewsTitle.setText("政务服务");
                break;
            case CategoryActivity.CATEGORY_TYPE_DJGZ:
                tvNewsTitle.setText("党建工作");
                break;
            case CategoryActivity.CATEGORY_TYPE_ZTZL:
                tvNewsTitle.setText("专题专栏");
                break;
            case CategoryActivity.CATEGORY_TYPE_XXNY:
                tvNewsTitle.setText("休闲农业");
                break;
        }
        mListView = lvNews.getRefreshableView();
        mDatas = new ArrayList<>();
        getDatas(1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }

    private void getDatas(int page){
        showWaitDialog("正在加载...");
        RemoteApi.getNavNewsList(mHandler, categoryId, page, PAGE_SIZE);
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

    private class GetListNewsTask extends BaseTask<Void,List<NewsBean>>{

        private byte[] datas;

        public GetListNewsTask(Activity activity, String message,byte[] datas) {
            super(activity, message);
            this.datas = datas;
        }

        @Override
        protected List<NewsBean> doInBackground(Void... params) {
            try {
                String result = new String(datas,"gb2312");
                Gson gson = new Gson();
                Log.d("test",result);
                if (result != null && !StringUtils.isEmpty(result)){
                    NewsList news = gson.fromJson(result, NewsList.class);
                    if (news != null && news.getData().size() > 0) {
                        return news.getData();
                    }else {
                        return null;
                    }
                }else {
                    error = "服务器错误，请重试！";
                }
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
        public void doStuffWithResult(List<NewsBean> datas) {
            if (currentNum == 1){
                mDatas.addAll(datas);
            }
        }
    }
}
