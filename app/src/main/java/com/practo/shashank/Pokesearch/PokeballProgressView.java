package com.practo.shashank.Pokesearch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class PokeballProgressView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private float mRotatePokeball;
    private float mAddRotation;
    Paint paint ;
    private Thread updatePokeball;
    public PokeballProgressView(Context context) {
        super(context);
        init(null, 0);
    }

    public PokeballProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PokeballProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PokeballProgressView, defStyle, 0);


        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.PokeballProgressView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.PokeballProgressView_exampleDrawable)) {

        }

        a.recycle();

        paint = new Paint();
        mRotatePokeball = 0;
        mAddRotation = 10;
        // Update TextPaint and text measurements from attributes


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.

        mRotatePokeball += mAddRotation;

        if(mRotatePokeball >= 50){
            mAddRotation = -10;
        }
        if(mRotatePokeball <= -50){
            mAddRotation = 10;
        }
        paint.setStrokeWidth(contentWidth / 20);



        final RectF oval = new RectF();
        oval.set(paddingTop, paddingTop, contentWidth, contentHeight);

        paint.setColor(Color.RED);

        // if you want tilt right ==

        canvas.drawArc(oval, 0 + mRotatePokeball, -180, true, paint);

        paint.setColor(Color.WHITE);
        canvas.drawArc(oval, 0 + mRotatePokeball, 180, false, paint);


        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);

        canvas.drawCircle(oval.centerX(), oval.centerY(), contentWidth / 10, paint);

        canvas.drawArc(oval, 180 + mRotatePokeball, 180, true, paint);
        canvas.drawArc(oval, -180 + mRotatePokeball, -180, true, paint);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(oval.centerX(), oval.centerY(), contentWidth / 10, paint);


        // Draw the example drawable on top of the text.

    }
}