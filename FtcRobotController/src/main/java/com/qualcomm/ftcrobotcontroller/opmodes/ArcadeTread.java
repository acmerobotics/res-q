package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.RobotController;

/**
 * Created by Admin on 9/24/2015.
 */
public class ArcadeTread extends RobotController {

    private DriveHardware driveHardware;

    @Override
    public void init() {
        super.init();
        driveHardware = new DriveHardware();
        registerHardwareInterface("drive", driveHardware);
    }

    @Override
    public void loop() {
        double leftRight = gamepad1.right_stick_x;
        double forwardBack = gamepad1.left_stick_y;
        if (Math.abs(leftRight) > 0.05) {
            driveHardware.setMappedMotorSpeeds(leftRight, -leftRight);
        } else if (Math.abs(forwardBack) > 0.05) {
            driveHardware.setMappedMotorSpeeds(forwardBack, forwardBack);
        } else {
            driveHardware.setMotorSpeeds(0, 0);
        }
    }

}
