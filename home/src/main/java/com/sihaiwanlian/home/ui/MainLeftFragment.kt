package com.sihaiwanlian.home.ui

import android.os.Bundle
import android.view.View
import com.sihaiwanlian.baselib.base.BaseFragment
import com.sihaiwanlian.home.R
import kotlinx.android.synthetic.main.home_fragment_mainleft.*


/**
 * FileName: com.sihaiwanlian.home.ui.MainLeftFragment.java
 * Author: mouxuefei
 * date: 2018/3/21
 * version: V1.0
 * desc:
 */
class MainLeftFragment : BaseFragment() {

    companion object {
        fun newInstance(position: Int): MainLeftFragment {
            val fragment = MainLeftFragment()
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

    override fun getContentView() = R.layout.home_fragment_mainleft

    override fun initView(view: View) {
    }

    override fun savedstanceState(savedInstanceState: Bundle?) {
        home_fragment_left_mapView.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        home_fragment_left_mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        home_fragment_left_mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        home_fragment_left_mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        home_fragment_left_mapView.onDestroy()
    }
}