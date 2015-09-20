package com.sanshisoft.wenjiang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.bean.NewsBean;
import com.sanshisoft.wenjiang.bean.NewsExBean;
import com.sanshisoft.wenjiang.common.OnNewsClickListener;
import com.sanshisoft.wenjiang.common.OnNewsExClickListener;
import com.sanshisoft.wenjiang.utils.CommenUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenleicpp on 2015/9/11.
 * 普通列表页（公示公告，农业资讯，农业服务）
 */
public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    private List<NewsBean> mDatas;
    private Context mContext;
    private OnNewsClickListener mListener;

    public NewsRecyclerAdapter(Context context){
        mContext = context;
    }

    public void setOnNewsClickListener(OnNewsClickListener listener){
        mListener = listener;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            NewsBean nb = (NewsBean) v.getTag();
            if (mListener != null) {
                mListener.OnNewsClick(nb);
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_news,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsBean nb = mDatas.get(position);
        holder.mTvTitle.setText(nb.getTitle());
        holder.mTvDate.setText(CommenUtils.parseDate(nb.getDate()));
        holder.itemView.setTag(nb);
        holder.itemView.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_item_news_title)
        TextView mTvTitle;
        @Bind(R.id.tv_item_news_date)
        TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setList(List<NewsBean> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }
}
