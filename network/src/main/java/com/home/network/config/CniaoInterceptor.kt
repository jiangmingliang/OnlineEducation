package com.home.network.config

import com.blankj.utilcode.util.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.Buffer

/**
 *
 * @author: 浩楠 2022/6/12/15:26
 */
class CniaoInterceptor : Interceptor {
    companion object {
        private val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .create()
    }

    private val mapType = object : TypeToken<Map<String, Any>>() {}.type

    override fun intercept(chain: Interceptor.Chain): Response {
        val orginRequest = chain.request()
        //附加的公共hander，封装clientInfo,deviceInfo等，也可以在post请求中，自定义封装handers的字段内容
        //注意这里，服务器用于校验的字段，只能是以下的字段内容，可以缺失，但是不能额外添加，因为服务器端未处理
        val attachHanders = mutableListOf<Pair<String, String>>(
            "appid" to NET_CONFIG_APPID,
            "platform" to "android",//如果重复请求，可能会报重复签名错误，vapi平台标记则不会
            "timestamp" to System.currentTimeMillis().toString(),
            "brand" to DeviceUtils.getManufacturer(),
            "model" to DeviceUtils.getModel(),
            "uuid" to DeviceUtils.getUniqueDeviceId(),
            "network" to NetworkUtils.getNetworkType().name,
            "system" to DeviceUtils.getSDKVersionName(),
            "version" to AppUtils.getAppVersionName()
        )
        //token仅有值的时候才传递
        val tokenstr =
            "eyJOeXAioiJKV1QiLCJhbGcioiJIUzI1NiJ9.eyJpYXQiOjE1OTgzNjYzNTAsIm5iZiI6MTU5ODM2NjM1MCwianRpLigiMD11NDkxYTctMWJ1Yy0ONDhkLWFhYWItN2Q4MGFkZDIwODI5TiwizXhwLiexNTk4OTcxMTUwLCJpzGVudG10eSI6Mjk3NQ4LCJmcmVzaCI6ZmFSc2UsInR5cGUioiJhY2N1c3MifQ.umlek01wJzgPCESPE_1bR2wRu5FLyjP2VUgkQuigwfE"
        val localToken = SPStaticUtils.getString(SP_KEY_USER_TOKEN, tokenstr)
        if (localToken.isNotEmpty()) {
            attachHanders.add("token" to localToken)

        }
        //get的请求，参数
        orginRequest.url.queryParameterNames.forEach { key ->
            attachHanders.add(key to (orginRequest.url.queryParameter(key) ?: ""))
        }
        //post的请求，frombody形式，或json形式的，都需要将内部的字段，遍历出来参与sign的计算
        val requestBody=orginRequest.body
        if (orginRequest.method=="POST"){
            if (requestBody is FromBody){
                for (i in 0 until requestBody.size){
                    attachHanders.add(requestBody.name(i) to requestBody.value(i))
                }
            }
            //json的body需要将requseBody反序列化为json转为map application/json
            if (requestBody?.contentType()?.type=="application" && requestBody.contentType()?.subtype=="json"){
                kotlin. runCatching {
                  val  buffer= Buffer()
                    requestBody.writeTo (buffer)
                    buffer.readBytestring () .utf8 ()
                    ).onSuccess (
                    var map  = gson.fromJson<Map<String, Any>> (it, mapType)
                    map. forEach { entry ->
                        //ΕIXME: 2020/8/25 value 目前json单层级
                        attachHanders.add(entry.key to entry.value.tostring())
                    }
                }
            }
        }
        //todo算法：都必须是非空参数！！ sign =MD5（ascii排序后的handers及parms的key=value拼接&后，最后拼接appkey和value）
        // 32位的大写
        val signValue=attachHanders
            .sortedBy { it.first }
            .joinToString("&") { "${it.first}$=${it.second}" }
            .plus("&appkey=$NET_CONFIG_APPKEY")
        val newBuilder=orginRequest.newBuilder()
            .cacheControl(CacheControl.FORCE_NETWORK)
        attachHanders.forEach{newBuilder.header(it.first,it.second)}
        newBuilder.header("sign",EncryptUtils.encryptMD5ToString(signValue))
        if (orginRequest.method=="POST" &&  requestBody!=null){
            newBuilder.post(requestBody)
        }else if (orginRequest.method=="GET"){
            newBuilder.get()
        }
        return chain.proceed(newBuilder.build())
    }
}