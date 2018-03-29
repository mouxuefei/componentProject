package com.sihaiwanlian.home.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import com.alibaba.android.arouter.facade.annotation.Route
import com.roughike.bottombar.OnTabSelectListener
import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.utils.AppExit2Back
import com.sihaiwanlian.home.R
import com.sihaiwanlian.home.factory.FragmentFactory
import com.sihaiwanlian.home.factory.FragmentFactory.FRAGEMNT_LEFT
import kotlinx.android.synthetic.main.home_activity_home.*
import java.util.*

@Route(path = "/home/HomeActivity")
class HomeActivity : BaseActivity() {
    private var fragments: ArrayList<Fragment>
    private var mCurrentTabPostion: Int = 0
    override fun getContentView() = R.layout.home_activity_home

    init {
        fragments = getFragments()
    }

    override fun initData() {}

    override fun initView() {
        initViewPager()
    }

    val initViewPager = {
        main_viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragments.get(position)
            }
            override fun getCount(): Int {
                return fragments.size
            }
        }
        main_viewPager.setCurrentItem(0, false)
        main_viewPager.setOffscreenPageLimit(3)
        bottomBar.setOnTabSelectListener(OnTabSelectListener { tabId ->
            if (tabId == R.id.bb_menu_main) {
                mCurrentTabPostion = FragmentFactory.FRAGEMNT_LEFT
                main_viewPager.setCurrentItem(FRAGEMNT_LEFT, false)

            } else if (tabId == R.id.bb_menu_lookcar) {
                mCurrentTabPostion = FragmentFactory.FRAGMENT_CENTER_LEFT
                main_viewPager.setCurrentItem(FragmentFactory.FRAGMENT_CENTER_LEFT, false)

            } else if (tabId == R.id.bb_menu_data) {
                mCurrentTabPostion = FragmentFactory.FRAGEMNT_CENTER_RIGHT
                main_viewPager.setCurrentItem(FragmentFactory.FRAGEMNT_CENTER_RIGHT, false)

            } else if (tabId == R.id.bb_menu_setting) {
                mCurrentTabPostion = FragmentFactory.FRAGEMNT_RIGHT
                main_viewPager.setCurrentItem(FragmentFactory.FRAGEMNT_RIGHT, false)

            }
        })
        main_viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                bottomBar.selectTabAtPosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun getFragments(): ArrayList<Fragment> {
        val fragments = ArrayList<Fragment>()
        fragments.add(FragmentFactory.getInstance().createFragment(FRAGEMNT_LEFT))
        fragments.add(FragmentFactory.getInstance().createFragment(FragmentFactory.FRAGMENT_CENTER_LEFT))
        fragments.add(FragmentFactory.getInstance().createFragment(FragmentFactory.FRAGEMNT_CENTER_RIGHT))
        fragments.add(FragmentFactory.getInstance().createFragment(FragmentFactory.FRAGEMNT_RIGHT))
        return fragments
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppExit2Back.exitApp(applicationContext)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }



}
