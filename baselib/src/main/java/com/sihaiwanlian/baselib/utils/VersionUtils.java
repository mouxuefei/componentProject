package com.sihaiwanlian.baselib.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.sihaiwanlian.baselib.base.LibApplication;

/**
 * Created by ZHT on 2017/6/1.
 * 版本相关工具类
 */

public class VersionUtils {

    private VersionUtils() {
        throw new IllegalArgumentException("cannot create VersionUtils constructor!");
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            PackageManager manager = LibApplication.Companion.getMContext().getPackageManager();
            info = manager.getPackageInfo(LibApplication.Companion.getMContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return info;
    }

    public static String getPackageName() {
        return getPackageInfo().packageName;
    }

    /**
     * 获取应用版本名
     * @return 版本名
     */
    public static String getVersionName() {
        return getPackageInfo().versionName;
    }

    /**
     * 获取应用版本号
     * @return 版本号
     */
    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }
}
