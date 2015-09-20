package com.sanshisoft.wenjiang.bean;

import java.util.List;

/**
 * Created by chenleicpp on 2015/9/20.
 */
public class ImageNewList {
    private int result_code;
    private String message;
    private int total_count;
    private List<ImageNewBean> data;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<ImageNewBean> getData() {
        return data;
    }

    public void setData(List<ImageNewBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ImageNewList{" +
                "result_code=" + result_code +
                ", message='" + message + '\'' +
                ", total_count=" + total_count +
                ", data=" + data +
                '}';
    }
}
