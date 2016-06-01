package com.acmerobotics.library.camera;

import android.content.Context;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.acmerobotics.library.robot.AspectSurfaceView;

public class RawPreview extends AspectSurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private FtcCamera.CameraHandler cameraHandler;

    public RawPreview(Context context, FtcCamera.CameraHandler handler) {
        super(context);

        cameraHandler = handler;

        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Message msg = Message.obtain(cameraHandler, FtcCamera.CameraHandler.MSG_SET_SURFACE_TEXTURE, surfaceHolder);
        msg.sendToTarget();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
