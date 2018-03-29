package com.sihaiwanlian.baseproject.result;

/**
 * FileName: com.sihaiwanlian.baseproject.result.RegisterBean.java
 * Author: mouxuefei
 * date: 2018/3/20
 * version: V1.0
 * desc:
 */
public class RegisterBean {
    /**
     * accountInfo : {"accountStatus":0,"password":"d528624fc9aa6a84b05a63616ce19025","uuid":13683012773,"username":"18600592368"}
     * token : loginUser18600592368_f989b3c2f9fe1305a1df335d897bc499
     */

    public AccountInfoBean accountInfo;
    public String token;

    public static class AccountInfoBean {
        /**
         * accountStatus : 0
         * password : d528624fc9aa6a84b05a63616ce19025
         * uuid : 13683012773
         * username : 18600592368
         */

        public int accountStatus;
        public String password;
        public long uuid;
        public String username;
    }
}
