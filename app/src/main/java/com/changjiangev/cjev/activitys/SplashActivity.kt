package com.changjiangev.cjev.activitys

import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import com.alibaba.android.arouter.launcher.ARouter
import com.changjiangev.cjev.R
import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.utils.CommonUtil
import com.sihaiwanlian.baselib.utils.sp.UserManager

/**
 * FileName: ${PACKAGE_NAME}.${NAME}.java
 * Author: mouxuefei
 * date: 2017/7/5
 * version: V1.0
 * desc:
 */

class SplashActivity : BaseActivity() {

    override fun getContentView() = R.layout.act_splash

    override fun initView() {
        Handler().postDelayed({
            val token = UserManager.getInstance().token
            //这个是判断是否已经走完实名认证,并且已经绑定了车辆
            val hasSelectCar = UserManager.getInstance().hasSelectCar
            if (!TextUtils.isEmpty(token)) {//判断token是否为空
                if (hasSelectCar) {//判断是否绑定车辆
                    ARouter.getInstance().build("/home/HomeActivity").navigation()
                    finish()
                } else {
                    finish()
                    CommonUtil.startActivtiy(this@SplashActivity, LoginActivity::class.java)
                }
            } else {
                finish()
                CommonUtil.startActivtiy(this@SplashActivity, LoginActivity::class.java)
            }
        }, 2000)
    }

    /**
     * 禁用返回键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun initData() {
    }

    override fun setStatusBar() {
    }

    override fun hasFinishTransitionAnim(): Boolean {
        return false
    }
}


