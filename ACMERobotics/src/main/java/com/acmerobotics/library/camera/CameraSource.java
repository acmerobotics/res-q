package com.acmerobotics.library.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.acmerobotics.library.image.MirrorNode;
import com.acmerobotics.library.image.MonoNode;
import com.acmerobotics.library.module.core.BaseModule;
import com.acmerobotics.library.module.core.Dependency;
import com.acmerobotics.library.module.core.Inject;
import com.acmerobotics.library.module.core.Injector;
import com.acmerobotics.library.module.core.Provider;
import com.acmerobotics.library.module.hardware.HardwareModule;
import com.acmerobotics.library.tree.Node;
import com.acmerobotics.library.tree.Tree;
import com.acmerobotics.library.ui.RobotUILayout;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class CameraSource implements Camera.PreviewCallback, Camera.PictureCallback, RobotUILayout.Callback {

    public enum CameraMode {
        MANUAL              (true),
        MANUAL_NO_PREVIEW   (false),
        CONTINUOUS          (true);

        public boolean preview;

        CameraMode(boolean p) {
            preview = p;
        }
    }

    public enum Orientation {
        PORTRAIT,
        LANDSCAPE
    }

    private CameraMode cameraMode;
    private Orientation orientation;

    private OpMode opMode;
    private Activity activity;
    private Camera camera;
    private RawPreview rawPreview;
    private CameraHandler cameraHandler;
    private RobotUILayout robotUILayout;
    private ProcessedPreview processedPreview;
    private FrameLayout frameLayout;
    private Camera.Size previewSize;
    private YUVConverter converter;
    private Bitmap lastFrame;
    private Tree tree;

    private int previewWidth;
    private int previewHeight;

    public CameraSource(final Activity activity, CameraMode mode) {
        orientation = Orientation.PORTRAIT;
        cameraMode = mode;
        this.activity = activity;

        converter = new YUVConverter(activity);

        Tree.Builder builder = new Tree.Builder();
        builder.add(MirrorNode.class);
        tree = builder.finish();
    }

    @Inject
    public CameraSource(Activity activity) {
        this(activity, CameraMode.CONTINUOUS);
    }

    public void begin() {
        openCamera();

        if (cameraMode.preview) createLayout();
    }

    private void createLayout() {
        robotUILayout = new RobotUILayout(activity);
        robotUILayout.setCallback(this);
        robotUILayout.start();
    }

    public void takePicture() {
        camera.takePicture(null, this, null);
    }

    @Override
    public void layoutCreated(RelativeLayout layout) {
        layout.setHorizontalGravity(Gravity.CENTER);
        cameraHandler = new CameraHandler(this);
        rawPreview = new RawPreview(activity, cameraHandler);
        processedPreview = new ProcessedPreview(activity);
        frameLayout = new FrameLayout(activity);
        LinearLayout.LayoutParams weightedParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0F);
        rawPreview.setLayoutParams(weightedParams);
        processedPreview.setLayoutParams(weightedParams);
        processedPreview.setZOrderOnTop(true);
        frameLayout.addView(rawPreview);
        frameLayout.addView(processedPreview);
        layout.addView(frameLayout);
    }

    @Override
    public void layoutDestroyed(RelativeLayout layout) {

    }

    private void openCamera() {
        if (camera != null) {
            throw new RuntimeException("camera already initialized");
        }
        camera = Camera.open(getCameraId());
        camera.setDisplayOrientation(90);
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
            Log.d("Size", i + ": " + size.width + ", " + size.height);
        }
        previewSize = previewSizes.get(6);
        previewWidth = previewSize.width;
        previewHeight = previewSize.height;
        parameters.setPreviewSize(previewWidth, previewHeight);
        parameters.setPictureSize(previewWidth, previewHeight);
        parameters.setPictureFormat(ImageFormat.NV21);
        camera.setParameters(parameters);
    }

    private int getCameraId() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        int numOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numOfCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return i;
            }
        }
        return 0;
    }

    private void handleSetSurfaceHolder(SurfaceHolder holder) {
        rawPreview.setAspectRatio(getAspectRatio());
        processedPreview.setAspectRatio(getAspectRatio());
        camera.setPreviewCallback(this);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        camera.startPreview();
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        processFrame(bytes);
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        processFrame(bytes);
    }

    private void processFrame(byte[] bytes) {
        int width = getActualWidth();
        int height = getActualHeight();

        Bitmap currentFrame = converter.convertYUV(width, height, bytes);

        Bitmap correctedFrame;
        if (orientation == Orientation.PORTRAIT) {
            correctedFrame = rotateBitmap(width, height, currentFrame);
        } else {
            correctedFrame = currentFrame;
        }

        tree.send(correctedFrame);

        lastFrame = correctedFrame;

        processedPreview.displayBitmap(correctedFrame);
    }

    private Bitmap rotateBitmap(int width, int height, Bitmap currentFrame) {
        Bitmap correctedFrame;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        correctedFrame = Bitmap.createBitmap(currentFrame, 0, 0, width, height, matrix, true);
        return correctedFrame;
    }

    public float getAspectRatio() {
        float width = getWidth();
        float height = getHeight();
        return width / height;
    }

    public int getActualWidth() {
        return previewWidth;
    }

    public int getActualHeight() {
        return previewHeight;
    }

    public int getWidth() {
        if (orientation == Orientation.PORTRAIT) {
            return previewHeight;
        } else {
            return previewWidth;
        }
    }

    public int getHeight() {
        if (orientation == Orientation.PORTRAIT) {
            return previewWidth;
        } else {
            return previewHeight;
        }
    }

    public void releaseCamera() {
        if (camera != null) {
            if (cameraMode.preview) {
                rawPreview.getHolder().removeCallback(rawPreview);
                camera.stopPreview();
                camera.setPreviewCallback(null);
            }
            camera.release();
            camera = null;
        }
        if (robotUILayout != null) {
            robotUILayout.stop();
            robotUILayout = null;
        }
        if (converter != null) {
            converter.release();
            converter = null;
        }
    }

    public Bitmap getLastFrame() {
        return lastFrame;
    }

    public static class CameraHandler extends Handler {

        public static final int MSG_SET_SURFACE_TEXTURE = 0;

        private WeakReference<CameraSource> weakCamera;

        public CameraHandler(CameraSource camera) {
            weakCamera = new WeakReference<CameraSource>(camera);
        }

        public void invalidateHandler() {
            weakCamera.clear();
        }

        @Override  // runs on UI thread
        public void handleMessage(Message inputMessage) {
            int what = inputMessage.what;

            CameraSource camera = weakCamera.get();
            if (camera == null) {
                Log.i("CameraHandler", "internal camera is null");
                return;
            }

            switch (what) {
                case MSG_SET_SURFACE_TEXTURE:
                    camera.handleSetSurfaceHolder((SurfaceHolder) inputMessage.obj);
                    break;
                default:
                    throw new RuntimeException("unknown msg " + what);
            }
        }

    }
}