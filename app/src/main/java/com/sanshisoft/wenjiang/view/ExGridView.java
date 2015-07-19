package com.sanshisoft.wenjiang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by chenleicpp on 2015/7/19.
 */
public class ExGridView extends GridView {

    public ExGridView(Context context) {
        super(context);
    }

    public ExGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
