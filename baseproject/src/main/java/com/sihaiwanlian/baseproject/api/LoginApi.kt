package com.sihaiwanlian.baseproject.api

import com.sihaiwanlian.baselib.http.entity.BaseModel
import com.sihaiwanlian.baseproject.request.ForgetPwdRequest
import com.sihaiwanlian.baseproject.request.LoginRequest
import com.sihaiwanlian.baseproject.request.RegisterRequest
import com.sihaiwanlian.baseproject.result.ForgetPwdBean
import com.sihaiwanlian.baseproject.result.GetIdentityCodeBean
import com.sihaiwanlian.baseproject.result.LoginBean
import com.sihaiwanlian.baseproject.result.RegisterBean
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * FileName: com.sihaiwanlian.baseproject.api.LoginApi.java
 * Author: mouxuefei
 * date: 2018/3/20
 * version: V1.0
 * desc:
 */
interface LoginApi {
    /**
     *登录
     */
    @POST("accounts/login")
    fun login(@Body request: LoginRequest):
            Observable<BaseModel<LoginBean>>

    @GET("phone/verification-code")
    fun getIdentityCode(
            @Query("phone") phone: String,
            @Query("timestamp") timestmap: String,
            @Query("appKey") appKey: String,
            @Query("sign") sign: String
    ): Observable<GetIdentityCodeBean>

    @POST("accounts/register")
    fun register(@Body request: RegisterRequest):
            Observable<BaseModel<RegisterBean>>

    @POST("accounts/password/reset")
    fun forgetPwd(@Body request: ForgetPwdRequest):
            Observable<BaseModel<ForgetPwdBean>>
}