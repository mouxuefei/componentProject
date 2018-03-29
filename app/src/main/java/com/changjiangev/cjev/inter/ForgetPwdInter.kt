package com.changjiangev.cjev.inter

/**
 * FileName: com.changjiangev.cjev.inter.ForgetPwdInter.java
 * Author: mouxuefei
 * date: 2018/3/20
 * version: V1.0
 * desc:
 */
interface ForgetPwdInter {

    interface ForgetPwdPresenter {
        fun getIdentityCode(phoneNumber: String)
        fun resetPwdSure(phoneNumber: String, pwd: String, surePwd: String, identityCode: String)
    }

    interface ForgetPwdViewInter {
        fun getIdentitySuccess()

        fun getIdentityFail()
    }

}