package com.sanshisoft.wenjiang.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
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
import com.sanshisoft.wenjiang.bean.ImageBean;
import com.sanshisoft.wenjiang.bean.NewsExBean;
import com.sanshisoft.wenjiang.common.OnImageClickListener;
import com.sanshisoft.wenjiang.common.OnNewsExClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenleicpp on 2015/9/13.
 * recycler样式的adapter
 */
public class NewsImageAdapter extends RecyclerView.Adapter<NewsImageAdapter.ViewHolder> {

    private List<ImageBean> mDatas;
    private DisplayImageOptions options;
    private OnImageClickListener mListener;

    public NewsImageAdapter(){
        options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                .considerExifParams(true).build();
    }

    public void setOnImageClickListener(OnImageClickListener listener){
        mListener = listener;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ImageBean ib = (ImageBean) v.getTag();
            if (mListener != null) {
                mListener.OnImageClick(ib);
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview_news,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageBean ib = mDatas.get(position);
        holder.mTvTitle.setText(ib.getNew_title());
        if (!ib.getNew_img().equals("pic/")){
            String url = AppConfig.BASE_URL + ib.getNew_img();
            ImageLoader.getInstance().displayImage(url, holder.mIvPhoto, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }
            });
        }else {
            holder.mIvPhoto.setImageResource(R.drawable.image_loading);
        }
        holder.itemView.setTag(ib);
        holder.itemView.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_image_news)
        ImageView mIvPhoto;
        @Bind(R.id.tv_image_title)
        TextView mTvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setList(List<ImageBean> list){
        this.mDatas = list;
        notifyDataSetChanged();
    }
}
