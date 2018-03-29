package com.sihaiwanlian.home.presenter

import java.util.*

/**
 * Created by mou on 2017/9/22.
 */

interface FragmentCenterRightPresenter {
    fun selectBeginTime(startMills: Long, endDate: Calendar)

    fun selectEndTime(endMills: Long, startDate: Calendar)

    fun loadDataByNet(isRetry: Boolean, isRefresh: Boolean, startMills: Long, endMills: Long)
}
