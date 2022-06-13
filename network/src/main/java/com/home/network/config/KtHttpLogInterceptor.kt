package com.home.network.config

import android.util.Log
import okhttp3.*
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author: 浩楠 2022/6/11/18:44
 * 用于记录okhttp的网络日志的拦截器
 */
class KtHttpLogInterceptor(bleock: (KtHttpLogInterceptor.() -> Unit)? = null) :
    Interceptor {
    private var logLevel: LogLevel = LogLevel.NONE//打印日期的标记
    private var colorLevel: ColorLovel = ColorLovel.DEBUG//默认是debug级别的logcat
    private var logTag = TAG//日志的logcat的tag

    init {
        bleock?.invoke(this)
    }

    /**
     * 设置LogLevel
     */
    fun logLevel(level: LogLevel): KtHttpLogInterceptor {
        logLevel = level
        return this
    }

    /**
     * 设置颜色ColorLevel
     */
    fun colorLevel(level: ColorLovel): KtHttpLogInterceptor {
        colorLevel = level
        return this
    }

    /**
     * 设置Log的TAG
     */
    fun logTag(tag: String): KtHttpLogInterceptor {
        logTag = tag
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        //请求
        val request = chain.request()
        //响应
        return kotlin.runCatching { chain.proceed(request) }
            .onFailure {
                it.printStackTrace()
                logIT(
                    it.message.toString(),
                    ColorLovel.ERROR
                )
            }.onSuccess { response ->
                if (logLevel == LogLevel.NONE) {
                    return response
                }
                //记录请求日志
                logRequest(request,chain.connection())
                //记录响应日志
                logResponse(response)
            }.getOrThrow()
    }

    /**
     * 记录请求日志
     */
    private fun logRequest(request: Request, connection: Connection?) {
        val sb = StringBuilder()
        sb.appendln("\r\n")
        sb.appendln("->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->")
        when (logLevel) {
            LogLevel.NONE -> {
                /*do nothing*/
            }
            LogLevel.BASIC -> {
                logBasicReq(sb, request, connection)
            }
            LogLevel.HEADERS -> {
                logHeadersReq(sb, request, connection)
            }
            LogLevel.BODY -> {
                logBodyReq(sb, request, connection)
            }
        }
        sb.appendln("->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->")
        logIT(sb)
    }
    //region log request

    private fun logBodyReq(sb: StringBuilder, request: Request, connection: Connection?) {
        logHeadersReq(sb, request, connection)
        sb.appendln("RequestBody：${request.body.toString()}$")
    }

    private fun logHeadersReq(sb: StringBuilder, request: Request, connection: Connection?) {
        logBasicReq(sb, request, connection)
        val headerStr = request.headers.joinToString("") { header ->
            "请求 Heander：{${header.first}=${header.second}}\n"
        }
        sb.appendln(headerStr)
    }

    private fun logBasicReq(sb: StringBuilder, request: Request, connection: Connection?) {
        sb.appendln("请求 method: ${request.method} url: ${decodeUrlStr(request.url.toString())} tag: ${request.tag()} protocol: ${connection?.protocol() ?: Protocol.HTTP_1_1}")

    }
    //endregion
    /**
     *记录响应日志
     * [response] 响应数据
     */
    private fun logResponse(response: Response) {
        val sb = StringBuffer()
        sb.appendln("\r\n")
        sb.appendln("<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<")
        when (logLevel) {
            LogLevel.NONE -> {
                /*do nothing*/
            }
            LogLevel.BASIC -> {
                logBasicRep(sb, response)
            }
            LogLevel.HEADERS -> {
                logHeadersRep(response, sb)
            }
            LogLevel.BODY -> {
                logHeadersRep(response, sb)
                //body.string会抛IO异常
                kotlin.runCatching {
                    //peek类似于clone数据流，监视，窥探，不能直接用原来的body的string流数据作为日志，会消费掉io，所以这里是peek，监测
                    val peekBody = response.peekBody(1024 * 1024)
                    sb.appendln(peekBody.string())
                }.getOrNull()
            }
        }
        sb.appendln("<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<")
        logIT(sb, ColorLovel.INFO)
    }

    //region log request
    private fun logHeadersRep(response: Response, sb: StringBuffer) {
        logBasicRep(sb, response)
        val headersStr = response.headers.joinToString(separator = "") { header ->
            "响应 Hander: {${header.first}=${header.second}}\n"
        }
        sb.appendln(headersStr)
    }

    private fun logBasicRep(sb: StringBuffer, response: Response) {
        sb.appendln("响应 protocol: ${response.protocol} code: ${response.code} message: ${response.message}")
            .appendln("响应 request Url: ${decodeUrlStr(response.request.url.toString())}")
            .appendln(
                "响应 sentRequestTime: ${
                    toDateTimeStr(
                        response.sentRequestAtMillis,
                        MILIS_PATTERN
                    )
                } receivedResponseTime: ${
                    toDateTimeStr(
                        response.receivedResponseAtMillis,
                        MILIS_PATTERN
                    )
                }"
            )
    }
    //endregion
    /**
     * 对于url编码的string解码
     */
    private fun decodeUrlStr(url: String): String? {
        return kotlin.runCatching { URLDecoder.decode(url, "utf-8") }
            .onFailure { it.printStackTrace() }.getOrNull()
    }

    /**
     *打印日志
     * [any] 数据的对象
     * [tempLevel] 便于临时调整打印等级的颜色
     */
    private fun logIT(any: Any, tempLevel: ColorLovel? = null) {
        when (tempLevel ?: colorLevel) {
            ColorLovel.VERBOSE -> Log.v(logTag, any.toString())
            ColorLovel.DEBUG -> Log.d(logTag, any.toString())
            ColorLovel.INFO -> Log.i(logTag, any.toString())
            ColorLovel.WARN -> Log.w(logTag, any.toString())
            ColorLovel.ERROR -> Log.e(logTag, any.toString())
        }
    }


    companion object {
        private const val TAG = "<KtHttp>"//默认的TAG

        //时间格式
        const val MILIS_PATTERN = "yyyy-MM--dd HH:mm:ss.SSSXXX"

        //转化为格式化的时间字符串
        fun toDateTimeStr(millis: Long, pattern: String): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(millis)
        }
    }

    /**
     * 打印日志的范围
     */
    enum class LogLevel {
        NONE,//不打印
        BASIC,//只打印行首，请求/响应
        HEADERS,//打印请求和响应的所有header
        BODY,//打印所有
    }

    /**
     * log颜色等级，应用于Android logcat分为 v，d，i，w，e
     */
    enum class ColorLovel {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }
}