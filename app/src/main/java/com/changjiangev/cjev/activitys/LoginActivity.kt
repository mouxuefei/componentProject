package com.changjiangev.cjev.activitys

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.changjiangev.cjev.R
import com.changjiangev.cjev.inter.LoginInter
import com.changjiangev.cjev.presenterimp.LoginPresenterImp
import com.orhanobut.logger.Logger
import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.utils.CommonUtil
import com.sihaiwanlian.baselib.utils.PermissionHelper
import com.sihaiwanlian.baselib.utils.sp.UserManager
import com.sihaiwanlian.baseproject.config.Constants
import com.sihaiwanlian.baseproject.result.LoginBean
import com.sihaiwanlian.baseproject.widget.nicedialog.BaseNiceDialog
import com.sihaiwanlian.baseproject.widget.nicedialog.NiceDialogUtils
import com.sihaiwanlian.baseproject.widget.nicedialog.ViewConvertListener
import com.sihaiwanlian.baseproject.widget.nicedialog.ViewHolder
import kotlinx.android.synthetic.main.act_login.*


/**
 * Created by mou on 2017/6/1.
 */

class LoginActivity : BaseActivity(), LoginInter.LoginViewInter, PermissionHelper.PermissionListener {

    private var mLoginPresenterImp: LoginPresenterImp? = null
    private var mPermissionHelper: PermissionHelper? = null

    override fun getContentView() = R.layout.act_login

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionHelper?.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun initView() {
        mLoginPresenterImp = LoginPresenterImp(this, this)
    }

    override fun initData() {
        initPermission()
        initUserName()
        initListner()
    }

    private fun initListner() {
        btn_login.setOnClickListener {
            val LoginPhoneNum = login_username.text.toString().trim()
            val loginPwd = login_pwd.text.toString().trim()
            mLoginPresenterImp?.login(LoginPhoneNum, loginPwd)
        }
        tv_regester.setOnClickListener {
            CommonUtil.startActivtiy(this, RealNameActivity::class.java)
        }
        act_login_forgetPwd.setOnClickListener {
            CommonUtil.startActivtiy(this, ForgetPwdActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        initIntent()
    }

    val initIntent = {
        val bundle = intent.extras
        if (bundle != null) {
            val reLogin = bundle.getInt(Constants.KEY_RELOGIN)
            if (reLogin == Constants.CHANGE_PWD_SUCCESS) {
                NiceDialogUtils.getInstance().showDialogAtCenter(R.layout.dialog_change_phone, supportFragmentManager, object : ViewConvertListener() {

                    override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                        val tvTitle = holder.getView<TextView>(R.id.dialog_open_door_tv_allDoor)
                        tvTitle.setText(R.string.change_pwd_success_login_again)
                        holder.getView<View>(R.id.dialog_btnSure).setOnClickListener { dialog.dismiss() }
                    }
                })
            } else if (reLogin == Constants.CHANGE_PHONE_SUCCESS) {
                NiceDialogUtils.getInstance().showDialogAtCenter(R.layout.dialog_change_phone, supportFragmentManager, object : ViewConvertListener() {
                    override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                        val tvTitle = holder.getView<TextView>(R.id.dialog_open_door_tv_allDoor)
                        tvTitle.setText(R.string.change_phone_success_login_again)
                        holder.getView<View>(R.id.dialog_btnSure).setOnClickListener { dialog.dismiss() }
                    }
                })
            }
        }

    }

    val initUserName = {
        val userName = UserManager.getInstance().userName
        Logger.e("username=$userName")
        if (!TextUtils.isEmpty(userName)) {
            login_username.setText(userName)
            login_username.setSelection(userName.length)
        }
    }

    val initPermission = {
        mPermissionHelper = PermissionHelper(this)
        if (!PermissionHelper.hasPermissions(this@LoginActivity, *Constants.PERMISSIONS_NEEDS_ALL)) {
            mPermissionHelper?.requestPermissions(getString(R.string.must_permission), this, *Constants.PERMISSIONS_NEEDS_ALL)
        }
    }

    override fun animBack(): Boolean {
        return false
    }

    override fun loginSuccess(loginBean: LoginBean?) {
        //保存手机号
        UserManager.getInstance().saveUserName(loginBean?.accountInfo?.username)
        Logger.e("user=" + loginBean?.accountInfo?.username)
        //保存头像url
        UserManager.getInstance().saveUserIconUrl(loginBean?.accountInfo?.avatar)
        //保存token
        UserManager.getInstance().saveToken(loginBean?.token)
        //用户id
        UserManager.getInstance().saveUserId(loginBean?.accountInfo?.uuid.toString())
        ARouter.getInstance().build("/home/HomeActivity").withTransition(R.anim.in_from_right, R.anim.out_from_left).navigation()
        finish()
    }

    override fun loginFailed() {
        showToastBottom(R.string.login_fail)
    }


    override fun doAfterGrand(vararg permission: String) {

    }

    override fun doAfterDenied(vararg permission: String) {
        showToastBottom("您拒绝了权限，部分功能将无法使用")
    }
}



