package com.sanshisoft.wenjiang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseFragment;
import com.sanshisoft.wenjiang.bean.ImageBean;
import com.sanshisoft.wenjiang.bean.ImageList;
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
 */
public class NewsImageFragment extends BaseFragment {

    @Bind(R.id.gv_news)
    PullToRefreshGridView gvNews;

    private GridView mGridView;
    private List<ImageBean> mDatas;
    private ImageNewsAdapter mAdapter;
    private int totalPage;
    private static final int PAGE_SIZE = 12;
    private int currentNum = 1;

    private String mProjectType;
    private int mCategoryId;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_image,container,false);
        ButterKnife.bind(this, view);

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
        mGridView = gvNews.getRefreshableView();
        mDatas = new ArrayList<>();
        mAdapter = new ImageNewsAdapter(getActivity());
        getDatas(1);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                if (mProjectType.equals(ProjectType.TYPE_WJTC)) {
                    bundle.putString(NewsActivity.NEWS_CATEGORY, "温江特产");
                } else {
                    bundle.putString(NewsActivity.NEWS_CATEGORY, "休闲农业");
                }
                bundle.putInt(NewsActivity.NEWS_ID, mDatas.get(position).getNew_id());
                bundle.putInt(NewsActivity.CATEGORY_ID, mCategoryId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getDatas(int page){
        if (page == 1)
            showWaitDialog(R.string.loading);
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
                            mGridView.setAdapter(mAdapter);
                            hideWaitDialog();
                        } else if (currentNum <= totalPage) {
                            mDatas.addAll(imageDatas);
                            mAdapter.setList(mDatas);
                        } else if (currentNum > totalPage) {
                            ToastUtils.quickToast(getActivity(), "最后一页，尚无更多新闻");
                        }
                    }else{
                        ToastUtils.quickToast(getActivity(), "尚无新闻，请稍后重试！");
                    }
                    gvNews.onRefreshComplete();
                }
            }else {
                ToastUtils.quickToast(getActivity(),"服务器错误，请重试！");
            }
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
            hideWaitDialog();
            gvNews.onRefreshComplete();
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
}
