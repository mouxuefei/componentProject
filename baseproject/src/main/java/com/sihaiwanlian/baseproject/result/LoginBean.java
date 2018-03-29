package com.sihaiwanlian.baseproject.result;

/**
 * FileName: com.sihaiwanlian.baseproject.result.LoginRequest.java
 * Author: mouxuefei
 * date: 2018/3/20
 * version: V1.0
 * desc:
 */


public class LoginBean {
    /**
     * accountInfo : {"accountStatus":0,"password":"dae3a6ff1559c0492f3058b93befc19a","avatar":"http://10.1.32.147:8080/cnmobile/app_avatar1514874630567.jpg","uuid":1234567895,"username":"18512812703"}
     * token : loginUser18512812703_861df87dc09beb3dc13ffb694fc34826
     * vehicle : {"vehicleColor":"","isAuth":9102,"isDefault":1,"licensePlate":"","vin":"VEHICLEREALNAME21","vehicleModelName":"TEST1","vehicleOrderStatus":9203,"uuid":"20016"}
     */

    public AccountInfoBean accountInfo;
    public String token;
    public VehicleBean vehicle;

    public static class AccountInfoBean {
        /**
         * accountStatus : 0
         * password : dae3a6ff1559c0492f3058b93befc19a
         * avatar : http://10.1.32.147:8080/cnmobile/app_avatar1514874630567.jpg
         * uuid : 1234567895
         * username : 18512812703
         */

        public int accountStatus;
        public String password;
        public String avatar;
        public int uuid;
        public String username;
    }

    public static class VehicleBean {
        /**
         * vehicleColor :
         * isAuth : 9102
         * isDefault : 1
         * licensePlate :
         * vin : VEHICLEREALNAME21
         * vehicleModelName : TEST1
         * vehicleOrderStatus : 9203
         * uuid : 20016
         */

        public String vehicleColor;
        public int isAuth;
        public int isDefault;
        public String licensePlate;
        public String vin;
        public String vehicleModelName;
        public int vehicleOrderStatus;
        public String uuid;
    }

}
