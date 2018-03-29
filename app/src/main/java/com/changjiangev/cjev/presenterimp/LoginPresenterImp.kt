package com.changjiangev.cjev.presenterimp

import android.text.TextUtils
import com.changjiangev.cjev.R
import com.changjiangev.cjev.inter.LoginInter
import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.base.BasePresenter
import com.sihaiwanlian.baselib.http.entity.BaseModel
import com.sihaiwanlian.baselib.http.observer.ProgressModelObserver
import com.sihaiwanlian.baselib.utils.CommonUtil
import com.sihaiwanlian.baselib.utils.ToastBottomUtils
import com.sihaiwanlian.baselib.utils.UIUtils
import com.sihaiwanlian.baseproject.config.Constants
import com.sihaiwanlian.baseproject.request.LoginRequest
import com.sihaiwanlian.baseproject.result.LoginBean
import com.sihaiwanlian.baseproject.retrofit.LoginRetrofit
import com.sihaiwanlian.baseproject.utils.SecurityUtils


/**
 * FileName: ${PACKAGE_NAME}.${NAME}.java
 * Author: mouxuefei
 * date: 2017/9/21
 * version: V1.0
 * desc:
 */

class LoginPresenterImp(var mLoginActivity: BaseActivity, var mLoginViewInter: LoginInter.LoginViewInter)
    : BasePresenter(), LoginInter.LoginPresenter {

    override fun login(userName: String, passWord: String) {
        if (TextUtils.isEmpty(userName)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.please_input_phonenumber))
            return
        }
        if (userName.length < 11) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.please_input_phonenumber))
            return
        }

        if (!CommonUtil.isPhoneNum(userName)) {
            mLoginActivity.showToastBottom(UIUtils.getString(R.string.phone_not_right))
            return
        }
        if (TextUtils.isEmpty(passWord)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.please_input_loginPwd))
            return
        }

        if (passWord.length < 6) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.pwd_must_six))
            return
        }
        doLogin(userName, passWord)
    }

    /**
     * 登录
     */
    val doLogin = { username: String, password: String ->
        val loginRequest = LoginRequest()
        val timestamp = System.currentTimeMillis().toString()
        loginRequest.username = username
        loginRequest.password = SecurityUtils.md5(password)
        loginRequest.timestamp = timestamp
        loginRequest.appKey = Constants.V_APPKEY
        paramsCompareMap.clear()
        paramsCompareMap.put("username", username)
        paramsCompareMap.put("password", SecurityUtils.md5(password))
        val sign = SecurityUtils.changeMap2Str(paramsCompareMap, timestamp)
        loginRequest.sign = sign
        LoginRetrofit
                .getService()
                .login(loginRequest)
                .compose(compose(mLoginActivity.bindToLifecycle<BaseModel<LoginBean>>()))
                .subscribe(object : ProgressModelObserver<LoginBean>(mLoginActivity, "登陆中...") {
                    override fun onHandleSuccess(t: LoginBean?) {
                        mLoginViewInter.loginSuccess(t)
                    }
                })
    }
}
