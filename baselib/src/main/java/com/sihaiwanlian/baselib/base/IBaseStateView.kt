package com.sihaiwanlian.baselib.base

/**
 * FileName: com.sihaiwanlian.baselib.base.IBaseView.java
 * Author: mouxuefei
 * date: 2017/12/22
 * version: V1.0
 * desc: 用于列表加载过程中出现的各种情况的封装
 */
interface IBaseStateView{
    fun showLoadingLayout()
    fun showRetryLayout()
    fun showEmptyLayout()
}