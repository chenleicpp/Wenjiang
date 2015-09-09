package com.sanshisoft.wenjiang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanshisoft.wenjiang.R;

/**
 * Created by chenleicpp on 2015/9/9.
 */
public class NewsImageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_image,container,false);
        return view;
    }
}
