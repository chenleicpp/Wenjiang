package com.sanshisoft.wenjiang.api.remote;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sanshisoft.wenjiang.api.ApiHttpClient;

/**
 * Created by chenleicpp on 2015/7/19.
 */
public class RemoteApi {
    /**
     * 获取二级网站导航
     * @param handler
     */
    public static void getNavigation(AsyncHttpResponseHandler handler){
        String navigationUrl = "getNavigation.asp";
        ApiHttpClient.post(navigationUrl, handler);
    }
}
