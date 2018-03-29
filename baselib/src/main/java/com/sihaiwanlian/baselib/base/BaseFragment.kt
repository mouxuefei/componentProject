package com.sihaiwanlian.baselib.base
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sihaiwanlian.baselib.R
import com.sihaiwanlian.baselib.utils.EventBusUtils
import com.sihaiwanlian.baselib.utils.EventBusUtils.unregister
import com.sihaiwanlian.baselib.utils.ProgressDialogUtils

import com.squareup.leakcanary.RefWatcher

/**
 * @FileName: com.mou.demo.basekotlin.BaseFragment.java
 * @author: mouxuefei
 * @date: 2017-12-19 15:48
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
abstract class BaseFragment : Fragment() {
    companion object {
        val PARAMS = "PARAMS"
    }

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false
    protected var mContext: Context? = null
    private var progressDialog: ProgressDialogUtils? = null
    /**
     * 当前布局
     */
    private var rootView: View? = null
    internal var refWatcher: RefWatcher? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        if (null != rootView) {
//            val parent = rootView?.parent as ViewGroup
//            parent.removeView(rootView)
//        } else {
//        }
        val layout = getContentView()
        rootView = inflater.inflate(layout, container, false)
        this.mContext = context

        if (useEventBus()) {
           EventBusUtils.register(context)
        }
        //leakcanary工具，在ondestory中检测内存泄漏
        refWatcher = LibApplication.getRefWatcher(context)
        return rootView
    }

    open fun savedstanceState(savedInstanceState: Bundle?) {}

    protected abstract fun initData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        initDialog()
        initView(view)
        savedstanceState(savedInstanceState)
        initData()
        lazyLoadDataIfPrepared()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    protected abstract fun lazyLoad()


    private fun initDialog() {
        progressDialog = ProgressDialogUtils(context, R.style.dialog_transparent_style)
    }


    fun showToastBottom(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 是否使用eventBus,默认为使用(false)，
     *
     * @return
     */
    open fun useEventBus(): Boolean {
        return false
    }

    /**
     * init view
     */
    protected abstract fun getContentView(): Int

    protected abstract fun initView(view: View)


    /**
     * 显示加载的ProgressDialog
     */
    fun showProgressDialog() {
        progressDialog?.showProgressDialog()
    }

    /**
     * 显示有加载文字ProgressDialog，文字显示在ProgressDialog的下面
     */
    fun showProgressDialog(text: String) {
        progressDialog?.showProgressDialogWithText(text)
    }

    /**
     * 隐藏加载的ProgressDialog
     */
    fun dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog?.dismissProgressDialog()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (useEventBus()) {
           unregister(context)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (refWatcher != null) {
            refWatcher?.watch(this)
        }
    }
}