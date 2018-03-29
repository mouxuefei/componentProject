package com.sihaiwanlian.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sihaiwanlian.baselib.R;


/**
 * @author Yaooi
 * @time 2016/10/8  20:07
 * @desc
 */
public class ScrollingView extends View {

    private float mSpeed;
    private Bitmap mBitmap;
    private Paint mPaint;
    private float mOffset = 0;

    public ScrollingView(Context context) {
        super(context);
    }

    public ScrollingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //读取自定义的背景和速度
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollingView);
        mSpeed = typedArray.getFloat(R.styleable.ScrollingView_speed, 1.0f);
        int resourceId = typedArray.getResourceId(R.styleable.ScrollingView_scrollingBackground, 0);
        //设置位图
        mBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        //回收typedArray
        typedArray.recycle();
        mPaint = new Paint();
    }

    //确定控件大小


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //已经知道了mode，直接拿大小
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, mBitmap.getHeight());
    }

    //画出来

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOffset > getMeasuredWidth()) {
            mOffset = 0;
        }

        int left = (int) mOffset;
        canvas.drawBitmap(mBitmap, left, 0, mPaint);
        mOffset = mOffset + mSpeed;
        invalidate();
    }

}
