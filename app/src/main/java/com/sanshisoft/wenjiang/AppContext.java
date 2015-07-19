package com.sanshisoft.wenjiang;

import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;
import com.sanshisoft.wenjiang.api.ApiHttpClient;

/**
 * Created by chenleicpp on 2015/7/19.
 */
public class AppContext extends Application{

    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    private void init(){
        AsyncHttpClient client = new AsyncHttpClient();
        ApiHttpClient.setHttpClient(client);
    }
}
