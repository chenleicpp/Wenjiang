package com.sanshisoft.wenjiang.api.remote;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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

    /**
     * 获取三级新闻列表
     * @param handler
     * @param id
     * @param num
     * @param size
     */
    public static void getNavNewsList(AsyncHttpResponseHandler handler,int id,int num,int size){
        String url = "getNavNewsList.asp";
        RequestParams params = new RequestParams();
        params.put("category_id",id);
        params.put("page_num",num);
        params.put("page_size",size);
        ApiHttpClient.post(url,params,handler);
    }
}
