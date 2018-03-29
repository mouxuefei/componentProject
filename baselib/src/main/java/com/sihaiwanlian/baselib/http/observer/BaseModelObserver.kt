package com.sihaiwanlian.baselib.http.observer

import com.google.gson.JsonParseException
import com.orhanobut.logger.Logger
import com.sihaiwanlian.baselib.http.constant.CodeStatus
import com.sihaiwanlian.baselib.http.entity.BaseModel
import com.sihaiwanlian.baselib.utils.ToastBottomUtils
import com.sihaiwanlian.baselib.utils.ToastBottomUtils.showToastBottom
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * @FileName: com.mou.demo.http.observer.BaseObserver.java
 * @author: mouxuefei
 * @date: 2017-12-21 16:40
 * @version V1.0 配置了baseModel,状态码统一处理的observer
 * @desc
 */
abstract class BaseModelObserver<T> : Observer<BaseModel<T>> {


    var mDisposable: Disposable? = null
    override fun onSubscribe(d: Disposable) {
        mDisposable = d
        OnBegin()
    }

    override fun onNext(value: BaseModel<T>) {
        if (value.code == CodeStatus.SUCCESS) {
            val t = value.result
            onHandleSuccess(t)
        } else {
            onHandleError(value.code)
        }
    }

    override fun onError(e: Throwable) {
        failError()
        showErrorToast(e)
    }

    override fun onComplete() {

    }

    protected fun OnBegin() {}

    protected abstract fun onHandleSuccess(t: T?)
    /**
     *
     */
    open fun failError() {}
    /**
     * 返回码错误
     */
    open fun codeError(code: Int) {
    }

    private fun onHandleError(code: Int) {

        val message: String
        if (code == CodeStatus.REQUEST_TIME_OUT || code == CodeStatus.REQUEST_TIME_ERROR) {
            message = "请求超时"
            ToastBottomUtils.showToastBottom(message)
        } else if (code == CodeStatus.SERVER_ERROR) {
            message = "服务器异常"
            ToastBottomUtils.showToastBottom(message)
        } else if (code == CodeStatus.PARAMETER_MISSING) {
            message = "参数缺失"
            ToastBottomUtils.showToastBottom(message)
        } else if (code == CodeStatus.LOGIN_STATUS_NO_ACCOUNT) {
            message = "您的手机号码还未注册"
            ToastBottomUtils.showToastBottom(message)
        } else if (code == CodeStatus.LOGIN_STATUS_ACCOUNT_OR_PASSWORD_ERROR) {
            message = "账号或密码错误"
            ToastBottomUtils.showToastBottom(message)
        } else if (code == CodeStatus.VERIFICATION_CODE_ERROR || code == CodeStatus.VERIFICATION_CODE_OUT_TIME) {
            message = "验证码错误"
            ToastBottomUtils.showToastBottom(message)
        } else if (code == CodeStatus.ACCOUNT_EXSIT) {
            message = "您的手机号码已注册"
            ToastBottomUtils.showToastBottom(message)
        } else if (code == CodeStatus.PIN_ERROR) {
            message = "PIN码错误"
            ToastBottomUtils.showToastBottom(message)
        }else{
            codeError(code)
        }
    }


    private fun showErrorToast(e: Throwable) {
        Logger.e("exception=${e.toString()}")
        if (e is SocketTimeoutException || e is ConnectException) {
            showToastBottom("连接失败,请检查网络状况!")
        } else if (e is JsonParseException) {
            showToastBottom("数据解析失败")
        } else {
            showToastBottom("请求失败")
        }
    }
}