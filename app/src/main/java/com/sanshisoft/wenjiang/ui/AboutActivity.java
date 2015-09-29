package com.sanshisoft.wenjiang.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.AppContext;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.adapter.AboutAdapter;
import com.sanshisoft.wenjiang.api.remote.RemoteApi;
import com.sanshisoft.wenjiang.base.BaseActivity;
import com.sanshisoft.wenjiang.bean.AboutBean;
import com.sanshisoft.wenjiang.bean.AboutList;
import com.sanshisoft.wenjiang.bean.UpdateBean;
import com.sanshisoft.wenjiang.bean.UpdateList;
import com.sanshisoft.wenjiang.common.DividerItemDecoration;
import com.sanshisoft.wenjiang.common.OnAboutClickListener;
import com.sanshisoft.wenjiang.service.UpdateService;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chenleicpp on 2015/9/28.
 */
public class AboutActivity extends BaseActivity implements OnAboutClickListener{

    @Bind(R.id.ib_titlebar_back)
    ImageView ibTitlebarBack;
    @Bind(R.id.ib_titlebar_category)
    ImageView ibTitlebarCategory;

    @Bind(R.id.about_recyclerview)
    RecyclerView mRecyclerView;
    private AboutAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<AboutBean> mDatas;

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
                startActivity(new Intent(AboutActivity.this, CategoryActivity.class));
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mDatas = new ArrayList<>();
        mAdapter = new AboutAdapter();
        mAdapter.setOnAboutClickListener(this);

        getData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    private void getData(){
        RemoteApi.getAboutusList(mHander,4);
    }

    private AsyncHttpResponseHandler mHander = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                String result = new String(responseBody,"gb2312");
                Gson gson = new Gson();
                Log.d("test", result);
                if (result != null && !StringUtils.isEmpty(result)){
                    AboutList al = gson.fromJson(result,AboutList.class);
                    List<AboutBean> datas = al.getData();
                    mAdapter.setList(datas);
                    mRecyclerView.setAdapter(mAdapter);
                }else {
                    ToastUtils.quickToast(AboutActivity.this, "服务器错误，请重试！");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ToastUtils.quickToast(AboutActivity.this, "服务器错误，请重试！");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtils.quickToast(AboutActivity.this, error.getMessage());
        }
    };

    @Override
    public void OnAboutClick(AboutBean ab) {
        if (ab.getTitle().equals("版本更新")){
            ToastUtils.quickToast(AboutActivity.this, "检测新版本，请稍后");
            RemoteApi.getUpdate(mHander2);
        }else {
            startActivity(FooterActivity.newIntent(this,ab.getDetailUrl()));
        }
    }

    private AsyncHttpResponseHandler mHander2 = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                String result = new String(responseBody,"gb2312");
                Gson gson = new Gson();
                Log.d("test", result);
                if (result != null && !StringUtils.isEmpty(result)){
                    UpdateList ul = gson.fromJson(result,UpdateList.class);
                    List<UpdateBean> datas = ul.getData();
                    UpdateBean updateBean = datas.get(0);
                    final String downloadUrl = updateBean.getDownloadUrl();
                    int remoteVesion = updateBean.getVersionCode();
                    int localVersion = AppContext.getInstance().getVersionCode();
                    if (remoteVesion > localVersion){
                        AlertDialog dialog = new AlertDialog.Builder(AboutActivity.this)
                                .setMessage("存在新版本，是否更新？")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(AboutActivity.this,UpdateService.class);
                                        intent.putExtra("download_url", downloadUrl);
                                        AboutActivity.this.startService(intent);
                                    }
                                }).create();
                        dialog.show();
                    }else {
                        ToastUtils.quickToast(AboutActivity.this, "没有检测到新版本");
                    }
                }else {
                    ToastUtils.quickToast(AboutActivity.this, "更新错误，请重试！");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ToastUtils.quickToast(AboutActivity.this, "服务器错误，请重试！");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtils.quickToast(AboutActivity.this, error.getMessage());
        }
    };
}
