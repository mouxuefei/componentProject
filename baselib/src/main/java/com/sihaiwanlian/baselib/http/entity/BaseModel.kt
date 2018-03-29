package com.sihaiwanlian.baselib.http.entity

import com.google.gson.annotations.SerializedName

/**
 * @FileName: com.mou.demo.http.entity.BaseModel.java
 * @author: mouxuefei
 * @date: 2017-12-21 14:46
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class BaseModel<E> {
    @SerializedName("code")
    var code: Int = 0
    @SerializedName("code_msg")
    var code_msg: String? = null
    @SerializedName("result")
    var result: E? = null
}