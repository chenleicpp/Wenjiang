package com.sanshisoft.wenjiang.bean;

/**
 * Created by chenleicpp on 2015/9/23.
 * 顶部滑动导航图片新闻bean
 */
public class SliderBean {
    private int id;
    private String title;
    private String newUrl;
    private String thumb;

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

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "SliderBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", newUrl='" + newUrl + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
