package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.SmartDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cIMUHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Ryan on 12/13/2015.
 */
public class BlockAuto extends LinearRobotController {

    private SmartDriveHardware smartDriveHardware;
    private DriveHardware driveHardware;
    private I2cIMUHardware gyroHardware;
    private UltrasonicPairHardware usHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        driveHardware = new DriveHardware();
        gyroHardware = new I2cIMUHardware();
        smartDriveHardware = new SmartDriveHardware(driveHardware, gyroHardware);
        usHardware = new UltrasonicPairHardware();

        registerHardwareInterface("drive", driveHardware);
        registerHardwareInterface("gyro", gyroHardware);
        registerHardwareInterface("gyro_drive", smartDriveHardware);

        promptAllianceColor();

        waitForStart();

        long startMillis = System.currentTimeMillis();

        driveHardware.setMotorSpeeds(-0.2, -0.2);
        waitMillis(1000);
        driveHardware.setMotorSpeeds(0, 0);

        if (getAllianceColor() == AllianceColor.RED) smartDriveHardware.turnLeftSync(135);
        else smartDriveHardware.turnRightSync(135);

        driveHardware.setMotorSpeeds(0.5, 0.5);
        waitMillis(2000);
        driveHardware.stopMotors();

        if (getAllianceColor() == AllianceColor.RED) smartDriveHardware.turnLeftSync(90);
        else smartDriveHardware.turnRightSync(90);

        while ((System.currentTimeMillis() - startMillis) < 10000) {
            waitOneFullHardwareCycle();
        }

        driveHardware.setMotorSpeeds(0.5, 0.5);
        waitMillis(500);
        driveHardware.setMotorSpeeds(0, 0);
    }
}
