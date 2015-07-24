package com.sanshisoft.wenjiang.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.NewsAdapter;
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
    public static final String NEWS_ID = "new_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String NEWS_TYPE = "type";
    public static final String NEWS_CATEGORY = "category";
    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    @Bind(R.id.tv_news_title)
    TextView tvNewsTitle;
    @Bind(R.id.lv_news)
    PullToRefreshListView lvNews;

    private static final int PAGE_SIZE = 15;

    private int currentNum = 1;
    private int categoryId;
    private int categoryType;
    private String categoryName;

    private ListView mListView;
    private List<NewsBean> mDatas;
    private NewsAdapter mAdapter;
    private int totalPage;
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
            categoryId = b.getInt(CATEGORY_ID);
            categoryType = b.getInt(NEWS_TYPE);
            categoryName = b.getString(NEWS_CATEGORY);
        }

        lvNews.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        lvNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentNum++;
                getDatas(currentNum);
            }
        });
        tvNewsTitle.setText(categoryName);
        mListView = lvNews.getRefreshableView();
        mDatas = new ArrayList<>();
        mAdapter = new NewsAdapter(this);
        getDatas(1);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(NewsActivity.this, NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(NEWS_CATEGORY,categoryName);
                bundle.putInt(NEWS_ID, mDatas.get(position).getId());
                bundle.putInt(CATEGORY_ID,categoryId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
            lvNews.onRefreshComplete();
            ToastUtils.quickToast(NewsActivity.this, error.getMessage());
        }
    };

    private class GetListNewsTask extends BaseTask<Void,NewsList>{

        private byte[] datas;

        public GetListNewsTask(Activity activity, String message,byte[] datas) {
            super(activity, message);
            this.datas = datas;
        }

        @Override
        protected NewsList doInBackground(Void... params) {
            try {
                String result = new String(datas,"gb2312");
                Gson gson = new Gson();
                Log.d("test",result);
                if (result != null && !StringUtils.isEmpty(result)){
                    NewsList news = gson.fromJson(result, NewsList.class);
                    updateTotalPage(news.getTotal_count());
                    if (news != null) {
                        return news;
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
                ToastUtils.quickToast(NewsActivity.this, error);
            }
        }

        @Override
        public void doStuffWithResult(NewsList news) {
            List<NewsBean> datas = news.getData();
            if (news.getTotal_count() > 0) {
                if (currentNum == 1) {
                    mDatas.addAll(datas);
                    mAdapter.setList(mDatas);
                    mListView.setAdapter(mAdapter);
                } else if (currentNum <= totalPage) {
                    mDatas.addAll(datas);
                    mAdapter.setList(mDatas);
                } else if (currentNum > totalPage) {
                    ToastUtils.quickToast(NewsActivity.this, "最后一页，尚无更多新闻");
                }
            }else{
                finish();
                ToastUtils.quickToast(NewsActivity.this, "尚无新闻，请稍后重试！");
            }
            lvNews.onRefreshComplete();
            hideWaitDialog();
        }
    }

    private void updateTotalPage(int total) {
        if (total % PAGE_SIZE == 0){
            totalPage = total / PAGE_SIZE;
        }else {
            totalPage = (total / PAGE_SIZE) + 1;
        }
    }
}
