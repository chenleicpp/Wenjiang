package com.sanshisoft.wenjiang.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.bean.NewsExBean;
import com.sanshisoft.wenjiang.common.OnNewsExClickListener;
import com.sanshisoft.wenjiang.ui.NewsDetailActivity;
import com.sanshisoft.wenjiang.ui.NewsExActivity;
import com.sanshisoft.wenjiang.utils.CommenUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenleicpp on 2015/9/11.
 */
public class NewsExRecyclerAdapter extends RecyclerView.Adapter<NewsExRecyclerAdapter.ViewHolder> {

    private List<NewsExBean> mDatas;
    private Context mContext;
    private OnNewsExClickListener mListener;

    public NewsExRecyclerAdapter(Context context){
        mContext = context;
    }

    public void setOnNewsExClickListener(OnNewsExClickListener listener){
        mListener = listener;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            NewsExBean neb = (NewsExBean) v.getTag();
            if (mListener != null) {
                mListener.OnNewsExClick(neb);
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_news_ex,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsExBean neb = mDatas.get(position);
        holder.mTvTitle.setText(neb.getNew_title());
        holder.mTvDate.setText(CommenUtils.parseDate(neb.getNew_date()));
        holder.mTvCategory.setText("【" + neb.getNew_category() + "】");
        holder.itemView.setTag(neb);
        holder.itemView.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_item_news_ex_title)
        TextView mTvTitle;
        @Bind(R.id.tv_item_news_ex_category)
        TextView mTvCategory;
        @Bind(R.id.tv_item_news_ex_date)
        TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setList(List<NewsExBean> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }
}
