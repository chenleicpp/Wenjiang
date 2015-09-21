package com.sanshisoft.wenjiang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.NewsExRecyclerAdapter;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseFragment;
import com.sanshisoft.wenjiang.bean.NewsExBean;
import com.sanshisoft.wenjiang.bean.NewsExList;
import com.sanshisoft.wenjiang.common.DividerItemDecoration;
import com.sanshisoft.wenjiang.common.EndlessRecyclerOnScrollListener;
import com.sanshisoft.wenjiang.common.OnNewsExClickListener;
import com.sanshisoft.wenjiang.common.ProjectType;
import com.sanshisoft.wenjiang.ui.NewsDetailActivity;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenleicpp on 2015/9/9.
 * 党建工作，专题专栏，政务服务
 */
public class NewsExFragment extends BaseFragment implements OnNewsExClickListener{

    private String mProjectType;
    private int mCategoryId;
    private String mCategoryName;

    @Bind(R.id.news_ex_recyclerview)
    RecyclerView mRecyclerView;

    private NewsExRecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private int currentNum = 1;
    private int totalPage;
    private List<NewsExBean> mDatas;

    private static final int PAGE_SIZE = 10;

    public static Fragment newInstance(String projectType,int categoryId){
        Fragment fragment = new NewsExFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProjectType.PROJECT_TYPE, projectType);
        bundle.putInt(ProjectType.PROJECT_ID,categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            mProjectType = bundle.getString(ProjectType.PROJECT_TYPE);
            if (mProjectType.equals(ProjectType.TYPE_DJGZ)){
                mCategoryName = "党建工作";
            }else if (mProjectType.equals(ProjectType.TYPE_ZTZL)){
                mCategoryName = "专题专栏";
            }else if (mProjectType.equals(ProjectType.TYPE_ZWFW)){
                mCategoryName = "政务服务";
            }
            mCategoryId = bundle.getInt(ProjectType.PROJECT_ID);
            setRetainInstance(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_ex,container,false);
        ButterKnife.bind(this, view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getDatas(currentPage);
                currentNum = currentPage;
            }
        });
        mDatas = new ArrayList<>();
        mAdapter = new NewsExRecyclerAdapter(getActivity());
        mAdapter.setOnNewsExClickListener(this);

        getDatas(1);
        return view;
    }

    @Override
    public void OnNewsExClick(NewsExBean neb) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NewsDetailActivity.NEWS_CATEGORY, mCategoryName);
        bundle.putInt(NewsDetailActivity.NEWS_ID, neb.getNew_id());
        bundle.putInt(NewsDetailActivity.CATEGORY_ID,mCategoryId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getDatas(int page){
        RemoteApi.getDjgzList(mHandler, mCategoryId, page, PAGE_SIZE);
    }

    private AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            doResponseData(responseBody);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtils.quickToast(getActivity(), error.getMessage());
        }
    };

    private void doResponseData(byte[] datas){
        try {
            String result = new String(datas,"gb2312");
            Gson gson = new Gson();
            Log.d("test", result);
            if (result != null && !StringUtils.isEmpty(result)){
                NewsExList news = gson.fromJson(result, NewsExList.class);
                updateTotalPage(news.getTotal_count());
                if (news != null) {
                    List<NewsExBean> newsData = news.getData();
                    if (news.getTotal_count() > 0) {
                        if (currentNum == 1) {
                            mDatas.addAll(newsData);
                            mAdapter.setList(mDatas);
                            mRecyclerView.setAdapter(mAdapter);
                            hideWaitDialog();
                        } else if (currentNum <= totalPage) {
                            mDatas.addAll(newsData);
                            mAdapter.setList(mDatas);
                        } else if (currentNum > totalPage && currentNum != 1) {
                            ToastUtils.quickToast(getActivity(), "最后一页，尚无更多新闻");
                        }
                    }else{
                        ToastUtils.quickToast(getActivity(), "尚无新闻，请稍后重试！");
                    }
                }
            }else {
                ToastUtils.quickToast(getActivity(),"服务器错误，请重试！");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ToastUtils.quickToast(getActivity(), "服务器错误，请重试！");
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
