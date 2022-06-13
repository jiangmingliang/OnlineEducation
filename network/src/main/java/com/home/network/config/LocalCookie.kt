package com.home.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 *
 * @author: 浩楠 2022/6/11/18:40
 */
internal class LocalCookieJar : CookieJar {
    //cookie的本地化存储
    private val cache= mutableListOf<Cookie>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        //过期的token
        val invalidCookies:MutableList<Cookie> =ArrayList()
        //有效的toekn
        val validCoookies:MutableList<Cookie> =ArrayList()
        for (cookie in cache){
            if (cookie.expiresAt<System.currentTimeMillis()){
                //判断是否过期
                invalidCookies.add(cookie)
            }else if(cookie.matches(url)){
                //匹配Cookie对应的url
                validCoookies.add(cookie)
            }
        }
        //缓存中移除过期的token
        cache.removeAll (invalidCookies)
        //返回List<Cookie> 让request进行设置
        return  validCoookies
    }

    /**
     * 将cookie保存
     */
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cache.addAll(cookies)
    }
}