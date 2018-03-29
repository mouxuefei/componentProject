package com.sihaiwanlian.baselib.http.progress;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.sihaiwanlian.baselib.R;


/**
 * Created by mouxuefei on 2017/7/27.
 * desc:
 */

@SuppressWarnings("ConstantConditions")
public class CustomProgressDialog extends Dialog {

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    private String titleStr;
    private TextView mTvTitle;

    public CustomProgressDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_netwrok);
        // 设置居中
        getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.0f;
        getWindow().setAttributes(lp);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
//        setCancelable(false);
    }

    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            mTvTitle.setText(titleStr);
        }

    }
    private void initView() {
        mTvTitle =  findViewById(R.id.tv_loading_text);
    }
}