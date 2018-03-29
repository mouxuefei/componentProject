package com.changjiangev.cjev.presenterimp

import android.text.TextUtils
import com.changjiangev.cjev.R
import com.changjiangev.cjev.inter.ForgetPwdInter
import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.base.BasePresenter
import com.sihaiwanlian.baselib.http.constant.CodeStatus
import com.sihaiwanlian.baselib.http.entity.BaseModel
import com.sihaiwanlian.baselib.http.observer.ProgressModelObserver
import com.sihaiwanlian.baselib.http.observer.ProgressObserver
import com.sihaiwanlian.baselib.utils.CommonUtil
import com.sihaiwanlian.baselib.utils.ToastBottomUtils
import com.sihaiwanlian.baselib.utils.UIUtils
import com.sihaiwanlian.baseproject.config.Constants
import com.sihaiwanlian.baseproject.request.ForgetPwdRequest
import com.sihaiwanlian.baseproject.result.ForgetPwdBean
import com.sihaiwanlian.baseproject.result.GetIdentityCodeBean
import com.sihaiwanlian.baseproject.retrofit.LoginRetrofit
import com.sihaiwanlian.baseproject.utils.SecurityUtils


/**
 * FileName: ${PACKAGE_NAME}.${NAME}.java
 * Author: mouxuefei
 * date: 2017/9/21
 * version: V1.0
 * desc:
 */

class ForgetPwdPresenterImp(val mBaseActivity: BaseActivity, val mForgetPwdViewInter: ForgetPwdInter.ForgetPwdViewInter)
    : BasePresenter(), ForgetPwdInter.ForgetPwdPresenter {

    override fun getIdentityCode(phoneNumber: String) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.phone_cant_empty))
            return
        }
        if (CommonUtil.isPhoneNum(phoneNumber)) {
            //获取验证码
            getIdentityCodeByNet(phoneNumber)
        } else {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.phone_not_right))
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
                            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.get_identity_code_fail))
                        }
                        if (t?.code == CodeStatus.GETCERTIFICODE_SUCCESS) {
                            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.get_identity_code_success))
                            mForgetPwdViewInter.getIdentitySuccess()
                        } else {
                            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.get_identity_code_fail))
                        }
                    }

                    override fun requestError() {
                        mForgetPwdViewInter.getIdentityFail()
                    }

                })
    }

    override fun resetPwdSure(phoneNumber: String, pwd: String, surePwd: String, identityCode: String) {
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
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.please_input_newpwd))
            return
        }
        if (pwd.length < 6) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.pwd_must_six))
            return
        }

        if (TextUtils.isEmpty(surePwd)) {
            ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.please_sure_newpwd))
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
        val forgetPwdRequest = ForgetPwdRequest()
        val timestamp = System.currentTimeMillis().toString()
        forgetPwdRequest.username = phoneNumber
        forgetPwdRequest.timestamp = timestamp
        forgetPwdRequest.appKey = Constants.V_APPKEY
        forgetPwdRequest.password = SecurityUtils.md5(pwd)
        forgetPwdRequest.verificationCode = identityCode


        paramsCompareMap.clear()
        paramsCompareMap.put("username", phoneNumber)
        paramsCompareMap.put("password", SecurityUtils.md5(pwd))
        paramsCompareMap.put("verificationCode", identityCode)
        val sign = SecurityUtils.changeMap2Str(paramsCompareMap, timestamp)
        forgetPwdRequest.sign = sign
        LoginRetrofit.getService()
                .forgetPwd(forgetPwdRequest)
                .compose(compose(mBaseActivity.bindToLifecycle<BaseModel<ForgetPwdBean>>()))
                .subscribe(object : ProgressModelObserver<ForgetPwdBean>(mBaseActivity, "重置密码中...") {
                    override fun onHandleSuccess(t: ForgetPwdBean?) {
                        ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.reset_pwd_success_two))
                        mBaseActivity.finish()
                    }

                    override fun codeError(code: Int) {
                        ToastBottomUtils.showToastBottom(UIUtils.getString(R.string.reset_pwd_fail))
                    }

                })
    }
}
