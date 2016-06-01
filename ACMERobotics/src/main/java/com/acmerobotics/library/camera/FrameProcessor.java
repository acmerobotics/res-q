package com.acmerobotics.library.camera;

import android.graphics.Bitmap;

public abstract class FrameProcessor {

    public static class Default extends FrameProcessor {
        @Override
        protected void process(Bitmap bitmap) {

        }
    }

    public static class SquareCorners extends FrameProcessor {

        private int[] blank;
        private int size;

        public SquareCorners() {
            size = 75;
            blank = new int[size * size];
            for (int i = 0; i < blank.length; i++) {
                blank[i] = 0xFF000000;
            }
        }

        @Override
        protected void process(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            bitmap.setPixels(blank, 0, size, 0, 0, size, size);
            bitmap.setPixels(blank, 0, size, width - size, height - size, size, size);
            bitmap.setPixels(blank, 0, size, width - size, 0, size, size);
            bitmap.setPixels(blank, 0, size, 0, height - size, size, size);
        }
    }

    protected abstract void process(Bitmap bitmap);

}
