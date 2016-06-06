package com.acmerobotics.library.image;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.acmerobotics.library.tree.Node;

public class RotateNode extends Node<Bitmap, Bitmap> {

    private int angle;

    public RotateNode() {
        angle = 0;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int newAngle) {
        angle = newAngle;
    }

    private Bitmap rotateBitmap(int width, int height, Bitmap currentFrame) {
        Bitmap correctedFrame;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        correctedFrame = Bitmap.createBitmap(currentFrame, 0, 0, width, height, matrix, true);
        return correctedFrame;
    }

    @Override
    public Bitmap process(Bitmap input) {
        return rotateBitmap(input.getWidth(), input.getHeight(), input);
    }

}
