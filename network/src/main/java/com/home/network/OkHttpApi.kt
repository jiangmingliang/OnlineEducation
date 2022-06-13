package com.home.network

import androidx.collection.SimpleArrayMap
import com.google.gson.Gson
import com.home.network.config.CniaoInterceptor
import com.home.network.config.KtHttpLogInterceptor
import com.home.network.config.RetryIntercepter
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.io.File
import java.util.concurrent.TimeUnit

/**
 *
 * @author: 浩楠 2022/6/11/16:23
 */
class OkHttpApi private constructor(): HttpApi {


    private var baseurl = "http://api.qingyunke.com/"
    var maxRetry = 0//最大重试次数
    private val callMap = SimpleArrayMap<Any, Call>()

    /**
     * OkHttpClient
     */
    private var mClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)//完整请求超时时长，从发起到接收返回数据，默认值0，不限定
        .connectTimeout(10, TimeUnit.SECONDS)//与服务器建立连接时长，默认10s
        .readTimeout(10, TimeUnit.SECONDS)//读取服务器返回的数据时长
        .writeTimeout(10, TimeUnit.SECONDS)//向服务器写入数据的时长，默认10s
        .retryOnConnectionFailure(true)//重连
        .followRedirects(false)//重定向
        .cache(Cache(File("sdcard/cache", "okhttp"), 1024))//
        //        .cookieJar(CookieJar.NO_COOKIES)
        .cookieJar(LocalCookieJar())
        .addNetworkInterceptor(CniaoInterceptor())//公共hander拦截器
        .addNetworkInterceptor(KtHttpLogInterceptor {
            logLevel(KtHttpLogInterceptor.LogLevel.BODY)
            logTag("HHHDH")
        }) //添加网络拦截器,可以对okHttp的网络请求做拦截处理,不同于应用拦截器,这里能感知所有网络状态,比如重定向。
        .addNetworkInterceptor(RetryIntercepter(maxRetry))
        .build()

    /**
     * get异步
     */
    override fun get(params: Map<String, Any>, path: String, callback: IHttpCallback) {
        val url = "$baseurl$path"
        val urlBuilder = url.toHttpUrl().newBuilder()
        params.forEach { entry ->
            urlBuilder.addEncodedQueryParameter(entry.key, entry.value.toString())
        }
        val request: Request = Request.Builder()
            .get()
            .tag(params)
            .url(urlBuilder.build())
            .cacheControl(CacheControl.FORCE_CACHE)
            .build()
        val newCall=mClient.newCall(request)
        //存储请求，用于取消
        callMap.put(request.tag(),newCall)
        newCall.enqueue(object :Callback{
            override fun onFailure(call: Call, e: java.io.IOException) {
                callback.onFailed(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(response.body?.string())
            }
        })
    }


    /**
     * post异步
     */
    override fun post(body: Any, urlStr: String, callback: IHttpCallback) {
        val reqBody=Gson().toJson(body).toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .post(reqBody)
            .url(urlStr)
            .tag(body)
            .build()
        val newCall=mClient.newCall(request)
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailed(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(response.body?.string())
            }
        })
    }

    /**
     * 取消网络请求，tab每次请求的id标记，也就是请求的传参
     */
    override fun cancelRequest(tag: Any) {
        callMap.get(tag)?.cancel()
    }

    /**
     * 取消所有网络请求
     */
    override fun cancelAllRequest() {
        for (i in 0 until callMap.size()){
            callMap.get(callMap.keyAt(i))?.cancel()
        }
    }
}