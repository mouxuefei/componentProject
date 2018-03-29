package com.sihaiwanlian.baseproject.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sihaiwanlian.baselib.R;
import com.sihaiwanlian.baselib.utils.UIUtils;


/**
 * Created by mou on 2017/8/23.
 */

public class WaveLoadingView extends View {

    private static final float DEFAULT_AMPLITUDE_RATIO = 0.1f;
    private static final float DEFAULT_AMPLITUDE_VALUE = 20.0f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    private static final int DEFAULT_WAVE_PROGRESS_VALUE = 100;
    private static final int DEFAULT_WAVE_COLOR = Color.parseColor("#49B4F0");//默认的背景色
    private static final int DEFAULT_WAVE_BACKGROUND_COLOR = Color.parseColor("#00000000");
    private static final int DEFAULT_TITLE_COLOR = Color.WHITE;
    private static final int DEFAULT_STROKE_COLOR = Color.TRANSPARENT;
    private static final float DEFAULT_BORDER_WIDTH = 0;
    private static final float DEFAULT_TITLE_STROKE_WIDTH = 0;
    private static final int DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE.ordinal();
    private static final int DEFAULT_ROUND_RECTANGLE_X_AND_Y = 30;
    private static final float DEFAULT_TITLE_TOP_SIZE = 16.0f;
    private static final float DEFAULT_TITLE_CENTER_SIZE = 26.0f;
    private static final float DEFAULT_TITLE_BOTTOM_SIZE = 16.0f;
    private float radius;//半径
    private int centerXY;//圆心的位置

    public enum ShapeType {
        TRIANGLE,
        CIRCLE,
        SQUARE,
        RECTANGLE
    }


    private int mCanvasSize;
    private int mCanvasHeight;
    private int mCanvasWidth;
    private float mAmplitudeRatio;//波浪的高度
    private int mWaveBgColor;
    private int mWaveColor;
    private int mShapeType;
    private int mTriangleDirection;
    private int mRoundRectangleXY;
    //画外边缘线的画笔
    private Paint linePaint;
    // Properties.
    private String mTopTitle = "剩余流量";
    private String mCenterTitle = "0.0";
    private String mBottomTitle = "GB";
    private float mDefaultWaterLevel;
    private float mWaterLevelRatio = 1f;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    private int mProgressValue = DEFAULT_WAVE_PROGRESS_VALUE;
    private boolean mIsRoundRectangle;

    // Object used to draw.
    // Shader containing repeated waves.
    private BitmapShader mWaveShader;
    private Bitmap bitmapBuffer;
    // Shader matrix.
    private Matrix mShaderMatrix;
    //画波浪
    private Paint mWavePaint;
    //画背景
    private Paint mWaveBgPaint;
    // Paint to draw border.
    private Paint mBorderPaint;
    // Point to draw title.
    private Paint mTopTitlePaint;
    private Paint mBottomTitlePaint;
    private Paint mCenterTitlePaint;
    //画上面的文字
    private Paint mTopTitleStrokePaint;
    private Paint mBottomTitleStrokePaint;
    private Paint paintCircleBg;//画背景

    // Animation.
    private ObjectAnimator waveShiftAnim;
    private AnimatorSet mAnimatorSet;

    private Context mContext;

    // Constructor & Init Method.
    public WaveLoadingView(final Context context) {
        this(context, null);
    }

    public WaveLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        // Init Wave.
        mShaderMatrix = new Matrix();
        mWavePaint = new Paint();
        // The ANTI_ALIAS_FLAG bit AntiAliasing smooths out the edges of what is being drawn,
        // but is has no impact on the interior of the shape.
        mWavePaint.setAntiAlias(true);
        mWaveBgPaint = new Paint();
        mWaveBgPaint.setAntiAlias(true);
        // Init Animation
        initAnimation();

        // Load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.WaveLoadingView, defStyleAttr, 0);

        // Init ShapeType
        mShapeType = attributes.getInteger(R.styleable.WaveLoadingView_wlv_shapeType, DEFAULT_WAVE_SHAPE);

        // Init Wave
        mWaveColor = attributes.getColor(R.styleable.WaveLoadingView_wlv_waveColor, DEFAULT_WAVE_COLOR);
        mWaveBgColor = attributes.getColor(R.styleable.WaveLoadingView_wlv_wave_background_Color, DEFAULT_WAVE_BACKGROUND_COLOR);

        mWaveBgPaint.setColor(mWaveBgColor);

        // Init AmplitudeRatio
        float amplitudeRatioAttr = attributes.getFloat(R.styleable.WaveLoadingView_wlv_waveAmplitude, DEFAULT_AMPLITUDE_VALUE) / 1000;
        mAmplitudeRatio = (amplitudeRatioAttr > DEFAULT_AMPLITUDE_RATIO) ? DEFAULT_AMPLITUDE_RATIO : amplitudeRatioAttr;

        // Init Progress
        mProgressValue = attributes.getInteger(R.styleable.WaveLoadingView_wlv_progressValue, DEFAULT_WAVE_PROGRESS_VALUE);
//        setProgressValue(mProgressValue);

        // Init RoundRectangle
        mIsRoundRectangle = attributes.getBoolean(R.styleable.WaveLoadingView_wlv_round_rectangle, false);
        mRoundRectangleXY = attributes.getInteger(R.styleable.WaveLoadingView_wlv_round_rectangle_x_and_y, DEFAULT_ROUND_RECTANGLE_X_AND_Y);

        // Init Border
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(attributes.getDimension(R.styleable.WaveLoadingView_wlv_borderWidth, dp2px(DEFAULT_BORDER_WIDTH)));
        mBorderPaint.setColor(attributes.getColor(R.styleable.WaveLoadingView_wlv_borderColor, DEFAULT_WAVE_COLOR));

        // Init Top Title
        mTopTitlePaint = new Paint();
        mTopTitlePaint.setColor(attributes.getColor(R.styleable.WaveLoadingView_wlv_titleTopColor, DEFAULT_TITLE_COLOR));
        mTopTitlePaint.setStyle(Paint.Style.FILL);
        mTopTitlePaint.setAntiAlias(true);
        mTopTitlePaint.setTextSize(attributes.getDimension(R.styleable.WaveLoadingView_wlv_titleTopSize, sp2px(DEFAULT_TITLE_TOP_SIZE)));

        mTopTitleStrokePaint = new Paint();
        mTopTitleStrokePaint.setColor(attributes.getColor(R.styleable.WaveLoadingView_wlv_titleTopStrokeColor, DEFAULT_STROKE_COLOR));
        mTopTitleStrokePaint.setStrokeWidth(attributes.getDimension(R.styleable.WaveLoadingView_wlv_titleTopStrokeWidth, dp2px(DEFAULT_TITLE_STROKE_WIDTH)));
        mTopTitleStrokePaint.setStyle(Paint.Style.STROKE);
        mTopTitleStrokePaint.setAntiAlias(true);
        mTopTitleStrokePaint.setTextSize(mTopTitlePaint.getTextSize());


        // Init Center Title
        mCenterTitlePaint = new Paint();
        mCenterTitlePaint.setColor(attributes.getColor(R.styleable.WaveLoadingView_wlv_titleCenterColor, DEFAULT_TITLE_COLOR));
        mCenterTitlePaint.setStyle(Paint.Style.FILL);
        mCenterTitlePaint.setAntiAlias(true);
        mCenterTitlePaint.setTextSize(attributes.getDimension(R.styleable.WaveLoadingView_wlv_titleCenterSize, sp2px(DEFAULT_TITLE_CENTER_SIZE)));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mCenterTitlePaint.setTypeface(font);

        // Init Bottom Title
        mBottomTitlePaint = new Paint();
        mBottomTitlePaint.setColor(attributes.getColor(R.styleable.WaveLoadingView_wlv_titleBottomColor, DEFAULT_TITLE_COLOR));
        mBottomTitlePaint.setStyle(Paint.Style.FILL);
        mBottomTitlePaint.setAntiAlias(true);
        mBottomTitlePaint.setTextSize(attributes.getDimension(R.styleable.WaveLoadingView_wlv_titleBottomSize, sp2px(DEFAULT_TITLE_BOTTOM_SIZE)));

        mBottomTitleStrokePaint = new Paint();
        mBottomTitleStrokePaint.setColor(attributes.getColor(R.styleable.WaveLoadingView_wlv_titleBottomStrokeColor, DEFAULT_STROKE_COLOR));
        mBottomTitleStrokePaint.setStrokeWidth(attributes.getDimension(R.styleable.WaveLoadingView_wlv_titleBottomStrokeWidth, dp2px(DEFAULT_TITLE_STROKE_WIDTH)));
        mBottomTitleStrokePaint.setStyle(Paint.Style.STROKE);
        mBottomTitleStrokePaint.setAntiAlias(true);
        mBottomTitleStrokePaint.setTextSize(mBottomTitlePaint.getTextSize());

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setDither(true);

        paintCircleBg = new Paint();
        paintCircleBg.setStyle(Paint.Style.FILL);
        paintCircleBg.setColor(Color.parseColor("#8849b4f0"));
        attributes.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        mCanvasSize = canvas.getWidth();

        if (canvas.getHeight() < mCanvasSize) {
            mCanvasSize = canvas.getHeight();
        }


        if (mWaveShader != null) {
            if (mWavePaint.getShader() == null) {
                mWavePaint.setShader(mWaveShader);
            }

            mShaderMatrix.setScale(1, mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0, mDefaultWaterLevel);
            mShaderMatrix.postTranslate(mWaveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());
            mWaveShader.setLocalMatrix(mShaderMatrix);     // Assign matrix to invalidate the shader.
            float borderWidth = mBorderPaint.getStrokeWidth();   // Get borderWidth.
            switch (mShapeType) {
                case 1:   // Draw circle
                    drawCircle(canvas, borderWidth);
                    break;
            }
            //画上面的文字
            drawTopText(canvas);
            //画中间的文字
            drawCenterText(canvas);
            //花下面的文字
            drawBottomText(canvas);
        } else {
            mWavePaint.setShader(null);
        }
    }

    private void drawCircle(Canvas canvas, float borderWidth) {
        //画外边缘线
        if (borderWidth > 0) {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                    (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
        }
        radius = getWidth() / 2f - borderWidth;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius - UIUtils.sp2px(10), paintCircleBg);
        //画背景
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius - UIUtils.sp2px(10), mWaveBgPaint);
        //画波浪
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius - UIUtils.sp2px(10), mWavePaint);
    }


    private void drawBottomText(Canvas canvas) {
        if (!TextUtils.isEmpty(mBottomTitle)) {
            float bottom = mBottomTitlePaint.measureText(mBottomTitle);
            // Draw the bottom text
            canvas.drawText(mBottomTitle, (getWidth() - bottom) / 2,
                    getHeight() * 6 / 10.0f - ((mBottomTitlePaint.descent() + mBottomTitlePaint.ascent()) / 2), mBottomTitlePaint);
        }
    }

    private void drawCenterText(Canvas canvas) {
        if (!TextUtils.isEmpty(mCenterTitle)) {
            float middle = mCenterTitlePaint.measureText(mCenterTitle);
            // Draw the centered text
            canvas.drawText(mCenterTitle, (getWidth() - middle) / 2,
                    getHeight() / 2 - ((mCenterTitlePaint.descent() + mCenterTitlePaint.ascent()) / 2), mCenterTitlePaint);
        }
    }

    private void drawTopText(Canvas canvas) {
        if (!TextUtils.isEmpty(mTopTitle)) {
            float top = mTopTitlePaint.measureText(mTopTitle);
            canvas.drawText(mTopTitle, (getWidth() - top) / 2, getHeight() * 4 / 10.0f, mTopTitlePaint);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // If shapType is rectangle
        if (getShapeType() == 3) {
            mCanvasWidth = w;
            mCanvasHeight = h;
        } else {
            mCanvasSize = w;
            if (h < mCanvasSize){
                mCanvasSize = h;
            }

        }
        updateWaveShader();
    }

    private void updateWaveShader() {
        // IllegalArgumentException: width and height must be > 0 while loading Bitmap from View
        // http://stackoverflow.com/questions/17605662/illegalargumentexception-width-and-height-must-be-0-while-loading-bitmap-from
        if (bitmapBuffer == null || haveBoundsChanged()) {
            if (bitmapBuffer != null){
                bitmapBuffer.recycle();
            }

            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            if (width > 0 && height > 0) {
                double defaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / width;
                float defaultAmplitude = height * DEFAULT_AMPLITUDE_RATIO;
                mDefaultWaterLevel = height * DEFAULT_WATER_LEVEL_RATIO;
                float defaultWaveLength = width;

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);

                Paint wavePaint = new Paint();
                wavePaint.setStrokeWidth(2);
                wavePaint.setAntiAlias(true);

                // Draw default waves into the bitmap.
                // y=Asin(ωx+φ)+h
                final int endX = width + 1;
                final int endY = height + 1;

                float[] waveY = new float[endX];

                wavePaint.setColor(adjustAlpha(mWaveColor, 0.3f));
                for (int beginX = 0; beginX < endX; beginX++) {
                    double wx = beginX * defaultAngularFrequency;
                    float beginY = (float) (mDefaultWaterLevel + defaultAmplitude * Math.sin(wx));
                    canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
                    waveY[beginX] = beginY;
                }

                wavePaint.setColor(mWaveColor);
                final int wave2Shift = (int) (defaultWaveLength / 4);
                for (int beginX = 0; beginX < endX; beginX++) {
                    canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
                }
                mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
                this.mWavePaint.setShader(mWaveShader);
            }
        }
    }

    private boolean haveBoundsChanged() {
        return getMeasuredWidth() != bitmapBuffer.getWidth() ||
                getMeasuredHeight() != bitmapBuffer.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        if (getShapeType() == 3) {//长方形
            setMeasuredDimension(width, height);
        } else {//
            int imageSize = (width < height) ? width : height;
            centerXY = imageSize / 2;
            setMeasuredDimension(imageSize, imageSize);
        }

    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else {
            result = mCanvasWidth;
        }
        return result;
    }

    private int measureHeight(int measureSpecHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else {
            result = mCanvasHeight;
        }
        return (result + 2);
    }


    public int getShapeType() {
        return mShapeType;
    }

    /**
     * 设置波浪的高度
     */
    public void setAmplitudeRatio(int amplitudeRatio) {
        if (this.mAmplitudeRatio != (float) amplitudeRatio / 1000) {
            this.mAmplitudeRatio = (float) amplitudeRatio / 1000;
            invalidate();
        }
    }


    /**
     * 设置进度
     */
    public void setProgressValue(int progress) {
        mProgressValue = progress;
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", mWaterLevelRatio, (mProgressValue * 1f / 100));
        if (progress > 50) {
            waterLevelAnim.setDuration(800);
        } else {
            waterLevelAnim.setDuration(1200);
        }
//        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSetProgress = new AnimatorSet();
        animatorSetProgress.play(waterLevelAnim);
        animatorSetProgress.start();
    }


    public void setWaveShiftRatio(float waveShiftRatio) {
        if (this.mWaveShiftRatio != waveShiftRatio) {
            this.mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    public void setWaterLevelRatio(float waterLevelRatio) {
        if (this.mWaterLevelRatio != waterLevelRatio) {
            this.mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public void setBottomTitle(String bottomTitle) {
        mBottomTitle = bottomTitle;
        invalidate();
    }

    public void setCenterTitle(String centerTitle) {
        mCenterTitle = centerTitle;
        invalidate();
    }

    public void startAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    public void endAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }

    public void cancelAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
    }

    private void initAnimation() {
        // Wave waves infinitely.
        waveShiftAnim = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(waveShiftAnim);
    }

    @Override
    protected void onAttachedToWindow() {
        startAnimation();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelAnimation();
        super.onDetachedFromWindow();
    }

    /**
     */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Paint.setTextSize(float textSize) default unit is px.
     */
    private int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}