package com.changjiangev.cjev.base

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Handler
import com.alibaba.android.arouter.launcher.ARouter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.sihaiwanlian.baselib.base.LibApplication
import kotlin.properties.Delegates

/**
 * Created by mou on 2017/7/31.
 * desc:
 */

class BaseApplication : LibApplication() {
    val isDebug: Boolean
        get() = true

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        mMainThreadHandler = Handler()
        initLogger()
        initARouter()
//        CrashHandler.getInstance(this).init(applicationContext)
    }

    val initARouter = {
        if (isDebug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
    }

    val initLogger = {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)   // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("villa")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f)
        //非默认值
        {
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) {//非默认值
            val newConfig = Configuration()
            newConfig.setToDefaults()//设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    companion object {
        var mContext: Context by Delegates.notNull()
            private set
        var mMainThreadHandler: Handler? = null
    }

}
