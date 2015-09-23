package com.sanshisoft.wenjiang.bean;

import java.util.List;

/**
 * Created by chenleicpp on 2015/9/23.
 */
public class SliderList {
    private int result_code;
    private List<SliderBean> data;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public List<SliderBean> getData() {
        return data;
    }

    public void setData(List<SliderBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SliderList{" +
                "result_code=" + result_code +
                ", data=" + data +
                '}';
    }
}
