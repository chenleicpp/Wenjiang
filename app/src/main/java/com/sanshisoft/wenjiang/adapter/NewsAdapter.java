package com.sanshisoft.wenjiang.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.bean.NavigationBean;
import com.sanshisoft.wenjiang.bean.NewsBean;
import com.sanshisoft.wenjiang.ui.NewsActivity;

import java.util.List;

/**
 * Created by chenleicpp on 2015/7/21.
 */
public class NewsAdapter extends BaseAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;
    private List<NewsBean> mDatas;

    public NewsAdapter(Context context){
        mCtx = context;
        mInflater = LayoutInflater.from(mCtx);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
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
            convertView = mInflater.inflate(R.layout.item_list_news,parent,false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_news_title);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_item_news_date);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NewsBean nb = mDatas.get(position);
        holder.tvTitle.setText(nb.getTitle());

        return convertView;
    }

    public void setList(List<NewsBean> list){
        this.mDatas = list;
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        private TextView tvTitle;
        private TextView tvDate;
    }
}
