package com.sanshisoft.wenjiang.bean;

import java.util.List;

/**
 * Created by chenleicpp on 2015/9/29.
 */
public class UpdateList {
    private int result_code;
    private int total_count;
    private String message;
    private List<UpdateBean> data;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UpdateBean> getData() {
        return data;
    }

    public void setData(List<UpdateBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UpdateList{" +
                "result_code=" + result_code +
                ", total_count=" + total_count +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
