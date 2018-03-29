package com.sihaiwanlian.baselib.base;

import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sihaiwanlian.baselib.R;


/**
 * 所有标题的activity的父类
 * 在这里主要统一处理标题
 * Created by mou on 2016/7/13.
 */
public abstract class BaseTitleActivity extends BaseActivity {

    private final static int MENU_GROUP_ID = 0;
    //toolbar右边的icon的数组
    private final static int[] MENU_IDS = {0, 1, 2, 3};
    TextView titleTv;
    public Toolbar toolbar;
    FrameLayout container;
    private RelativeLayout baseParentRl;
    private String[] rightMenuTexts;
    private int[] rightMenuIcons;

    @Override
    public int getContentView() {
        return R.layout.activtiy_base_title;
    }


    @CallSuper
    @Override
    public void initView() {
        baseParentRl = findViewById(R.id.base_content);
        container = findViewById(R.id.base_container);
        container.addView(getLayoutInflater().inflate(getChildView(), null));
        toolbar = findViewById(R.id.base_toolbar);
        titleTv = findViewById(R.id.base_title_tv);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (hasBackIcon()) {
            toolbar.setNavigationIcon(R.drawable.return_icon);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private boolean hasBackIcon() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (rightMenuIcons != null) {
            for (int i = 0; i < rightMenuIcons.length; i++) {
                MenuItem item = menu.add(MENU_GROUP_ID, MENU_IDS[i], 0, "");
                item.setIcon(ContextCompat.getDrawable(this, rightMenuIcons[i]));
                item.setShowAsAction(Menu.FLAG_ALWAYS_PERFORM_CLOSE);
            }
        }
        if (rightMenuTexts != null) {
            for (int i = 0; i < rightMenuTexts.length; i++) {
                MenuItem item = menu.add(MENU_GROUP_ID, MENU_IDS[i], 0, "");
                item.setTitle(rightMenuTexts[i]);
                item.setShowAsAction(Menu.FLAG_ALWAYS_PERFORM_CLOSE);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onRightMenuClick(item.getItemId());
        return false;
    }

    /**
     * 设置toolbar右边的文字
     */
    public void setRightMenuTexts(String... rightMenuTexts) {
        this.rightMenuTexts = rightMenuTexts;
    }

    /**
     * 设置toolbar右边的icon
     */
    public void setRightMenuIcons(int... rightMenuIcons) {
        this.rightMenuIcons = rightMenuIcons;
    }

    /**
     * 当toolbar右边的icon，被点击，数据0,1,2,3
     */
    protected void onRightMenuClick(int itemId) {

    }

    protected abstract int getChildView();


    @Override
    public boolean isTitleActivity() {
        return true;
    }

    /**
     * 设置中间的title
     */
    protected void setActivityTitle(int resId) {
        titleTv.setText(resId);
    }

    protected void setActivityTitle(String text) {
        titleTv.setText(text);
    }

    /**
     * 设置中间title的颜色
     */
    public void setActivityTitleColor(int colorId) {
        titleTv.setTextColor(getResources().getColor(colorId));
    }

    protected View getContainer() {
        return container;
    }

    protected View getTitleView() {
        return titleTv;
    }


    public RelativeLayout getBaseParentRl() {
        return baseParentRl;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public String[] getRightMenuTexts() {
        return rightMenuTexts;
    }

    public int[] getRightMenuIcons() {
        return rightMenuIcons;
    }

    /**
     * 设置toolbar不同见
     */
    public void setToolBarGone() {
        toolbar.setVisibility(View.GONE);
    }
}
