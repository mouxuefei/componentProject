package com.sihaiwanlian.baseproject.widget.nicedialog;

import android.support.v4.app.FragmentManager;

import com.sihaiwanlian.baselib.R;


/**
 * Created by mou on 2017/9/19.
 */

public class NiceDialogUtils {
    private static volatile NiceDialogUtils instance=null;
    public static NiceDialogUtils getInstance(){
        if (instance==null){
            synchronized(NiceDialogUtils.class){
                if(instance==null){
                    instance=new NiceDialogUtils();
                }
            }
        }

        return instance;
    }
    private NiceDialogUtils(){}

    /**
     * 从底部弹出的dialog
     * @param resId
     * @param fragmentManager
     * @param viewConvertListener
     */
    public void showDialogAtBottom(int resId, FragmentManager fragmentManager,ViewConvertListener viewConvertListener){
        NiceDialog
                .init()
                .setLayoutId(resId)
                .setConvertListener(viewConvertListener)
                .setAnimStyle(R.style.popwindow_bottom_style)
                .setDimAmount(0.5f)
                .setShowBottom(true)
                .setOutCancel(true)
                .show(fragmentManager);
    }

    /**
     * 从中间弹出的dialog
     * @param resId
     * @param fragmentManager
     * @param viewConvertListener
     */
    public void showDialogAtCenter(int resId, FragmentManager fragmentManager,ViewConvertListener viewConvertListener){
        NiceDialog.init()
                .setLayoutId(resId)
                .setConvertListener(viewConvertListener)
                .setAnimStyle(R.style.popwindow_center_style)
                .setDimAmount(0.5f)
                .setMargin(35)
                .setShowBottom(false)
                .setOutCancel(true)
                .show(fragmentManager);
    }

    public void showDialogAtCenterCantCancel(int resId, FragmentManager fragmentManager,ViewConvertListener viewConvertListener){
        NiceDialog.init()
                .setLayoutId(resId)
                .setConvertListener(viewConvertListener)
                .setAnimStyle(R.style.popwindow_center_style)
                .setDimAmount(0.5f)
                .setMargin(35)
                .setShowBottom(false)
                .setOutCancel(false)
                .show(fragmentManager);
    }
}
