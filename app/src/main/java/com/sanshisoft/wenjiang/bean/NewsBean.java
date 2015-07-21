package com.sanshisoft.wenjiang.bean;

/**
 * Created by chenleicpp on 2015/7/21.
 */
public class NewsBean {
    private int id;
    private String title;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
