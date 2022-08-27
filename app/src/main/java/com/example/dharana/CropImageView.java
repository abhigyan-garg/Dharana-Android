package com.example.dharana;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.dharana.fragments.ChakraFragment;
import com.example.dharana.fragments.KriyaFragment;

public class CropImageView extends AppCompatImageView {
    private static final float DEFAULT_HORIZONTAL_OFFSET = 0.5f;
    private static final float DEFAULT_VERTICAL_OFFSET = 1;

    private final float mHorizontalOffsetPercent = DEFAULT_HORIZONTAL_OFFSET;
    private final float mVerticalOffsetPercent = DEFAULT_VERTICAL_OFFSET;

    private ChakraFragment chakraFragment;
    private KriyaFragment kriyaFragment;

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageView(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        applyCropOffset();
    }

    private void applyCropOffset() {
        Matrix matrix = getImageMatrix();

        float verticalScale;
        float horizontalScale;
        int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int drawableWidth = 0, drawableHeight = 0;
        if (getDrawable() != null) {
            drawableWidth = getDrawable().getIntrinsicWidth();
            drawableHeight = getDrawable().getIntrinsicHeight();
        }

        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            horizontalScale = (float) viewWidth / (float) drawableWidth;
            verticalScale = (float) viewHeight / (float) drawableHeight;
        }
         else if( (float) viewHeight/((float) viewWidth / (float) drawableWidth * (float) drawableHeight)<16f/21f) {
            horizontalScale = (float) viewWidth / (float) drawableWidth;
            verticalScale = (float) viewHeight / (float) (drawableHeight * (16f/21f));
        }
        else {
            horizontalScale = (float) viewWidth / (float) drawableWidth;
            verticalScale = horizontalScale;
        }

        float viewToDrawableWidth = viewWidth / horizontalScale;
        float viewToDrawableHeight = viewHeight / verticalScale;
        float xOffset = mHorizontalOffsetPercent * (drawableWidth - viewToDrawableWidth);
        float yOffset = mVerticalOffsetPercent * (drawableHeight - viewToDrawableHeight);

        RectF drawableRect =
                new RectF(
                        xOffset,
                        yOffset,
                        xOffset + viewToDrawableWidth,
                        yOffset + viewToDrawableHeight);
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);

        setImageMatrix(matrix);
        if(chakraFragment != null)
            chakraFragment.setupLights(drawableHeight * verticalScale);
        else if(kriyaFragment != null)
            kriyaFragment.setupStar(drawableHeight * verticalScale, drawableWidth * horizontalScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(chakraFragment != null) {
            RelativeLayout.LayoutParams whiteLightLayoutParams = (RelativeLayout.LayoutParams) chakraFragment.getWhiteLight().getLayoutParams();
            chakraFragment.getWhiteLight().setLayoutParams(whiteLightLayoutParams);
        }
        else if(kriyaFragment != null) {
            RelativeLayout.LayoutParams starLayoutParams = (RelativeLayout.LayoutParams) kriyaFragment.getStar().getLayoutParams();
            kriyaFragment.getStar().setLayoutParams(starLayoutParams);
        }
        super.onDraw(canvas);
    }

    public void setChakraFragment(ChakraFragment chakraFragment) {
        this.chakraFragment = chakraFragment;
    }

    public void setKriyaFragment(KriyaFragment kriyaFragment) {
        this.kriyaFragment = kriyaFragment;
    }


}