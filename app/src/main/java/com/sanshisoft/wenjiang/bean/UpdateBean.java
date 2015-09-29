package com.sanshisoft.wenjiang.bean;

/**
 * Created by chenleicpp on 2015/9/29.
 */
public class UpdateBean {
    private int versionCode;
    private String downloadUrl;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "versionCode=" + versionCode +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
