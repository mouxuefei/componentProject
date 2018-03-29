package com.sihaiwanlian.baselib.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

import com.jaeger.library.StatusBarUtil
import com.sihaiwanlian.baselib.R
import com.sihaiwanlian.baselib.base.LibApplication.Companion.getRefWatcher
import com.sihaiwanlian.baselib.utils.ActivityUtils.pushActivity
import com.sihaiwanlian.baselib.utils.ActivityUtils.removeActivity
import com.sihaiwanlian.baselib.utils.CleanLeakUtils.fixHuaWeiMemoryLeak
import com.sihaiwanlian.baselib.utils.CleanLeakUtils.fixInputMethodManagerLeak
import com.sihaiwanlian.baselib.utils.EventBusUtils
import com.sihaiwanlian.baselib.utils.ProgressDialogUtils
import com.squareup.leakcanary.RefWatcher
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

/**
 * @FileName: com.mou.demo.basekotlin.BaseActivity.java
 * @author: mouxuefei
 * @date: 2017-12-19 15:05
 * @version V1.0 Activity的基类
 * @desc
 */
abstract class BaseActivity : RxAppCompatActivity() {
    protected var mContext: Context by Delegates.notNull()//非空属性:Delegates.notNull()
    protected var progressDialog: ProgressDialogUtils by Delegates.notNull()
    protected var refWatcher: RefWatcher? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//强制竖屏
        mContext = this
        setContentView(getContentView())
        setStatusBar()
        if (useEventBus()) {
            //eventbus注册
            EventBusUtils.register(this)
        }
        initView()
        onSetContentViewNext(savedInstanceState)
        pushActivity(this)
        initDialog()
        initData()
        //leakcanary工具，在ondestory中检测内存泄漏
        refWatcher = getRefWatcher(this)
    }

    open fun isTitleActivity(): Boolean = false


    /**
     * 网络请求
     */
    protected fun <T> compose(lifecycle: LifecycleTransformer<T>): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                    .retry(2)//重连次数
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { }
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle)
        }
    }

    abstract fun initData()

    private fun initDialog() {
        progressDialog = ProgressDialogUtils(this, R.style.dialog_transparent_style)
    }

    open fun onSetContentViewNext(savedInstanceState: Bundle?) {

    }

    abstract fun initView()

    open fun useEventBus(): Boolean = false


    protected open fun setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0)
    }

    /**
     * 创建布局
     */
    abstract fun getContentView(): Int

    /**
     * 显示加载的ProgressDialog
     */
    fun showProgressDialog() {
        progressDialog.showProgressDialog()
    }

    /**
     * 显示有加载文字ProgressDialog，文字显示在ProgressDialog的下面
     */
    fun showProgressDialog(text: String) {
        progressDialog.showProgressDialogWithText(text)
    }

    /**
     * 隐藏加载的ProgressDialog
     */
    fun dismissProgressDialog() {
        progressDialog.dismissProgressDialog()
    }

    fun showToastBottom(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToastBottom(msgId: Int) {
        Toast.makeText(this, resources.getText(msgId), Toast.LENGTH_SHORT).show()
    }

    override fun finish() {
        super.finish()
        if (hasFinishTransitionAnim()) {
            if (animBack()) overridePendingTransitionExit() else overridePendingTransitionEnter()
        }
    }

    open fun animBack(): Boolean {
        return true
    }

    open fun hasFinishTransitionAnim(): Boolean {
        return true
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        if (hasEnterTransitionAnim()) {
            overridePendingTransitionEnter()
        }

    }

    protected fun hasEnterTransitionAnim(): Boolean {
        return true
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransitionEnter()
    }

    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left)
    }


    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            //如果不是落在EditText区域，则需要关闭输入法
            if (HideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private fun HideKeyboard(view: View?, event: MotionEvent): Boolean {
        if (view != null && view is EditText) {

            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)

            //获取现在拥有焦点的控件view的位置，即EditText
            val left = location[0]
            val top = location[1]
            val bottom = top + view.height
            val right = left + view.width
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            val isInEt = (event.x > left && event.x < right && event.y > top
                    && event.y < bottom)
            return !isInEt
        }
        return false
    }

    override fun onDestroy() {
        removeActivity(this)
        fixHuaWeiMemoryLeak()
        fixInputMethodManagerLeak(this)
        super.onDestroy()
        if (useEventBus()) {
            EventBusUtils.unregister(this)
        }
        refWatcher?.watch(this)
    }
}