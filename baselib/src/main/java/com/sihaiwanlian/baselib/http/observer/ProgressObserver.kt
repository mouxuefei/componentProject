package com.sihaiwanlian.baselib.http.observer

import android.content.Context
import android.os.Bundle
import android.os.Message
import com.sihaiwanlian.baselib.http.progress.ProgressDialogHandler
import io.reactivex.disposables.Disposable

/**
 * @FileName: com.mou.demo.http.observer.ProgressObserver.java
 * @author: mouxuefei
 * @date: 2017-12-21 16:47
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
abstract class ProgressObserver<T>(val context: Context, val dialogMsg: String) :
        BaseObserver<T>() {
    private var mProgressDialogHandler: ProgressDialogHandler

    init {
        mProgressDialogHandler = ProgressDialogHandler(context)
    }

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        showProgressDialog()
    }

    override fun onComplete() {
        super.onComplete()
        dismissProgressDialog()
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     */
    override fun onError(e: Throwable) {
        super.onError(e)
        dismissProgressDialog()
    }

    private fun showProgressDialog() {
        //            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        val message = Message()
        message.what = ProgressDialogHandler.SHOW_PROGRESS_DIALOG
        val bundle = Bundle()
        bundle.putString("dialogMsg", dialogMsg)
        message.data = bundle
        mProgressDialogHandler.sendMessage(message)
    }

    private fun dismissProgressDialog() {
        mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget()
    }

}