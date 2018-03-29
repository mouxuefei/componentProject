package com.sihaiwanlian.baselib.http.constant

/**
 * Created by mou on 2017/9/28.
 */

public object CodeStatus {

    //一般请求的成功
    val SUCCESS = 0

    //获取验证码成功
    val GETCERTIFICODE_SUCCESS = 1

    /**
     * 验证码错误.
     */
    val VERIFICATION_CODE_ERROR = 9300
    /**
     * 验证码过期
     */
    val VERIFICATION_CODE_OUT_TIME = 9301

    /**
     * 账号不存在.
     */
    val LOGIN_STATUS_NO_ACCOUNT = 9001

    /**
     * 账号密码错误.
     */
    val LOGIN_STATUS_ACCOUNT_OR_PASSWORD_ERROR = 9002
    /**
     * 账号已存在
     */
    val ACCOUNT_EXSIT = 9003

    /**
     * PIN码错误 .
     */
    val PIN_ERROR = 9004
    /**
     * 车辆已经绑定账号
     */
    val VEHICLE_HAS_BINDING_ACCOUNT = 9005
    /**
     * 未实名认证.
     */
    val ACCOUNT_STATUS_NOT_REALNAME_AUTHENTICATION = 9101

    /**
     * 实名认证中.
     */
    val ACCOUNT_STATUS_REALNAME_HANDING = 9102

    /**
     * 实名认证失败.
     */
    val ACCOUT_STATUS_FAIL_REALNAME = 9103

    /**
     * 实名认证通过;
     */
    val ACCOUNT_STAUTS_BINDING_VEHICLE = 9104

    //    /**
    //     * 账户已经存在 .
    //     */
    //    public static final Integer ACCOUNT_REGISTER_STATUS_EXIST = 9200;

    /*---------------所有请求都有-------------*/
    /**
     * 参数缺失.
     */
    val PARAMETER_MISSING = 9010

    /**
     * 请求时间差异较大.
     */
    val REQUEST_TIME_ERROR = 9011

    /**
     * 请求被篡改.
     */
    val REQUEST_TAMPERED = 9012

    /**
     * 请求超时
     */
    val REQUEST_TIME_OUT = 9088

    /**
     * 服务器异常
     */
    val SERVER_ERROR = 9099

    /*---------------登录之后所有请求都有-------------*/

    /**
     * TOKEN过期 或是没有TOKEN，需要重新登录.
     */
    val NO_TOKEN = 9999

    /*---------------控制-------------*/
    /**
     * 终端不在线
     */
    val SERVER_NOTONLINE = 1027

    //控制和寻车
    /** 未定购. */
    public val SERVICE_ORDER_STATUS_NOT_ORDERED = 9201

    /** 未激活. */
    val SERVICE_ORDER_STATUS_NO_ACTIVE = 9202

    /** 服务生效中. */
    val SERVICE_ORDER_STATUS_EFFECTIVE = 9203

    /** 服务过期. */
    val SERVICE_ORDER_STATUS_EXPIRATION = 9204
}
