package com.sanshisoft.wenjiang.bean;

/**
 * Created by chenleicpp on 2015/7/19.
 * 导航子栏目实体类
 */
public class NavigationBean {
    private int type;
    private int id;
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NavigationBean{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", id=" + id +
                '}';
    }
}
