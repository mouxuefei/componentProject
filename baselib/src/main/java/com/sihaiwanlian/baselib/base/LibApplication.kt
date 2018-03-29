package com.sihaiwanlian.baselib.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.orhanobut.logger.Logger
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kotlin.properties.Delegates

/**
 * FileName: com.sihaiwanlian.baselib.base.LibApplication.java
 * Author: mouxuefei
 * date: 2018/3/5
 * version: V1.0
 * desc:
 */
open class LibApplication : Application() {
    private var refWatcher: RefWatcher? = null

    companion object {
        var mContext: Context by Delegates.notNull()
            private set
        fun getRefWatcher(context: Context?): RefWatcher? {
            val myApplication = context?.applicationContext as LibApplication
            return myApplication.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        //LeakCanary初始化
        refWatcher = setupLeakCanary()

        //初始化生命周期
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
    }

    private fun setupLeakCanary(): RefWatcher {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            RefWatcher.DISABLED
        } else LeakCanary.install(this)
    }


    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Logger.e("onCreated: " + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Logger.e("onDestroy: " + activity.componentName.className)
        }
    }

}