package com.qualcomm.ftcrobotcontroller.hardware;
import android.hardware.Camera;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 10/8/2015.
 */
public class CameraHardware extends HardwareInterface {
//I AM SORRY FOR THIS CODE IN ADVANCE!
    @Override
    public void init(OpMode mode) {

    }
    public Camera camera;
    private Camera openCamera(){
        int cameraId = -1;
        Camera cam = null;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        try {
            cam = Camera.open(cameraId);
        } catch (Exception e) {

        }
        return cam;
    }
    public void photo(int x, int y){
        //Camera.Parameters p = .setParameters();
        //p.setPictureSize(x,y);
    }
}
