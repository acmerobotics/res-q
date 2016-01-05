package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.control.RobotController;

/**
 * Created by Admin on 9/21/2015.
 */
public class TankDrive extends RobotController {

    private DriveHardware driveHardware;

    @Override
    public void init() {
        super.init();

        driveHardware = new DriveHardware();
        registerHardwareInterface("drive", driveHardware);
    }

    @Override
    public void loop() {
        driveHardware.setMappedMotorSpeeds(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

        super.loop();
    }
}
