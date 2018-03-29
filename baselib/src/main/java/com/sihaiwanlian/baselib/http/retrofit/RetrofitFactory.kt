package com.mou.blog.http.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import com.sihaiwanlian.baselib.http.constant.URLConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * FileName: com.mou.demo.http.retrofit.RetrofitFactory.java
 * Author: mouxuefei
 * date: 2017/12/22
 * version: V1.0
 * desc:
 */
abstract class RetrofitFactory<T> {
    private val TIMEOUT: Long = 15//超时时间
    var apiService: T

    init {
        val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val builder = chain.request().newBuilder()
                    // 添加请求头header
                    //builder.addHeader("token", "123");
                    chain.proceed(builder.build())
                }
                .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                    Logger.e("message${message}")
                }).setLevel(HttpLoggingInterceptor.Level.BODY))//设置打印得日志内容
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()

        apiService = Retrofit.Builder()
                .baseUrl(URLConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(buildGson())) // 添加Gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Retrofit到RxJava的转换器
                .client(httpClient)
                .build()
                .create(getApiService())
    }

    abstract fun getApiService(): Class<T>

    private fun buildGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
    }

    fun getService(): T {
        return apiService
    }
}