package com.xj.maxlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This layout could set maxWidth,maxHeight,ratio
 * <p>
 * Created by jun xu on 19-2-1.
 */
public class MaxLayout extends FrameLayout {

    private static final String TAG = "MaxLayout";
    public static final float DEF_VALUE = -1f;

    public static final int W_H = 1;
    public static final int H_W = 2;
    public static final int STANDRAD = 0;

    private Context mContext;

    private float mMaxHeight = -1f;
    private float mMaxWidth = -1f;

    private @RatioStandrad
    int mRatioStandrad;
    private float mRatio;

    @IntDef({STANDRAD, W_H, H_W})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RatioStandrad {
    }

    public MaxLayout(Context context) {
        this(context, null, 0);
    }

    public MaxLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.MaxLayout);
        mMaxHeight = ta.getDimension(R.styleable.MaxLayout_ml_maxheight, DEF_VALUE);
        mMaxWidth = ta.getDimension(R.styleable.MaxLayout_ml_maxWidth, DEF_VALUE);
        mRatioStandrad = ta.getInt(R.styleable.MaxLayout_ml_ratio_standard, STANDRAD);
        mRatio = ta.getFloat(R.styleable.MaxLayout_ml_ratio, 0);

        ta.recycle();
    }

    public void setMaxHeight(float maxHeight) {
        mMaxHeight = maxHeight;
    }

    public void setMaxWidth(float maxWidth) {
        mMaxWidth = maxWidth;
    }

    public void setRatioStandrad(int ratioStandrad) {
        mRatioStandrad = ratioStandrad;
    }

    /**
     * only mRatioStandrad is {@link #W_H, #H_W},this is effective
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    public float getMaxHeight() {
        return mMaxHeight;
    }

    public float getMaxWidth() {
        return mMaxWidth;
    }

    public int getRatioStandrad() {
        return mRatioStandrad;
    }

    public float getRatio() {
        return mRatio;
    }

    public void clearMaxWidthFlag() {
        mMaxWidth = -1f;
    }

    public void clearMaxHeightFlag() {
        mMaxHeight = -1f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 是否设置了比例
        boolean setRatio = isSetRatio();
        // 没有设置最大宽度，高度，宽高比例，不需要调整，直接返回
        if (mMaxWidth <= DEF_VALUE && mMaxHeight <= DEF_VALUE && !setRatio) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        // 拿到原来宽度，高度的 mode 和 size
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        Log.d(TAG, "origin onMeasure: widthSize =" + widthSize + "heightSize = " + heightSize);

        if (mRatioStandrad == W_H) { // 当模式已宽度为基准
            widthSize = getWidth(widthSize);

            if (mRatio >= 0) {
                heightSize = (int) (widthSize * mRatio);

            }

            heightSize = getHeight(heightSize);
            int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
            super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);

        } else if (mRatioStandrad == H_W) { // 当模式已高度为基准
            heightSize = getHeight(heightSize);

            if (mRatio >= 0) {
                widthSize = (int) (heightSize * mRatio);
            }

            widthSize = getWidth(widthSize);

            int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);

        } else { // 当没有设定比例的时候
            widthSize = getWidth(widthSize);
            heightSize = getHeight(heightSize);
            int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
            super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);

        }

        Log.d(TAG, "adjust onMeasure: widthSize =" + widthSize + "heightSize = " + heightSize);

    }

    // 对宽度进行调整，是否超出最大宽度，超出取最大宽度，没超出，取原来的值
    private int getWidth(int widthSize) {
        if (mMaxWidth <= DEF_VALUE) {
            return widthSize;
        }

        return widthSize <= mMaxWidth ? widthSize : (int) mMaxWidth;
    }

    // 对高度进行调整，是否超出最大高度，超出取最大高度，没超出，取原来的值
    private int getHeight(int heightSize) {
        if (mMaxHeight <= DEF_VALUE) {
            return heightSize;
        }
        return heightSize <= mMaxHeight ? heightSize : (int) mMaxHeight;
    }

    private boolean isSetRatio() {
        return mRatio > 0f && (mRatioStandrad == W_H || mRatioStandrad == H_W);
    }

}