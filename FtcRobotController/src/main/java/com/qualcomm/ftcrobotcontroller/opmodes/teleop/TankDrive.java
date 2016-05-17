package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.control.RobotController;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by Admin on 9/21/2015.
 */
public class TankDrive extends RobotController {

    public void loop(DriveHardware driveHardware, Gamepad gamepad) {
        driveHardware.setMappedMotorSpeeds(-gamepad.left_stick_y, -gamepad.right_stick_y);
    }

}
