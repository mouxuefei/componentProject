package com.sihaiwanlian.baseproject.utils;

import com.orhanobut.logger.Logger;
import com.sihaiwanlian.baseproject.config.Constants;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


/**
 * ProjectName:app-server.
 * Description: 加密工具类.
 * Copyright © 2017 ,www.oneiotworld.com All Rights Reserved.
 *
 * @author steven.
 * @date 2017-10-09 11:37 .
 * @since JDK 1.8
 */
public final class SecurityUtils {


    /**
     * RSA密码初始化大小.
     */
    private static final int RSA_INIT_SIZE = 512;

    /**
     * 数字 256 .
     */
    public static final int MAGIC_NUM_256 = 256;

    /**
     * 数字 16.
     */
    public static final int MAGIC_NUM_16 = 16;

    /**
     * 全局数组.
     */
    private static final String[] STRDIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
            "f"};

    /**
     * 私有构造方法
     */
    private SecurityUtils() {

    }


    /**
     * MD5签名
     *
     * @param content content
     * @return String
     */
    public static String md5(String content) {
        String newstr = null;
        try {
            newstr = new String(content);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            newstr = byteToString(md.digest(content.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return newstr;
    }

    /**
     * @param strByte strByte
     * @return String
     */
    private static String byteToString(byte[] strByte) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < strByte.length; i++) {
            buffer.append(byteToArrayString(strByte[i]));
        }
        return buffer.toString();
    }


    /**
     * @param strByte strByte
     */
    private static String byteToArrayString(byte strByte) {
        int intRet = strByte;
        if (intRet < 0) {
            intRet += MAGIC_NUM_256;
        }
        int int1 = intRet / MAGIC_NUM_16;
        int int2 = intRet % MAGIC_NUM_16;
        return STRDIGITS[int1] + STRDIGITS[int2];
    }

    /**
     * MD5签名
     *
     * @param content content
     * @return String
     */
    public static String md5Sign(String content) {
        return new String(Hex.encodeHex(DigestUtils.md5(content)));
    }

    public static String changeMap2Str(Map<String, Object> paramsCompareMap, String timeStamp) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INSTANCE.getV_APPKEY());
        for (Map.Entry<String, Object> entry : paramsCompareMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            stringBuffer.append(key).append("=").append(value);
        }
        stringBuffer.append(timeStamp);
        Logger.e("content=" + stringBuffer.toString());
        String s1 = md5(stringBuffer.toString());
        Logger.e("md5=" + s1);
        String s = md5Sign(s1);
        return s;
    }


    public static String changeMap2Web(Map<String, Object> paramsCompareMap, String appkey,String secrety) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(appkey);
        for (Map.Entry<String, Object> entry : paramsCompareMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            stringBuffer.append(key).append("=").append(value);
        }
        stringBuffer.append(secrety);
        Logger.e("content=" + stringBuffer.toString());
        String s1 = md5(stringBuffer.toString());
//        Logger.e("md5=" + s1);
//        String s = md5Sign(s1);
        return s1;
    }

}
