
package com.sihaiwanlian.baselib.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.sihaiwanlian.baselib.base.LibApplication;

/**
 * ClassName:ToastUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年9月4日 下午4:41:10 <br/>
 *
 * @author mou
 */
public class ToastUtils {

    private static Toast sToast;

    /**
     * 中间的吐司
     */
    public static void showToastCenter(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(LibApplication.Companion.getMContext(), msg, Toast.LENGTH_SHORT);
            sToast.setGravity(Gravity.CENTER, 0, 0);
        }
        sToast.setText(msg);
        sToast.show();
    }
}
