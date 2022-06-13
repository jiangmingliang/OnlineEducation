package com.home.network


/**
 *
 * @author: 浩楠 2022/6/11/16:06
 * 网络请求统一接口类
 */
interface HttpApi {

    /**
     * 抽象的http的get请求封装,异步
     */
    fun get(params: Map<String, Any>, path: String, callback: IHttpCallback) {}

    /**
     * 抽象的http的get请求封装,同步
     */
    fun getSync(params: Map<String, Any>, path: String): Any? {
        return Any()
    }

    /**
     * 抽象的http的post请求封装，异步
     */
    fun post(body: Any, path: String, callback: IHttpCallback) {}

    /**
     * 抽象的http的post请求封装，同步
     */
    fun postSync(body: Any, path: String): Any? = Any()

    /**
     *标记函数
     * [cancelRequest] 取消一个请求
     * [cancelAllRequest] 取消所有请求
     */
    fun cancelRequest(tag:Any)
    fun cancelAllRequest()
}