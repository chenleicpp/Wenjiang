package com.sanshisoft.wenjiang.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.ImageNewsAdapter;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseActivity;
import com.sanshisoft.wenjiang.base.BaseTask;
import com.sanshisoft.wenjiang.bean.ImageBean;
import com.sanshisoft.wenjiang.bean.ImageList;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chenleicpp on 2015/7/24.
 */
public class ImageNewsActivity extends BaseActivity {

    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_ID = "category_id";

    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;
    @Bind(R.id.tv_news_title)
    TextView tvNewsTitle;
    @Bind(R.id.gv_news)
    PullToRefreshGridView gvNews;

    private int categoryId;
    private String categoryName;
    private GridView mGridView;
    private List<ImageBean> mDatas;
    private ImageNewsAdapter mAdapter;
    private int totalPage;
    private static final int PAGE_SIZE = 15;
    private int currentNum = 1;

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
                startActivity(new Intent(ImageNewsActivity.this, CategoryActivity.class));
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            categoryId = bundle.getInt(CATEGORY_ID);
            categoryName = bundle.getString(CATEGORY_NAME);
        }

        gvNews.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        gvNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                currentNum++;
                getDatas(currentNum);
            }
        });
        tvNewsTitle.setText(categoryName);
        mGridView = gvNews.getRefreshableView();
        mDatas = new ArrayList<>();
        mAdapter = new ImageNewsAdapter(this);
        getDatas(1);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ImageNewsActivity.this, NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(NewsActivity.NEWS_CATEGORY, categoryName);
                bundle.putInt(NewsActivity.NEWS_ID, mDatas.get(position).getNew_id());
                bundle.putInt(NewsActivity.CATEGORY_ID, categoryId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_news;
    }

    private void getDatas(int page){
        showWaitDialog("正在加载...");
        RemoteApi.getWjtcList(mHandler,categoryId,page,PAGE_SIZE);
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
            gvNews.onRefreshComplete();
            ToastUtils.quickToast(ImageNewsActivity.this, error.getMessage());
        }
    };

    private class GetListNewsTask extends BaseTask<Void,ImageList> {

        private byte[] datas;

        public GetListNewsTask(Activity activity, String message,byte[] datas) {
            super(activity, message);
            this.datas = datas;
        }

        @Override
        protected ImageList doInBackground(Void... params) {
            try {
                String result = new String(datas,"gb2312");
                Gson gson = new Gson();
                Log.i("test", result);
                if (result != null && !StringUtils.isEmpty(result)){
                    ImageList news = gson.fromJson(result, ImageList.class);
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
                ToastUtils.quickToast(ImageNewsActivity.this, error);
            }
        }

        @Override
        public void doStuffWithResult(ImageList news) {
            List<ImageBean> datas = news.getData();
            if (news.getTotal_count() > 0) {
                if (currentNum == 1) {
                    mDatas.addAll(datas);
                    mAdapter.setList(mDatas);
                    mGridView.setAdapter(mAdapter);
                } else if (currentNum <= totalPage) {
                    mDatas.addAll(datas);
                    mAdapter.setList(mDatas);
                } else if (currentNum > totalPage) {
                    ToastUtils.quickToast(ImageNewsActivity.this, "最后一页，尚无更多新闻");
                }
            }else{
                finish();
                ToastUtils.quickToast(ImageNewsActivity.this, "尚无新闻，请稍后重试！");
            }
            gvNews.onRefreshComplete();
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
