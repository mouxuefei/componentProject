package com.sihaiwanlian.baselib.http.progress;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.sihaiwanlian.baselib.R;
import com.sihaiwanlian.baselib.utils.ProgressDialogUtils;

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialogUtils pd;

    private Context context;


    public ProgressDialogHandler(Context context) {
        super();
        this.context = context;

    }

    private void initProgressDialog(String dialogMsg) {
        if (pd == null) {
            pd = new ProgressDialogUtils(context, R.style.dialog_transparent_style);
            pd.showProgressDialogWithText(dialogMsg);
            pd.showProgressDialog();
        }
    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dismissProgressDialog();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                Bundle data = msg.getData();
                String dialogMsg = data.getString("dialogMsg");
                initProgressDialog(dialogMsg);
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
