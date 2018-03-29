package com.sihaiwanlian.baseproject.config

import android.Manifest

/**
 * Created by mou on 2017/7/31.
 * desc:
 */

object Constants {
    //Appkey的值
    val V_APPKEY = "cm150760580823399"
    //appKey的键
    val K_APPKEY = "appKey"
    //username
    val K_USERNAME = "username"
    //时间戳的键
    val K_TIMESTAMP = "timestamp"
    //token
    val K_TOKEN = "token"
    //sign
    val K_SIGN = "sign"
    //iccid
    val K_ICCID = "iccid"
    //开始位置
    val K_START_LOCATION = "start_location"
    //结束位置
    val K_END_LOCATION = "end_location"
    //导航方式
    val TARGET_GUIDE = "target_guide"
    //步行
    val TARGET_WALK = 11
    //骑行
    val TARGET_RIDE = 22
    //驾车
    val TARGET_DRIVE = 33
    //所有权限
    val PERMISSIONS_NEEDS_ALL = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE)
    //定位权限
    val PERMISSIONS_NEEDS_LOCATION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    //相机权限
    //所有权限
    val PERMISSIONS_NEEDS_CAMER = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    //从哪个Activity进入
    val COME_FROM_WHICH = "come_from"
    //从主界面
    val FROM_MAIN_ACT = "from_main"
    //从登陆界面进入
    val FROM_LOGIN = "from_login"
    //从个人中心进入
    val FROM_PERSONAL = "from_personal"
    //修改邮箱
    val CHANGE_EMAIL = "change_email"
    //邮箱
    val EMAIL = "email"
    //修改紧急联系人
    val CHANGE_EMERGENCE_MAN = "change_emergence_man"
    //紧急联系人名字
    val EMERGENCE_NAME = "emergence_name"
    //紧急联系人电话
    val EMERGENCE_PHONE = "emergence_phone"
    //实名认证的姓名
    val REAL_NAME = "real_name"
    //实名认证的电话
    val TELEPHONE_NUMBER = "telephone_number"
    //实名认证的身份证
    val IDENTITY_CARD = "identity_Card"
    //性别
    val SEX_TYPE = "sex"
    //男
    val SEX_MAN = 1
    //女
    val SEX_WOMAN = 2
    //手持照片
    val URL_WHITH_HAND = "url_whith_hand"
    //身份证正面
    val URL_FACE = "url_face"
    //身份证反面
    val URL_BACK = "url_back"
    //pin码
    val PIN = "pin"
    //实名认证通过
    val CERTIFICATION_PASS = 3
    //实名认证没有通过
    val CERTIFICATION_NOT_PASS = 4
    //认证失败
    val CERTIFICATION_FAIL = 5
    //判断是否实名认证通过
    val CERTIFICATION_TYPE = "certification_type"
    //intent传递电话的key
    val PHONE_NUMBER = "phone_number"
    //七天的毫秒值
    val EIGHTDAYMILLS: Long = 604800000
    //默认车辆
    val IS_DEFAULTCAR = 1
    //開始时间
    val BEGIN_TIME = "begin_time"
    //结束时间
    val END_TIME = "end_time"

    val OPEN_ALLDOOR = 11
    val OPEN_DRIVEDOOR = 12
    val CLOSE_ALLDOOR = 13
    val CLOSE_DRIVEDOOR = 14
    val OPEN_TRUNK = 15
    val OPEN_CARWINDOW = 16
    val OPEN_SKYWINDOW = 17
    val OPEN_CARANDSKYWINDOW = 18
    val CLOSE_CARWINDOW = 19
    val CLOSE_SKYWINDOW = 20
    val CLOSE_CARANDSKYWINDOW = 21
    val OPEN_LIGHT = 22

    val CLOSE_AIR = 23
    val OPEN_AIR = 25
    val OPEN_VOICE = 24

    //修改密码成功
    val CHANGE_PWD_SUCCESS = 100
    //修改手机号码成功
    val CHANGE_PHONE_SUCCESS = 200
    //重新登录的key
    val KEY_RELOGIN = "KEY_RELOGIN"
}
