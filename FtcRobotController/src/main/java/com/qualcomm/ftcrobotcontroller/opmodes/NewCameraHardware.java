package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Admin on 10/19/2015.
 */
public class NewCameraHardware extends HardwareInterface {

    private Camera camera;
    private int width, height;
    private YuvImage yuvImage = null;
    public static final int PALETTE_SIZE = 8;
    public static final int ITER = 10;
    public Preview preview;

    public static class Preview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;
        private Camera.PreviewCallback previewCallback;

        public Preview(Context context, Camera camera, Camera.PreviewCallback previewCallback) {
            super(context);
            mCamera = camera;

            this.previewCallback = previewCallback;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewCallback(previewCallback);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {

            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewCallback(previewCallback);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){

            }
        }
    }

    public static class Color {
        public static int[] parseColor(int color) {
            int[] colors = new int[3];
            colors[0] = (color >> 16) & 0xff;
            colors[1] = (color >> 8) & 0xff;
            colors[2] = color & 0xff;
            return colors;
        }

        public static int[] randomColor() {
            Random rand = new Random();
            return parseColor(rand.nextInt() * 0xffffff);
        }
    }

    public interface PictureCallback {
        public void onPictureTaken(NewCameraHardware.Color[][] pixels, Camera camera);
    }

    @Override
    public void init(OpMode mode) {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); // open the front-facing camera
        } catch (Exception e) {
            camera = null;
        }
        ((FtcRobotControllerActivity) mode.hardwareMap.appContext).startPreview(camera, NewCameraHardware.this, new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                width = parameters.getPreviewSize().width;
                height = parameters.getPreviewSize().height;
                yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
            }
        });
    }

    public boolean hasImage() {
        return yuvImage != null;
    }

    public Bitmap getLastImage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 0, out);
        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public static int distance(int[] color1, int[] color2) {
        int sum = 0;
        for (int i = 0; i < color1.length; i++) {
            sum += (int) Math.pow(color1[i] - color2[i], 2);
        }
        return (int) Math.round(Math.sqrt(sum));
    }

    public static int[] getPredominantColor(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int length = width * height;
        int[][] pixels = new int[length][3];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] color = NewCameraHardware.Color.parseColor(image.getPixel(x, y));
                pixels[x * height + y] = color;
            }
        }
        ArrayList<ArrayList<int[]>> bins = new ArrayList<ArrayList<int[]>>();
        int[][] centroids = new int[PALETTE_SIZE][3];
        for (int k = 0; k < PALETTE_SIZE; k++) {
            centroids[k] = Color.randomColor();
        }
        for (int j = 0; j < ITER; j++) {
            bins = new ArrayList<ArrayList<int[]>>();
            for (int i = 0; i < PALETTE_SIZE; i++) {
                bins.add(new ArrayList<int[]>());
            }
            for (int i = 0; i < length; i++) {
                int min = 1000;
                int centroid = 0;
                for (int k = 0; k < PALETTE_SIZE; k++) {
                    int dist = distance(centroids[k], pixels[i]);
                    if (dist < min) {
                        min = dist;
                        centroid = k;
                    }
                }
                bins.get(centroid).add(pixels[i]);
            }
            for (int i = 0; i < PALETTE_SIZE; i++) {
                int[] sum = {0, 0, 0};
                int size = bins.get(i).size();
                for (int k = 0; k < size; k++) {
                    for (int l = 0; l < 3; l++) {
                        sum[l] += bins.get(i).get(k)[l];
                    }
                }
                for (int l = 0; l < 3; l++) {
                    centroids[i][l] = sum[l] / size;
                }
            }
        }
        int centroid = 0;
        int max = 0;
        for (int u = 0; u < PALETTE_SIZE; u++) {
            if (bins.get(u).size() > max) {
                max = bins.get(u).size();
                centroid = u;
            }
        }
        int[] totals = {0, 0, 0};
        for (int z = 0; z < bins.get(centroid).size(); z++) {
            for (int y = 0; y < 3; y++) {
                totals[y] += bins.get(centroid).get(z)[y];
            }
        }
        for (int y = 0; y < 3; y++) {
            totals[y] /= bins.get(centroid).size();
        }
        return totals;
    }

    public void takePicture(NewCameraHardware.PictureCallback pictureCallback) {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

            }
        });
    }
}
