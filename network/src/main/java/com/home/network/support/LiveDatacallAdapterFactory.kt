package com.home.network.support


import com.home.network.model.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * @author: 浩楠 2022/6/12/19:59
 * 用于将retrofit的返回数据，转换为livedata的adapter的工程类 */
class LiveDatacallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotation: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LivaData::class.java) {
            return null
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        if (rawObservableType != ApiResponse::class.java) {
            throw IllegalArgumentException("type must be a resonurce")
        }
        val bodyType = getParameterUpperBound(0, observableType)
        return LiveDatacallAdapter<Any>(bodyType)
    }
}