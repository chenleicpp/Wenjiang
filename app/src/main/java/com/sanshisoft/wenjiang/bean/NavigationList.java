package com.sanshisoft.wenjiang.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/20.
 * Navigation结果集返回
 */
public class NavigationList {
    private int result_code;
    private List<NavigationBean> data;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public List<NavigationBean> getData() {
        return data;
    }

    public void setData(List<NavigationBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NavigationList{" +
                "result_code=" + result_code +
                ", data=" + data +
                '}';
    }
}
