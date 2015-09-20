package com.sanshisoft.wenjiang.bean;

/**
 * Created by chenleicpp on 2015/9/20.
 * 特指温江农业图文混排实体类
 */
public class ImageNewBean {
    private int new_id;
    private String new_title;
    private String new_date;
    private String new_thumb;

    public int getNew_id() {
        return new_id;
    }

    public void setNew_id(int new_id) {
        this.new_id = new_id;
    }

    public String getNew_title() {
        return new_title;
    }

    public void setNew_title(String new_title) {
        this.new_title = new_title;
    }

    public String getNew_date() {
        return new_date;
    }

    public void setNew_date(String new_date) {
        this.new_date = new_date;
    }

    public String getNew_thumb() {
        return new_thumb;
    }

    public void setNew_thumb(String new_thumb) {
        this.new_thumb = new_thumb;
    }

    @Override
    public String toString() {
        return "ImageNewBean{" +
                "new_id=" + new_id +
                ", new_title='" + new_title + '\'' +
                ", new_date='" + new_date + '\'' +
                ", new_thumb='" + new_thumb + '\'' +
                '}';
    }
}
