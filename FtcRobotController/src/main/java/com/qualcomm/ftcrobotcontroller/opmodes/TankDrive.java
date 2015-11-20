package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.RobotController;

/**
 * Created by Admin on 9/21/2015.
 */
public class TankDrive extends RobotController {

    private DriveHardware driveHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.begin();

        driveHardware = new DriveHardware();
        this.registerHardwareInterface("drive", driveHardware);

        waitOneFullHardwareCycle();

        waitForStart();

        while(opModeIsActive()) {
            driveHardware.setMappedMotorSpeeds(gamepad1.left_stick_y, gamepad1.right_stick_y);

            this.update();

            waitOneFullHardwareCycle();
        }
    }
}
