package com.changjiangev.cjev.activitys

import com.changjiangev.cjev.R
import com.changjiangev.cjev.inter.ForgetPwdInter
import com.changjiangev.cjev.presenterimp.ForgetPwdPresenterImp
import com.sihaiwanlian.baselib.base.BaseActivity
import kotlinx.android.synthetic.main.activity_forget_pwd.*
import kotlinx.android.synthetic.main.titlebar_trans.*


/**
 * Created by mou on 2017/6/15.
 * desc:忘记密码
 */

class ForgetPwdActivity : BaseActivity(), ForgetPwdInter.ForgetPwdViewInter {

    private var mForgetPwdPresenterImp: ForgetPwdPresenterImp? = null

    override fun getContentView(): Int {
        return R.layout.activity_forget_pwd
    }

    override fun initView() {
        mForgetPwdPresenterImp = ForgetPwdPresenterImp(this, this)
    }

    override fun initData() {
        initToolBar()
        initListner()
    }

    private fun initListner() {
        titleBar_btn_back.setOnClickListener {
            finish()
        }
        forgetpwd_CountDownButton.setOnClickListener {
            val phoneNumber = forgetpwd_et_iphone.text.toString().trim()
            if (mForgetPwdPresenterImp != null) {
                mForgetPwdPresenterImp?.getIdentityCode(phoneNumber)
            }
        }
        forgetpwd_btn.setOnClickListener {
            val phoneNumber = forgetpwd_et_iphone.text.toString().trim()
            val pwd = forgetpwd_et_pwd.text.toString().trim()
            val pwdSure = forgetpwd_et_sure_pwd.text.toString().trim()
            val verticleCode = forgetpwd_et_certificate.text.toString().trim()
            mForgetPwdPresenterImp?.resetPwdSure(phoneNumber, pwd, pwdSure, verticleCode)
        }
    }

    private fun initToolBar() {
        titleBar_centerTV.text = getString(R.string.reset_pwd)
    }

    override fun getIdentitySuccess() {
        forgetpwd_CountDownButton.setEnableCountDown(true)

    }

    override fun getIdentityFail() {
        forgetpwd_CountDownButton.removeCountDown()
    }

    override fun onDestroy() {
        super.onDestroy()
        forgetpwd_CountDownButton.removeCountDown()
    }

}
