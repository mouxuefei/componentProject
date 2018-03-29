package com.changjiangev.cjev.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sihaiwanlian.baselib.utils.ActivityUtils;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    public static final String TAG = "CatchExcep";
    private static BaseApplication sApplication;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }


    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance(BaseApplication application) {
        sApplication = application;
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Logger.e(TAG, "error : ", e);
            }
            ActivityUtils.INSTANCE.popAllActivity();
            //杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            //彻底杀死当前app
            ActivityManager manager = (ActivityManager) sApplication.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(sApplication.getPackageName());
            System.gc();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序异常,即将退出!\r\n我们将在第一时间修复，不便之处请谅解!",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        ex.printStackTrace();
        //上报错误日志
//        CrashReport.postCatchedException(ex, new Thread());
        return true;
    }
}