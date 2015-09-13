package com.sanshisoft.wenjiang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.ImageNewsAdapter;
import com.sanshisoft.wenjiang.adapter.NewsImageAdapter;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseFragment;
import com.sanshisoft.wenjiang.bean.ImageBean;
import com.sanshisoft.wenjiang.bean.ImageList;
import com.sanshisoft.wenjiang.common.BottomRecyclerOnScrollListener;
import com.sanshisoft.wenjiang.common.OnImageClickListener;
import com.sanshisoft.wenjiang.common.ProjectType;
import com.sanshisoft.wenjiang.ui.NewsActivity;
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
 * 温江特产，休闲农业
 */
public class NewsImageFragment extends BaseFragment implements OnImageClickListener{

    @Bind(R.id.news_image_recyclerview)
    RecyclerView mRecyclerView;

    private List<ImageBean> mDatas;
    private NewsImageAdapter mAdapter;
    private int totalPage;
    private static final int PAGE_SIZE = 15;
    private int currentNum = 1;
    private boolean isLoading = false;

    private String mProjectType;
    private int mCategoryId;
    private boolean isFirstToast;

    public static Fragment newInstance(String projectType,int categoryId){
        Fragment fragment = new NewsImageFragment();
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
            mCategoryId = bundle.getInt(ProjectType.PROJECT_ID);
            setRetainInstance(true);
        }
        isFirstToast = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_image,container,false);
        ButterKnife.bind(this, view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addOnScrollListener(new BottomRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (!isLoading){
                    currentNum++;
                    getDatas(currentNum);
                    isLoading = true;
                }
            }
        });
        mDatas = new ArrayList<>();
        mAdapter = new NewsImageAdapter();
        mAdapter.setOnImageClickListener(this);
        getDatas(1);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getDatas(int page){
        RemoteApi.getWjtcList(mHandler, mCategoryId, page, PAGE_SIZE);
    }

    private void doResponseData(byte[] datas) {
        try {
            String result = new String(datas,"gb2312");
            Gson gson = new Gson();
            Log.i("test", result);
            if (result != null && !StringUtils.isEmpty(result)){
                ImageList news = gson.fromJson(result, ImageList.class);
                updateTotalPage(news.getTotal_count());
                if (news != null) {
                    List<ImageBean> imageDatas = news.getData();
                    if (news.getTotal_count() > 0) {
                        if (currentNum == 1) {
                            mDatas.addAll(imageDatas);
                            mAdapter.setList(mDatas);
                            mRecyclerView.setAdapter(mAdapter);
                        } else if (currentNum <= totalPage) {
                            mDatas.addAll(imageDatas);
                            mAdapter.setList(mDatas);
                        } else if (currentNum > totalPage) {
                            if (isFirstToast){
                                ToastUtils.quickToast(getActivity(), "最后一页，尚无更多新闻");
                                isFirstToast = false;
                            }

                        }
                    }else{
                        ToastUtils.quickToast(getActivity(), "尚无新闻，请稍后重试！");
                    }
                }
            }else {
                ToastUtils.quickToast(getActivity(),"服务器错误，请重试！");
            }
            isLoading = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

    private void updateTotalPage(int total) {
        if (total % PAGE_SIZE == 0){
            totalPage = total / PAGE_SIZE;
        }else {
            totalPage = (total / PAGE_SIZE) + 1;
        }
    }

    @Override
    public void OnImageClick(ImageBean ib) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsDetailActivity.class);
        Bundle bundle = new Bundle();
        if (mProjectType.equals(ProjectType.TYPE_WJTC)) {
            bundle.putString(NewsActivity.NEWS_CATEGORY, "温江特产");
        } else {
            bundle.putString(NewsActivity.NEWS_CATEGORY, "休闲农业");
        }
        bundle.putInt(NewsActivity.NEWS_ID, ib.getNew_id());
        bundle.putInt(NewsActivity.CATEGORY_ID, mCategoryId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
