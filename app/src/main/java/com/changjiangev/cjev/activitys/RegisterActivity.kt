package com.changjiangev.cjev.activitys

import com.changjiangev.cjev.R
import com.changjiangev.cjev.event.LoginFinishEvent
import com.changjiangev.cjev.inter.RegisterInter
import com.changjiangev.cjev.presenterimp.RegisterPresenterImp
import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.utils.EventBusUtils
import com.sihaiwanlian.baselib.utils.sp.UserManager
import com.sihaiwanlian.baseproject.result.RegisterBean
import kotlinx.android.synthetic.main.act_register.*
import kotlinx.android.synthetic.main.titlebar_trans.*


/**
 * Created by mou on 2017/6/15.
 * desc:注册
 */
class RegisterActivity : BaseActivity(), RegisterInter.RegisterViewInter {


    private var mRegisterPresenterImp: RegisterPresenterImp? = null
    private var mRealName: String? = null
    private var mIdCard: String? = null
    private var mGender: String? = null

    override fun getContentView(): Int {
        return R.layout.act_register
    }

    override fun initView() {
        mRegisterPresenterImp = RegisterPresenterImp(this, this)
    }

    override fun initData() {
        initToolBar()
        initIntent()
        initListner()
    }

    val initListner = {
        register_CountDownButton.setOnClickListener {
            val phoneNumber = register_et_iphone.text.toString().trim()
            mRegisterPresenterImp?.getIdentityCode(phoneNumber)

        }
        registerBtn.setOnClickListener {
            val phoneNumber = register_et_iphone.text.toString().trim()
            val pwd = register_et_pwd.text.toString().trim()
            val pwdSure = register_et_sure_pwd.text.toString().trim()
            val certificationCode = register_et_certificate.text.toString().trim()
            var gender = 1
            if ("女" == mGender) {
                gender = 0
            }
            mRegisterPresenterImp?.registerSure(phoneNumber, pwd, pwdSure, certificationCode, mRealName!!, mIdCard!!, gender)
        }
        titleBar_btn_back.setOnClickListener {
            finish()
        }
    }

    val initIntent = {
        val extras = intent.extras
        if (extras != null) {
            mRealName = extras.getString("realName")
            mIdCard = extras.getString("idCard")
            mGender = extras.getString("gender")
        }
    }


    val initToolBar = {
        titleBar_centerTV.text = getString(R.string.register)
    }

    override fun registerSuccess(registerBean: RegisterBean?) {
        showToastBottom(getString(R.string.register_success))
        //注冊成功,保存uuid,token,用户名
        UserManager.getInstance().saveUserId(registerBean?.accountInfo?.uuid.toString())
        UserManager.getInstance().saveToken(registerBean?.token)
        UserManager.getInstance().saveUserName(registerBean?.accountInfo?.username)
        finish()
        EventBusUtils.sendEvent(LoginFinishEvent())
    }

    override fun getIdentitySuccess() {
        register_CountDownButton.setEnableCountDown(true)
    }

    override fun getIdentityFail() {
        register_CountDownButton.removeCountDown()
    }

    override fun onDestroy() {
        super.onDestroy()
        register_CountDownButton.removeCountDown()
    }
}
