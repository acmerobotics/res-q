package com.acmerobotics.library.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.acmerobotics.library.ui.AspectSurfaceView;

public class ProcessedPreview extends AspectSurfaceView {

    public ProcessedPreview(Context context) {
        super(context);
    }

    public void displayBitmap(Bitmap bitmap) {
        Canvas canvas = getHolder().lockCanvas();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(bitmap,
            new Rect(0, 0, bitmapWidth, bitmapHeight),
            new Rect(0, 0, canvasWidth, canvasHeight),
        null);
        getHolder().unlockCanvasAndPost(canvas);
    }

}
