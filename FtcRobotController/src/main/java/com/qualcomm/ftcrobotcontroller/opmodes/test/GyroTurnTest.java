package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.GyroDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cGyroHardware;
import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 12/6/2015.
 */
public class GyroTurnTest extends LinearRobotController {

    public GyroDriveHardware gyroDriveHardware;
    public I2cGyroHardware gyroHardware;
    public DriveHardware driveHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        gyroHardware = new I2cGyroHardware();
        registerHardwareInterface("gyro", gyroHardware);
        driveHardware = new DriveHardware();
        registerHardwareInterface("drive", driveHardware);
        gyroDriveHardware = new GyroDriveHardware(driveHardware, gyroHardware);
        RobotLog.d("Init: " + registerHardwareInterface("gyro_drive", gyroDriveHardware));

        waitForStart();

        while(true) {
            gyroDriveHardware.turnLeftSync(90);

            waitMillis(1000);

//            gyroDriveHardware.turnRightSync(90);
//
//            waitMillis(1000);
        }

//        gyroDriveHardware.setMotorSpeeds(0.8, 0.8);
//
//        waitMillis(5000);
//
//        gyroDriveHardware.stopMotors();
    }
}
