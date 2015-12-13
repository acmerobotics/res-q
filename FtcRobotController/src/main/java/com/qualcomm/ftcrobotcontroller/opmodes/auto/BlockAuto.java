package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import com.qualcomm.ftcrobotcontroller.control.LinearRobotController;
import com.qualcomm.ftcrobotcontroller.hardware.drive.DriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.drive.GyroDriveHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.I2cGyroHardware;
import com.qualcomm.ftcrobotcontroller.hardware.sensors.UltrasonicPairHardware;

/**
 * Created by Ryan on 12/13/2015.
 */
public class BlockAuto extends LinearRobotController {

    private GyroDriveHardware gyroDriveHardware;
    private DriveHardware driveHardware;
    private I2cGyroHardware gyroHardware;
    private UltrasonicPairHardware usHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        driveHardware = new DriveHardware();
        gyroHardware = new I2cGyroHardware();
        gyroDriveHardware = new GyroDriveHardware(driveHardware, gyroHardware);
        usHardware = new UltrasonicPairHardware();

        registerHardwareInterface("drive", driveHardware);
        registerHardwareInterface("gyro", gyroHardware);
        registerHardwareInterface("gyro_drive", gyroDriveHardware);

        promptAllianceColor();

        waitForStart();

        long startMillis = System.currentTimeMillis();

        driveHardware.setMotorSpeeds(-0.2, -0.2);
        waitMillis(1000);
        driveHardware.setMotorSpeeds(0, 0);

        if (getAllianceColor() == AllianceColor.RED) gyroDriveHardware.turnLeftSync(135);
        else gyroDriveHardware.turnRightSync(135);

        driveHardware.setMotorSpeeds(0.5, 0.5);
        waitMillis(2000);
        driveHardware.stopMotors();

        if (getAllianceColor() == AllianceColor.RED) gyroDriveHardware.turnLeftSync(90);
        else gyroDriveHardware.turnRightSync(90);

        while ((System.currentTimeMillis() - startMillis) < 10000) {
            waitOneFullHardwareCycle();
        }

        driveHardware.setMotorSpeeds(0.5, 0.5);
        waitMillis(500);
        driveHardware.setMotorSpeeds(0, 0);
    }
}
