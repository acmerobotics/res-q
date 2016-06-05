package com.acmerobotics.library.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

public class AspectSurfaceView extends SurfaceView {

    private float aspectRatio;
    private Activity activity;
    private View contentView;

    /** @param aspect quotient of the width / height */
    public AspectSurfaceView(Context context, float aspect) {
        super(context);

        contentView = null;

        aspectRatio = aspect;
        activity = (Activity) context;
    }

    public AspectSurfaceView(Context context) {
        this(context, 1.0F);
    }

    public void setAspectRatio(float ratio) {
        aspectRatio = ratio;
        requestLayout();
        invalidate();
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setContentView(View v) {
        contentView = v;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        float viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int calcWidth, calcHeight;
        if (aspectRatio > 1.0) {
            calcWidth = (int) viewWidth;
            calcHeight = Math.round(viewWidth / aspectRatio);
        } else {
            calcHeight = (int) viewHeight;
            calcWidth = Math.round(viewHeight * aspectRatio);
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(calcWidth, widthMode), MeasureSpec.makeMeasureSpec(calcHeight, heightMode));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (contentView != null) {
            contentView.draw(canvas);
        }
    }
}
