package com.sanshisoft.wenjiang.bean;

/**
 * Created by chenleicpp on 2015/7/24.
 */
public class ImageBean {
    private int new_id;
    private String new_title;
    private String new_img;

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

    public String getNew_img() {
        return new_img;
    }

    public void setNew_img(String new_img) {
        this.new_img = new_img;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "new_id=" + new_id +
                ", new_title='" + new_title + '\'' +
                ", new_img='" + new_img + '\'' +
                '}';
    }
}
