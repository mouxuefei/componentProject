package com.sihaiwanlian.home.ui

import android.os.Bundle
import android.view.View
import com.sihaiwanlian.baselib.base.BaseFragment
import com.sihaiwanlian.home.R

/**
 * FileName: com.sihaiwanlian.home.ui.MainLeftFragment.java
 * Author: mouxuefei
 * date: 2018/3/21
 * version: V1.0
 * desc:
 */
class MainCenterLeftFragment : BaseFragment() {
    companion object {
        fun newInstance(position: Int): MainCenterLeftFragment {
            val fragment = MainCenterLeftFragment()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initData() {

    }


    override fun lazyLoad() {
    }

    override fun getContentView() = R.layout.home_fragment_main_centerleft

    override fun initView(view: View) {
    }
}