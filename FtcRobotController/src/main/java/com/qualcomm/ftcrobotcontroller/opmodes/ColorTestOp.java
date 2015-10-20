package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Bitmap;

import com.qualcomm.ftcrobotcontroller.hardware.RobotController;

/**
 * Created by Admin on 10/19/2015.
 */
public class ColorTestOp extends RobotController {
    private NewCameraHardware cameraHardware;

    @Override
    public void init() {
        cameraHardware = new NewCameraHardware();
        this.registerHardwareInterface("camera", cameraHardware);
        super.init();
    }

    @Override
    public void loop() {
        if (cameraHardware.hasImage()) {
            Bitmap bitmap = cameraHardware.getLastImage();
            int[] color = NewCameraHardware.getPredominantColor(bitmap);
            telemetry.addData("RGB", color[0] + ":" + color[1] + ":" + color[2]);
        }
    }
}
