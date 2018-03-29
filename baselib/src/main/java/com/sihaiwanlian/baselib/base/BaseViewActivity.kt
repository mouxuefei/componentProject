package com.sihaiwanlian.baselib.base

import com.sihaiwanlian.baselib.base.BaseActivity


/**
 * FileName: com.mou.demo.base.BaseViewActivity.java
 * Author: mouxuefei
 * date: 2017/12/25
 * version: V1.0
 * desc:
 */
abstract class BaseViewActivity : BaseActivity(), IBaseView {
    override fun showLoading() {
        showProgressDialog("加载中...")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }
}