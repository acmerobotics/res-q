package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.hardware.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.GyroDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.I2cGyroHardware;
import com.qualcomm.ftcrobotcontroller.hardware.LinearRobotController;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 12/6/2015.
 */
public class AutoTurn extends LinearRobotController {

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
