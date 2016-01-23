package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.SmartDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cIMUHardware;
import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;

/**
 * Created by Ryan on 12/6/2015.
 */
public class GyroTurnTest extends LinearRobotController {

    public SmartDriveHardware smartDriveHardware;
    public I2cIMUHardware gyroHardware;
    public DriveHardware driveHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        gyroHardware = new I2cIMUHardware();
        registerHardwareInterface("gyro", gyroHardware);
        driveHardware = new DriveHardware();
        registerHardwareInterface("drive", driveHardware);
        smartDriveHardware = new SmartDriveHardware(driveHardware, gyroHardware);
        registerHardwareInterface("gyro_drive", smartDriveHardware);

        waitForStart();

        while(true) {
            smartDriveHardware.turnLeftSync(90);

            waitMillis(5000);
        }
    }
}
