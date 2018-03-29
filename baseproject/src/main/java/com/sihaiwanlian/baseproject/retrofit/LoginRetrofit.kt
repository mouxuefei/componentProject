package com.sihaiwanlian.baseproject.retrofit

import com.mou.blog.http.retrofit.RetrofitFactory
import com.sihaiwanlian.baseproject.api.LoginApi

/**
 * FileName: com.sihaiwanlian.baseproject.retrofit.LoginRetrofit.java
 * Author: mouxuefei
 * date: 2018/3/20
 * version: V1.0
 * desc:
 */

object LoginRetrofit : RetrofitFactory<LoginApi>() {

    override fun getApiService(): Class<LoginApi> {
        return LoginApi::class.java
    }
}