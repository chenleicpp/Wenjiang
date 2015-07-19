package com.sanshisoft.wenjiang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.sanshisoft.wenjiang.R;

import java.util.List;

/**
 * Created by chenleicpp on 2015/7/19.
 * 二级网站导航adapter
 */
public class CategoryAdapter<T> extends BaseAdapter {

    private Context mCtx;
    private LayoutInflater mInflater;
    private List<T> mListDatas;

    public CategoryAdapter(Context context){
        this.mCtx = context;
        this.mInflater = LayoutInflater.from(mCtx);
    }

    public void setList(List<T> list){
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
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_child_category,parent,false);
            holder.childCatogory = (Button) convertView.findViewById(R.id.btn_child_category);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private static class ViewHolder{
        private Button childCatogory;
    }

}
