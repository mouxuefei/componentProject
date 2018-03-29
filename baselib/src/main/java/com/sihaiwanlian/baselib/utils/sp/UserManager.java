package com.sihaiwanlian.baselib.utils.sp;

import android.content.Context;

import com.sihaiwanlian.baselib.base.LibApplication;
import com.sihaiwanlian.baselib.http.constant.CodeStatus;


/**
 * Created by mou on 2016/7/18.
 */
public class UserManager {

    private static UserManager mInstance;
    private SPUtils mSpUtils;

    private UserManager(Context context) {
        mSpUtils = new SPUtils(context);
    }

    public static UserManager getInstance() {
        if (mInstance == null) {
            synchronized (UserManager.class) {
                if (mInstance == null) {
                    mInstance = new UserManager(LibApplication.Companion.getMContext());
                }
            }
        }

        return mInstance;
    }

    /**
     * 保存token
     */
    public void saveToken(String token) {
        mSpUtils.saveStringValue("token", token);
    }

    public String getToken() {
        return mSpUtils.getStringValue("token");
    }

    /**
     * 保存账号
     */
    public void saveUserName(String userName) {
        mSpUtils.saveStringValue("userName", userName);
    }

    public String getUserName() {
        return mSpUtils.getStringValue("userName");
    }

    /**
     * 保存userid
     */
    public void saveUserId(String userid) {
        mSpUtils.saveStringValue("userId", userid);
    }

    public String getUserId() {
        return mSpUtils.getStringValue("userId");
    }

    /**
     * 保存vin号
     */
    public void saveVehicleVin(String VehicleVin) {
        mSpUtils.saveStringValue("VehicleVin", VehicleVin);
    }

    public String getVehicleVin() {
        return mSpUtils.getStringValue("VehicleVin");
    }

    /**
     * 保存车架号
     */
    public void saveLicensePlate(String LicensePlate) {
        mSpUtils.saveStringValue("LicensePlate", LicensePlate);
    }

    public String getLicensePlate() {
        return mSpUtils.getStringValue("LicensePlate");
    }

    /**
     * 保存PIN码
     */
    public void savePin(String pin) {
        mSpUtils.saveStringValue("pin","");
    }

    public String getPin() {
        return mSpUtils.getStringValue("pin");
    }

    /**
     * 保存头像url
     */
    public void saveUserIconUrl(String saveUserIconUrl) {
        mSpUtils.saveStringValue("saveUserIconUrl", saveUserIconUrl);
    }

    public String getUserIonUrl() {
        return mSpUtils.getStringValue("saveUserIconUrl");
    }

    /**
     * 保存是否wifi下自动更新
     */
    public void saveWifiUpdate(boolean wifiUpdate) {
        mSpUtils.saveBooleanValue("wifiUpdate", wifiUpdate);
    }

    public boolean getWifiUpdate() {
        return mSpUtils.getBooleanValue("wifiUpdate");
    }

    /**
     * 保存是否已经绑定了车辆,绑定了车辆就可以不用登陆,直接去主界面,没绑定车辆每次进入都要去登录
     */
    public void saveHasSelectCar(boolean saveHasSelectCar) {
        mSpUtils.saveBooleanValue("saveHasSelectCar", saveHasSelectCar);
    }

    public boolean getHasSelectCar() {
        return mSpUtils.getBooleanValue("saveHasSelectCar");
    }

    public void saveServiceCode(int code) {
        mSpUtils.saveIntValue("serviceBuy", code);
    }

    public int getServiceCode() {
        return mSpUtils.getIntValue("serviceBuy", CodeStatus.INSTANCE.getSERVICE_ORDER_STATUS_NOT_ORDERED());
    }

    /**
     * 清除所有信息,注销登陆的
     */
    public void clearAllInfo() {
        saveHasSelectCar(false);
        saveVehicleVin("");
        savePin("");
        saveLicensePlate("");
        saveServiceCode(CodeStatus.INSTANCE.getSERVICE_ORDER_STATUS_NOT_ORDERED());
        saveUserIconUrl("");
        saveToken("");
        saveUserId("");
    }

    public void clearAllInfoAndUser(){
        saveHasSelectCar(false);
        saveVehicleVin("");
        savePin("");
        saveLicensePlate("");
        saveServiceCode(CodeStatus.INSTANCE.getSERVICE_ORDER_STATUS_NOT_ORDERED());
        saveUserIconUrl("");
        saveToken("");
        saveUserId("");
        saveUserName("");
    }
}
