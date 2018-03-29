package com.changjiangev.cjev.presenterimp

import android.text.TextUtils
import com.changjiangev.cjev.R
import com.changjiangev.cjev.inter.RegisterInter
import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.base.BasePresenter
import com.sihaiwanlian.baselib.http.constant.CodeStatus
import com.sihaiwanlian.baselib.http.entity.BaseModel
import com.sihaiwanlian.baselib.http.observer.ProgressModelObserver
import com.sihaiwanlian.baselib.http.observer.ProgressObserver
import com.sihaiwanlian.baselib.utils.CommonUtil
import com.sihaiwanlian.baselib.utils.ToastBottomUtils
import com.sihaiwanlian.baselib.utils.ToastBottomUtils.showToastBottom
import com.sihaiwanlian.baselib.utils.UIUtils
import com.sihaiwanlian.baselib.utils.UIUtils.getString
import com.sihaiwanlian.baseproject.config.Constants
import com.sihaiwanlian.baseproject.request.RegisterRequest
import com.sihaiwanlian.baseproject.result.GetIdentityCodeBean
import com.sihaiwanlian.baseproject.result.RegisterBean
import com.sihaiwanlian.baseproject.retrofit.LoginRetrofit
import com.sihaiwanlian.baseproject.utils.SecurityUtils


/**
 * FileName: ${PACKAGE_NAME}.${NAME}.java
 * Author: mouxuefei
 * date: 2017/9/21
 * version: V1.0
 * desc:
 */

class RegisterPresenterImp(val mBaseActivity: BaseActivity, val mRegisterViewInter: RegisterInter.RegisterViewInter) :
        BasePresenter(), RegisterInter.RegisterPresenter {

    override fun getIdentityCode(phoneNumber: String) {
        if (TextUtils.isEmpty(phoneNumber)) {
            showToastBottom(UIUtils.getString(R.string.phone_cant_empty))
            return
        }
        if (CommonUtil.isPhoneNum(phoneNumber)) {
            getIdentityCodeByNet(phoneNumber)
        } else {
            showToastBottom(UIUtils.getString(R.string.phone_not_right))

        }

    }

    /**
     * 获取验证码
     */
    val getIdentityCodeByNet = { phoneNumber: String ->
        val timestamp = System.currentTimeMillis().toString()
        paramsCompareMap.clear()
        paramsCompareMap.put("phone", phoneNumber)
        val sign = SecurityUtils.changeMap2Str(paramsCompareMap, timestamp)
        LoginRetrofit
                .getService()
                .getIdentityCode(phoneNumber, timestamp, Constants.V_APPKEY, sign)
                .compose(compose(mBaseActivity.bindToLifecycle<GetIdentityCodeBean>()))
                .subscribe(object : ProgressObserver<GetIdentityCodeBean>(mBaseActivity, "获取验证码中...") {
                    override fun onHandleSuccess(t: GetIdentityCodeBean?) {
                        if (t == null) {
                            showToastBottom(getString(R.string.get_identity_code_fail))
                        }

                        if (t?.code == CodeStatus.ACCOUNT_EXSIT) {
                            showToastBottom(getString(R.string.sorry_yourPhone_hasRegisted))
                            return
                        }
                        if (t?.code == CodeStatus.GETCERTIFICODE_SUCCESS) {
                            showToastBottom(getString(R.string.get_identity_code_success))
                            mRegisterViewInter.getIdentitySuccess()
                        } else {
                            showToastBottom(getString(R.string.get_identity_code_fail))
                        }
                    }

                    override fun requestError() {
                        mRegisterViewInter.getIdentitySuccess()
                    }

                })
    }

    override fun registerSure(phoneNumber: String, pwd: String, surePwd: String, identityCode: String, realName: String, idCard: String, gender: Int) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.please_input_phonenumber))
            return
        }

        if (phoneNumber.length < 11) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.please_input_phonenumber))
            return
        }

        if (!CommonUtil.isPhoneNum(phoneNumber)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.phone_not_right))
            return
        }

        if (TextUtils.isEmpty(pwd)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.pwd_not_right))
            return
        }

        if (pwd.length < 6) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.pwd_must_six))
            return
        }
        if (TextUtils.isEmpty(surePwd)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.please_sure_pwd))
            return
        }
        if (pwd != surePwd) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.pwd_should_same))
            return
        }

        if (TextUtils.isEmpty(identityCode)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.verificationCode_cant_empty))
            return
        }

        val registerRequest = RegisterRequest()
        val timestamp = System.currentTimeMillis().toString()
        registerRequest.username = phoneNumber
        registerRequest.password = SecurityUtils.md5(pwd)
        registerRequest.timestamp = timestamp
        registerRequest.appKey = Constants.V_APPKEY
        registerRequest.verificationCode = identityCode
        registerRequest.gender = gender
        registerRequest.idCard = idCard
        registerRequest.realName = realName
        paramsCompareMap.clear()
        paramsCompareMap.put("username", phoneNumber)
        paramsCompareMap.put("gender", gender)
        paramsCompareMap.put("idCard", idCard)
        paramsCompareMap.put("realName", realName)
        paramsCompareMap.put("password", SecurityUtils.md5(pwd))
        paramsCompareMap.put("verificationCode", identityCode)
        val sign = SecurityUtils.changeMap2Str(paramsCompareMap, timestamp)
        registerRequest.sign = sign
        LoginRetrofit
                .getService()
                .register(registerRequest)
                .compose(compose(mBaseActivity.bindToLifecycle<BaseModel<RegisterBean>>()))
                .subscribe(object : ProgressModelObserver<RegisterBean>(mBaseActivity, "注册中...") {
                    override fun onHandleSuccess(t: RegisterBean?) {
                        mRegisterViewInter.registerSuccess(t)
                    }

                    override fun codeError(code: Int) {
                        showToastBottom("注册失败")
                    }
                })
    }
}
