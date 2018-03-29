package com.sihaiwanlian.baseproject.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.sihaiwanlian.baselib.utils.UIUtils;


/**
 * colorful arc progress bar
 * Created by shinelw on 12/4/15.
 */
public class ColorArcProgressBar extends View {
    private Paint allArcPaint;//底部圆的画笔
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint curSpeedPaint;


    private PaintFlagsDrawFilter mDrawFilter;


    private int maxValues = 100;//最大值
    private int currentValues = 0;//当前值
    private int endvalues = 0;//当前值

    private int bgArcWidth = dipToPx(15);//背景弧的宽度

    private float textSize = UIUtils.sp2px(26);
    private float curSpeedSize = UIUtils.sp2px(16);

    private String hintColor = "#0B9AE8";


    private String bgArcColor = "#E7E7E7";//圆弧背景颜色
    private String titleString = "剩余电量";
    private int mSize; // 最终的大小
    private int mRadus;

    private float mCenterX, mCenterY; // 圆心坐标
    RectF bgRectf;
    private float startangle = 180f;
    private String centerTitle="%";

    public ColorArcProgressBar(Context context) {
        this(context, null);
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int realWidth = startMeasure(widthMeasureSpec);
        int realHeight = startMeasure(heightMeasureSpec);
        /**
         * 因为是以正方形为基础
         */
        mSize = Math.min(realHeight, realWidth);
        setMeasuredDimension(mSize, mSize);
        mCenterX = mCenterY = getMeasuredWidth() / 2f;
        mRadus = getMeasuredWidth() / 2 - bgArcWidth / 2;
        int scaleArcRadius = mSize / 2 - bgArcWidth / 2;
        bgRectf = new RectF(getPaddingLeft() + bgArcWidth / 2, getPaddingTop() + bgArcWidth / 2,
                getMeasuredWidth() - bgArcWidth / 2, getMeasuredHeight() - bgArcWidth / 2);

        Shader mShader = new LinearGradient(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), new int[]{Color.parseColor("#EE3B3B"), Color.parseColor("#FFCC44")}, null, Shader.TileMode.REPEAT);
        //新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标
        // 连接这2个点就拉出一条渐变线了，
        // 玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，
        // 如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        progressPaint.setShader(mShader);
    }

    /**
     * 测量大小
     */
    private int startMeasure(int whSpec) {
        int result = 0;
        int size = MeasureSpec.getSize(whSpec);
        int mode = MeasureSpec.getMode(whSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = UIUtils.dp2px(220);
        }
        return result;
    }

    private void initPaint() {
        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor("#22000000"));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(bgArcWidth);
        progressPaint.setColor(Color.RED);


        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.WHITE);
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        vTextPaint.setTypeface(font);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.WHITE);
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);


        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

        //整个弧
        canvas.drawCircle(mCenterX, mCenterY, mRadus, allArcPaint);
        if (currentValues!=-1){
            //画进度圆弧
            int percent = currentValues * (-360) / maxValues;
            canvas.drawArc(bgRectf, startangle, percent, false, progressPaint);
        }
        if (currentValues ==-1){
            vTextPaint.setColor(Color.parseColor("#ffd200"));
            //写中间进度
            canvas.drawText("异常", mCenterX, mCenterY + textSize * 2 / 3, vTextPaint);
        }else{
            vTextPaint.setColor(Color.WHITE);
            canvas.drawText(currentValues + centerTitle, mCenterX, mCenterY + textSize * 2 / 3, vTextPaint);
        }
        vTextPaint.setColor(Color.WHITE);
        //写title
        canvas.drawText(titleString, mCenterX, mCenterY - textSize / 2, curSpeedPaint);
    }


    /**
     * 设置当前值
     *
     * @param currentValues
     */
    public void setCurrentValues(int currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = -1;
        }
        setAnimator(currentValues);
    }


    ValueAnimator valueAnimator;
    private int animatorDuration;

    private void setAnimator(final int percent) {
        //根据变化的幅度来调整动画时长
        animatorDuration = (int) Math.abs(percent - endvalues) * 20;
        //   在这里我们用到了ValueAnimator，ValuAnimator本质上就是通过设置一个起始值和结束值，来取到一个从起始值到结束值的一个逐渐增长的Animation值。在draw方法中使用这个值并且不断的重绘，就能达到一种动画效果。
        valueAnimator = ValueAnimator.ofInt(currentValues, percent).setDuration(animatorDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //把获取到的
                currentValues = (int) animation.getAnimatedValue();
                invalidate();

            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentValues = (int) percent;
            }
        });
        valueAnimator.start();

    }


    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

}
