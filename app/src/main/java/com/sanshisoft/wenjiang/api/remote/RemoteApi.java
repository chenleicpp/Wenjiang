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

    /**
     * 获取四级新闻详情
     * @param handler
     * @param categoryId
     * @param newId
     */
    public static void getNavNewsDetail(AsyncHttpResponseHandler handler,int categoryId,int newId){
        String url = "getNavNewsDetail.asp";
        RequestParams params = new RequestParams();
        params.put("category_id",categoryId);
        params.put("new_id",newId);
        ApiHttpClient.post(url,params,handler);
    }

    /**
     * 首页图片新闻列表页（温江特产、休闲农业）
     * @param hanlder
     * @param type 大类id和小类id
     * @param num
     * @param size
     */
    public static void getWjtcList(AsyncHttpResponseHandler hanlder,int type,int num,int size){
        String url = "getWjtcList.asp";
        RequestParams params = new RequestParams();
        params.put("type",type);
        params.put("page_num",num);
        params.put("page_size",size);
        ApiHttpClient.post(url,params,hanlder);
    }

    /**
     * 首页文字新闻集合列表页（政务服务、专题专栏、党建工作）
     * @param handler
     * @param type
     * @param num
     * @param size
     */
    public static void getDjgzList(AsyncHttpResponseHandler handler,int type,int num,int size){
        String url = "getDjgzList.asp";
        RequestParams params = new RequestParams();
        params.put("type",type);
        params.put("page_num",num);
        params.put("page_size",size);
        ApiHttpClient.post(url,params,handler);
    }
}
