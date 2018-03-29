package com.sihaiwanlian.baselib.base

import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * FileName: com.mou.demo.mvp.BasePresenter.java
 * Author: mouxuefei
 * date: 2017/12/22
 * version: V1.0
 * desc:所有的presenter的基类
 */

abstract class BasePresenter {
   open val paramsCompareMap: TreeMap<String, Any> = TreeMap(Comparator { obj1, obj2 -> obj1.compareTo(obj2) })
    /**
     * 网络请求
     */
    protected fun <O> compose(lifecycle: LifecycleTransformer<O>): ObservableTransformer<O, O> {
        return ObservableTransformer { observable ->
            observable
                    .retry(2)//重连次数
                    .subscribeOn(Schedulers.io())//订阅者在io线程
                    .observeOn(AndroidSchedulers.mainThread())//观察者在主线程
                    .compose(lifecycle)
        }
    }


}