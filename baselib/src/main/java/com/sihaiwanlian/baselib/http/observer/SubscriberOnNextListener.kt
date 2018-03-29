package com.sihaiwanlian.baselib.http.observer

/**
 * @FileName: com.mou.demo.http.observer.SubscriberOnNextListener.java
 * @author: mouxuefei
 * @date: 2017-12-21 16:58
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
interface SubscriberOnNextListener<T> {
    fun onNext(t: T)
}