package com.sanshisoft.wenjiang.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.percolate.caffeine.ToastUtils;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.bean.NavigationBean;
import com.sanshisoft.wenjiang.ui.NewsActivity;

import java.util.List;

/**
 * Created by chenleicpp on 2015/7/19.
 * 二级网站导航adapter
 */
public class CategoryAdapter extends BaseAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;
    private List<NavigationBean> mListDatas;

    public CategoryAdapter(Context context){
        this.mCtx = context;
        this.mInflater = LayoutInflater.from(mCtx);
    }

    public void setList(List<NavigationBean> list){
        this.mListDatas = list;
    }

    @Override
    public int getCount() {
        return mListDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mListDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_child_category,parent,false);
            holder.childCatogory = (Button) convertView.findViewById(R.id.btn_child_category);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NavigationBean navi = mListDatas.get(position);
        holder.childCatogory.setText(navi.getCategory_name());
        holder.childCatogory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mCtx, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(NewsActivity.NEWS_ID,navi.getCategory_id());
                bundle.putInt(NewsActivity.NEWS_TYPE,navi.getCategory_type());
                intent.putExtras(bundle);
                mCtx.startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder{
        private Button childCatogory;
    }

}
