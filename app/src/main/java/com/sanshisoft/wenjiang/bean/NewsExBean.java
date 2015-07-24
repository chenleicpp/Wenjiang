package com.sanshisoft.wenjiang.bean;

/**
 * Created by chenleicpp on 2015/7/24.
 */
public class NewsExBean {
    private int new_id;
    private String new_title;
    private String new_date;
    private String new_category;

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

    public String getNew_category() {
        return new_category;
    }

    public void setNew_category(String new_category) {
        this.new_category = new_category;
    }

    @Override
    public String toString() {
        return "NewsExBean{" +
                "new_id=" + new_id +
                ", new_title='" + new_title + '\'' +
                ", new_date='" + new_date + '\'' +
                ", new_category='" + new_category + '\'' +
                '}';
    }
}
