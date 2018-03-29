package com.sihaiwanlian.home.factory;


import android.support.v4.app.Fragment;

import com.sihaiwanlian.home.ui.MainCenterLeftFragment;
import com.sihaiwanlian.home.ui.MainCenterRightFragment;
import com.sihaiwanlian.home.ui.MainLeftFragment;
import com.sihaiwanlian.home.ui.MainRightFragment;


/**
 * 类    名:  FragmentFactory
 * 创 建 者:  mou
 * 创建时间:  2016/10/15 09:05
 * 描    述： 负责完成Framgent的创建过程
 */
public class FragmentFactory {
    public static final int FRAGEMNT_LEFT = 0;
    public static final int FRAGMENT_CENTER_LEFT = 1;
    public static final int FRAGEMNT_CENTER_RIGHT = 2;
    public static final int FRAGEMNT_RIGHT = 3;
    private static volatile FragmentFactory instance = null;
    public static FragmentFactory getInstance() {
        synchronized (FragmentFactory.class) {
            if (instance == null) {
                instance = new FragmentFactory();
            }
        }
        return instance;
    }

    private FragmentFactory() {
    }

    /**
     * 根据position创建对应的Framgent
     */
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case FRAGEMNT_LEFT:
                fragment = MainLeftFragment.Companion.newInstance(FRAGEMNT_LEFT);
                break;
            case FRAGMENT_CENTER_LEFT:
                fragment = MainCenterLeftFragment.Companion.newInstance(FRAGMENT_CENTER_LEFT);
                break;
            case FRAGEMNT_CENTER_RIGHT:
                fragment = MainCenterRightFragment.Companion.newInstance(FRAGEMNT_CENTER_RIGHT);
                break;
            case FRAGEMNT_RIGHT:
                fragment = MainRightFragment.Companion.newInstance(FRAGEMNT_RIGHT);
                break;
        }
        return fragment;
    }

}
