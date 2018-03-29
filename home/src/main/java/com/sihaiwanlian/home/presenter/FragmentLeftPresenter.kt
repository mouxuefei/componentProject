package com.sihaiwanlian.home.presenter

/**
 * Created by mou on 2017/9/21.
 */

interface FragmentLeftPresenter {
    fun openCarDoor(pin: String)

    fun closecarDoor(pin: String)

    fun openTrunk(pin: String)

    fun openCarWindow(pin: String)

    fun closeCarWindow(pin: String)

    fun openLight(pin: String)

    fun openWhistle(pin: String)

    fun closeAir(pin: String)

    fun getMainData(isRefresh: Boolean)

    fun putControlIdByNet(accountId: String, controlType: Int, openSeconds: Int, pin: String)
}
