package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.SmartDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.IMUHardware;
import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Ryan on 12/6/2015.
 */
public class GyroTurnTest extends LinearRobotController {

    public SmartDriveHardware smartDriveHardware;
    public GyroSensor gyroHardware;
    public DriveHardware driveHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        gyroHardware =  hardwareMap.gyroSensor.get("gyro");

        driveHardware = new DriveHardware();
        registerHardwareInterface("drive", driveHardware);
        smartDriveHardware = new SmartDriveHardware(driveHardware, gyroHardware);
        registerHardwareInterface("gyro_drive", smartDriveHardware);

        waitForStart();

        while(true) {
            waitMillis(5000);

            smartDriveHardware.turnLeftSync(90);
        }
    }
}
