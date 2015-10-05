package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.RobotController;

/**
 * Created by Admin on 9/21/2015.
 */
public class TankTread extends RobotController {

    private DriveHardware driveHardware;

    @Override
    public void init() {
        super.init();
        driveHardware = new DriveHardware();
        this.registerHardwareInterface("drive", driveHardware);
    }

    @Override
    public void loop() {
        driveHardware.setMappedMotorSpeeds(gamepad1.left_stick_y, gamepad1.right_stick_y);
    }
}
