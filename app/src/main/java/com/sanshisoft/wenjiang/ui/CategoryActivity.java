package com.sanshisoft.wenjiang.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.CategoryAdapter;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseActivity;
import com.sanshisoft.wenjiang.base.BaseTask;
import com.sanshisoft.wenjiang.bean.NavigationBean;
import com.sanshisoft.wenjiang.bean.NavigationList;
import com.sanshisoft.wenjiang.view.ExGridView;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chenleicpp on 2015/7/19.
 * 二级网站导航主界面
 */
public class CategoryActivity extends BaseActivity {

    public static final int CATEGORY_TYPE_NYZX = 1;
    public static final int CATEGORY_TYPE_ZWFW = 2;
    public static final int CATEGORY_TYPE_DJGZ = 3;
    public static final int CATEGORY_TYPE_ZTZL = 4;
    public static final int CATEGORY_TYPE_XXNY = 5;

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

    @Bind(R.id.ll_container_nyzx)
    LinearLayout llContainerNyzx;
    @Bind(R.id.ll_container_zwfw)
    LinearLayout llContainerZwfw;
    @Bind(R.id.ll_container_djgz)
    LinearLayout llContainerDjgz;
    @Bind(R.id.ll_container_ztzl)
    LinearLayout llContainerZtzl;
    @Bind(R.id.ll_container_xxny)
    LinearLayout llContainerXxny;

    private List<NavigationBean> mListNyzx, mListZwfw, mListDjgz, mListZtzl, mListXxny;

    private CategoryAdapter mAdapterNyzx, mAdapterZwfw, mAdapterDjgz, mAdapterZtzl, mAdapterXxny;

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
        //农业资讯
        mListNyzx = new ArrayList<NavigationBean>();
        //政务服务
        mListZwfw = new ArrayList<NavigationBean>();
        //党建工作
        mListDjgz = new ArrayList<NavigationBean>();
        //专题专栏
        mListZtzl = new ArrayList<NavigationBean>();
        //休闲农业
        mListXxny = new ArrayList<NavigationBean>();

        mAdapterNyzx = new CategoryAdapter(this);
        mAdapterZwfw = new CategoryAdapter(this);
        mAdapterDjgz = new CategoryAdapter(this);
        mAdapterZtzl = new CategoryAdapter(this);
        mAdapterXxny = new CategoryAdapter(this);

        hideAllCategoryTitles();

        showWaitDialog("正在获取...");
        RemoteApi.getNavigation(mHandler);
    }

    private void hideAllCategoryTitles() {
        llContainerNyzx.setVisibility(View.GONE);
        llContainerZwfw.setVisibility(View.GONE);
        llContainerDjgz.setVisibility(View.GONE);
        llContainerZtzl.setVisibility(View.GONE);
        llContainerXxny.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_category;
    }

    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            getCategoryList(responseBody);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtils.quickToast(CategoryActivity.this, error.getMessage());
        }
    };

    private void getCategoryList(byte[] responseBody) {
        GetCategoryTask task = new GetCategoryTask(this, "正在获取...", responseBody);
        task.execute();
    }

    private class GetCategoryTask extends BaseTask<Void, List<NavigationBean>> {

        private byte[] datas;

        public GetCategoryTask(Activity activity, String message, byte[] datas) {
            super(activity, message);
            this.datas = datas;
        }

        @Override
        protected List<NavigationBean> doInBackground(Void... params) {
            try {
                String result = new String(datas, "gb2312");
                Gson gson = new Gson();
                if (result != null && !StringUtils.isEmpty(result)) {
                    NavigationList nl = gson.fromJson(result, NavigationList.class);
                    if (nl != null) {
                        List<NavigationBean> datas = nl.getData();
                        if (datas.size() > 0) {
                            for (int i = 0; i < datas.size(); i++) {
                                NavigationBean nb = datas.get(i);
                                if (nb.getCategory_type() == CATEGORY_TYPE_NYZX) {
                                    mListNyzx.add(nb);
                                } else if (nb.getCategory_type() == CATEGORY_TYPE_ZWFW) {
                                    mListZwfw.add(nb);
                                } else if (nb.getCategory_type() == CATEGORY_TYPE_DJGZ) {
                                    mListDjgz.add(nb);
                                } else if (nb.getCategory_type() == CATEGORY_TYPE_ZTZL) {
                                    mListZtzl.add(nb);
                                } else if (nb.getCategory_type() == CATEGORY_TYPE_XXNY) {
                                    mListXxny.add(nb);
                                } else {
                                    continue;
                                }
                            }
                        }
                        return nl.getData();
                    }
                } else {
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
            if (error != null && !StringUtils.isEmpty(error)) {
                ToastUtils.quickToast(mActivity, error);
            }
        }

        @Override
        public void doStuffWithResult(List<NavigationBean> navigationBeans) {
            if (mListNyzx != null && mListNyzx.size() > 0) {
                mAdapterNyzx.setList(mListNyzx);
                gvNyzx.setAdapter(mAdapterNyzx);
                llContainerNyzx.setVisibility(View.VISIBLE);
            }
            if (mListZwfw != null && mListZwfw.size() > 0){
                mAdapterZwfw.setList(mListZwfw);
                gvZwfw.setAdapter(mAdapterZwfw);
                llContainerZwfw.setVisibility(View.VISIBLE);
            }
            if (mListDjgz != null && mListDjgz.size() > 0){
                mAdapterDjgz.setList(mListDjgz);
                gvDjgz.setAdapter(mAdapterDjgz);
                llContainerDjgz.setVisibility(View.VISIBLE);
            }
            if (mListZtzl != null && mListZtzl.size() > 0){
                mAdapterZtzl.setList(mListZtzl);
                gvZtzl.setAdapter(mAdapterZtzl);
                llContainerZtzl.setVisibility(View.VISIBLE);
            }
            if (mListXxny != null && mListXxny.size() > 0){
                mAdapterXxny.setList(mListXxny);
                gvXxny.setAdapter(mAdapterXxny);
                llContainerXxny.setVisibility(View.VISIBLE);
            }
            hideWaitDialog();
        }
    }
}
