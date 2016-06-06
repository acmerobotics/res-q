package com.acmerobotics.library.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.v8.renderscript.*;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.acmerobotics.library.image.StreamNode;
import com.acmerobotics.library.image.MonoNode;
import com.acmerobotics.library.image.RotateNode;
import com.acmerobotics.library.image.YUVNode;
import com.acmerobotics.library.inject.core.Injector;
import com.acmerobotics.library.robot.RobotOpMode;
import com.acmerobotics.library.tree.Tree;
import com.acmerobotics.library.ui.RobotUILayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class CameraSource implements StreamNode.StreamListener, Camera.PreviewCallback, Camera.PictureCallback, RobotUILayout.Callback {

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
        PORTRAIT    (90),
        LANDSCAPE   (0);

        public int angle;

        Orientation(int a) {
            angle = a;
        }
    }

    private CameraMode cameraMode;
    private Orientation orientation;

    private RobotOpMode opMode;
    private Activity activity;
    private Camera camera;
    private RawPreview rawPreview;
    private CameraHandler cameraHandler;
    private RobotUILayout robotUILayout;
    private ProcessedPreview processedPreview;
    private FrameLayout frameLayout;
    private Camera.Size previewSize;
    private RotateNode rotateNode;
    private RenderScript rs;
    private Bitmap latestFrame;
    private Tree.Builder treeBuilder;

    private int previewWidth;
    private int previewHeight;

    public CameraSource(final RobotOpMode mode) {
        cameraMode = CameraMode.CONTINUOUS;
        activity = (Activity) mode.hardwareMap.appContext;
        opMode = mode;
        rs = RenderScript.create(activity);

        Injector injector = mode.injector;
        injector.module.bind(RenderScript.class).toInstance(rs);

        treeBuilder = new Tree.Builder(injector);
        treeBuilder.add(YUVNode.class);
        rotateNode = (RotateNode) treeBuilder.add(RotateNode.class).getCurrent();
        treeBuilder.add(MonoNode.class);
        StreamNode node = (StreamNode) treeBuilder.add(StreamNode.class).getCurrent();
        node.setCallback(this);
        treeBuilder.parent();

        setOrientation(Orientation.PORTRAIT);
    }

    public void begin() {
        openCamera();

        if (cameraMode.preview) createPreview();
    }

    private void createPreview() {
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
        camera.setDisplayOrientation(orientation.angle);
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

    @Override
    public void onFrame(Bitmap bitmap) {
        latestFrame = bitmap;
        processedPreview.displayBitmap(bitmap);
    }

    private void processFrame(byte[] bytes) {
        int width = getActualWidth();
        int height = getActualHeight();

        Tree tree = treeBuilder.tree();

        YuvImage image = new YuvImage(bytes, ImageFormat.NV21, width, height, null);

        tree.send(image);
    }

    public Tree.Builder getTreeBuilder() {
        return treeBuilder;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        rotateNode.setAngle(orientation.angle);
        if (camera != null) {
            camera.setDisplayOrientation(orientation.angle);
        }
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
        if (rs != null) {
            rs.destroy();
            rs = null;
        }
    }

    public Bitmap getLatestFrame() {
        return latestFrame;
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