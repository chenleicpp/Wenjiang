package com.sanshisoft.wenjiang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.bean.AboutBean;
import com.sanshisoft.wenjiang.common.OnAboutClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenleicpp on 2015/9/28.
 */
public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHodler> {

    private List<AboutBean> mDatas;
    private OnAboutClickListener mListener;

    public void setOnAboutClickListener(OnAboutClickListener listener){
        this.mListener = listener;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AboutBean ab = (AboutBean) v.getTag();
            if (mListener != null) {
                mListener.OnAboutClick(ab);
            }
        }
    };

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_about,parent,false);
        return new ViewHodler(item);
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {
        AboutBean ab = mDatas.get(position);
        holder.mTvTitle.setText(ab.getTitle());
        holder.itemView.setTag(ab);
        holder.itemView.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHodler extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_item_about_title)
        TextView mTvTitle;

        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setList(List<AboutBean> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }
}
