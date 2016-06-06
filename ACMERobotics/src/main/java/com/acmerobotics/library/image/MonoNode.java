package com.acmerobotics.library.image;

import android.graphics.Bitmap;
import android.support.v8.renderscript.*;

import com.acmerobotics.library.tree.Node;

public class MonoNode extends Node<Bitmap, Bitmap> {

    private RenderScript rs;
    private ScriptIntrinsicColorMatrix script;
    private Allocation in, out;
    private Matrix3f colorMatrix;

    public MonoNode(RenderScript renderScript) {
        rs = renderScript;
        script = ScriptIntrinsicColorMatrix.create(rs, Element.U8_4(rs));

        float third = 1F / 3F;
        colorMatrix = new Matrix3f(new float[] {
                third, third, third,
                third, third, third,
                third, third, third
        });

//        colorMatrix = new Matrix3f(new float[] {
//                0, 0, 0,
//                0, 0, 0,
//                1F, 1F, 1F,
//        });
    }

    public Bitmap process(Bitmap bitmap) {
        in = Allocation.createFromBitmap(rs, bitmap);
        Bitmap output = bitmap.copy(bitmap.getConfig(), true);
        out = Allocation.createFromBitmap(rs, output);

        script.setColorMatrix(colorMatrix);
        script.forEach(in, out);

        out.copyTo(bitmap);
        return bitmap;
    }
}
