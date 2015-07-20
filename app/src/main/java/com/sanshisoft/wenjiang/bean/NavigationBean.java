package com.sanshisoft.wenjiang.bean;

/**
 * Created by chenleicpp on 2015/7/19.
 * 导航子栏目实体类
 */
public class NavigationBean {
    private int category_type;
    private int category_id;
    private String category_name;

    public int getCategory_type() {
        return category_type;
    }

    public void setCategory_type(int category_type) {
        this.category_type = category_type;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "NavigationBean{" +
                "category_type=" + category_type +
                ", category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                '}';
    }
}
