package com.changjiangev.cjev.inter

import com.sihaiwanlian.baseproject.result.RegisterBean

/**
 * FileName: com.changjiangev.cjev.inter.RegisterInter.java
 * Author: mouxuefei
 * date: 2018/3/20
 * version: V1.0
 * desc:
 */
interface RegisterInter {
    interface RegisterPresenter {
        fun getIdentityCode(phoneNumber: String)
        fun registerSure(phoneNumber: String, pwd: String, surePwd: String, identityCode: String, realName: String, idCard: String, gender: Int)
    }

    interface RegisterViewInter {
        fun registerSuccess(registerBean: RegisterBean?)

        fun getIdentitySuccess()

        fun getIdentityFail()
    }

}