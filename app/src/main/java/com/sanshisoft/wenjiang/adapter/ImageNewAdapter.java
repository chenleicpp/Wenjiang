package com.sanshisoft.wenjiang.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.sanshisoft.wenjiang.AppConfig;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.bean.ImageNewBean;
import com.sanshisoft.wenjiang.common.OnImageNewClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenleicpp on 2015/9/20.
 * 温江农业adapter
 */
public class ImageNewAdapter extends RecyclerView.Adapter<ImageNewAdapter.ViewHolder> {

    private List<ImageNewBean> mDatas;
    private DisplayImageOptions options;
    private OnImageNewClickListener mListener;

    public ImageNewAdapter(){
        options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                .considerExifParams(true).build();
    }

    public void setOnImageNewClickListener(OnImageNewClickListener listener){
        mListener = listener;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ImageNewBean inb = (ImageNewBean) v.getTag();
            if (mListener != null) {
                mListener.OnImageClick(inb);
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_news_wjny,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageNewBean inb = mDatas.get(position);
        holder.mTvTitle.setText(inb.getNew_title());
        holder.mTvDate.setText(inb.getNew_date());
        if (!inb.getNew_thumb().equals("/pic/")){
            String url = AppConfig.BASE_URL + inb.getNew_thumb();
            ImageLoader.getInstance().displayImage(url, holder.mIvThumb, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }
            });
        }else {
            holder.mIvThumb.setImageResource(R.drawable.image_loading_default);
        }
        holder.itemView.setTag(inb);
        holder.itemView.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_wjny_thumb)
        ImageView mIvThumb;
        @Bind(R.id.tv_wjny_title)
        TextView mTvTitle;
        @Bind(R.id.tv_wjny_date)
        TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setList(List<ImageNewBean> list){
        this.mDatas = list;
        notifyDataSetChanged();
    }

}
