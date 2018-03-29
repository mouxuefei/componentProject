package com.sihaiwanlian.baseproject.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;

public class RippleButton extends AppCompatButton {
    private int mX;
    private ObjectAnimator mAnimator;
    private int mCurRadius = 0;
    private RadialGradient mRadialGradient;
    private Paint mPaint;
    private float mDensity;
    private Rect mRect;
    private float mDownY;

    public RippleButton(Context context) {
        super(context);
        init();
    }

    public RippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        mPaint = new Paint();
    }

    private int dp(int dp) {
        return (int) (dp * mDensity + 0.5f);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mX != event.getX()) {
            mX = (int) event.getX();
            mRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mDownY==0){
                mDownY = event.getY();
            }
            setRadius(dp(60));
            return true;
        }else if (event.getAction() ==MotionEvent.ACTION_MOVE){
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            if (!mRect.contains(
                    getLeft() + (int) event.getX(),
                    getTop() + (int) event.getY())) {
                setRadius(0);
            } else {

                setRadius(dp(60));
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mRect.contains(
                    getLeft() + (int) event.getX(),
                    getTop() + (int) event.getY())){
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }

                if (mAnimator == null) {
                    mAnimator = ObjectAnimator.ofInt(this,"radius",dp(50), getWidth());
                }
                mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setRadius(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                mAnimator.start();
            }

        }

        return super.onTouchEvent(event);
    }

    public void setRadius(final int radius) {
        mCurRadius = radius;
        if (mCurRadius > 0) {
            mRadialGradient = new RadialGradient(mX, mDownY, mCurRadius, Color.parseColor("#dddddd"), Color.parseColor("#dddddd"), Shader.TileMode.MIRROR);
            mPaint.setShader(mRadialGradient);
        }
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mX, mDownY, mCurRadius, mPaint);
    }
}