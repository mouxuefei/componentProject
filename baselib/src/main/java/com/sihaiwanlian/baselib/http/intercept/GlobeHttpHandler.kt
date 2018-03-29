package com.sihaiwanlian.baselib.http.intercept

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @FileName: com.mou.demo.http.intercept.GlobeHttpHandler.java
 * @author: mouxuefei
 * @date: 2017-12-21 16:37
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
interface GlobeHttpHandler{
    abstract fun onHttpResultResponse(httpResult: String, chain: Interceptor.Chain, response: Response): Response

    abstract fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request


}