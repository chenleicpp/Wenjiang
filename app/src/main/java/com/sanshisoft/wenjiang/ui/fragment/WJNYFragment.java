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
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.AppConfig;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.ImageNewAdapter;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseFragment;
import com.sanshisoft.wenjiang.bean.ImageNewBean;
import com.sanshisoft.wenjiang.bean.ImageNewList;
import com.sanshisoft.wenjiang.bean.SliderBean;
import com.sanshisoft.wenjiang.bean.SliderList;
import com.sanshisoft.wenjiang.common.DividerItemDecoration;
import com.sanshisoft.wenjiang.common.EndlessRecyclerOnScrollListener;
import com.sanshisoft.wenjiang.common.OnImageNewClickListener;
import com.sanshisoft.wenjiang.common.ProjectType;
import com.sanshisoft.wenjiang.ui.NewsDetailActivity;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenleicpp on 2015/9/20.
 * 温江农业
 */
public class WJNYFragment extends BaseFragment implements OnImageNewClickListener,BaseSliderView.OnSliderClickListener{

    private String mProjectType;
    private int mCategoryId;
    private String mCategoryName;

    @Bind(R.id.wjny_recyclerview)
    RecyclerView mRecyclerView;

    @Bind(R.id.wjny_slider)
    SliderLayout mSlider;

    private ImageNewAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private int currentNum = 1;
    private int totalPage;
    private List<ImageNewBean> mDatas;

    private static final int PAGE_SIZE = 10;

    public static Fragment newInstance(String projectType,int categoryId){
        Fragment fragment = new WJNYFragment();
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
            if (mProjectType.equals(ProjectType.TYPE_WJNY)){
                mCategoryName = "温江农业";
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
        View view = inflater.inflate(R.layout.fragment_wjny,container,false);
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
        mAdapter = new ImageNewAdapter();
        mAdapter.setOnImageNewClickListener(this);

        RecyclerViewHeader header = (RecyclerViewHeader) view.findViewById(R.id.header);
        header.attachTo(mRecyclerView, true);

        getSliderData();

        getDatas(1);
        return view;
    }

    private void getSliderData(){
        RemoteApi.getImagesPagerList(mHander2,mCategoryId);
    }

    private AsyncHttpResponseHandler mHander2 = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                String result = new String(responseBody,"gb2312");
                Gson gson = new Gson();
                Log.d("test", result);
                if (result != null && !StringUtils.isEmpty(result)){
                    SliderList sl = gson.fromJson(result,SliderList.class);
                    if (sl != null){
                        List<SliderBean> datas = sl.getData();
                        for (int i=0;i<datas.size();i++){
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            textSliderView
                                    .description(datas.get(i).getTitle())
                                    .image(AppConfig.BASE_URL + datas.get(i).getThumb())
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(WJNYFragment.this);

                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra_id", datas.get(i).getId()+"");

                            mSlider.addSlider(textSliderView);
                        }
                        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mSlider.setCustomAnimation(new DescriptionAnimation());
                        mSlider.setDuration(4000);
                    }
                }else {
                    ToastUtils.quickToast(getActivity(),"服务器错误，请重试！");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ToastUtils.quickToast(getActivity(), "服务器错误，请重试！");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtils.quickToast(getActivity(), error.getMessage());
        }
    };

    private void getDatas(int page){
        RemoteApi.getWjnyTabList(mHandler, mCategoryId, page, PAGE_SIZE);
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
                ImageNewList news = gson.fromJson(result, ImageNewList.class);
                updateTotalPage(news.getTotal_count());
                if (news != null) {
                    List<ImageNewBean> newsData = news.getData();
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
                            //ToastUtils.quickToast(getActivity(), "最后一页，尚无更多新闻");
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

    @Override
    public void OnImageClick(ImageNewBean inb) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NewsDetailActivity.NEWS_CATEGORY, mCategoryName);
        bundle.putInt(NewsDetailActivity.NEWS_ID, inb.getNew_id());
        bundle.putInt(NewsDetailActivity.CATEGORY_ID,mCategoryId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int id = Integer.parseInt(slider.getBundle().get("extra_id").toString());
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NewsDetailActivity.NEWS_CATEGORY, mCategoryName);
        bundle.putInt(NewsDetailActivity.NEWS_ID, id);
        bundle.putInt(NewsDetailActivity.CATEGORY_ID,mCategoryId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
