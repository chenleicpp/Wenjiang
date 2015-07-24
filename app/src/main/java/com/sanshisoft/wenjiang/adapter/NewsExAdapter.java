package com.sanshisoft.wenjiang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.bean.NewsBean;
import com.sanshisoft.wenjiang.bean.NewsExBean;
import com.sanshisoft.wenjiang.utils.CommenUtils;

import java.util.List;

/**
 * Created by chenleicpp on 2015/7/21.
 */
public class NewsExAdapter extends BaseAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;
    private List<NewsExBean> mDatas;

    public NewsExAdapter(Context context){
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
            convertView = mInflater.inflate(R.layout.item_list_news_ex,parent,false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_news_ex_title);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_item_news_ex_date);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_item_news_ex_category);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NewsExBean neb = mDatas.get(position);
        holder.tvTitle.setText(neb.getNew_title());
        holder.tvDate.setText(CommenUtils.parseDate(neb.getNew_date()));
        holder.tvCategory.setText("【"+neb.getNew_category()+"】");
        return convertView;
    }

    public void setList(List<NewsExBean> list){
        this.mDatas = list;
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvCategory;
    }
}
