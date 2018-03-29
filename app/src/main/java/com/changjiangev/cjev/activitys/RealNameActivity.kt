package com.changjiangev.cjev.activitys

import android.os.Bundle
import android.text.TextUtils
import com.changjiangev.cjev.R
import com.changjiangev.cjev.event.LoginFinishEvent
import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.utils.CommonUtil
import com.sihaiwanlian.baselib.utils.IDCardUtil
import com.sihaiwanlian.baseproject.result.IdcardInfoExtractor
import kotlinx.android.synthetic.main.act_realname.*
import kotlinx.android.synthetic.main.titlebar_trans.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * FileName: com.sihaiwanlian.cmccnev.feature.login.activitys.RealNameActivity.java
 * Author: mouxuefei
 * date: 2017/12/13
 * version: V1.0
 * desc:
 */
class RealNameActivity : BaseActivity() {
    private var mRealName: String? = null
    private var mIdCard: String? = null
    private var mGender: String? = null


    override fun getContentView(): Int {
        return R.layout.act_realname
    }

    override fun initView() {
        titleBar_btn_back.setOnClickListener {
            finish()
        }
        realNameBtn.setOnClickListener {
            mRealName = register_et_name.text.toString().trim()
            mIdCard = register_et_idcard.text.toString().trim()
            if (TextUtils.isEmpty(mRealName)) {
                showToastBottom("请输入您的真实姓名")
                return@setOnClickListener
            }
            if (mRealName?.length!! < 2) {
                showToastBottom("姓名不少于2位")
                return@setOnClickListener
            }

            if (!CommonUtil.isName(mRealName)) {
                showToastBottom("请输入正确的姓名");
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(mIdCard)) {
                showToastBottom("请输入您的身份证号码")
                return@setOnClickListener
            }
            if (!IDCardUtil.isIDCard(mIdCard)) {
                showToastBottom("请输入正确的身份证号码")
                return@setOnClickListener
            }
            val idcardInfo = IdcardInfoExtractor(mIdCard)
            mGender = idcardInfo.gender
            val bundle = Bundle()
            bundle.putString("realName", mRealName)
            bundle.putString("idCard", mIdCard)
            bundle.putString("gender", mGender)
            CommonUtil.startActivtiy(this, RegisterActivity::class.java, bundle)
        }
    }


    override fun initData() {
        titleBar_centerTV.text = "注册"
    }

    /**
     * 切换车辆之后发送的事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeBindCarEvent(event: LoginFinishEvent) {
        finish()
    }
}
