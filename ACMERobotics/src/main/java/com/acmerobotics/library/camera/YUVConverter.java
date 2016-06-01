package com.acmerobotics.library.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.*;

public class YUVConverter {

    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB scriptIntrinsicYuvToRGB;
    private Type.Builder yuvType, rgbaType;
    private Allocation in, out;

    public YUVConverter(Context context) {
        createScript(context);
    }

    private void createScript(Context context) {
        rs = RenderScript.create(context);
        scriptIntrinsicYuvToRGB = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
    }

    public Bitmap convertYUV(int width, int height, byte[] bytes) {
        yuvType = new Type.Builder(rs, Element.U8(rs)).setX(bytes.length);
        in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

        rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
        out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);

        in.copyFrom(bytes);

        scriptIntrinsicYuvToRGB.setInput(in);
        scriptIntrinsicYuvToRGB.forEach(out);

        Bitmap currentFrame = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        out.copyTo(currentFrame);
        return currentFrame;
    }

    public void release() {
        if (rs != null) {
            rs.destroy();
            rs = null;
        }
    }

}
