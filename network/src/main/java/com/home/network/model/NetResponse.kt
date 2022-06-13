package com.home.network.model

/**
 *
 * @author: 浩楠 2022/6/12/16:03
 * 基础的网络返回数据结构
 */
data  class NetResponse (
    val code:Int,//响应码
    val data:Any?,//响应内容
    val message:String,//响应数据的结果描述
)