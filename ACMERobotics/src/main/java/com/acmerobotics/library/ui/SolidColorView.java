package com.acmerobotics.library.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class SolidColorView extends View {

    private int color;
    private int height;
    private int width;

    public SolidColorView(Context context, int color) {
        super(context);

        this.width = 0;
        this.height = 0;
        this.color = color;
    }

    public void setPreferredWidth(int width) {
        this.width = width;
    }

    public void setPreferredHeight(int height) {
        this.height = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, mWidth, mHeight, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth, parentHeight;
        if (width == 0) {
            parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            parentWidth = width;
        }
        if (height == 0) {
            parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            parentHeight = height;
        }
        System.out.println("width: " + parentWidth);
        System.out.println("height: " + parentHeight);
        this.setMeasuredDimension(parentWidth, parentHeight);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
