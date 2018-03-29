package com.changjiangev.cjev.inter

import com.sihaiwanlian.baseproject.result.LoginBean

/**
 * FileName: com.changjiangev.cjev.inter.LoginInter.java
 * Author: mouxuefei
 * date: 2018/3/20
 * version: V1.0
 * desc:
 */
interface LoginInter {
    interface LoginPresenter {
        fun login(userName: String, passWord: String)
    }

    interface LoginViewInter {
        fun loginSuccess(loginBean: LoginBean?)
        fun loginFailed()
    }
}