package com.ejlchina.http;

import com.ejlchina.http.internal.AsyncHttpTask;
import com.ejlchina.http.internal.SyncHttpTask;
import com.ejlchina.http.internal.HttpClient.Builder;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * HTTP 客户端接口
 * @author 15735
 *
 */
public interface HTTP {

	/**
	 * 异步请求
	 * @param url 请求地址
	 * @return 异步HTTP任务
	 */
    AsyncHttpTask async(String url);

	/**
	 * 同步请求
	 * @param url 请求地址
	 * @return 同步HTTP任务
	 */
    SyncHttpTask sync(String url);
    
    /**
     * 根据标签取消HTTP任务，只要任务的标签包含指定的Tag就会被取消
     * @param tag 标签
     * @return 被取消的任务数量
     */
    int cancel(String tag);
    
    /**
     * OkHttp 原生请求 （该请求不经过 预处理器）
     * @param request 请求
     * @return Call
     */
    Call request(Request request);
    
    /**
     * Websocket（该请求不经过 预处理器）
     * @param request 请求
     * @param listener 监听器
     * @return
     */
    WebSocket webSocket(Request request, WebSocketListener listener);
    
    /**
     * HTTP 构建器
     * @return HTTP 构建器
     */
	static Builder builder() {
		return new Builder();
	}
    
}
