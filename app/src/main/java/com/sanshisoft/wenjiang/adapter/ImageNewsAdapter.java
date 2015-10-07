package com.sanshisoft.wenjiang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.sanshisoft.wenjiang.AppConfig;
import com.sanshisoft.wenjiang.R;
import com.sanshisoft.wenjiang.bean.ImageBean;

import java.util.List;

/**
 * Created by chenleicpp on 2015/7/21.
 */
@Deprecated
public class ImageNewsAdapter extends BaseAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;
    private List<ImageBean> mDatas;
    DisplayImageOptions options;

    public ImageNewsAdapter(Context context){
        mCtx = context;
        mInflater = LayoutInflater.from(mCtx);
        options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                .considerExifParams(true).build();
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
            convertView = mInflater.inflate(R.layout.item_gridview_news,parent,false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_image_title);
            holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_image_news);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ImageBean ib = mDatas.get(position);
        holder.tvTitle.setText(ib.getNew_title());
        if (!ib.getNew_img().equals("pic/")){
            String url = AppConfig.BASE_URL + ib.getNew_img();
            ImageLoader.getInstance().displayImage(url, holder.ivPic, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }
            });
        }else {
            holder.ivPic.setImageResource(R.drawable.image_loading_default);
        }
        return convertView;
    }

    public void setList(List<ImageBean> list){
        this.mDatas = list;
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        private ImageView ivPic;
        private TextView tvTitle;
    }
}
