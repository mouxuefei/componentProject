package com.sihaiwanlian.baseproject.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.sihaiwanlian.baselib.utils.UIUtils;

import static com.sihaiwanlian.baselib.utils.UIUtils.dp2px;


/**
 * Created by mou on 2017/9/6.
 */

public class BatteryContinueView extends View {
    private Paint allArcPaint;//底部圆的画笔
    private Paint innerrPaint;//底部圆的画笔
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint curSpeedPaint;
    private Paint mPaintPointer;
    private PaintFlagsDrawFilter mDrawFilter;


    private int maxValues = 100;//最大值
    private int currentValues = 0;//当前值
    private int error = -1;
    private int endvalues = 0;//当前值

    private int bgArcWidth = dipToPx(5);//背景弧的宽度

    private float textSize = UIUtils.sp2px(19);
    private float curSpeedSize = UIUtils.sp2px(16);

    private String hintColor = "#0B9AE8";
    private String bgArcColor = "#22000000";//圆弧背景颜色
    private String titleString = "续航里程";

    private int mSize; // 最终的大小
    private int mRadus;

    private float mCenterX, mCenterY; // 圆心坐标
    RectF bgRectf;//最外层的矩形
    RectF innerRectf;//内存的矩形
    private float startangle = 135f;
    private float sweepAngle = 270;
    private int mSecondeRedus;
    private String centerTitle ="0";

    public BatteryContinueView(Context context) {
        this(context, null);
    }

    public BatteryContinueView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryContinueView(Context context, AttributeSet attrs, int defStyleAttr) {
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

//        Shader mShader = new LinearGradient(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), new int[]{Color.parseColor("#4F8DFE"), Color.parseColor("#27C4FD")}, null, Shader.TileMode.REPEAT);
//        //新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标
//        // 连接这2个点就拉出一条渐变线了，
//        // 玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，
//        // 如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
//        progressPaint.setShader(mShader);

        mSecondeRedus = mRadus - dp2px(10);
        //画内部圆弧
        innerRectf = new RectF(getPaddingLeft() + bgArcWidth / 2 + dp2px(10), getPaddingTop() + bgArcWidth / 2 + dp2px(10),
                getMeasuredWidth() - bgArcWidth / 2 - dp2px(10), getMeasuredHeight() - bgArcWidth / 2 - dp2px(10));
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
            result = dp2px(220);
        }
        return result;
    }

    private void initPaint() {
        path = new Path();
        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //内部的圆
        innerrPaint = new Paint();
        innerrPaint.setAntiAlias(true);
        innerrPaint.setStyle(Paint.Style.STROKE);
        innerrPaint.setStrokeWidth(dp2px(2));
        innerrPaint.setColor(Color.parseColor("#80d0fe"));
        innerrPaint.setStrokeCap(Paint.Cap.ROUND);


        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(bgArcWidth);
        progressPaint.setColor(Color.parseColor("#14FEE2"));


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

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setDither(true);
        linePaint.setColor(Color.parseColor("#80d0fe"));


        mPaintPointer = new Paint();
        mPaintPointer.setAntiAlias(true);
        mPaintPointer.setStrokeWidth(3);

        mPaintPointer.setStyle(Paint.Style.FILL);
        mPaintPointer.setColor(Color.WHITE);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    }

    //画外边缘线的画笔
    private Paint linePaint;

    /**
     * 实现画刻度线的功能
     */
    private void drawLine(final Canvas canvas) {
        // 保存之前的画布状态
        canvas.save();
        // 移动画布，实际上是改变坐标系的位置
        canvas.translate(mCenterX, mCenterX);
        canvas.rotate(45);
        // 设置画笔的宽度（线的粗细）
        linePaint.setStrokeWidth(UIUtils.sp2px(1));
        // 累计叠加的角度
        float c = 0;
        float sweepAngle = 270;

        float a = sweepAngle / 45;

        for (int i = 0; i <= 45; i++) {
            double p = c / (double) sweepAngle;
            canvas.drawLine(0, mSecondeRedus - dp2px(10), 0, mSecondeRedus - dp2px(2), linePaint);
            // 画过的角度进行叠加
            c += a;
            canvas.rotate(a);
        }
        // 恢复画布状态。
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

        //整个弧
        canvas.drawArc(bgRectf, startangle, sweepAngle, false, allArcPaint);
        //画内部圆弧
        canvas.drawArc(innerRectf, startangle, sweepAngle, false, innerrPaint);
        drawLine(canvas);
        if (currentValues!=error){
            //画进度圆弧
            int percent = currentValues * (270) / maxValues;
            canvas.drawArc(bgRectf, startangle, percent, false, progressPaint);
        }

        if (currentValues == error) {
            vTextPaint.setColor(Color.parseColor("#ffd200"));
            //写中间进度
            canvas.drawText("异常", mCenterX, getMeasuredHeight() - textSize * 3 / 2, vTextPaint);
        } else {
            vTextPaint.setColor(Color.WHITE);
            //写中间进度
            canvas.drawText( centerTitle+" km", mCenterX, getMeasuredHeight() - textSize * 3 / 2, vTextPaint);
        }
        vTextPaint.setColor(Color.WHITE);
        //写title
        canvas.drawText(titleString, mCenterX, getMeasuredHeight() - textSize / 2, curSpeedPaint);
        drawPointer(canvas);
    }

    private int mPotinterRaduis;
    private Path path;

    /**
     * 绘制指针
     */
    private void drawPointer(Canvas canvas) {
        mPotinterRaduis = mRadus / 8;
        float initAngle = getAngleFromResult(currentValues);
        path.reset();
        float[] point1 = getCoordinatePoint(mPotinterRaduis / 2, initAngle + 90);
        path.moveTo(point1[0], point1[1]);
        float[] point2 = getCoordinatePoint(mPotinterRaduis / 2, initAngle - 90);
        path.lineTo(point2[0], point2[1]);
        float[] point3 = getCoordinatePoint(mRadus / 3, initAngle);
        path.lineTo(point3[0], point3[1]);
        path.close();
        canvas.drawPath(path, mPaintPointer);
        // 绘制三角形指针底部的圆弧效果
        canvas.drawCircle((point1[0] + point2[0]) / 2, (point1[1] + point2[1]) / 2,
                mPotinterRaduis * 3 / 4, mPaintPointer);
    }

    private int mMin = 0; // 最小值

    /**
     * 通过数值得到角度位置
     */
    private float getAngleFromResult(float result) {
        if (result > maxValues) {
            return currentValues;
        }
        return sweepAngle * (result - mMin) / (maxValues - mMin) + startangle;
    }


    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     */
    public float[] getCoordinatePoint(int radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
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

    public void setMaxValues(int maxValues) {
        this.maxValues = maxValues;
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

    public void setCenterTitle(String centerTitle) {
        this.centerTitle = centerTitle;
        invalidate();
    }
}