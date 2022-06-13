package com.home.network

/**
 *
 * @author: 浩楠 2022/6/11/16:11
 * 网络请求的接口回调
 */
interface IHttpCallback {
    /**
     * 网络请求成功回调
     * [data]返回回调的数据
     * @param data返回回调的数据结果
     */
    fun onSuccess(data:Any?)

    /**
     * 网络请求失败回调
     * [error]返回错误的数据类
     */
    fun  onFailed(error:Any?)
}